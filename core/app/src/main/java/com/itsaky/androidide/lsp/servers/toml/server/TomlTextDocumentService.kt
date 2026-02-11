package com.itsaky.androidide.lsp.servers.toml.server

import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.TextDocumentService
import java.util.concurrent.CompletableFuture

class TomlTextDocumentService(private val server: TomlLanguageServer) : TextDocumentService {

    override fun didOpen(params: DidOpenTextDocumentParams) {
        val uri = params.textDocument.uri
        val text = params.textDocument.text
        TomlDocumentCache.update(uri, text)
        refreshDiagnostics(uri, text)
    }

    override fun didChange(params: DidChangeTextDocumentParams) {
        val uri = params.textDocument.uri
        if (params.contentChanges.isNotEmpty()) {
            val text = params.contentChanges[0].text
            TomlDocumentCache.update(uri, text)
            refreshDiagnostics(uri, text)
        }
    }

    override fun didClose(params: DidCloseTextDocumentParams) {
        TomlDocumentCache.remove(params.textDocument.uri)
    }

    override fun didSave(params: DidSaveTextDocumentParams) {}

    override fun completion(params: CompletionParams): CompletableFuture<Either<List<CompletionItem>, CompletionList>> {
        return CompletableFuture.supplyAsync {
            val content = TomlDocumentCache.get(params.textDocument.uri) ?: ""
            Either.forLeft(TomlCompletionProvider.compute(content, params.position))
        }
    }

    override fun hover(params: HoverParams): CompletableFuture<Hover> {
        return CompletableFuture.supplyAsync {
            val content = TomlDocumentCache.get(params.textDocument.uri) ?: return@supplyAsync null
            TomlHoverProvider.compute(content, params.position)
        }
    }

    override fun semanticTokensFull(params: SemanticTokensParams): CompletableFuture<SemanticTokens> {
        return CompletableFuture.supplyAsync {
            val content = TomlDocumentCache.get(params.textDocument.uri) ?: return@supplyAsync SemanticTokens(emptyList())
            val tokens = TomlSemanticTokens(content).compute()
            SemanticTokens(tokens)
        }
    }

    private fun refreshDiagnostics(uri: String, content: String) {
        val diagnostics = TomlDiagnostics.compute(content)
        server.getClient()?.publishDiagnostics(PublishDiagnosticsParams(uri, diagnostics))
    }
}