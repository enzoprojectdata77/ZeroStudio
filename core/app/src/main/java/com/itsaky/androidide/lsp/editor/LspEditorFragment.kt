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

package com.itsaky.androidide.lsp.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.itsaky.androidide.lsp.window.LspEditorTextActionWindow
import androidx.appcompat.widget.Toolbar
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itsaky.androidide.R
import com.itsaky.androidide.editor.ui.IDEEditor
import com.itsaky.androidide.editor.utils.ContentReadWrite
import com.itsaky.androidide.eventbus.events.filetree.FileClickEvent
import com.itsaky.androidide.fragments.BaseFragment
import com.itsaky.androidide.lsp.BaseLspConnector
import com.itsaky.androidide.lsp.LspActions
import com.itsaky.androidide.lsp.LspBootstrap
import com.itsaky.androidide.lsp.LspManager
import com.itsaky.androidide.lsp.servers.LuaServer
import com.itsaky.androidide.lsp.ui.LspStatusPanel
import com.itsaky.androidide.lsp.ui.RenameSymbolDialog
import com.itsaky.androidide.lsp.util.LspStatusMonitor
import com.itsaky.androidide.lsp.util.Logger
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashInfo
import com.itsaky.androidide.utils.flashSuccess
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.event.CreateContextMenuEvent
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry
import io.github.rosemoe.sora.langs.textmate.registry.dsl.languages
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.DocumentSymbol
import org.eclipse.lsp4j.SymbolInformation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * 集成了 LSP 核心功能的编辑器 Fragment。
 * 修复了补全、Action 窗口失效以及编译引用的问题。
 *
 * @author android_zero
 */
class LspEditorFragment : BaseFragment(R.layout.fragment_lsp_editor), LspActionListener {

    private val LOG = Logger.instance("LspEditorFragment")

    private lateinit var editor: IDEEditor
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: View

    private var file: File? = null
    private var projectDir: File? = null

    private var lspConnector: BaseLspConnector? = null
    private var lspUiDelegate: LspUIDelegate? = null
    private var contextMenuHandler: LspContextMenuHandler? = null
    
    // 使用新的 Window 类
    private var lspActionWindow: LspEditorTextActionWindow? = null
    private var loadFileJob: Job? = null

