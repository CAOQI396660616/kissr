package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolIncomeListBean(

    val id: Long ? = 0L,

    val display_income_title: String? = "", // 标题
    val status_text: String? = "", // 计算中 待到账等
    val status: String? = "", // 计算中 待到账等 对应的状态

    val income_time: String? = "", //收入时间
    val created_at: String? = "", //收入时间
    val display_from_user_text: String? = "", //收入时间

    val usd_amount: String? = "", // $3.33

)

/*
    {
        "today_income":0.0,
        "total_income":0.0,
        "withdrawable_amount":0.0,
        "pending_amount":0.0,
        "exchange_rate":"3000金币=$1.0"
    }

*/