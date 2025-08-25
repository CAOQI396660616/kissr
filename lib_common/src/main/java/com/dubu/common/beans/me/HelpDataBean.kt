package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HelpDataBean(
    var des: String? = "",
    var questionList: List<QuestionDataBean>? = null,
)

@JsonClass(generateAdapter = true)
data class QuestionDataBean(
    var question: String? = "",
    var answer: String? = "",
)