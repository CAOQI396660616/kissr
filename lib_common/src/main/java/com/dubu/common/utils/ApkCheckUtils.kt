package com.dubu.common.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*

/**
 * APK 检测工具类
 */
object ApkCheckUtils {

    data class ApkInfo(
        val isAvailable: Boolean,
        val fileName: String? = null,
        val fileSizeBytes: Long = 0,
        val lastModified: String? = null,
        val contentType: String? = null,
        val isHttpError: Boolean? = false,
    )

    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .build()

    /**
     * 检查 APK 是否可用，并返回文件信息
     * @param url APK 下载链接
     */
    fun checkApkUrl(url: String): ApkInfo {
        val request = Request.Builder()
            .url(url)
            .head() // 只获取响应头
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return ApkInfo(false)
                }

                val contentType = response.header("Content-Type") ?: ""
                val contentLength = response.header("Content-Length")?.toLongOrNull() ?: 0
                val lastModified = response.header("Last-Modified")?.let { formatHttpDate(it) }
                val fileName = getFileNameFromUrl(url) ?: getFileNameFromHeader(response.header("Content-Disposition"))

                val isApk = contentType.contains("application/vnd.android.package-archive", true) ||
                        contentType.contains("application/octet-stream", true) ||
                        fileName?.endsWith(".apk", true) == true

                ApkInfo(
                    isAvailable = isApk && contentLength > 0,
                    fileName = fileName,
                    fileSizeBytes = contentLength,
                    lastModified = lastModified,
                    contentType = contentType,
                    isHttpError = false,
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ApkInfo(isAvailable = false, isHttpError = true)
        }
    }

    /**
     * 从 URL 提取文件名
     */
    private fun getFileNameFromUrl(url: String): String? {
        return try {
            val conn = URLConnection.guessContentTypeFromName(url)
            val parts = url.split("/")
            parts.lastOrNull()?.takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 从响应头 Content-Disposition 提取文件名
     */
    private fun getFileNameFromHeader(header: String?): String? {
        if (header.isNullOrEmpty()) return null
        val regex = "filename=\"?([^\";]+)\"?".toRegex()
        return regex.find(header)?.groupValues?.getOrNull(1)
    }

    /**
     * 格式化 HTTP 日期
     */
    private fun formatHttpDate(dateStr: String): String? {
        return try {
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            val date = sdf.parse(dateStr)
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date ?: return null)
        } catch (e: Exception) {
            null
        }
    }
}
