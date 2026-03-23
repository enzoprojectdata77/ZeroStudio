package com.itsaky.androidide.actions.editor

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.EditorActionItem
import io.github.rosemoe.sora.widget.CodeEditor
import com.itsaky.androidide.resources.R

/**
 * 触发系统自定义文本操作菜单的 Action（自动收集系统应用扩展功能，如翻译、小爱同学等的intent浮动菜单）
 *
 * 已修复问题：【内存泄漏修复优化】：
 * 1. 移除了 private val context 和 private var currentEditor 强引用，避免泄漏 Activity
 * 2. 所有的对象引用都在运行时从 ActionData 中获取，实现绝对的无状态安全。
 *
 * @author android_zero
 */
class SystemTextMenuAction(context: Context, override val order: Int) : EditorActionItem {

    override val id: String = "ide.editor.selection.system_actions"
    
    override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS
    
    override var label: String = "system menus"
    override var visible: Boolean = true
    override var enabled: Boolean = true
    override var icon: Drawable? = null
    override var requiresUIThread: Boolean = true

    init {
        // 在初始化阶段加载图标，不再将 context 作为类的持久属性保存
        icon = ContextCompat.getDrawable(context, R.drawable.more_vert)
    }

    override fun prepare(data: ActionData) {
        super.prepare(data)
        // 动态验证即可，不再使用 currentEditor 长期缓存
        val editor = data.get(CodeEditor::class.java)
        enabled = editor != null
        visible = editor != null
    }

    override suspend fun execAction(data: ActionData): Any {
        // 运行时动态获取 Editor 和 Context，彻底断开强引用关系
        val editor = data.get(CodeEditor::class.java) ?: return false
        val context = data.get(Context::class.java) ?: return false

        val text = editor.text
        val cursor = editor.cursor
        
        // 如果有选中文本则获取选中内容，否则传空字符串
        val selectedText = if (cursor.isSelected) {
            text.subSequence(cursor.left().index, cursor.right().index).toString()
        } else {
            ""
        }

        // 计算弹窗显示的物理位置（光标底部）
        val leftLine = cursor.leftLine
        val leftCol = cursor.leftColumn
        
        val layoutOffset = editor.layout.getCharLayoutOffset(leftLine, leftCol)
        val screenPos = IntArray(2)
        editor.getLocationInWindow(screenPos)
        
        val posX = (screenPos[0] + editor.measureTextRegionOffset() + layoutOffset[1] - editor.offsetX).toInt()
        // 加上行高，使其显示在光标下方
        val posY = (screenPos[1] + layoutOffset[0] - editor.offsetY + editor.rowHeight).toInt()

        // 实例化并显示 Compose 浮窗
        val popup = SystemTextActionsPopup(context, editor, selectedText)
        popup.show(editor, posX, posY)

        return true
    }

    override fun dismissOnAction(): Boolean {
        return true 
    }
}