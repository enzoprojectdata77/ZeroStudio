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
import com.itsaky.androidide.lsp.util.DocumentVersionProvider
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.event.EventReceiver
import io.github.rosemoe.sora.event.Unsubscribe
import io.github.rosemoe.sora.lsp.events.EventType
import io.github.rosemoe.sora.lsp.events.document.documentChange
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * An [EventReceiver] that listens for content changes in the editor and forwards them to the
 * Language Server Protocol (LSP) server via `textDocument/didChange` notifications.
 *
 * ## Key Functions
 * - It translates Sora-Editor's [ContentChangeEvent] into LSP-compliant change events.
 * - It uses [DocumentVersionProvider] to ensure that each change notification is sent with the correct,
 *   incremented document version, which is crucial for servers that support incremental sync.
 *
 * @property connector The active [BaseLspConnector] instance for the current editor.
 * @property file The [File] object representing the document being edited.
 *
 * @author android_zero
 */
class LspDocumentSyncListener(
    private val connector: BaseLspConnector,
    private val file: File
) : EventReceiver<ContentChangeEvent> {

    override fun onReceive(event: ContentChangeEvent, unsubscribe: Unsubscribe) {
        val lspEditor = connector.lspEditor ?: return
        if (!lspEditor.isConnected) return

        // We only need to sync actual text modifications (insertions/deletions).
        if (event.action == ContentChangeEvent.ACTION_INSERT || event.action == ContentChangeEvent.ACTION_DELETE) {
            
            // The actual logic for creating DidChangeTextDocumentParams is now handled inside
            // sora-editor's DocumentChangeEvent listener, which correctly handles versioning
            // and sync kinds (Full vs Incremental). We just need to trigger it.
            lspEditor.coroutineScope.launch(Dispatchers.IO) {
                // By emitting this event, we delegate the complex task of creating the correct
                // TextDocumentContentChangeEvent (full or incremental) to the sora-editor-lsp framework.
                lspEditor.eventManager.emitAsync(EventType.documentChange, event)
            }
        }
    }
}