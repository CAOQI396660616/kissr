package com.dubu.common.beans.recharge

import com.squareup.moshi.JsonClass


/*
* 首页顶部轮播视频对应的数据
* */
@JsonClass(generateAdapter = true)
data class RechargeCoinDesBean(
    val coin: Int? = 0,// 充值金币数量
    val bouns: Int? = 0,//积分数量
    val coinLv: Int? = 0,//金币等级 对应不同图片
    val recharge: String? = "",//充值金额
    val discount: String? = "",//折扣
)

