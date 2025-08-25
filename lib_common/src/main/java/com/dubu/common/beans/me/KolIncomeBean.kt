package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolIncomeBean(


    val exchange_rate: String? = "",//兑换率说明
    val pending_amount: String? = "",// 待到账金额（美元）
    val today_income: String? = "",// 今日收入（美元）
    val total_income: String? = "",// 累计收入（美元）
    val withdrawable_amount: String? = ""// 可提现金额（美元）
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