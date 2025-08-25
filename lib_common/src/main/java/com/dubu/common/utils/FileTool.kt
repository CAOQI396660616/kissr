package com.dubu.common.utils

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.format.Formatter
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.dubu.common.base.BaseApp
import com.dubu.common.ext.closeQuietly
import com.dubu.common.ext.isValid
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


object FileTool {
    private const val DOC_ROOT = ".tencent"
    private const val DOC_DIR = ".WeiXin/.sys/.sgconfig"
    private const val SVGA_DIR = "svga"
    private const val FILE_AUTHORITY = "fileProvider"
    private const val CACHE_DIR = "caches"
    private const val COMMON_DIR = "common"
    private const val CRASH_DIR = "crash"
    private const val TEMP_TAKE_PHOTO_NAME = "temp_photo.jpg"

    @JvmStatic
    fun getAppRootDir(context: Context): File {
        val file: File? = context.getExternalFilesDir("")

        return file ?: getInternalFile(context)
    }


    @JvmStatic
    private fun getInternalFile(context: Context): File {
        val file = context.filesDir
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }


    @JvmStatic
    fun getSvgaCacheDir(context: Context): File {
        return getAppDir(context, SVGA_DIR)
    }

    @JvmStatic
    fun getAppCrashDir(context: Context): File {
        return getAppDir(context, CRASH_DIR)
    }

    @JvmStatic
    fun getAppCommonDir(context: Context): File {
        return getAppDir(context, COMMON_DIR)
    }


    @JvmStatic
    fun getAppCacheDir(context: Context): File {
        return getAppDir(context, CACHE_DIR)
    }

    @JvmStatic
    fun getAppCachePath(context: Context): String {
        return getAppCacheDir(context).absolutePath
    }


    private fun getAppDir(context: Context, path: String): File {
        val file = File(getAppRootDir(context), path)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }


    fun getTakePhotoTempFile(context: Context): File {
        return File(getAppDir(context, CACHE_DIR), TEMP_TAKE_PHOTO_NAME)
    }


    fun commonDocumentDir(): File? {
        val root = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString()
                    .plus(File.separator)
                    .plus(DOC_ROOT)
            )
        } else {
            if (!PermissionsTool.grantedStorage(BaseApp.instance)) {
                return null
            }
            File(
                Environment
                    .getExternalStorageDirectory()
                    .toString()
                    .plus(File.separator)
                    .plus(DOC_ROOT)
            )
        }


        if (!root.exists()) {
            val s = root.mkdirs()
            if (!s) return null
        }
        val nm = File(root, ".nomedia")
        if (!nm.exists()) {
            try {
                nm.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val doc = File(root, DOC_DIR)
        if (doc.exists()) return doc
        return if (doc.mkdirs()) {
            doc
        } else null
    }


    @JvmStatic
    fun removeFile(file: File?): Boolean {
        if (file == null || !file.exists()) return true
        return file.deleteRecursively()
    }

    @JvmStatic
    fun moveFile(srcFile: File?, desFile: File?) {
        if (srcFile == null || !srcFile.exists()) {
            return
        }
        if (desFile == null) {
            return
        }

        if (!desFile.exists()) {
            val targetFile = srcFile.copyTo(desFile, false)
            if (targetFile.exists()) {
                srcFile.delete()
            }
        } else {
            srcFile.delete()
        }
    }


    fun getTotalInternalMemorySize(context: Context?): String {
        return try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            Formatter.formatFileSize(context, blockSize * totalBlocks)
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    fun getAvailableInternalMemorySize(context: Context?): String {
        return try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            Formatter.formatFileSize(context, availableBlocks * blockSize)
        } catch (e: java.lang.Exception) {
            ""
        }
    }


    fun getFileUri(mContext: Context, mFile: File): Uri? {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val auth = "${mContext.packageName}.${FILE_AUTHORITY}"
            FileProvider.getUriForFile(mContext, auth, mFile)
        } else {
            Uri.fromFile(mFile)
        }
    }


    /**
     * change media uri to app directory uri
     * so fucking stupid google permission
     */
    @TargetApi(Build.VERSION_CODES.S)
    suspend fun mediaFileToLocal(context: Context, outFile: File, srcUri: Uri): Uri? {

        val ret = getFromMediaUriPfd(outFile, context.contentResolver, srcUri)
        return if (ret != null) {
            getFileUri(context, ret)
        } else null
    }


    fun getMediaLocalFile(context: Context, srcUri: Uri): File {
        val srcPath = getPathFromUri(context, srcUri)
        val out = if (srcPath.isValid()) {
            System.currentTimeMillis().toString().plus(srcPath!!.substringAfterLast(File.separator))
        } else {
            System.currentTimeMillis().toString().plus("tmp_out.jpg")
        }
        return File(getAppCacheDir(context), out)
    }


    private fun getFromMediaUriPfd(tmpFile: File, resolver: ContentResolver, uri: Uri): File? {
        var input: FileInputStream? = null
        var output: FileOutputStream? = null
        try {
            val pfd = resolver.openFileDescriptor(uri, "r")
            val fd = pfd!!.fileDescriptor
            input = FileInputStream(fd)
            output = FileOutputStream(tmpFile)
            var read: Int
            val bytes = ByteArray(4096)
            while (input.read(bytes).also { read = it } != -1) {
                output.write(bytes, 0, read)
            }
            return tmpFile
        } catch (ignored: IOException) {
            // Nothing we can do
        } finally {
            input?.closeQuietly()
            output?.closeQuietly()
        }
        return null
    }

    fun uriToFileName(uri: Uri, context: Context): String {
        return when (uri.scheme) {
            ContentResolver.SCHEME_FILE -> uri.toFile().name
            ContentResolver.SCHEME_CONTENT -> {
                val cursor = context.contentResolver.query(uri, null, null, null, null, null)
                cursor?.let {
                    it.moveToFirst()
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val displayName = it.getString(index)
                    cursor.close()
                    displayName
                } ?: extensionMiMeType(context, uri)

            }

            else -> extensionMiMeType(context, uri)
        }
    }

    private fun extensionMiMeType(context: Context, uri: Uri) = "${System.currentTimeMillis()}.${
        MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
    }"


    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    fun getPathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                val id = documentId.split(":").toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(id)
                filePath = getDataColumn(
                    context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selection,
                    selectionArgs
                )
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                filePath = getDataColumn(context, contentUri, null, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null)
        } else if ("file" == uri.scheme) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
        }
        return filePath
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.closeQuietly()
        }
        return path
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

}