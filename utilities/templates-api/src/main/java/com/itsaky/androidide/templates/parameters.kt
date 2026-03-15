/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.templates

import android.view.View
import androidx.annotation.StringRes
import com.itsaky.androidide.templates.Language.Java
import com.itsaky.androidide.templates.Language.Kotlin
import com.itsaky.androidide.templates.ParameterConstraint.NONEMPTY
import com.itsaky.androidide.templates.ParameterConstraint.PACKAGE
import com.itsaky.androidide.templates.R.string
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import com.itsaky.androidide.preferences.internal.ProjectsPreferences

enum class ParameterConstraint {

  /**
   * Value must be unique.
   */
  UNIQUE,

  /**
   * Value must be a valid Java package name.
   */
  PACKAGE,

  /**
   * Value must a valid fully qualified Java class name.
   */
  CLASS,

  /**
   * Value must be a valid Java class name.
   */
  CLASS_NAME,

  /**
   * Value must be a valid Gradle module name.
   */
  MODULE_NAME,

  /**
   * Value must not be empty or blank.
   */
  NONEMPTY,

  /**
   * Value must be a valid layout file name.
   */
  LAYOUT,

  /**
   * Value must path to a file.
   */
  FILE,

  /**
   * Value must path to a directory.
   */
  DIRECTORY,

  /**
   * Used with [FILE] and [DIRECTORY]. Asserts that the file/directory at the given path exists.
   */
  EXISTS
}

abstract class Parameter<T>(
    @StringRes val name: Int,
    @StringRes val description: Int?,
    val default: T,
    var constraints: List<ParameterConstraint>
) {

  private val observers = hashSetOf<Observer<T>>()
  private val lock = ReentrantLock()
  private var _value: T? = null

  private var actionBeforeCreateView: ((Parameter<T>) -> Unit)? = null
  private var actionAfterCreateView: ((Parameter<T>) -> Unit)? = null

  var isVisible: Boolean = true
    set(value) {
      field = value
      onVisibilityChanged?.invoke(value)
    }

  var onVisibilityChanged: ((Boolean) -> Unit)? = null
  
  /**
   * The value of this parameter.
   */
  val value: T
    get() = _value ?: default

  /**
   * Set the new value to this parameter.
   *
   * @param value The new parameter value.
   * @param notify Whether the observers must be notified of the change or not.
   */
  fun setValue(value: T, notify: Boolean = true) {
    this._value = value

    if (notify) {
      notifyObservers()
    }
  }

  /**
   * Resets the parameter value to the default value and removes any external value observers.
   *
   * @param notify Whether the observers should be notified about this change or not.
   */
  fun reset(notify: Boolean = true) {
    setValue(default, notify)
    clearObservers()
  }

  /**
   * Adds the [Observer] instance to the list of observers.
   *
   * @param observer The observer to add.
   * @return Whether the observer was added or not.
   */
  fun observe(observer: Observer<T>): Boolean {
    return lock.withLock {
      observers.add(observer)
    }
  }

  /**
   * Removes the [Observer] instance from the list of observers.
   *
   * @param observer The observer to remove.
   * @return Whether the observer was removed or not.
   */
  fun removeObserver(observer: Observer<T>): Boolean {
    return lock.withLock {
      observers.remove(observer)
    }
  }

  fun release() {
    clearObservers()

    this.actionBeforeCreateView = null
    this.actionAfterCreateView = null
  }

  private fun clearObservers() {
    lock.withLock {
      observers.clear()
    }
  }

  /**
   * Perform the given action before the view is created.
   *
   * @param action The action to execute.
   * @see beforeCreateView
   */
  fun doBeforeCreateView(action: (Parameter<T>) -> Unit) {
    this.actionBeforeCreateView = action
  }

  /**
   * Perform the given action after the view is created.
   *
   * @param action The action to execute.
   * @see afterCreateView
   */
  fun doAfterCreateView(action: (Parameter<T>) -> Unit) {
    this.actionBeforeCreateView = action
  }

  /**
   * Called before the layout for this widget is created.
   */
  open fun beforeCreateView() {
    this.actionBeforeCreateView?.invoke(this)
  }

  /**
   * Called after the layout for this widget is created.
   */
  open fun afterCreateView() {
    this.actionAfterCreateView?.invoke(this)
  }

  private fun notifyObservers() {
    lock.withLock {
      observers.forEach {
        if (it !is DefaultObserver || it.isEnabled) {
          it.onChanged(this)
        }
      }
    }
  }

  /**
   * An [Observer] observes changes to values of a [Parameter].
   */
  fun interface Observer<T> {

    /**
     * Called when the value of the parameter is changed.
     *
     * @param parameter The parameter that was changed (contains the new value).
     */
    fun onChanged(parameter: Parameter<T>)
  }

  /**
   * Default implementation of [Observer] which can enabled or disabled.
   */
  abstract class DefaultObserver<T>(var isEnabled: Boolean = true) :
    Observer<T> {

    /**
     * Executes the given [action] with this observer disabled.
     *
     * @param action The action to perform.
     */
    fun disableAndRun(action: () -> Unit) {
      val enabled = isEnabled
      isEnabled = false
      action()
      isEnabled = enabled
    }
  }
}

