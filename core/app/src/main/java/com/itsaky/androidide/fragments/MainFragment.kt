/*
 *  @author android_zero
 */

package com.itsaky.androidide.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.itsaky.androidide.activities.MainActivity
import com.itsaky.androidide.activities.PreferencesActivity
import com.itsaky.androidide.activities.TerminalActivity
import com.itsaky.androidide.models.MainScreenAction
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.viewmodel.MainViewModel
import com.itsaky.androidide.utils.*
import java.io.File
import com.itsaky.androidide.fragments.git.function.*

class MainFragment : BaseFragment() {

    private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })

    // 使用 Compose State 管理历史列表，确保数据变化时 UI 自动刷新
    private val recentProjectsState = mutableStateListOf<ProjectHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 初始化时加载一次历史记录
        refreshHistory()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                        ZeroStudioMainLayout()
                    }
                }
            }
        }
    }

    /**
     * 刷新历史记录数据
     */
    private fun refreshHistory() {
        val history = RecentProjectsManager.getHistory(requireContext())
        recentProjectsState.clear()
        recentProjectsState.addAll(history)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ZeroStudioMainLayout() {
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ZeroStudio", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = { /* TODO: 关联 MainActivity 侧滑菜单 */ }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: 全局搜索 */ }) {
                            Icon(Icons.Default.Search, "Search")
                        }
                        IconButton(onClick = { /* TODO: 个人中心 */ }) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFF0F0F0))) {
                                Icon(Icons.Outlined.Person, "User", modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("首页") })
                    NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.History, null) }, label = { Text("历程") })
                    NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Build, null) }, label = { Text("工具") })
                    NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Person, null) }, label = { Text("我的") })
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                // 左侧浮动轨道 (48dp 宽)
                FloatingSideRail()

                // 主内容区 (偏移 72dp 以躲避轨道)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 72.dp, end = 16.dp, top = 12.dp)
                ) {
                    // 快速开始渐变卡片
                    QuickStartGradientCard()
                    
                    Spacer(modifier = Modifier.height(28.dp))

                    // 最近项目与智能缓存
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // 左列：项目列表
                        Column(modifier = Modifier.weight(1.5f)) {
                            SectionTitle("最近项目")
                            if (recentProjectsState.isEmpty()) {
                                Text("暂无历史项目", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(8.dp))
                            } else {
                                recentProjectsState.forEach { project ->
                                    RecentProjectItem(project)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // 右列：智能预存
                        Column(modifier = Modifier.weight(0.8f)) {
                            SectionTitle("智能预存")
                            SmartCacheItem("Centiflor", "C", Color(0xFF009688))
                            SmartCacheItem("AlphaCache", "P", Color(0xFF673AB7))
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                    
                    // 工具与服务
                    SectionTitle("工具与服务")
                    ToolsServiceGrid()
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    @Composable
    private fun QuickStartGradientCard() {
        val cardGradient = Brush.linearGradient(
            colors = listOf(Color(0xFF3F1D9B), Color(0xFF00A79D)),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f)
        )

        Card(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(cardGradient).padding(24.dp)) {
                Column {
                    Text("快速开始", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickActionButton(Icons.Default.Add, "新建项目") {
                            viewModel.setScreen(MainViewModel.SCREEN_TEMPLATE_LIST)
                        }
                        QuickActionButton(Icons.Default.FolderOpen, "打开项目") {
                            pickDirectory()
                        }
                        QuickActionButton(Icons.Default.Share, "克隆仓库") {
                            cloneGitRepo()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun QuickActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
        Surface(
            onClick = onClick,
            color = Color.White.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }

    @Composable
    private fun RecentProjectItem(project: ProjectHistory) {
        Surface(
            onClick = { openProject(File(project.path)) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            color = Color(0xFFF8F9FB),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(42.dp).clip(CircleShape).background(project.color), contentAlignment = Alignment.Center) {
                    Text(project.letter, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(project.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF212121))
                    Text(project.path, fontSize = 10.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
        }
    }

    @Composable
    private fun SmartCacheItem(name: String, letter: String, color: Color) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            color = Color(0xFFF8F9FB),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
                    Text(letter, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            }
        }
    }

    @Composable
    private fun ToolsServiceGrid() {
        val tools = listOf(
            Icons.Default.Code to Color(0xFFE1BEE7),
            Icons.Default.Terminal to Color(0xFFC8E6C9),
            Icons.Default.Construction to Color(0xFFBBDEFB),
            Icons.Default.Settings to Color(0xFFFFCCBC),
            Icons.Default.CloudSync to Color(0xFFD1C4E9),
            Icons.Default.BugReport to Color(0xFFFFF9C4)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(tools) { (icon, color) ->
                Surface(modifier = Modifier.size(56.dp), color = color, shape = RoundedCornerShape(16.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = Color.Black.copy(alpha = 0.6f), modifier = Modifier.size(26.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun FloatingSideRail() {
        val railBrush = Brush.verticalGradient(listOf(Color(0xFF311B92), Color(0xFF009688)))
        
        Column(
            modifier = Modifier
                .padding(start = 12.dp, top = 24.dp)
                .width(48.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(railBrush)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            RailIcon(Icons.Default.Terminal) { 
                startActivity(Intent(requireActivity(), TerminalActivity::class.java))
            }
            RailIcon(Icons.Default.Settings) { 
                startActivity(Intent(requireActivity(), PreferencesActivity::class.java))
            }
            Spacer(modifier = Modifier.height(32.dp))
            RailIcon(Icons.Default.Folder) { pickDirectory() }
            RailIcon(Icons.Default.Update) { /* TODO */ }
        }
    }

    @Composable
    private fun RailIcon(icon: ImageVector, onClick: () -> Unit) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp).clickable { onClick() })
    }

    @Composable
    private fun SectionTitle(text: String) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF333333), modifier = Modifier.padding(vertical = 12.dp))
    }


    private fun pickDirectory() {
        pickDirectory(this::openProject)
    }

    /**
     * 打开项目并记录历史
     */
    fun openProject(root: File) {
        // 持久化记录
        RecentProjectsManager.addProject(requireContext(), root)
        
        // 刷新 UI 状态
        refreshHistory()
        
        // 启动编辑器
        (requireActivity() as MainActivity).openProject(root)
    }

    private fun cloneGitRepo() {
        ZeroCloneDialogBottomSheetFragment.newInstance(repoId = "")
            .show(childFragmentManager, "CloneBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}