package com.itsaky.androidide.repository.dependencies.models.datas

import com.itsaky.androidide.repository.dependencies.models.interfaces.*
import com.itsaky.androidide.repository.dependencies.models.enums.*


/**
 * 对应 TOML 文件中 <code>[plugins]</code> 块的单项定义。
 */
data class CatalogPlugin(
    val alias: String,
    val id: String,
    val versionRef: String?
)