abstract class ParameterBuilder<T> {

  @StringRes
  var name: Int? = null

  @StringRes
  var description: Int? = null
  var default: T? = null

  var constraints: List<ParameterConstraint> = emptyList()

  protected open fun validate() {
    checkNotNull(name) { "Parameter must have a name" }
    checkNotNull(default) { "Parameter must have a default value" }
  }

  abstract fun build(): Parameter<T>
}

class BooleanParameter(@StringRes name: Int, @StringRes description: Int?,
  default: Boolean, constraints: List<ParameterConstraint>
) : Parameter<Boolean>(name, description, default, constraints)

class BooleanParameterBuilder : ParameterBuilder<Boolean>() {

  override fun build(): BooleanParameter {
    return BooleanParameter(name!!, description, default!!, constraints)
  }

}

/**
 * Base class for parameters which are presented using a text field.
 *
 * @property startIcon The drawable resource that will be used as the start icon.
 * @property endIcon The drawable resource that will be used as the end icon.
 * @property onStartIconClick Function which will be used when the start icon
 *     is clicked. Click listener to the icon will be set on if this is non-null.
 * @property onEndIconClick Function which will be used when the end icon is
 *     clicked. Click listener to the icon will be set on if this is non-null.
 */
abstract class TextFieldParameter<T>(@StringRes name: Int,
  @StringRes description: Int?, default: T,
  val startIcon: ((TextFieldParameter<T>) -> Int)?,
  val endIcon: ((TextFieldParameter<T>) -> Int)?,
  val onStartIconClick: View.OnClickListener?,
  val onEndIconClick: View.OnClickListener?,
  constraints: List<ParameterConstraint>
) : Parameter<T>(name, description, default, constraints)

abstract class TextFieldParameterBuilder<T>(
  var startIcon: ((TextFieldParameter<T>) -> Int)? = null,
  var endIcon: ((TextFieldParameter<T>) -> Int)? = null,
  var onStartIconClick: View.OnClickListener? = null,
  var onEndIconClick: View.OnClickListener? = null
) : ParameterBuilder<T>()

class StringParameter(@StringRes name: Int, @StringRes description: Int?,
  default: String,
  startIcon: ((TextFieldParameter<String>) -> Int)?,
  endIcon: ((TextFieldParameter<String>) -> Int)?,
  onStartIconClick: View.OnClickListener?,
  onEndIconClick: View.OnClickListener?,
  constraints: List<ParameterConstraint>
) : TextFieldParameter<String>(name, description, default, startIcon, endIcon,
  onStartIconClick, onEndIconClick, constraints)

class StringParameterBuilder : TextFieldParameterBuilder<String>() {

  override fun build(): StringParameter {
    return StringParameter(name = name!!, description = description,
      default = default!!, startIcon = startIcon, endIcon = endIcon,
      onStartIconClick = onStartIconClick, onEndIconClick = onEndIconClick,
      constraints = constraints)
  }
}

class EnumParameter<T : Enum<*>>(@StringRes name: Int,
  @StringRes description: Int?, default: T,
  startIcon: ((TextFieldParameter<T>) -> Int)?,
  endIcon: ((TextFieldParameter<T>) -> Int)?,
  onStartIconClick: View.OnClickListener?,
  onEndIconClick: View.OnClickListener?,
  constraints: List<ParameterConstraint>,
  val displayName: ((T) -> String)? = null,
  val filter: ((T) -> Boolean)? = null
) : TextFieldParameter<T>(name, description, default, startIcon, endIcon,
  onStartIconClick, onEndIconClick, constraints) {

  /**
   * Get the display name for this [EnumParameter].
   */
  fun getDisplayName(): String? {
    return this.displayName?.invoke(value)
  }
}

class EnumParameterBuilder<T : Enum<*>> : TextFieldParameterBuilder<T>() {

  var displayName: ((T) -> String)? = null
  var filter: ((T) -> Boolean)? = null

