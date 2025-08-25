package com.dubu.common.helper


object LinkHelper {

    //https://fichat.reelhunter.online/#/live-streamer?id=34&sid=97044

    private const val WEB_URL = "https://fichat.reelhunter.online/#/live-streamer"
    private const val WEB_P_QUES = "?"
    private const val WEB_P_ATTR = "="
    private const val WEB_P_CONNECT = "&"

    private const val WEB_P_ID = "id"
    private const val WEB_P_INVITE_CODE = "sid"

    @JvmStatic
    fun toWebUserInfo(id: String, inviteCode: String): String {
        val sp= StringBuilder(WEB_URL)

        sp.append(WEB_P_QUES)

        sp.append(WEB_P_ID)
        sp.append(WEB_P_ATTR)
        sp.append(id)

        sp.append(WEB_P_CONNECT)

        sp.append(WEB_P_INVITE_CODE)
        sp.append(WEB_P_ATTR)
        sp.append(inviteCode)




        return sp.toString()
    }
}