package com.dubu.common.constant

/**
 * Author:v
 * Time:2022/10/19
 */
object Consts2Common {
    const val PACK_NAME_FB = "com.facebook.katana"
    const val PACK_NAME_WA = "com.whatsapp"
    const val PACK_NAME_MSG = "com.facebook.orca"
    const val PACK_NAME_LN = "jp.naver.line.android"
    const val PACK_NAME_TT = "com.twitter.android"
    const val PACK_NAME_TAM = "org.telegram.messenger"

    var COMMON_V_SHARE_CHANNEL_TYPE_FB: Int = 0
    var COMMON_V_SHARE_CHANNEL_TYPE_LINE: Int = 1
    var COMMON_V_SHARE_CHANNEL_TYPE_MG: Int = 2
    var COMMON_V_SHARE_CHANNEL_TYPE_WHATS: Int = 3
    var COMMON_V_SHARE_CHANNEL_TYPE_LINK: Int = 4
    var COMMON_V_SHARE_CHANNEL_TYPE_TWITTER: Int = 5
    var COMMON_V_SHARE_CHANNEL_TYPE_TELEGRAM: Int = 6


    const val OTHER_LOGIN = "other_login"

    const val DEFAULT_LANGUAGE: String = "en"//默认语言区域对应的简码



    //登录接口传递参数
    //guest/facebook/google
    const val LOGIN_TYPE_GUEST: String = "guest"
    const val LOGIN_TYPE_FACEBOOK: String = "facebook"
    const val LOGIN_TYPE_GOOGLE: String = "google"
}