package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


/*
* 翻译结果 单条
* */
@JsonClass(generateAdapter = true)
data class TranslateResultBean(
    var text: String? = "", //消息的json
    val translate: String? = "", //消息的类型
)


@JsonClass(generateAdapter = true)
data class TranslateListResultBean(
    val error_count: Long? = 0L,
    val success_count: Long? = 0L,
    val total_count: Long? = 0L,
    val translations: MutableList<TranslationListItemBean> ? =null
)


@JsonClass(generateAdapter = true)
data class TranslationListItemBean(
    val index: Long? = 0L,
    val original_text: String,
    val translated_text: String
    //    val confidence: Double,
    //    val source_language: String,
    //    val target_language: String,
)

// 消息数据类
@JsonClass(generateAdapter = true)
data class TranslateTask(
    val index: Int,
    val originalText: String,
    var translatedText: String? = null
)