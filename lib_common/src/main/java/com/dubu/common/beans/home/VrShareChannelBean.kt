package com.dubu.common.beans.home

import com.squareup.moshi.JsonClass

//api- 语音房-分享渠道
@JsonClass(generateAdapter = true)
data class VrShareChannelBean(
    var shareChannelType: Int? = 0,
    var shareChannelName: String? = "",
)