  override fun build(): EnumParameter<T> {
    return EnumParameter(name = name!!, description = description,
      default = default!!, startIcon = startIcon, endIcon = endIcon,
      onStartIconClick = onStartIconClick, onEndIconClick = onEndIconClick,
      constraints = constraints, displayName = displayName, filter = filter)
  }
}

/**
 * Create a new [StringParameter] for accepting string input.
 */
inline fun stringParameter(crossinline block: StringParameterBuilder.() -> Unit
): StringParameter = StringParameterBuilder().apply(block).build()

/**
 * Create a new [BooleanParameter] for accepting boolean input.
 */
inline fun booleanParameter(crossinline block: BooleanParameterBuilder.() -> Unit
): BooleanParameter = BooleanParameterBuilder().apply(block).build()

inline fun <T : Enum<*>> enumParameter(crossinline block: EnumParameterBuilder<T>.() -> Unit
): EnumParameter<T> = EnumParameterBuilder<T>().apply(block).build()

/**
 * 获取项目名称输入框参数
 * @author android_zero 优化：支持从偏好设置读取默认值
 */
inline fun projectNameParameter(crossinline configure: StringParameterBuilder.() -> Unit = {}) =
    stringParameter {
      name = string.project_app_name
      default = ProjectsPreferences.defaultProjectName
      startIcon = { R.drawable.ic_android }
      constraints = listOf(NONEMPTY)
      configure()
    }


/**
 * 获取包名输入框参数
 * @author android_zero 优化：支持从偏好设置读取默认值
 */
inline fun packageNameParameter(crossinline configure: StringParameterBuilder.() -> Unit = {}) =
    stringParameter {
      name = string.package_name
      default = ProjectsPreferences.defaultPackageName
      startIcon = { R.drawable.ic_package }
      constraints = listOf(NONEMPTY, PACKAGE)
      configure()
    }

inline fun projectLanguageParameter(
    crossinline configure: EnumParameterBuilder<Language>.() -> Unit = {}
) =
    enumParameter<Language> {
      name = string.wizard_language
      default = Java
      displayName = Language::lang
      startIcon = {
        if (it.value == Kotlin) R.drawable.ic_language_kotlin else R.drawable.ic_language_java
      }

      configure()
    }

enum class NdkVersion(val version: String) {
    R29("29.0.14206865"),
    R29_BETA4("29.0.14033849-beta4"),
    R28C("28.2.13676358"),
    R27D("27.3.13750724"),
    R26D("26.3.11579264"),
    // R25C("25.2.9519653"),
    R24("24.0.8215888"),
    R23B("23.2.8568313"),
    R22B("22.1.7171670"),
    R21E("21.4.7075529");
    fun displayName(): String = version
}

enum class CmakeVersion(val version: String) {
    V3_10_2("3.10.2"), 
    V3_18_1("3.18.1"), 
    V3_22_1("3.22.1"), 
    V3_25_1("3.25.1"), 
    V3_30("3.30.0"), 
    V3_31_1("3.31.1");
    fun displayName(): String = version
}



inline fun minSdkParameter(crossinline configure: EnumParameterBuilder<Sdk>.() -> Unit = {}) =
    enumParameter<Sdk> {
      name = string.minimum_sdk
      default = Sdk.Lollipop
      displayName = Sdk::displayName
      startIcon = { R.drawable.ic_min_sdk }

      configure()
    }

inline fun useKtsParameter(crossinline configure: BooleanParameterBuilder.() -> Unit = {}) =
    booleanParameter {
      name = string.msg_use_kts
      default = true

      configure()
    }

inline fun projectNdkVersionParameter(crossinline configure: EnumParameterBuilder<NdkVersion>.() -> Unit = {}) =
    enumParameter<NdkVersion> {
        name = R.string.wizard_ndk_version
        default = NdkVersion.R29
        displayName = NdkVersion::displayName
        configure()
    }

inline fun projectCmakeVersionParameter(crossinline configure: EnumParameterBuilder<CmakeVersion>.() -> Unit = {}) =
    enumParameter<CmakeVersion> {
        name = R.string.wizard_cmake_version
        default = CmakeVersion.V3_22_1
        displayName = CmakeVersion::displayName
        configure()
    }

inline fun useNdkParameter(crossinline configure: BooleanParameterBuilder.() -> Unit = {}) =
    booleanParameter {
        name = R.string.msg_use_ndk
        default = false
        configure()
    }

inline fun useTomlParameter(crossinline configure: BooleanParameterBuilder.() -> Unit = {}) =
    booleanParameter {
        name = R.string.msg_use_toml 
        default = true 
        configure()
    }