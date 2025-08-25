package com.dubu.common.utils

import android.media.MediaDrm
import com.dubu.common.base.BaseApp
import com.dubu.common.constant.SpKey2Common
import com.dubu.common.ext.isValid
import com.tencent.mmkv.MMKV
import java.io.File
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread


/**
 * Author:v
 * Time:2022/7/21
 */
object UuidTool {


    /**
     * 可能为空
     */
    private fun getDrmId(): String {
        val wideVineUuid = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        return try {
            val wvDrm = MediaDrm(wideVineUuid)
            val wideVineId: ByteArray =
                wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
            val ss = wideVineId.toString(Charset.defaultCharset())
            UUID(
                ss.hashCode().toLong(),
                BaseApp.instance.packageName.hashCode().toLong()
            ).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getUuid(): String {

        var did: String? = null
        did = fetchUuidFromFile()
        if (did.isValid()) {
            MMKV.defaultMMKV().encode(SpKey2Common.DEVICE_ID, did)
            return did!!
        }
        did = MMKV.defaultMMKV().decodeString(SpKey2Common.DEVICE_ID, "")
        if (did.isValid()) {
            saveUuid(did!!, false)
            return did
        }
        did = getDrmId()

        if (did.isEmpty()) {
            did = UUID.randomUUID().toString()
        }
        saveUuid(did, true)
        return did
    }

    private fun saveUuid(id: String, mk: Boolean) {
        thread {
            if (mk) MMKV.defaultMMKV().encode(SpKey2Common.DEVICE_ID, id)
            saveUuid2File(id)
        }
    }


    private fun saveUuid2File(id: String) {
        val dir = FileTool.commonDocumentDir() ?: return
        val f = File(dir, ".settings")
        try {
            f.writeText(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun fetchUuidFromFile(): String? {
        val dir = FileTool.commonDocumentDir() ?: return null
        val f = File(dir, ".settings")
        if (f.exists()) {
            return try {
                f.readText()
            } catch (e: Exception) {
                null
            }
        }
        return null
    }


    fun clearUuidFile() {
        val dir = FileTool.commonDocumentDir() ?: return
        val f = File(dir, ".settings")
        if (f.exists()) {
            f.delete()
        }
    }


}