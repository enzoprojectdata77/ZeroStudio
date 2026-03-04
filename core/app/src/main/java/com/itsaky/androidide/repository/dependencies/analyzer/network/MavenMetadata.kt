/*
 * @author android_zero
 * 网络请求并解析 maven-metadata.xml
 */
package com.itsaky.androidide.repository.dependencies.analyzer.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

data class MavenMetadata(
    val latest: String?,
    val release: String?,
    val versions: List<String>
) {
    val bestLatest: String? get() = latest ?: release ?: versions.lastOrNull()
}

