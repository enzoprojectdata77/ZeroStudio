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

package com.itsaky.androidide.lsp.window

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import io.github.rosemoe.sora.event.*
import io.github.rosemoe.sora.lsp.editor.LspEditor
import io.github.rosemoe.sora.lsp.events.EventType
import io.github.rosemoe.sora.lsp.events.diagnostics.publishDiagnostics
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.component.EditorBuiltinComponent
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

/**
 * 使用 Compose 实现的全功能多模态 LSP 文本操作窗口。
 * 替换 Sora 默认的 EditorTextActionWindow，集成 LSP Quick Fix 功能。
 *
 * @author android_zero
 */
class LspEditorTextActionWindow(
    private val lspEditor: LspEditor,
    private val editor: CodeEditor
) : EditorTextActionWindow(editor), EditorBuiltinComponent {

    private val composeView = ComposeView(editor.context)
    private var isEnabled = true
    private var lastCause = 0
    private var lastPosition = -1
    private var lastScrollTime = 0L
    
    // 回调接口，用于在 Fragment 中处理点击
    private var onMoreClickListener: ((View, LspEditor) -> Unit)? = null

    init {
        // 设置 Compose 内容
        composeView.setContent {
            ActionWindowContent()
        }
        
        // 修复：使用 setter 方法设置内容视图
        setContentView(composeView)
        
        // 设置默认阴影和样式
        popup.elevation = 8f.dpToPx(editor.context)
        // 移除默认动画，使用 Compose 自带或默认淡入淡出
        popup.animationStyle = 0
        
        // 重新注册事件 (使用 createSubEventManager 避免与父类冲突，虽然父类也会注册一遍)
        val eventManager = editor.createSubEventManager()
        eventManager.subscribeAlways(SelectionChangeEvent::class.java, this::onSelectionChange)
        eventManager.subscribeAlways(ScrollEvent::class.java, this::onEditorScroll)
    }
    
    fun setOnMoreButtonClickListener(listener: (View, LspEditor) -> Unit) {
        this.onMoreClickListener = listener
    }

    @Composable
    private fun ActionWindowContent() {
        val backgroundColor = Color(editor.colorScheme.getColor(EditorColorScheme.TEXT_ACTION_WINDOW_BACKGROUND))
        val iconColor = Color(editor.colorScheme.getColor(EditorColorScheme.TEXT_ACTION_WINDOW_ICON_COLOR))
        
        Row(
            modifier = Modifier
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 全选
            ActionButton(Icons.Default.SelectAll, "Select All", iconColor) { editor.selectAll() }
            
            if (editor.cursor.isSelected) {
                // 剪切
                if (editor.isEditable) ActionButton(Icons.Default.ContentCut, "Cut", iconColor) { editor.cutText() }
                // 复制
                ActionButton(Icons.Default.ContentCopy, "Copy", iconColor) { editor.copyText() }
            }

            // 粘贴
            if (editor.isEditable && editor.hasClip()) {
                ActionButton(Icons.Default.ContentPaste, "Paste", iconColor) {
                    editor.pasteText()
                    // 粘贴后通常取消选择并移动光标
                    editor.setSelection(editor.cursor.rightLine, editor.cursor.rightColumn)
                }
            }
            
            // LSP 快速修复 / 更多
            ActionButton(Icons.Default.MoreHoriz, "More / Quick Fix", iconColor) {
                onMoreClicked()
                dismiss()
            }
        }
    }

    @Composable
    private fun ActionButton(icon: ImageVector, contentDescription: String, tint: Color, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(20.dp))
        }
    }

    private fun onMoreClicked() {
        onMoreClickListener?.invoke(composeView, lspEditor)
    }

    // --- 事件处理逻辑 (需要 override 父类方法以覆盖默认行为) ---

    // 修复：添加 override 修饰符
    override fun onSelectionChange(event: SelectionChangeEvent) {
        if (editor.eventHandler.hasAnyHeldHandle()) return
        lastCause = event.cause
        
        if (event.isSelected) {
            editor.postInLifecycle { displayWindow() }
        } else {
            if (event.cause == SelectionChangeEvent.CAUSE_TAP && event.left.index == lastPosition) {
                editor.postInLifecycle { displayWindow() }
            } else {
                dismiss()
            }
            lastPosition = if (event.cause == SelectionChangeEvent.CAUSE_TAP) event.left.index else -1
        }
    }

    // 修复：添加 override 修饰符
    override fun onEditorScroll(event: ScrollEvent) {
        val now = System.currentTimeMillis()
        if (now - lastScrollTime < 200) {
            dismiss()
        }
        lastScrollTime = now
    }

    override fun displayWindow() {
        if (!isEnabled || editor.isInMouseMode) return
        
        // 计算位置逻辑
        val cursor = editor.cursor
        val dpUnit = editor.dpUnit
        
        // 测量宽度
        composeView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
                           View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val viewWidth = composeView.measuredWidth
        val viewHeight = composeView.measuredHeight
        
        val leftX = editor.getOffset(cursor.leftLine, cursor.leftColumn)
        val rightX = editor.getOffset(cursor.rightLine, cursor.rightColumn)
        
        val panelX = ((leftX + rightX) / 2f - viewWidth / 2f).toInt()
        val rowTop = editor.getRowTop(cursor.leftLine) - editor.offsetY
        val rowBottom = editor.getRowBottom(cursor.leftLine) - editor.offsetY
        
        // 默认显示在行上方，如果空间不足则显示在行下方
        var panelY = (rowTop - viewHeight - (10 * dpUnit)).toInt()
        if (panelY < 0) {
            panelY = (rowBottom + (10 * dpUnit)).toInt()
        }
        
        setSize(viewWidth, viewHeight)
        setLocationAbsolutely(panelX, panelY)
        show()
    }

    override fun isEnabled() = isEnabled

    override fun setEnabled(enabled: Boolean) {
        this.isEnabled = enabled
        if (!enabled) dismiss()
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density

    // 实现父类接口，虽然我们使用了 Compose，但为了兼容性保留空实现
    override fun onClick(v: View?) {
        // no-op
    }
}