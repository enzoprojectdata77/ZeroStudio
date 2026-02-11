package com.itsaky.androidide.lsp.servers.toml.server

import com.itsaky.androidide.lsp.util.Logger
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageClientAware
import org.eclipse.lsp4j.services.LanguageServer
import org.eclipse.lsp4j.services.TextDocumentService
import org.eclipse.lsp4j.services.WorkspaceService
import java.util.concurrent.CompletableFuture

class TomlLanguageServer : LanguageServer, LanguageClientAware {
    private val LOG = Logger.instance("TomlLanguageServer")
    private var client: LanguageClient? = null
    
    // 初始化服务
    private val textDocumentService = TomlTextDocumentService(this)
    private val workspaceService = TomlWorkspaceService(this)

    override fun initialize(params: InitializeParams): CompletableFuture<InitializeResult> {
        val caps = ServerCapabilities()
        caps.textDocumentSync = Either.forLeft(TextDocumentSyncKind.Full)
        
        // 启用补全
        caps.completionProvider = CompletionOptions(false, listOf(".", "="))
        
        // 启用悬停
        caps.hoverProvider = Either.forLeft(true)
        
        // 启用语义高亮
        val legend = SemanticTokensLegend(
            listOf("keyword", "string", "number", "comment", "property", "boolean", "operator", "type"),
            emptyList()
        )
        caps.semanticTokensProvider = SemanticTokensWithRegistrationOptions(legend, true)

        return CompletableFuture.completedFuture(InitializeResult(caps))
    }

    override fun shutdown(): CompletableFuture<Any> = CompletableFuture.completedFuture(null)
    override fun exit() {}
    override fun getTextDocumentService(): TextDocumentService = textDocumentService
    override fun getWorkspaceService(): WorkspaceService = workspaceService
    override fun connect(client: LanguageClient) { this.client = client }
    fun getClient() = client
}