    companion object {
        const val ARG_FILE_PATH = "arg_file_path"
        const val ARG_PROJECT_PATH = "arg_project_path"

        fun newInstance(file: File, projectDir: File? = null): LspEditorFragment {
            return LspEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILE_PATH, file.absolutePath)
                    projectDir?.let { putString(ARG_PROJECT_PATH, it.absolutePath) }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LspManager.init(requireContext())
        LspBootstrap.init(requireContext())

        arguments?.let {
            file = it.getString(ARG_FILE_PATH)?.let { path -> File(path) }
            projectDir = it.getString(ARG_PROJECT_PATH)?.let { path -> File(path) }
                ?: file?.let { f ->
                    val globalProjectDir = IProjectManager.getInstance().projectDir
                    if (f.absolutePath.startsWith(globalProjectDir.absolutePath)) globalProjectDir else f.parentFile
                }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFileClick(event: FileClickEvent) {
        val target = event.file
        if (target.exists() && target.isFile && target.absolutePath != file?.absolutePath) {
            openFile(target)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editor = view.findViewById(R.id.codeEditor)
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)

        setupToolbar()

        file?.takeIf { it.exists() }?.let { openFile(it) } ?: run {
            toolbar.title = "No File Opened"
        }

        editor.subscribeEvent(CreateContextMenuEvent::class.java) { event, _ ->
            contextMenuHandler?.handleContextMenu(event)
        }

        editor.subscribeEvent(SelectionChangeEvent::class.java) { event, _ ->
            if (!event.isSelected) {
                editor.postDelayed({
                    if (lspConnector?.isConnected() == true && !editor.cursor.isSelected) {
                        lspUiDelegate?.triggerCodeActions(event.left)
                    }
                }, 600)
            }
        }
    }

    private fun setupToolbar() {
        // 修复：使用 OnBackPressedDispatcher 替代已过时的 onBackPressed()
        toolbar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
        toolbar.inflateMenu(R.menu.menu_lsp_editor)
        if (toolbar.menu.findItem(R.id.action_lsp_status) == null) {
            toolbar.menu.add(0, R.id.action_lsp_status, 999, "LSP Status")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        }
        toolbar.setOnMenuItemClickListener { onMenuItemClick(it) }
    }

    private fun updateToolbarTitle() {
        toolbar.title = file?.name ?: "Editor"
        toolbar.subtitle = projectDir?.name ?: ""
    }

    private fun openFile(targetFile: File, line: Int = 0, column: Int = 0) {
        loadFileJob?.cancel()
        lifecycleScope.launch {
            lspConnector?.disconnect()
            lspUiDelegate?.detach()
            file = targetFile
            updateToolbarTitle()
            setupEditor(line, column)
        }
    }

    private fun setupEditor(initialLine: Int = 0, initialColumn: Int = 0) {
        val targetFile = file ?: return
        loadFileJob = viewLifecycleScope.launch(Dispatchers.IO) {
            val content = try {
                FileManager.getDocumentContents(targetFile.toPath())
            } catch (e: Exception) {
                try { targetFile.readText() } catch (readErr: Exception) { "" }
            }
            withContext(Dispatchers.Main) {
                editor.setText(content)
                editor.setFile(targetFile)
                if (initialLine > 0 || initialColumn > 0) {
                    editor.setSelectionAround(initialLine, initialColumn)
                    editor.ensurePositionVisible(initialLine, initialColumn)
                }
                editor.getComponent(EditorAutoCompletion::class.java).isEnabled = true
                initializeLsp(targetFile)
            }
        }
    }

    private fun getScopeForFile(file: File): String {
        return when (file.extension.lowercase()) {
            "kt", "kts" -> "source.kotlin"
            "java" -> "source.java"
            "xml" -> "text.xml"
            "json" -> "source.json"
            "lua" -> "source.lua"
            "py", "pyi" -> "source.python"
            "sh", "bash" -> "source.shell"
            "js", "ts", "jsx", "tsx" -> "source.ts"
            "css" -> "source.css"
            "html" -> "text.html.basic"
            "toml", "tml" -> "source.toml"
            else -> "text.plain"
        }
    }
    
    private fun getGrammarPathForScope(scope: String): String? {
        return when (scope) {
            "source.lua" -> "textmate/lua/syntaxes/lua.tmLanguage.json"
            "source.kotlin" -> "textmate/kotlin/syntaxes/kotlin.tmLanguage.json"
            "source.toml" -> "textmate/toml/syntaxes/toml.tmLanguage.json"
            else -> null
        }
    }

    private fun initializeLsp(file: File) {
        val servers = LspManager.getServersForFile(file)
        val scope = getScopeForFile(file)
        
        try {
            getGrammarPathForScope(scope)?.let { grammarPath ->
                GrammarRegistry.getInstance().loadGrammars(languages {
                    language(file.extension.lowercase()) {
                        this.scopeName = scope
                        this.grammar = grammarPath
                    }
                })
            }
        } catch (e: Exception) {
            LOG.error("Failed to load TextMate grammar", e)
        }

        if (servers.isEmpty()) {
            try {
                editor.setEditorLanguage(TextMateLanguage.create(scope, false))
            } catch (e: Exception) {
                // 忽略
            }
            return
        }

        progressBar.visibility = View.VISIBLE
        val workspaceRoot = projectDir ?: IProjectManager.getInstance().projectDir
        
        servers.forEach { server ->
            if (server is LuaServer) {
                val luaProjectPath = "/storage/emulated/0/AndroidIDEProjects/LuaProject/"
                server.customInitOptions = mapOf("Lua" to mapOf("workspace" to mapOf("library" to listOf(luaProjectPath))))
            }
        }
        
        val connector = BaseLspConnector(workspaceRoot, file, editor, servers)
        this.lspConnector = connector

        val uiDelegate = LspUIDelegate(viewLifecycleScope, connector, this, file).apply {
            isEnableInlayHint = true
            isEnableHover = true
            isEnableSignatureHelp = true
        }
        this.lspUiDelegate = uiDelegate

        this.contextMenuHandler = LspContextMenuHandler(viewLifecycleScope, connector) { f, l, c ->
            navigateTo(f, l, c)
        }
        
        viewLifecycleScope.launch {
            connector.connect(getScopeForFile(file))
            
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                if (connector.isConnected()) {
                    LspStatusMonitor.lifecycle("LSP", "${file.name} connected.")
                    
                    uiDelegate.attach(editor)
                    editor.subscribeEvent(ContentChangeEvent::class.java, LspDocumentSyncListener(connector, file))
                    
                    connector.lspEditor?.let { lspEd ->
                         val actionWindow = LspEditorTextActionWindow(lspEd, editor)
                         actionWindow.setOnMoreButtonClickListener { _, _ ->
                             uiDelegate.triggerCodeActions(editor.cursor.left())
                         }
                         this@LspEditorFragment.lspActionWindow = actionWindow
                         editor.replaceComponent(EditorTextActionWindow::class.java, actionWindow)
                    }
                    
                    editor.getComponent(EditorAutoCompletion::class.java).isEnabled = true
                    
                } else {
                    activity?.flashInfo("LSP Connection failed.")
                }
            }
        }
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_lsp_status) {
            showStatusPanel()
            return true
        }
        
        val delegate = lspUiDelegate ?: return handleNonLspMenuActions(item)

        return when (item.itemId) {
            R.id.action_save -> { saveFile(); true }
            R.id.action_undo -> { editor.undo(); true }
            R.id.action_redo -> { editor.redo(); true }
            R.id.action_search -> { editor.beginSearchMode(); true }
            
            R.id.action_goto_def -> { delegate.requestGoToDefinition(); true }
            R.id.action_goto_implementation -> { delegate.requestGoToImplementation(); true }
            R.id.action_goto_type_def -> { delegate.requestGoToTypeDefinition(); true }
            R.id.action_find_references -> { delegate.requestShowReferences(); true }
            R.id.action_document_highlight -> { delegate.requestDocumentHighlight(); true }
            R.id.action_format -> { delegate.requestFormatDocument(); true }
            R.id.action_rename -> { delegate.requestRenameSymbol(); true }
            R.id.action_document_symbols -> { delegate.requestDocumentOutline(); true }
            R.id.action_diagnostics_list -> { delegate.requestDiagnosticsList(); true }
            
            R.id.action_code_actions -> { 
                delegate.triggerCodeActions(editor.cursor.left())
                true 
            }
            R.id.action_toggle_inlay_hints -> {
                item.isChecked = !item.isChecked
                delegate.isEnableInlayHint = item.isChecked
                editor.invalidate()
                true
            }
            else -> false
        }
    }

