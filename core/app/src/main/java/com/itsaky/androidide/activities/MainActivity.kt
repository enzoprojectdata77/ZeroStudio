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
package com.itsaky.androidide.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.Insets
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.itsaky.androidide.activities.editor.EditorActivityKt
import com.itsaky.androidide.app.EdgeToEdgeIDEActivity
import com.itsaky.androidide.fragments.MainFragment
import com.itsaky.androidide.fragments.TemplateDetailsFragment
import com.itsaky.androidide.fragments.TemplateListFragment
import com.itsaky.androidide.templates.ITemplateProvider
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashInfo
import com.itsaky.androidide.viewmodel.MainEvent
import com.itsaky.androidide.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : EdgeToEdgeIDEActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.apply {
                if (creatingProject.value == true) return@apply

                val newScreen = when (currentScreen.value) {
                    MainViewModel.SCREEN_TEMPLATE_DETAILS -> MainViewModel.SCREEN_TEMPLATE_LIST
                    MainViewModel.SCREEN_TEMPLATE_LIST -> MainViewModel.SCREEN_MAIN
                    else -> MainViewModel.SCREEN_MAIN
                }

                if (currentScreen.value != newScreen) {
                    setScreen(newScreen)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    override fun bindLayout(): View {
        return ComposeView(this).apply {
            id = View.generateViewId()
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    MainActivityScreen(viewModel)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 观察单次交互事件
        lifecycleScope.launch {
            viewModel.mainEvents.collect { event ->
                when (event) {
                    is MainEvent.OpenProjectSuccess -> {
                        val intent = Intent(this@MainActivity, EditorActivityKt::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            putExtra("extra_project_path", event.projectDir.absolutePath)
                        }
                        startActivity(intent)
                    }
                    is MainEvent.ShowMessage -> {
                        if (event.isError) flashError(event.messageResId)
                        else flashInfo(event.messageResId)
                    }
                    is MainEvent.RequestConfirmOpen -> askProjectOpenPermission(event.projectDir)
                }
            }
        }

        viewModel.checkAndOpenLastProject()

        viewModel.currentScreen.observe(this) { screen ->
            if (screen == -1) return@observe
            onBackPressedCallback.isEnabled = screen != MainViewModel.SCREEN_MAIN
        }

        if (viewModel.currentScreen.value == -1 && viewModel.previousScreen == -1) {
            viewModel.setScreen(MainViewModel.SCREEN_MAIN)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @Composable
    private fun MainActivityScreen(viewModel: MainViewModel) {
        var currentScreen by remember { mutableIntStateOf(MainViewModel.SCREEN_MAIN) }

        DisposableEffect(viewModel.currentScreen) {
            val observer = Observer<Int> { newScreen -> currentScreen = newScreen }
            viewModel.currentScreen.observe(this@MainActivity, observer)
            onDispose {
                viewModel.currentScreen.removeObserver(observer)
            }
        }
        
        // 动态 View ID
        val mainId = remember { View.generateViewId() }
        val listId = remember { View.generateViewId() }
        val detailsId = remember { View.generateViewId() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface
        ) { padding ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.surface),
                factory = { context ->
                    FrameLayout(context).apply {
                        val mainContainer = FragmentContainerView(context).apply { id = mainId }
                        val listContainer = FragmentContainerView(context).apply { id = listId }
                        val detailsContainer = FragmentContainerView(context).apply { id = detailsId }

                        addView(mainContainer, FrameLayout.LayoutParams(-1, -1))
                        addView(listContainer, FrameLayout.LayoutParams(-1, -1))
                        addView(detailsContainer, FrameLayout.LayoutParams(-1, -1))

                        supportFragmentManager.beginTransaction()
                            .replace(mainId, MainFragment())
                            .replace(listId, TemplateListFragment())
                            .replace(detailsId, TemplateDetailsFragment())
                            .commitNowAllowingStateLoss()
                    }
                },
                update = { view ->
                    val mainContainer = view.findViewById<View>(mainId)
                    val listContainer = view.findViewById<View>(listId)
                    val detailsContainer = view.findViewById<View>(detailsId)

                    mainContainer?.isVisible = currentScreen == MainViewModel.SCREEN_MAIN
                    listContainer?.isVisible = currentScreen == MainViewModel.SCREEN_TEMPLATE_LIST
                    detailsContainer?.isVisible = currentScreen == MainViewModel.SCREEN_TEMPLATE_DETAILS
                }
            )
        }
    }

    override fun onApplySystemBarInsets(insets: Insets) {
        // Compose 系统已自动处理，无需手动设置
    }

    private fun askProjectOpenPermission(root: File) {
        DialogUtils.newMaterialDialogBuilder(this)
            .setTitle(com.itsaky.androidide.resources.R.string.title_confirm_open_project)
            .setMessage(getString(com.itsaky.androidide.resources.R.string.msg_confirm_open_project, root.absolutePath))
            .setCancelable(false)
            .setPositiveButton(com.itsaky.androidide.resources.R.string.yes) { _, _ -> openProject(root) }
            .setNegativeButton(com.itsaky.androidide.resources.R.string.no, null)
            .show()
    }

    fun openProject(root: File) {
        viewModel.openProject(this, root)
    }

    override fun onDestroy() {
        ITemplateProvider.getInstance().release()
        super.onDestroy()
    }
}