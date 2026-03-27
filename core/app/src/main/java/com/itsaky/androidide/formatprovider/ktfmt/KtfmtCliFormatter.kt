package com.itsaky.androidide.formatprovider.ktfmt

import android.content.Context
import com.itsaky.androidide.formatprovider.CodeFormatter
import com.itsaky.androidide.preferences.KtfmtPreferences
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.executioncommand.TermuxCommand
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * CLI 版本的 Ktfmt 格式化提供器
 *
 * 通过 TermuxCommand 将源码输入至 stdin，通过 stdout 获取格式化后的内容。
 * 进程执行完毕后资源立即销毁。
 * 
 * @author android_zero
 */
class KtfmtCliFormatter(
    private val context: Context,
    private val file: File
) : CodeFormatter {

    override fun format(source: String): String {
        if (!KtfmtEnv.isInstalled()) return source

        // 获取项目根目录 (用于 .editorconfig 读取)
        val projectRoot = try {
            IProjectManager.getInstance().projectDir
        } catch (e: Exception) {
            file.parentFile
        }

        val args = mutableListOf("-jar", KtfmtEnv.KTFMT_JAR.absolutePath)
        
        // 追加风格选项
        args.add(KtfmtPreferences.style)
        
        if (KtfmtPreferences.keepImports) {
            args.add("--do-not-remove-unused-imports")
        }
        if (KtfmtPreferences.enableEditorConfig) {
            args.add("--enable-editorconfig")
        }
        if (KtfmtPreferences.quietMode) {
            args.add("--quiet")
        }

        args.add("--stdin-name=${file.absolutePath}")

        val result = runBlocking {
            TermuxCommand.run(context) {
                label("Ktfmt CLI")
                executable(Environment.JAVA.absolutePath)
                args(*args.toTypedArray())
                workingDir(projectRoot.absolutePath)
                stdin(source)
            }
        }

        if (result.isSuccess) {
            return result.stdout
        } else {
            // 解析失败时抛出异常，由上层处理
            throw RuntimeException("Ktfmt formatting failed. Code: ${result.exitCode}\nError: ${result.stderr}")
        }
    }
}