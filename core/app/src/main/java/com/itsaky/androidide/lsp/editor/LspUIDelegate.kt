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

import com.itsaky.androidide.lsp.BaseLspConnector
import com.itsaky.androidide.lsp.LspActions
import com.itsaky.androidide.lsp.util.Logger
import com.itsaky.androidide.lsp.util.LspStatusMonitor
import io.github.rosemoe.sora.graphics.inlayHint.ColorInlayHintRenderer
import io.github.rosemoe.sora.graphics.inlayHint.TextInlayHintRenderer
import io.github.rosemoe.sora.lang.styling.HighlightTextContainer
import io.github.rosemoe.sora.lang.styling.color.ConstColor
import io.github.rosemoe.sora.lang.styling.color.EditorColor
import io.github.rosemoe.sora.lang.styling.inlayHint.ColorInlayHint
import io.github.rosemoe.sora.lang.styling.inlayHint.InlayHintsContainer
import io.github.rosemoe.sora.lang.styling.inlayHint.TextInlayHint
import io.github.rosemoe.sora.lsp.editor.codeaction.CodeActionWindow
import io.github.rosemoe.sora.lsp.editor.diagnostics.LspDiagnosticTooltipLayout
import io.github.rosemoe.sora.lsp.editor.hover.HoverWindow
import io.github.rosemoe.sora.lsp.editor.signature.SignatureHelpWindow
import io.github.rosemoe.sora.lsp.utils.asLspPosition
import io.github.rosemoe.sora.lsp.utils.createTextDocumentIdentifier
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion
import io.github.rosemoe.sora.widget.component.EditorDiagnosticTooltipWindow
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.CodeAction
import org.eclipse.lsp4j.CodeActionContext
import org.eclipse.lsp4j.CodeActionParams
import org.eclipse.lsp4j.ColorInformation
import org.eclipse.lsp4j.Command
import org.eclipse.lsp4j.DocumentHighlight
import org.eclipse.lsp4j.DocumentHighlightKind
import org.eclipse.lsp4j.Hover
import org.eclipse.lsp4j.Location
import org.eclipse.lsp4j.Range
import org.eclipse.lsp4j.SignatureHelp
import org.eclipse.lsp4j.jsonrpc.messages.Either
import java.io.File
import java.util.concurrent.TimeUnit
import java.lang.ref.WeakReference
import org.eclipse.lsp4j.InlayHint as LspInlayHint
import io.github.rosemoe.sora.lang.styling.inlayHint.InlayHint as SoraInlayHint

/**
 * Manages UI components related to LSP features for a CodeEditor instance.
 *
 * It acts as the "View Controller" for LSP features, handling:
 * - Hover popups
 * - Signature help
 * - Code actions (Quick fixes)
 * - Inlay hints
 * - Document highlights
 * - Navigation actions (Go to Def, etc.)
 *
 * @author android_zero
 */
