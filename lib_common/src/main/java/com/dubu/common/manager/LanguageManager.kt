package com.dubu.common.manager

import android.content.Context
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.constant.Constants
import com.dubu.common.constant.SpKey2Common
import com.dubu.common.constant.Tag2Common
import com.dubu.common.ext.fromJson
import com.dubu.common.ext.toJson
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.rtc.language.LanguageJsonManager
import com.hjq.language.LocaleContract
import com.hjq.language.MultiLanguages
import com.tencent.mmkv.MMKV
import java.io.Serializable
import java.util.*

/*
* 处理登录相关的业务管理器
* */

object LanguageManager {


    /**
     * 用户重新打开app校验用户是否登录 同时处理数据到HiRealCache
     * @return [Boolean]
     */
    @JvmStatic
    fun getLastUserAppLang(): Locale? {

        val userAppLangByMMKV = getUserAppLangByMMKV()
        return getLocaleLanguageByCode(userAppLangByMMKV)

    }

    @JvmStatic
    fun setLocaleLanguageByCode(context:Context ,countryCode: String?) {
        countryCode ?: return

        saveUserAppLangToMMKV(countryCode)

        val localeLanguageByCode = getLocaleLanguageByCode(countryCode)
        val isNeedExitApp = MultiLanguages.setAppLanguage(context, localeLanguageByCode)

        HiLog.l(Tag2Common.TAG_12302, "语言切换 成功 isNeedExitApp = [$isNeedExitApp] setLocaleLanguageByCode : ${localeLanguageByCode.toString()} ")

        if (isNeedExitApp) {
            AppUtils.relaunchApp(true)
        }

    }

    @JvmStatic
    fun getLocaleLanguageByCode(countryCode: String?): Locale? {
        countryCode ?: return LocaleContract.getEnglishLocale()


        /*

            country_code	    name	            中文翻译
            zh	                China	            中文
            en	                English	            英语

            id	                Indonesia	        印度尼西亚语
            pt	                Portuguese	        葡萄牙语

            de	                German	            德语
            es	                Spanish	            西班牙语
            fr	                French	            法语

            ar	                Arabic	            阿拉伯语

        */

        return when (countryCode) {

            "zh" -> {
                LocaleContract.getChineseLocale()
            }
            "pt" -> {
                LocaleContract.getPortugalLocale()
            }
            "id" -> {
                LocaleContract.getIndonesiaLocale()
            }
            "ar" -> {
                LocaleContract.getArabicLocale()
            }
            "de" -> {
                LocaleContract.getGermanLocale()
            }
            "es" -> {
                LocaleContract.getSpainLocale()
            }
            "fr" -> {
                LocaleContract.getFrenchLocale()
            }
            //en
            else -> {
                LocaleContract.getEnglishLocale()
            }
        }


    }

    //----------------------------------------------------------------------------------------------


    @JvmStatic
    fun saveUserAppLangToMMKV(code: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveUserAppLangToMMKV 数据= $code")
        MMKV.defaultMMKV().encode(SpKey2Common.CURRENT_USER_APP_LANG, code)
    }

    @JvmStatic
    fun getUserAppLangByMMKV(): String? {
        val decodeString = MMKV.defaultMMKV().decodeString(SpKey2Common.CURRENT_USER_APP_LANG, "")
        if (decodeString.isNullOrEmpty()){
            saveUserAppLangToMMKV("en")
            return "en"
        }
        HiLog.e(Tag2Common.TAG_12300, "getUserAppLangByMMKV 数据= $decodeString")
        return decodeString
    }


    //----------------------------------------------------------------------------------------------

}