package com.dubu.common.beans.common

import com.squareup.moshi.JsonClass
import java.io.Serializable


//des 礼物列表接口 + 自定义消息礼物 所需数据bean
@JsonClass(generateAdapter = true)
data class GiftInfoBean(
    val name: String? = "",
    val image: String? = "",
    val giftSn: Long? = -1,
    val value: Long? = 0, //金币
    var nums: Long? = 0L,
    var isChoose: Boolean? = false,

) : Serializable, Comparable<GiftInfoBean> {
    override fun compareTo(other: GiftInfoBean): Int {
        return if (value!! < other.value!!) -1 else 1
    }
}


