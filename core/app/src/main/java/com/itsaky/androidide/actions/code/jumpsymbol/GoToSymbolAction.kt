package com.itsaky.androidide.actions.code.jumpsymbol

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorRelatedAction
import com.itsaky.androidide.tasks.launchAsyncWithProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * An action to parse the current file using Tree-Sitter and display a bottom sheet with all symbols.
 * 
 * Supports Java, Kotlin, and can be extended.
 *
 * @author android_zero
 */
class GoToSymbolAction(private val context: Context, override val order: Int) : EditorRelatedAction() {
    
    override val id: String = "ide.editor.cursor.go_to_symbol"

    init {
        label = context.getString(R.string.action_show_code_outline) // 确保 strings.xml 有这个字符串
        icon = ContextCompat.getDrawable(context, R.drawable.ic_edit_code_outline_action) // 确保有这个图标
    }

    override fun prepare(data: ActionData) {
        super.prepare(data)
        val editor = data.getEditor()
        visible = editor != null
        enabled = visible
    }

    override suspend fun execAction(data: ActionData): Boolean {
        val editor = data.getEditor() ?: return false
        val activity = editor.context as? AppCompatActivity ?: return false
        val file = data.get(File::class.java) ?: return false

        activity.lifecycleScope.launchAsyncWithProgress(
            configureFlashbar = { builder, _ ->
                builder.message("Parsing symbols...") // 建议放入 strings.xml
            }
        ) { _, _ ->
            val code = editor.text.toString()
            
            // 使用 Tree-Sitter 解析，避免 Unsafe 崩溃
            val symbols = withContext(Dispatchers.IO) {
                TreeSitterSymbolResolver.parseSymbols(context, file, code)
            }

            withContext(Dispatchers.Main) {
                if (symbols.isNotEmpty()) {
                    val sheet = SymbolListBottomSheet(symbols) { symbol ->
                        // Navigate to symbol
                        // 使用 setSelectionRegion 确保选中整行或者高亮开始位置
                        editor.setSelection(symbol.line, 0)
                        editor.ensurePositionVisible(symbol.line, 0)
                        
                        // 可选：高亮选中的行
                        editor.cursorAnimator.start()
                    }
                    sheet.show(activity.supportFragmentManager, "SymbolListBottomSheet")
                } else {
                    com.itsaky.androidide.utils.flashInfo(context.getString(R.string.msg_no_symbols_found))
                }
            }
        }
        return true
    }
}