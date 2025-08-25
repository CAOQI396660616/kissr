package com.dubu.common.utils
import android.util.Log
import java.io.File

object FileTreePrinter {

    private const val TAG = "FileTreePrinter"

    // 原始：完整递归打印
    fun printFileTree(root: File, indent: String = "") {
        if (!root.exists()) {
            Log.e(TAG, "路径不存在: ${root.absolutePath}")
            return
        }

        if (!root.isDirectory) {
            Log.e(TAG, "不是目录: ${root.absolutePath}")
            return
        }

        printDirectoryContents(root, indent)
    }

    private fun printDirectoryContents(dir: File, indent: String) {
        val files = dir.listFiles()
        if (files == null || files.isEmpty()) {
            Log.d(TAG, "$indent(空文件夹)")
            return
        }

        for (file in files.sortedBy { it.name }) {
            if (file.isDirectory) {
                Log.d(TAG, "$indent📁 ${file.name}")
                printDirectoryContents(file, "$indent    ")
            } else {
                Log.d(TAG, "$indent📄 ${file.name}")
            }
        }
    }

    // 🆕 新增：只打印最多 3 层目录结构
    fun printFileTreeWithMaxDepth(root: File, maxDepth: Int = 3, currentDepth: Int = 1, indent: String = "") {
        if (!root.exists()) {
            Log.e(TAG, "路径不存在: ${root.absolutePath}")
            return
        }

        if (!root.isDirectory) {
            Log.e(TAG, "不是目录: ${root.absolutePath}")
            return
        }

        if (currentDepth > maxDepth) return

        val files = root.listFiles()
        if (files == null || files.isEmpty()) {
            Log.d(TAG, "$indent(空文件夹)")
            return
        }

        for (file in files.sortedBy { it.name }) {
            if (file.isDirectory) {
                Log.d(TAG, "$indent📁 ${file.name}")
                printFileTreeWithMaxDepth(file, maxDepth, currentDepth + 1, "$indent    ")
            } else {
                Log.d(TAG, "$indent📄 ${file.name}")
            }
        }
    }
}
