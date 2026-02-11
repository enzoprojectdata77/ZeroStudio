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

package com.itsaky.androidide.lsp

import com.itsaky.androidide.lsp.util.Logger
import io.github.rosemoe.sora.lang.EmptyLanguage
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.lsp.client.languageserver.serverdefinition.CustomLanguageServerDefinition
import io.github.rosemoe.sora.lsp.client.languageserver.wrapper.EventHandler
import io.github.rosemoe.sora.lsp.editor.LspEditor
import io.github.rosemoe.sora.lsp.editor.LspEventManager
import io.github.rosemoe.sora.lsp.editor.LspProject
import io.github.rosemoe.sora.lsp.requests.Timeout
import io.github.rosemoe.sora.lsp.requests.Timeouts
import io.github.rosemoe.sora.widget.CodeEditor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.jsonrpc.messages.Either3
import java.io.File
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 核心 LSP 连接器。
 * 修复了语言包装逻辑，确保 LspLanguage 不会被 TextMateLanguage 覆盖。
 *
 * @author android_zero
 */
class BaseLspConnector(
    private val projectDir: File,
    private val file: File,
    private val codeEditor: CodeEditor,
    private val servers: List<BaseLspServer>
) {
    var lspEditor: LspEditor? = null

    companion object {
        private val LOG = Logger.instance("BaseLspConnector")
        private val projectCache = ConcurrentHashMap<String, LspProject>()
    }

    fun isConnected(): Boolean = lspEditor?.isConnected ?: false

    suspend fun connect(textMateScope: String) = withContext(Dispatchers.IO) {
        if (servers.isEmpty()) {
            LOG.warn("No LSP servers provided for ${file.name}. Aborting connection.")
            return@withContext
        }

        runCatching {
            val projectPath = projectDir.absolutePath
            val fileExt = file.extension

            val project = projectCache.getOrPut(projectPath) {
                LOG.info("Creating new LspProject for path: $projectPath")
                LspProject(projectPath)
            }

            servers.forEach { server ->
                val serverDef = createServerDefinition(fileExt, server)
                try {
                    project.addServerDefinition(serverDef)
                } catch (e: IllegalArgumentException) {
                    // Ignore duplicate definition
                }
            }

            // 1. 获取或创建 LspEditor
            val editorInstance = project.getOrCreateEditor(file.absolutePath)
            lspEditor = editorInstance

            // 2. 创建基础语言 (TextMate 或 Empty)
            // 注意：不要直接 codeEditor.setEditorLanguage()，而是赋值给 lspEditor.wrapperLanguage
            val baseLanguage = try {
                TextMateLanguage.create(textMateScope, false)
            } catch (e: Exception) {
                LOG.error("Error creating TextMateLanguage. Fallback to EmptyLanguage.", e)
                EmptyLanguage()
            }

            // 3. 在 UI 线程配置 Editor
            withContext(Dispatchers.Main) {
                // 将 TextMate 语言作为包装语言传给 LSP 框架
                // 这样 LSP 框架会接管代码补全和高亮，同时利用 TextMate 做语法高亮
                editorInstance.wrapperLanguage = baseLanguage
                
                // 将 View 绑定到 LspEditor，这会自动设置 editor.language = LspLanguage(wrapper)
                editorInstance.editor = codeEditor
                
                // 启用内联提示
                editorInstance.isEnableInlayHint = true 
            }

            if (isConnected()) {
                LOG.debug("LSP server already connected for ${file.name}")
                return@withContext
            }

            servers.forEach { launch { it.beforeConnect() } }

            LOG.info("Connecting to ${servers.size} server(s) for ${file.name}...")
            
            // 连接服务器
            editorInstance.connectWithTimeout()
            
            // 发送工作区变更通知
            val workspaceFolders = listOf(
                WorkspaceFolder(projectDir.toURI().toString(), projectDir.name)
            )
            val event = WorkspaceFoldersChangeEvent()
            event.added = workspaceFolders
            val params = DidChangeWorkspaceFoldersParams()
            params.event = event
            
            editorInstance.requestManager.didChangeWorkspaceFolders(params)
            editorInstance.openDocument()

            servers.forEach { launch { it.connectionSuccess(this@BaseLspConnector) } }
            LOG.info("Successfully connected to LSP server(s) for ${file.name}")

        }.onFailure { e ->
            LOG.error("Failed to connect LSP for ${file.name}", e)
            // 失败回退：直接设置 TextMate 语言，确保至少有高亮
            withContext(Dispatchers.Main) {
                try {
                    codeEditor.setEditorLanguage(TextMateLanguage.create(textMateScope, false))
                } catch (ex: Exception) {
                    codeEditor.setEditorLanguage(EmptyLanguage())
                }
            }
            servers.forEach { launch { it.connectionFailure(e.message) } }
        }
    }

    private fun createServerDefinition(fileExt: String, server: BaseLspServer): CustomLanguageServerDefinition {
        return object : CustomLanguageServerDefinition(
            fileExt,
            { workingDir -> server.getConnectionFactory().create(File(workingDir)) },
            server.serverName,
            null,
            server.supportedExtensions
        ) {
            override fun getInitializationOptions(uri: URI?): Any? = server.getInitializationOptions(uri)
            override val eventListener: EventHandler.EventListener get() = server
        }
    }

    fun getEventManager(): LspEventManager? = lspEditor?.eventManager

    fun getCapabilities(): ServerCapabilities? {
        return try {
            lspEditor?.languageServerWrapper?.getServerCapabilities()
        } catch (e: Exception) {
            null
        }
    }

    // ================== Capabilities Checks ==================
    fun isDefinitionSupported() = checkCap { it.definitionProvider }
    fun isTypeDefinitionSupported() = checkCap { it.typeDefinitionProvider }
    fun isImplementationSupported() = checkCap { it.implementationProvider }
    fun isReferencesSupported() = checkCap { it.referencesProvider }
    fun isDocumentSymbolSupported() = checkCap { it.documentSymbolProvider }
    fun isRenameSupported() = checkCap { it.renameProvider }
    fun isFormattingSupported() = checkCap { it.documentFormattingProvider }
    fun isCodeActionSupported() = checkCap { it.codeActionProvider }

    private fun checkCap(predicate: (ServerCapabilities) -> Any?): Boolean {
        val caps = getCapabilities() ?: return false
        val provider = predicate(caps) ?: return false
        if (provider is Either<*, *>) {
             return if (provider.isLeft) provider.left == true else provider.right != null
        }
        return true
    }

    // ================== Request Methods (Delegate to LspEditor/RequestManager) ==================

    suspend fun requestDefinition(): Either<List<Location>, List<LocationLink>> {
        return withContext(Dispatchers.Default) {
            val params = DefinitionParams(
                TextDocumentIdentifier(file.absolutePath),
                Position(codeEditor.cursor.leftLine, codeEditor.cursor.leftColumn)
            )
            val future = lspEditor!!.requestManager!!.definition(params)
            future!!.get(Timeout[Timeouts.DEFINITION].toLong(), TimeUnit.MILLISECONDS)
        }
    }

    suspend fun requestTypeDefinition(): Either<List<Location>, List<LocationLink>> {
        return withContext(Dispatchers.Default) {
            val params = TypeDefinitionParams(
                TextDocumentIdentifier(file.absolutePath),
                Position(codeEditor.cursor.leftLine, codeEditor.cursor.leftColumn)
            )
            val future = lspEditor!!.requestManager!!.typeDefinition(params)
            future!!.get(Timeout[Timeouts.DEFINITION].toLong(), TimeUnit.MILLISECONDS)
        }
    }

    suspend fun requestImplementation(): Either<List<Location>, List<LocationLink>> {
        return withContext(Dispatchers.Default) {
            val params = ImplementationParams(
                TextDocumentIdentifier(file.absolutePath),
                Position(codeEditor.cursor.leftLine, codeEditor.cursor.leftColumn)
            )
            val future = lspEditor!!.requestManager!!.implementation(params)
            future!!.get(Timeout[Timeouts.DEFINITION].toLong(), TimeUnit.MILLISECONDS)
        }
    }
    
    suspend fun requestReferences(): List<Location> {
        return withContext(Dispatchers.Default) {
            val params = ReferenceParams(
                TextDocumentIdentifier(file.absolutePath),
                Position(codeEditor.cursor.leftLine, codeEditor.cursor.leftColumn),
                ReferenceContext(true)
            )
            val future = lspEditor!!.requestManager!!.references(params)
            val result = future!!.get(Timeout[Timeouts.REFERENCES].toLong(), TimeUnit.MILLISECONDS)
            result.filterNotNull()
        }
    }

    suspend fun requestDocumentSymbols(): List<Either<SymbolInformation, DocumentSymbol>> {
        return withContext(Dispatchers.Default) {
            val params = DocumentSymbolParams(TextDocumentIdentifier(file.absolutePath))
            val future = lspEditor!!.requestManager!!.documentSymbol(params)
            val result = future!!.get(Timeout[Timeouts.SYMBOLS].toLong(), TimeUnit.MILLISECONDS)
            result ?: emptyList()
        }
    }
    
    suspend fun requestDocumentHighlight(): List<DocumentHighlight> {
        return withContext(Dispatchers.Default) {
            val params = DocumentHighlightParams(
                TextDocumentIdentifier(file.absolutePath),
                Position(codeEditor.cursor.leftLine, codeEditor.cursor.leftColumn)
            )
            val future = lspEditor!!.requestManager!!.documentHighlight(params)
            val result = future!!.get(Timeout[Timeouts.DOC_HIGHLIGHT].toLong(), TimeUnit.MILLISECONDS)
            result ?: emptyList()
        }
    }

    suspend fun requestRenameSymbol(newName: String): WorkspaceEdit {
        return withContext(Dispatchers.Default) {
            val params = RenameParams(
                TextDocumentIdentifier(file.absolutePath),
                Position(codeEditor.cursor.leftLine, codeEditor.cursor.leftColumn),
                newName
            )
            val future = lspEditor!!.requestManager!!.rename(params)
            future!!.get(Timeout[Timeouts.EXECUTE_COMMAND].toLong(), TimeUnit.MILLISECONDS)
        }
    }

    fun getDiagnostics(): List<Diagnostic> {
        return lspEditor?.diagnostics ?: emptyList()
    }

    suspend fun notifySave() {
        lspEditor?.saveDocument()
    }
    
    suspend fun disconnect() {
        runCatching {
            lspEditor?.disposeAsync()
            lspEditor = null
            LOG.info("LSP connection for ${file.name} disconnected.")
        }
    }
}