package com.dubu.common.utils

import com.dubu.common.beans.UserBean
import com.dubu.common.beans.apk.AppConfigBean
import com.dubu.common.beans.config.CountryInfo
import com.dubu.common.beans.config.LanguageInfo
import com.dubu.common.utils.hi.DeviceIdUtil

/**
 * Author:v
 * Time:2022/10/17
 */
object HiRealCache {

    //验证签名的秘钥
    var sign: String = "D4C086AE-16D5-4255-99CF-DE12E4268690"
    var user: UserBean? = null
    var userToken: String? = ""
    var userId: Long? = 0

    //本地存储Token 的有效期 目前后台是一天 所以暂定是 24-1 小时有效  ms
    var exTokenTime: Long = 1L * 1000 * 60 * 60 * 23

    var deviceId: String = ""
        get() {
            if (field.isEmpty()) {
                val deviceOptUUid = DeviceIdUtil.getDeviceOptUUid()
                field = deviceOptUUid
            }
            return field
        }

    var userGender: String = "" //0男 1女 3未知  (后台对应的是 M=man W=woman)
        get() {
            if (field.isEmpty()) {

                if (user == null) {
                    field = "3"
                } else {
                    if (user?.gender?.isNullOrEmpty() == true) {
                        field = "3"
                    } else {
                        field = when (user?.gender ?: "") {
                            "M" -> {
                                "0"
                            }
                            "W" -> {
                                "1"
                            }
                            else -> {
                                "3"
                            }
                        }
                    }
                }


            }
            return field
        }

    //记录手机状态栏的高度
    var phoneStatusBarHeight: Int? = 0



    /*
    * 获取后台配置接口对应的 国家和语言列表
    * */
    var countryList: MutableList<CountryInfo>? = null
    var languageList: MutableList<LanguageInfo>? = null


    /*
    * 打招呼
    * */
    var helloList: MutableList<String>? = null
    /*
    * 聊天
    * */
    var chatList: MutableList<String>? = null



}