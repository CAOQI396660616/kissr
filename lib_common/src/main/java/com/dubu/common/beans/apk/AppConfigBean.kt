package com.dubu.common.beans.apk

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AppConfigBean(

    @Json(name = "app_is_need_update")
    var isNeedUpdateApp: String? = "0", //是否需要升级

    @Json(name = "app_is_must_update")
    var isMustUpdateApp: String? = "0", //是否强制升级

    @Json(name = "apk_url")
    var apkUrl: String? = "", //下载apk地址

    @Json(name = "app_update_text")
    var text: String? = "", //apk更新文案


    @Json(name = "share_texts")
    var shareText: String? = "", //分享文案

    @Json(name = "call_default_price")
    var callDefaultPrice: String? = "", //主播通话默认一分钟价格 100

    @Json(name = "chat_default_price")
    var chatDefaultPrice: String? = "", //消息一条默认价格 5

    @Json(name = "host_coin_exchange_rate")
    var coinExchangeRate: String? = "", //金币兑换默认 1000

){
    /**
     * 判断是否需要强制更新应用
     * @return true 表示需要强制更新，false 表示不需要
     */
    fun isMustUpdate(): Boolean {
        return isMustUpdateApp == "1"
    }

    /**
     * 判断是否需要更新应用（非强制）
     * @return true 表示需要更新，false 表示不需要
     */
    fun isNeedUpdate(): Boolean {
        return isNeedUpdateApp == "1"
    }
}


/*
"system_setting_config":{
    "call_default_price":"100",
    "call_connection_threshold":"1",
    "call_countdown_reminder":"2",
    "chat_default_price":"5",
    "free_chat_count":"5",
    "desktop_reward":"100",
    "notification_reward":"100",
    "host_coin_exchange_rate":"1000",
    "share_texts":"[\"\\u9047\\u89c1\\u8d85\\u751c\\u4e3b\\u64ad\\uff0c\\u804a\\u5230\\u505c\\u4e0d\\u4e0b\\u6765\\uff01\\u5feb\\u6765\\u548c\\u6211\\u4e00\\u8d77\\u89c6\\u9891\\u8fde\\u7ebf\\u5427\\uff5e\",\"\\u60f3\\u627e\\u4eba\\u503e\\u8bc9\\u6216\\u88ab\\u5ba0\\u7231\\uff1f\\u8fd9\\u91cc\\u7684\\u4e3b\\u64ad\\u61c2\\u4f60\\uff0c\\u5feb\\u6765\\u8bd5\\u8bd51v1\\u804a\\u5929\\uff01\",\"\\u5fc3\\u52a8\\u4e0d\\u53ea\\u4e00\\u77ac\\uff0c\\u8fd9\\u91cc\\u6709\\u4e13\\u5c5e\\u4f60\\u76841v1\\u8fde\\u7ebf\\uff01\",\"\\u6bcf\\u4e00\\u6b21\\u8fde\\u7ebf\\uff0c\\u90fd\\u662f\\u5fc3\\u8df3\\u52a0\\u901f\\u7684\\u5f00\\u59cb\\uff5e\\u6765\\u4f53\\u9a8c\\u4e13\\u5c5e\\u4f60\\u7684\\u4e92\\u52a8\\u5427\\uff01\",\"1\\u5bf91\\u89c6\\u9891\\uff0c\\u6bd4\\u60f3\\u8c61\\u8fd8\\u523a\\u6fc0\\u2026\\u4f60\\u4e5f\\u6765\\u8bd5\\u8bd5\\uff0c\\u88ab\\u64a9\\u7684\\u611f\\u89c9\\uff5e\",\"\\u522b\\u53ea\\u5237\\u77ed\\u89c6\\u9891\\u4e86\\uff0c\\u8fd9\\u91cc\\u771f\\u4eba\\u5bf9\\u4f60\\u8bf4\\u665a\\u5b89\\uff0c\\u6bd4\\u5c4f\\u5e55\\u66f4\\u52a8\\u5fc3\\uff5e\"]",
    "app_is_need_update":"1",
    "app_is_must_update":"1",
    "apk_url":"https:\/\/fichat.qiepian.vip\/fichat\/assets\/fichat111.apk",
    "app_version":"1.1.1",
    "app_update_text":"app 1.1.1\u7248\u672c \u4fee\u590d\u4e86\u4e00\u4e9bbug"
}
* */