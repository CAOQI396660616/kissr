package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryBean(

    val country_code: String? = "",
    val country_image: String? = "",
    val country_name: String? = "",
)


/*

{
"country_code":"CN",
"country_name":"中国",
"country_image":"https://fichat-dev.tos-cn-guangzhou.volces.com/uploads/20250311/67cffcd9aa358.png?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250623%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250623T092631Z&X-Tos-Expires=3600&X-Tos-SignedHeaders=host&X-Tos-Signature=88472e96e4ad9b4425175d31327b0318f264b4f9d88f0e4d8bcbc8fdccaf07ff"
}
*/