class LspUIDelegate(
    private val scope: CoroutineScope,
    private val connector: BaseLspConnector,
    private val actionListener: LspActionListener,
    private val file: File
) {
    private val LOG = Logger.instance("LspUIDelegate")

    // Retrieve LspEditor dynamically from the connector to ensure we get the connected instance
    private val lspEditor
        get() = connector.lspEditor

    // Weak references to UI components to avoid memory leaks if the editor is detached but delegate lives on
    private var currentEditorRef: WeakReference<CodeEditor?> = WeakReference(null)
    private var hoverWindowRef: WeakReference<HoverWindow?> = WeakReference(null)
    private var signatureHelpWindowRef: WeakReference<SignatureHelpWindow?> = WeakReference(null)
    private var codeActionWindowRef: WeakReference<CodeActionWindow?> = WeakReference(null)

    // Caches to avoid unnecessary UI updates
    private var cachedInlayHints: List<LspInlayHint>? = null
    private var cachedDocumentColors: List<ColorInformation>? = null

    var isEnableHover = true
        set(value) {
            if (field == value) return
            field = value
            if (!value) hoverWindowRef.get()?.setEnabled(false)
            else hoverWindowRef.get()?.setEnabled(true)
        }

    var isEnableSignatureHelp = true
        set(value) {
            if (field == value) return
            field = value
            if (!value) signatureHelpWindowRef.get()?.setEnabled(false)
            else signatureHelpWindowRef.get()?.setEnabled(true)
        }

    var isEnableInlayHint = true
        set(value) {
            if (field == value) return
            field = value
            val editor = currentEditorRef.get()
            if (value) {
                editor?.registerInlayHintRenderers(
                    TextInlayHintRenderer.DefaultInstance,
                    ColorInlayHintRenderer.DefaultInstance
                )
                // Trigger update if we have cached data
                updateInlinePresentations()
            } else {
                resetInlinePresentations()
            }
        }

    val isShowSignatureHelp
        get() = signatureHelpWindowRef.get()?.isShowing ?: false

    val isShowHover
        get() = hoverWindowRef.get()?.isShowing ?: false

    val isShowCodeActions
        get() = codeActionWindowRef.get()?.isShowing ?: false

    val hoverWindow
        get() = hoverWindowRef.get()

    val signatureHelpWindow
        get() = signatureHelpWindowRef.get()

    val codeActionWindow
        get() = codeActionWindowRef.get()


    /**
     * Attaches this delegate to a specific CodeEditor instance.
     * Initializes all popup windows and renderers.
     */
    fun attach(editor: CodeEditor) {
        // Detach previous editor if any
        detach()
        currentEditorRef = WeakReference(editor)

        val currentLspEditor = lspEditor
        if (currentLspEditor == null) {
            LOG.warn("LspEditor is null. Cannot attach UI components fully.")
            return
        }

        // 1. Initialize Hover Window
        if (isEnableHover) {
            val hover = HoverWindow(editor, currentLspEditor.coroutineScope)
            hoverWindowRef = WeakReference(hover)
        }

        // 2. Initialize Signature Help Window
        if (isEnableSignatureHelp) {
            val sigHelp = SignatureHelpWindow(editor, currentLspEditor.coroutineScope)
            signatureHelpWindowRef = WeakReference(sigHelp)
        }

        // 3. Initialize Code Action Window
        // Use the coroutine scope from LspProject/Editor to ensure lifecycle safety
        val codeAction = CodeActionWindow(currentLspEditor, editor)
        codeActionWindowRef = WeakReference(codeAction)

        // 4. Register Inlay Hint Renderers
        if (isEnableInlayHint) {
            editor.registerInlayHintRenderers(
                TextInlayHintRenderer.DefaultInstance,
                ColorInlayHintRenderer.DefaultInstance
            )
        }

        // 5. Configure Diagnostics Tooltip (Red underline popup)
        // We replace the default layout with one optimized for LSP (showing only message)
        try {
            val diagnosticWindow = editor.getComponent(EditorDiagnosticTooltipWindow::class.java)
            // Only replace if it's not already our custom layout
            if (diagnosticWindow.layout !is LspDiagnosticTooltipLayout) {
                diagnosticWindow.layout = LspDiagnosticTooltipLayout()
            }
        } catch (e: Exception) {
            LOG.error("Failed to configure diagnostic tooltip", e)
        }
        
        LOG.info("LspUIDelegate attached to editor.")
    }

    /**
     * Detaches from the current editor and cleans up resources/windows.
     */
    fun detach() {
        hoverWindowRef.get()?.let {
            it.setEnabled(false)
            it.dismiss()
        }
        hoverWindowRef.clear()

        signatureHelpWindowRef.get()?.let {
            it.setEnabled(false)
            it.dismiss()
        }
        signatureHelpWindowRef.clear()

        codeActionWindowRef.get()?.let {
            it.setEnabled(false)
            it.dismiss()
        }
        codeActionWindowRef.clear()

        resetInlinePresentations()
        currentEditorRef.clear()
    }

    // --- UI Display Methods ---

    fun showSignatureHelp(signatureHelp: SignatureHelp?) {
        val window = signatureHelpWindowRef.get() ?: return
        val editor = currentEditorRef.get() ?: return

        editor.post {
            if (signatureHelp == null || signatureHelp.signatures.isEmpty()) {
                window.dismiss()
            } else {
                window.show(signatureHelp)
            }
        }
    }

    fun showHover(hover: Hover?) {
        val window = hoverWindowRef.get() ?: return
        val editor = currentEditorRef.get() ?: return

        // Don't show hover if signature help is active to avoid clutter
        if (isShowSignatureHelp) {
            editor.post { window.dismiss() }
            return
        }

        editor.post {
            if (hover == null) {
                window.dismiss()
            } else {
                window.show(hover)
            }
        }
    }

    fun showCodeActions(range: Range, actions: List<Either<Command, CodeAction>>) {
        val window = codeActionWindowRef.get() ?: return
        val editor = currentEditorRef.get() ?: return
        val autoCompletion = editor.getComponent(EditorAutoCompletion::class.java)

        editor.post {
            // Don't show code actions if autocomplete is open
            if (actions.isEmpty() || autoCompletion.isShowing) {
                window.dismiss()
            } else {
                window.show(range, actions)
            }
        }
    }

    fun showDocumentHighlight(highlights: List<DocumentHighlight>?) {
        val editor = currentEditorRef.get() ?: return

        val container = if (highlights.isNullOrEmpty()) null else HighlightTextContainer().apply {
            highlights.forEach {
                val color = when (it.kind) {
                    DocumentHighlightKind.Write -> EditorColor(EditorColorScheme.TEXT_HIGHLIGHT_STRONG_BACKGROUND)
                    else -> EditorColor(EditorColorScheme.TEXT_HIGHLIGHT_BACKGROUND)
                }
                val borderColor = when (it.kind) {
                    DocumentHighlightKind.Write -> EditorColor(EditorColorScheme.TEXT_HIGHLIGHT_STRONG_BORDER)
                    else -> EditorColor(EditorColorScheme.TEXT_HIGHLIGHT_BORDER)
                }
                add(
                    HighlightTextContainer.HighlightText(
                        it.range.start.line, it.range.start.character,
                        it.range.end.line, it.range.end.character,
                        color, borderColor
                    )
                )
            }
        }

        editor.post { editor.highlightTexts = container }
    }

    fun showInlayHints(inlayHints: List<LspInlayHint>?) {
        if (!isEnableInlayHint) return
        val normalized = inlayHints.normalizeList()
        if (cachedInlayHints != normalized) {
            cachedInlayHints = normalized
            updateInlinePresentations()
        }
    }

    fun showDocumentColors(documentColors: List<ColorInformation>?) {
        if (!isEnableInlayHint) return
        val normalized = documentColors.normalizeList()
        if (cachedDocumentColors != normalized) {
            cachedDocumentColors = normalized
            updateInlinePresentations()
        }
    }

    // --- Actions Triggered by User/Events ---

    /**
     * Triggers code actions (Quick Fixes) at the specified position.
     * Usually called when the user clicks a "lightbulb" icon or uses a shortcut.
     */
    fun triggerCodeActions(position: CharPosition) {
        val currentLspEditor = lspEditor ?: return
        
        // Use Default dispatcher for background work
        scope.launch(Dispatchers.Default) {
            try {
                // 1. Get diagnostics at the current line to provide context to the server
                val diagnostics = currentLspEditor.diagnostics?.filter {
                    val startLine = it.range.start.line
                    val endLine = it.range.end.line
                    position.line in startLine..endLine
                } ?: emptyList()

                // 2. Prepare params
                // We use the same position for start and end, effectively a caret position request
                val lspPos = position.asLspPosition()
                val params = CodeActionParams(
                    currentLspEditor.uri.createTextDocumentIdentifier(),
                    Range(lspPos, lspPos),
                    CodeActionContext(diagnostics)
                )

                LOG.info("Requesting code actions for ${file.name} at ${position.line}:${position.column}")

                // 3. Request from server
                val future = currentLspEditor.requestManager.codeAction(params)
                val actions = future?.get(3000, TimeUnit.MILLISECONDS) // 3s timeout

                // 4. Show window if actions exist
                if (!actions.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        showCodeActions(params.range, actions)
                    }
                } else {
                    LOG.info("No code actions available.")
                    withContext(Dispatchers.Main) {
                        // Ensure window is hidden if no actions found
                        codeActionWindowRef.get()?.dismiss()
                    }
                }
            } catch (e: Exception) {
                LOG.error("Failed to trigger code actions", e)
            }
        }
    }

    fun requestGoToDefinition() {
        LspActions.goToDefinition(scope, connector) { f, l, c ->
            actionListener.navigateTo(f, l, c)
        }
    }

    fun requestGoToImplementation() {
        scope.launch(Dispatchers.Default) {
            try {
                LspStatusMonitor.info("LSP", "Requesting Implementation...")
                val result = connector.requestImplementation()
                handleLocationResult(result, "Implementation")
            } catch (e: Exception) {
                LOG.error("Go to Implementation failed", e)
            }
        }
    }

    fun requestGoToTypeDefinition() {
        scope.launch(Dispatchers.Default) {
            try {
                LspStatusMonitor.info("LSP", "Requesting Type Definition...")
                val result = connector.requestTypeDefinition()
                handleLocationResult(result, "Type Definition")
            } catch (e: Exception) {
                LOG.error("Go to Type Definition failed", e)
            }
        }
    }

    private suspend fun handleLocationResult(
        result: Either<List<Location>, List<org.eclipse.lsp4j.LocationLink>>,
        actionName: String
    ) {
        val locations = if (result.isLeft) result.left else result.right.map {
            Location(it.targetUri, it.targetSelectionRange)
        }

        if (locations.isNotEmpty()) {
            val loc = locations[0]
            val path = LspActions.fixUriPath(loc.uri)
            withContext(Dispatchers.Main) {
                actionListener.navigateTo(File(path), loc.range.start.line, loc.range.start.character)
            }
        } else {
            LspStatusMonitor.warn("LSP", "No $actionName found")
            withContext(Dispatchers.Main) {
                // Feedback to user could be added here (Toast/Flashbar)
            }
        }
    }

    fun requestShowReferences() {
        actionListener.showReferencesList(connector)
    }

    fun requestDocumentHighlight() {
        scope.launch {
            val highlights = connector.requestDocumentHighlight()
            withContext(Dispatchers.Main) {
                showDocumentHighlight(highlights)
            }
        }
    }

    fun requestFormatDocument() {
        LspActions.formatDocument(scope, connector)
    }

    fun requestRenameSymbol() {
        val editor = currentEditorRef.get() ?: return
        val left = editor.cursor.left()
        
        // Find the word bounds at cursor
        val range = editor.getWordRange(left.line, left.column)

        if (range.start.index >= range.end.index) {
            // Invalid selection or no word found
            return
        }

        val currentName = editor.text.substring(range.start.index, range.end.index)
        actionListener.showRenameDialog(currentName)
    }

    fun performRename(newName: String) {
        LspActions.renameSymbol(scope, connector, newName)
    }

    fun requestDocumentOutline() {
        actionListener.showDocumentOutline(connector)
    }

    fun requestDiagnosticsList() {
        actionListener.showDiagnosticsList(connector)
    }

    // --- Private Helpers ---

    private fun updateInlinePresentations() {
        val editor = currentEditorRef.get() ?: return
        
        // Combine hints and colors into one container
        val container = InlayHintsContainer()
        cachedInlayHints?.inlayHintToDisplay()?.forEach(container::add)
        cachedDocumentColors?.colorInfoToDisplay()?.forEach(container::add)
        
        editor.post { 
            editor.inlayHints = if (container.isEmpty()) null else container 
        }
    }

    private fun resetInlinePresentations() {
        cachedInlayHints = null
        cachedDocumentColors = null
        currentEditorRef.get()?.let {
            it.post { it.inlayHints = null }
        }
    }
}

// --- Extensions for Data Conversion ---

private fun <T> List<T>?.normalizeList(): List<T>? = if (this.isNullOrEmpty()) null else this

private fun List<LspInlayHint>.inlayHintToDisplay(): List<SoraInlayHint> {
    return map {
        val label = it.label
        // Handle Either<String, List<InlayHintLabelPart>>
        val text = if (label.isLeft) label.left else label.right.firstOrNull()?.value ?: ""
        TextInlayHint(it.position.line, it.position.character, text)
    }
}

private fun List<ColorInformation>.colorInfoToDisplay(): List<SoraInlayHint> {
    return map {
        ColorInlayHint(
            it.range.start.line,
            it.range.start.character,
            ConstColor(
                android.graphics.Color.argb(
                    (it.color.alpha * 255).toInt(),
                    (it.color.red * 255).toInt(),
                    (it.color.green * 255).toInt(),
                    (it.color.blue * 255).toInt()
                )
            )
        )
    }
}

private fun InlayHintsContainer.isEmpty(): Boolean = this.getLineNumbers().isEmpty()