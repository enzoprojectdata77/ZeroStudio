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

package com.itsaky.androidide.lsp.servers

import android.content.Context
import com.itsaky.androidide.lsp.BaseLspServer
import com.itsaky.androidide.lsp.connection.ProcessStreamProvider
import com.itsaky.androidide.lsp.core.LspConnectionFactory
import com.itsaky.androidide.lsp.util.LspShellUtils
import com.itsaky.androidide.utils.Environment
import java.io.File
import com.itsaky.androidide.lsp.util.Logger

/**
 * An implementation of [BaseLspServer] for the Kotlin language.
 *
 * This server relies on the `kotlin-language-server` launcher script provided by AndroidIDE's
 * environment setup. This script correctly configures the Java environment and classpath
 * required to run the server.
 *
 * ## Work-flow Diagram
 * [connect()] -> [getConnectionFactory()] -> [ProcessStreamProvider] -> Executes [Environment.KOTLIN_LSP_LAUNCHER]
 *
 * @author android_zero
 */
class KotlinServer : BaseLspServer() {
    override val id: String = "kotlin-lsp"
    override val languageName: String = "Kotlin"
    override val serverName: String = "kotlin-language-server"
    override val supportedExtensions: List<String> = listOf("kt", "kts")

    /**
     * Checks if the Kotlin Language Server is properly installed by verifying the
     * existence and executability of its launcher script.
     */
    override fun isInstalled(context: Context): Boolean {
        return LspShellUtils.isTerminalEnvironmentReady() &&
               Environment.KOTLIN_LSP_LAUNCHER.exists() &&
               Environment.KOTLIN_LSP_LAUNCHER.canExecute()
    }

    /**
     * The installation of the Kotlin Language Server is handled by the main `ToolsManager`.
     * This method is a placeholder and could be used to trigger a re-installation if needed.
     */
    override fun install(context: Context) {
        // Installation is managed by com.itsaky.androidide.managers.ToolsManager.extractKotlinLanguageServer
        Logger.instance(javaClass.simpleName).info("Kotlin LSP installation is managed by ToolsManager.")
    }

    /**
     * Provides a connection factory that executes the Kotlin Language Server launcher script.
     */
    override fun getConnectionFactory(): LspConnectionFactory {
        return LspConnectionFactory { workingDir ->
            ProcessStreamProvider(
                command = listOf(Environment.KOTLIN_LSP_LAUNCHER.absolutePath),
                workingDir = workingDir,
                // The launcher script internally sets JAVA_HOME, but we can provide it as a fallback.
                env = mapOf("JAVA_HOME" to Environment.JAVA_HOME.absolutePath)
            )
        }
    }
        override fun isSupported(file: File): Boolean {
        return supportedExtensions.contains(file.getName().substringAfterLast("."))
    }
}