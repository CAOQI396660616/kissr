package com.dubu.common.manager

import com.dubu.common.constant.SpKey2Common
import com.dubu.common.constant.Tag2Common
import com.dubu.common.utils.HiLog
import com.tencent.mmkv.MMKV


/**
 *
 * 处理本地是否设置的重要的几个设置
 *   美颜
 *   通话价格
 *   打开通知
 *   擅长的语言
 *   服务菜单
 * @author cq
 * @date 2025/06/26
 */
object SettingsManager {

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS: 互动设置
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


    /**
     * 判断用户 关闭互动设置了没
     * 这个开关 是要主动打开的 所以 ""或者null 的时候 就认为默认打开
     * 若有 "close" 就认为是用户关闭了
     * @return [Boolean]
     */
    @JvmStatic
    fun checkUserCloseInteractive(): Boolean {
        val interactiveByMMKV = getInteractiveByMMKV()
        if (interactiveByMMKV.isNullOrEmpty())
            return false

        return true
    }

    @JvmStatic
    fun saveInteractiveToMMKV(data: String) {
        HiLog.e(Tag2Common.TAG_12300, "savePriceToMMKV 数据= $data")
        MMKV.defaultMMKV().encode(SpKey2Common.IMPORT_SET_INTERACTIVE, data)
    }

    @JvmStatic
    fun getInteractiveByMMKV(): String? {
        return MMKV.defaultMMKV().decodeString(SpKey2Common.IMPORT_SET_INTERACTIVE, "")
    }



    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    /**
     * 获取本地存储的重要设置 有几项没有设置
     * 目前是暂定总数四项 后续需要加上美颜这一项
     * @return [Int]
     */
    @JvmStatic
    fun getSumSettingUnSet(): Int {

        var getSumSettingUnSet = 0

        if (!checkUserIsSetPrice()) {
            getSumSettingUnSet++
        }

        if (!checkUserIsSetNotify()) {
            getSumSettingUnSet++
        }

        if (!checkUserIsSetLanguage()) {
            getSumSettingUnSet++
        }

        if (!checkUserIsSetService()) {
            getSumSettingUnSet++
        }

        return getSumSettingUnSet

    }


    @JvmStatic
    fun checkUserIsSetPrice(): Boolean {
        val str = getPriceByMMKV()
        HiLog.e(Tag2Common.TAG_12300, "####### checkUserIsSetPrice = $str ")

        if (str == null || str.isEmpty())
            return false

        return true

    }


    @JvmStatic
    fun checkUserIsSetNotify(): Boolean {
        val str = getNotifyByMMKV()
        HiLog.e(Tag2Common.TAG_12300, "####### checkUserIsSetNotify = $str ")

        if (str == null || str.isEmpty())
            return false

        return true

    }

    @JvmStatic
    fun checkUserIsSetLanguage(): Boolean {
        val str = getLanguageByMMKV()
        HiLog.e(Tag2Common.TAG_12300, "####### checkUserIsSetLanguage = $str ")

        if (str == null || str.isEmpty())
            return false

        return true

    }

    @JvmStatic
    fun checkUserIsSetService(): Boolean {
        val str = getServiceByMMKV()
        HiLog.e(Tag2Common.TAG_12300, "####### checkUserIsSetService = $str ")

        if (str == null || str.isEmpty())
            return false

        return true

    }


    //----------------------------------------------------------------------------------------------


    @JvmStatic
    fun savePriceToMMKV(data: String) {
        HiLog.e(Tag2Common.TAG_12300, "savePriceToMMKV 数据= $data")
        MMKV.defaultMMKV().encode(SpKey2Common.IMPORT_SET_PRICES, data)
    }

    @JvmStatic
    fun getPriceByMMKV(): String? {
        return MMKV.defaultMMKV().decodeString(SpKey2Common.IMPORT_SET_PRICES, "")
    }


    //----------------------------------------------------------------------------------------------

    @JvmStatic
    fun saveNotifyToMMKV(data: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveNotifyToMMKV 数据= $data")
        MMKV.defaultMMKV().encode(SpKey2Common.IMPORT_SET_NOTIFY, data)
    }

    @JvmStatic
    fun getNotifyByMMKV(): String? {
        return MMKV.defaultMMKV().decodeString(SpKey2Common.IMPORT_SET_NOTIFY, "")
    }

    //----------------------------------------------------------------------------------------------

    @JvmStatic
    fun saveLanguageToMMKV(data: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveNotifyToMMKV 数据= $data")
        MMKV.defaultMMKV().encode(SpKey2Common.IMPORT_SET_LANGUAGE, data)
    }

    @JvmStatic
    fun getLanguageByMMKV(): String? {
        return MMKV.defaultMMKV().decodeString(SpKey2Common.IMPORT_SET_LANGUAGE, "")
    }

    //----------------------------------------------------------------------------------------------

    @JvmStatic
    fun saveServiceToMMKV(data: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveNotifyToMMKV 数据= $data")
        MMKV.defaultMMKV().encode(SpKey2Common.IMPORT_SET_SERVICE, data)
    }

    @JvmStatic
    fun getServiceByMMKV(): String? {
        return MMKV.defaultMMKV().decodeString(SpKey2Common.IMPORT_SET_SERVICE, "")
    }

    //----------------------------------------------------------------------------------------------


}