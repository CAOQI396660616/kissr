package com.dubu.download.msnetwork.utils

import com.dubu.common.constant.Tag2Common
import com.dubu.common.utils.HiLog
import com.dubu.download.msnetwork.db.DownloadManager


object HttpDownloadManagerKt {

    /**
     * 移除未完成下载的任务 因为它们下载了一半 没啥意义 allen
     */
    @JvmStatic
    fun removeUnDownloadTask() {
        val all = DownloadManager.getInstance().all
        HiLog.e(Tag2Common.TAG_123XX,"removeUnDownloadTask all  ${all.size}")
        all.forEachIndexed { index, progress ->
            if(progress.fraction<1.0f && progress.fraction > 0.0f){
                val delFileOrFolder = IOUtils.delFileOrFolder(progress.filePath)
                DownloadManager.getInstance().delete(progress.tag)
                HiLog.e(Tag2Common.TAG_123XX,"删除 $index ,  progress = ${progress.fileName}= ${progress.fraction}= ${progress.status}= $delFileOrFolder ")
            }else{
                val delFileOrFolder = IOUtils.delFileOrFolder(progress.filePath)
                HiLog.e(Tag2Common.TAG_123XX,"没有删除 $index ,  progress = ${progress.fileName}= ${progress.fraction}= ${progress.status}= $delFileOrFolder ")
            }
        }
    }

}