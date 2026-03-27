package com.itsaky.androidide.formatprovider.ktfmt

import android.content.Context
import com.blankj.utilcode.util.FileUtils
import com.itsaky.androidide.utils.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object KtfmtEnv {
    const val KTFMT_VERSION = "0.62"
    const val KTFMT_JAR_NAME = "ktfmt-$KTFMT_VERSION-with-dependencies.jar"
    const val DOWNLOAD_URL = "https://github.com/facebook/ktfmt/releases/download/v$KTFMT_VERSION/$KTFMT_JAR_NAME"

    val KTFMT_DIR: File by lazy {
        val pluginDir = File(Environment.ANDROIDIDE_HOME, "ideplugin")
        Environment.mkdirIfNotExits(File(pluginDir, "ktfmt"))
    }

    val KTFMT_JAR: File by lazy {
        File(KTFMT_DIR, KTFMT_JAR_NAME)
    }

    fun isInstalled(): Boolean {
        return KTFMT_JAR.exists() && KTFMT_JAR.length() > 0
    }

    /**
     * 协程环境下下载 JAR
     */
    suspend fun downloadJar(onProgress: (Float) -> Unit): Boolean = withContext(Dispatchers.IO) {
        val tempFile = File(Environment.TMP_DIR, "ktfmt_temp_${System.currentTimeMillis()}.jar")
        try {
            FileUtils.createOrExistsFile(tempFile)
            val connection = URL(DOWNLOAD_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("HTTP ${connection.responseCode}")
            }

            val fileLength = connection.contentLength
            val input = connection.inputStream
            val output = FileOutputStream(tempFile)

            val data = ByteArray(8192)
            var total: Long = 0
            var count: Int

            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                if (fileLength > 0) {
                    onProgress(total.toFloat() / fileLength.toFloat())
                }
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()

            FileUtils.move(tempFile, KTFMT_JAR)
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            tempFile.delete()
            return@withContext false
        }
    }
}