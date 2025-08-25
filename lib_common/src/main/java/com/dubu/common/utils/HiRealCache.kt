package com.dubu.common.utils

import com.dubu.common.beans.UserBean
import com.dubu.common.beans.apk.AppConfigBean
import com.dubu.common.beans.config.CountryInfo
import com.dubu.common.beans.config.LanguageInfo
import com.dubu.common.beans.me.KolPostBean
import com.dubu.common.beans.rtc.RtcGiftMsgDataBean
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
    * 相册去往横向预览页面的时候数据共享
    * */
    var photoList: MutableList<KolPostBean>? = null
    var photoListShowIndex: Int = 0

    /*
    * 获取后台配置接口对应的 国家和语言列表
    * */
    var countryList: MutableList<CountryInfo>? = null
    var languageList: MutableList<LanguageInfo>? = null

    /*
    * 本地保存RTC视频以后得Gift礼物数据
    * */
    var giftRtcResult : HashMap<String, RtcGiftMsgDataBean>? = null
    var rtcVideoDuration: Int = 0


    /*
    * 打招呼
    * */
    var helloList: MutableList<String>? = null
    /*
    * 聊天
    * */
    var chatList: MutableList<String>? = null

    //todo allen:server (对接后台数据) 视频通话默认每分钟价格 需要对接后台json配置接口获取这个默认值 暂时写死100
    var videoGoldDefaultPrice: Int? = 100

    //todo allen:server (对接后台数据) 暂时造假升级app数据
    var appConfigBean: AppConfigBean? = null



    fun getRandomChatMessages(n: Int): List<String> {
        // 确保 chatList 不为空且有内容
        val sourceList = chatList ?: return emptyList()
        if (sourceList.isEmpty()) return emptyList()

        // 如果 n 大于列表长度，则最多返回列表所有元素
        val count = if (n > sourceList.size) sourceList.size else n

        // 打乱顺序并取前 count 条
        return sourceList.shuffled().take(count)
    }


}