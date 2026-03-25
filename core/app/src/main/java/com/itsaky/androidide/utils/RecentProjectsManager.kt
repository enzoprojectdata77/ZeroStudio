package com.itsaky.androidide.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.ui.graphics.Color
import java.io.File

/**
 * 项目历史数据模型
 */
data class ProjectHistory(
    val name: String,
    val path: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    // 动态生成首字母
    val letter: String get() = name.take(1).uppercase()
    
    // 根据路径哈希生成固定颜色，确保同一个项目的图标颜色始终一致
    val color: Color get() {
        val hash = path.hashCode()
        return Color(
            red = (hash and 0xFF0000 shr 16) / 255f * 0.5f + 0.4f,
            green = (hash and 0x00FF00 shr 8) / 255f * 0.5f + 0.4f,
            blue = (hash and 0x0000FF) / 255f * 0.5f + 0.4f,
            alpha = 1f
        )
    }
}

object RecentProjectsManager {
    private const val PREF_NAME = "recent_projects_prefs"
    private const val KEY_HISTORY = "project_history_json"
    private val gson = Gson()

    /**
     * 记录或更新打开的项目
     */
    fun addProject(context: Context, file: File) {
        if (!file.exists()) return
        val history = getHistory(context).toMutableList()
        val path = file.absolutePath
        
        // 去重：如果已存在则删除旧记录
        history.removeAll { it.path == path }
        
        // 插入到最前面
        history.add(0, ProjectHistory(name = file.name, path = path))
        
        // 限制记录数量（例如最多999条）
        val limitedHistory = history.take(999)
        
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_HISTORY, gson.toJson(limitedHistory))
            .apply()
    }

    /**
     * 获取历史记录列表
     */
    fun getHistory(context: Context): List<ProjectHistory> {
        val json = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_HISTORY, null) ?: return emptyList()
        
        return try {
            val type = object : TypeToken<List<ProjectHistory>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}