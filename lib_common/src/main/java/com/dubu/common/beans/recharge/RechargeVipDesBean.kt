package com.dubu.common.beans.recharge

import com.squareup.moshi.JsonClass


/*
* 首页顶部轮播视频对应的数据
* */
@JsonClass(generateAdapter = true)
data class RechargeVipDesBean(
    val recharge: String? = "9.99",// 充值金额
    val title: String? = "",//标题
    val content: String? = "",//内容
)

