package com.dubu.common.beans.shorts

import com.squareup.moshi.JsonClass


//des 礼物列表接口 + 自定义消息礼物 所需数据bean
@JsonClass(generateAdapter = true)
data class ChooseShortEventBean(
    val data: ShortNumDetailBean? = null,
    val key: String? = "",
    val state: Int? = 0,
    val disposable: Boolean? = false,
)

