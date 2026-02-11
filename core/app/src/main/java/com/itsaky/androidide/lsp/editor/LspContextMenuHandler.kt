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

import android.view.Menu
import com.itsaky.androidide.R
import com.itsaky.androidide.lsp.BaseLspConnector
import com.itsaky.androidide.lsp.LspActions
import com.itsaky.androidide.lsp.util.Logger
import io.github.rosemoe.sora.event.CreateContextMenuEvent
import io.github.rosemoe.sora.widget.CodeEditor
import java.io.File
import kotlinx.coroutines.CoroutineScope

/**
 * Handles the creation of the editor's context menu, dynamically adding LSP-related actions
 * based on the server's capabilities.
 *
 * @property scope The [CoroutineScope] to launch actions in.
 * @property connector The active [BaseLspConnector] instance.
 * @property onJumpRequest A callback to handle navigation requests (e.g., from "Go to Definition").
 *
 * @author android_zero
 */
class LspContextMenuHandler(
    private val scope: CoroutineScope,
    private val connector: BaseLspConnector,
    private val onJumpRequest: (file: File, line: Int, column: Int) -> Unit
) {

    private val LOG = Logger.instance("LspMenuHandler")

    /**
     * This method is called when the editor is about to show its context menu.
     * It inspects the connected LSP server's capabilities and adds relevant menu items.
     */
    fun handleContextMenu(event: CreateContextMenuEvent) {
        if (!connector.isConnected()) return

        val menu = event.menu
        
        // Add a separator if other items already exist, for visual clarity.
        if (menu.size() > 0) {
            menu.add(Menu.NONE, Menu.NONE, 99, "LSP Actions").apply { isEnabled = false }
        }

        // Add "Go to Definition" if supported
        // BaseLspConnector must expose these boolean checks based on ServerCapabilities
        if (connector.isDefinitionSupported()) {
            menu.add(MENU_GROUP_LSP, ID_GOTO_DEFINITION, 100, R.string.go_to_definition).apply {
                setIcon(com.itsaky.androidide.R.drawable.ic_open_with)
                setOnMenuItemClickListener {
                    LOG.debug("Go to Definition action triggered from context menu.")
                    LspActions.goToDefinition(scope, connector, onJumpRequest)
                    true
                }
            }
        }

        // Add "Rename Symbol" if supported
        if (connector.isRenameSupported()) {
            menu.add(MENU_GROUP_LSP, ID_RENAME_SYMBOL, 101, R.string.rename_symbol).apply {
                setIcon(com.itsaky.androidide.R.drawable.ic_edit)
                setOnMenuItemClickListener {
                    LOG.debug("Rename Symbol action triggered from context menu.")
                    // Call the rename dialog logic, here triggering via LspEditorFragment mechanism usually,
                    // but for context menu we might need to expose the callback or use LspActions to trigger generic flow
                    // For now, we assume the fragment handles it via menu bar, context menu integration usually requires
                    // passing the UI handler back.
                    // Simplified:
                    // (event.editor.context as? LspActionHandler)?.requestRename()
                    true
                }
            }
        }

        // Add "Format Document" if supported
        if (connector.isFormattingSupported()) {
            menu.add(MENU_GROUP_LSP, ID_FORMAT_DOC, 102, R.string.format_document).apply {
                setIcon(com.itsaky.androidide.R.drawable.ic_format_code)
                setOnMenuItemClickListener {
                    LOG.debug("Format Document action triggered from context menu.")
                    LspActions.formatDocument(scope, connector)
                    true
                }
            }
        }
    }

    companion object {
        private const val MENU_GROUP_LSP = 100
        private const val ID_GOTO_DEFINITION = 10001
        private const val ID_RENAME_SYMBOL = 10002
        private const val ID_FORMAT_DOC = 10003
    }
}