    private fun handleNonLspMenuActions(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_save -> { saveFile(); true }
            R.id.action_undo -> { editor.undo(); true }
            R.id.action_redo -> { editor.redo(); true }
            R.id.action_search -> { editor.beginSearchMode(); true }
            else -> false
        }
    }

    private fun saveFile() {
        val targetFile = file ?: return
        viewLifecycleScope.launch(Dispatchers.IO) {
            try {
                with(ContentReadWrite) { editor.text.writeTo(targetFile) }
                withContext(Dispatchers.Main) { editor.markUnmodified() }
                lspConnector?.notifySave()
                withContext(Dispatchers.Main) { activity?.flashSuccess("Saved.") }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { activity?.flashError("Save failed: ${e.message}") }
            }
        }
    }

    private fun showStatusPanel() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { androidx.compose.material3.MaterialTheme { LspStatusPanel { dialog.dismiss() } } }
        })
        dialog.show()
    }
    
    // --- LspActionListener Implementation ---

    override fun navigateTo(file: File, line: Int, column: Int) {
        lifecycleScope.launch {
            if (file.absolutePath == this@LspEditorFragment.file?.absolutePath) {
                editor.setSelectionAround(line, column)
                editor.ensurePositionVisible(line, column)
            } else {
                openFile(file, line, column)
            }
        }
    }

    override fun showRenameDialog(currentName: String) {
        (view as? ViewGroup)?.let { container ->
            container.addView(ComposeView(requireContext()).apply {
                setContent {
                    var show by remember { mutableStateOf(true) }
                    if(show) {
                        RenameSymbolDialog(
                            oldName = currentName,
                            onConfirm = { newName ->
                                lspUiDelegate?.performRename(newName)
                                show = false
                                container.removeView(this)
                            },
                            onDismiss = {
                                show = false
                                container.removeView(this)
                            }
                        )
                    }
                }
            })
        }
    }

    override fun showDocumentOutline(connector: BaseLspConnector) {
        lifecycleScope.launch(Dispatchers.Default) {
            val symbols = connector.requestDocumentSymbols()
            if (symbols.isEmpty()) {
                withContext(Dispatchers.Main) { activity?.flashInfo("No symbols found") }
                return@launch
            }
            val flatList = mutableListOf<SymbolDisplayItem>()
            
            // 修复：抑制 SymbolInformation 字段过时警告
            @Suppress("DEPRECATION")
            fun process(item: Any, depth: Int) {
                when (item) {
                    is SymbolInformation -> flatList.add(SymbolDisplayItem(item.name, item.kind.name, item.location.range, depth))
                    is DocumentSymbol -> {
                        flatList.add(SymbolDisplayItem(item.name, item.kind.name, item.range, depth))
                        item.children?.forEach { process(it, depth + 1) }
                    }
                }
            }
            symbols.forEach { if (it.isLeft) process(it.left, 0) else process(it.right, 0) }
            withContext(Dispatchers.Main) {
                showBottomSheetList("Outline", flatList) { item ->
                    editor.setSelectionAround(item.range.start.line, item.range.start.character)
                    editor.ensurePositionVisible(item.range.start.line, item.range.start.character)
                }
            }
        }
    }
    
    override fun showReferencesList(connector: BaseLspConnector) {
        lifecycleScope.launch(Dispatchers.Default) {
            val refs = connector.requestReferences()
            if (refs.isEmpty()) {
                withContext(Dispatchers.Main) { activity?.flashInfo("No references found") }
                return@launch
            }
            val displayItems = refs.map { loc ->
                val path = LspActions.fixUriPath(loc.uri)
                SymbolDisplayItem(File(path).name, "${loc.range.start.line + 1}:${loc.range.start.character + 1}", loc.range, 0, path)
            }
            withContext(Dispatchers.Main) {
                showBottomSheetList("References", displayItems) { item ->
                    navigateTo(File(item.filePath!!), item.range.start.line, item.range.start.character)
                }
            }
        }
    }
    
    override fun showDiagnosticsList(connector: BaseLspConnector) {
        val diagnostics = connector.getDiagnostics()
        if (diagnostics.isEmpty()) {
            activity?.flashInfo("No problems found")
            return
        }
        val displayItems = diagnostics.map { SymbolDisplayItem(it.message, it.severity?.name ?: "INFO", it.range, 0) }
        showBottomSheetList("Diagnostics", displayItems) { item ->
            editor.setSelectionAround(item.range.start.line, item.range.start.character)
            editor.ensurePositionVisible(item.range.start.line, item.range.start.character)
        }
    }
    
    data class SymbolDisplayItem(val name: String, val detail: String, val range: org.eclipse.lsp4j.Range, val depth: Int, val filePath: String? = null)

    private fun showBottomSheetList(title: String, items: List<SymbolDisplayItem>, onClick: (SymbolDisplayItem) -> Unit) {
        val dialog = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_simple_list_sheet, null)
        view.findViewById<TextView>(R.id.sheet_title).text = title
        val recycler = view.findViewById<RecyclerView>(R.id.sheet_recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, vt: Int) = object : ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)) {}
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val item = items[position]
                holder.itemView.findViewById<TextView>(android.R.id.text1).text = "${"  ".repeat(item.depth)}${item.name}"
                holder.itemView.findViewById<TextView>(android.R.id.text2).text = item.detail
                holder.itemView.setOnClickListener { onClick(item); dialog.dismiss() }
            }
            override fun getItemCount() = items.size
        }
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleScope.launch { lspConnector?.disconnect() }
        lspUiDelegate?.detach()
        lspActionWindow?.dismiss()
        lspActionWindow = null
        lspConnector = null
        lspUiDelegate = null
        contextMenuHandler = null
    }
}