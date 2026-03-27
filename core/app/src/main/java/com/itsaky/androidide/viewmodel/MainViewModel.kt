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
 *
 * @author android_zero
 */
package com.itsaky.androidide.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsaky.androidide.preferences.internal.GeneralPreferences
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.templates.Template
import com.itsaky.androidide.utils.RecentProjectsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * 分发UI 事件流
 */
sealed class MainEvent {
    data class OpenProjectSuccess(val projectDir: File) : MainEvent()
    data class ShowMessage(val messageResId: Int, val isError: Boolean = false) : MainEvent()
    data class RequestConfirmOpen(val projectDir: File) : MainEvent()
}

class MainViewModel : ViewModel() {

    companion object {
        const val SCREEN_MAIN = 0
        const val SCREEN_TEMPLATE_LIST = 1
        const val SCREEN_TEMPLATE_DETAILS = 2
    }

    private val _currentScreen = MutableLiveData(-1)
    private val _previousScreen = AtomicInteger(-1)
    private val _isTransitionInProgress = MutableLiveData(false)

    internal val template = MutableLiveData<Template<*>?>(null)
    internal val creatingProject = MutableLiveData(false)

    val currentScreen: LiveData<Int> = _currentScreen

    val previousScreen: Int
        get() = _previousScreen.get()

    var isTransitionInProgress: Boolean
        get() = _isTransitionInProgress.value ?: false
        set(value) {
            _isTransitionInProgress.value = value
        }

    private val _mainEvents = MutableSharedFlow<MainEvent>(extraBufferCapacity = 5)
    val mainEvents: SharedFlow<MainEvent> = _mainEvents.asSharedFlow()

    private var isOpeningProject = false

    fun setScreen(screen: Int) {
        _previousScreen.set(_currentScreen.value ?: SCREEN_MAIN)
        _currentScreen.value = screen
    }

    fun postTransition(owner: LifecycleOwner, action: Runnable) {
        if (isTransitionInProgress) {
            _isTransitionInProgress.observe(owner, object : Observer<Boolean> {
                override fun onChanged(t: Boolean) {
                    _isTransitionInProgress.removeObserver(this)
                    action.run()
                }
            })
        } else {
            action.run()
        }
    }

    /**
     * 项目打开逻辑
     */
    fun openProject(context: Context, root: File) {
        if (isOpeningProject) return
        isOpeningProject = true

        viewModelScope.launch(Dispatchers.Default) {
            try {
                // 验证目录有效性 (I/O)
                val isValid = withContext(Dispatchers.IO) { root.exists() && root.isDirectory }
                if (!isValid) {
                    _mainEvents.tryEmit(MainEvent.ShowMessage(com.itsaky.androidide.resources.R.string.msg_opened_project_does_not_exist, true))
                    return@launch
                }

                // 异步写入历史记录 (I/O)
                RecentProjectsManager.addProjectAsync(context, root)

                // 配置项目环境缓存 (I/O)
                withContext(Dispatchers.IO) {
                    IProjectManager.getInstance().openProject(root)
                }

                // 触发 UI 进行 Intent 跳转
                _mainEvents.tryEmit(MainEvent.OpenProjectSuccess(root))

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isOpeningProject = false
            }
        }
    }

    /**
     * 后台检查并恢复上次打开的项目
     */
    fun checkAndOpenLastProject() {
        if (!GeneralPreferences.autoOpenProjects) return
        val openedProject = GeneralPreferences.lastOpenedProject
        if (openedProject == GeneralPreferences.NO_OPENED_PROJECT || openedProject.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {
            val project = File(openedProject)
            val exists = withContext(Dispatchers.IO) { project.exists() }
            
            if (!exists) {
                _mainEvents.tryEmit(MainEvent.ShowMessage(com.itsaky.androidide.resources.R.string.msg_opened_project_does_not_exist, false))
                return@launch
            }

            if (GeneralPreferences.confirmProjectOpen) {
                _mainEvents.tryEmit(MainEvent.RequestConfirmOpen(project))
            } else {
                withContext(Dispatchers.IO) { IProjectManager.getInstance().openProject(project) }
                _mainEvents.tryEmit(MainEvent.OpenProjectSuccess(project))
            }
        }
    }
}