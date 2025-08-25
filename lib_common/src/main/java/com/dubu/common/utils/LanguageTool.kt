package com.dubu.common.utils

import android.os.Build
import android.os.LocaleList
import java.util.*

object LanguageTool {

    /**
     * 获取系统首选语言代码 (如 "zh")
     */
    fun getSystemLanguageCode(): String {
        return getSystemLocale().language
    }

    /**
     * 获取完整语言地区代码 (如 "zh-CN")
     */
    fun getFullLanguageTag(): String {
        val locale = getSystemLocale()
        return "${locale.language}-${locale.country}"
    }

    /**
     * 获取本地化的语言名称 (如 "中文")
     */
    fun getLocalizedLanguageName(): String {
        val locale = getSystemLocale()
        return locale.getDisplayLanguage(locale)
    }

    /**
     * 获取系统区域设置
     */
    private fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }
    }

    /**
     * 获取所有系统支持的语言列表 (API 24+)
     */
//    fun getSystemLanguageList(): List<Locale> {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            LocaleList.getDefault().
//        } else {
//            listOf(Locale.getDefault())
//        }
//    }
}