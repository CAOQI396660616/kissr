package com.dubu.common.http

import android.net.Uri

object NetWorkConst {

    const val BASE_URL_DEBUG: String = "https://fichat-api-new.reelhunter.online/api/" //主域名地址 测试地址
    const val BASE_URL_RELEASE: String = "https://fichat-api-new.reelhunter.online/api/" //主域名地址 测试地址

    const val BASE_URL_PRIVACY: String = "https://fichat.qiepian.vip/fichat/agreement/userAgreement.html"
    const val BASE_URL_UNION: String = "https://dev-guild.kisschat.com/union-join"


    const val POST: String = "POST"
    const val GET: String = "GET"

    //const val LOGIN_VISITOR = "user/guestLogin" //游客登录
    const val LOGIN_VISITOR = "kol/guestLogin" //游客登录
    const val LOGIN_EMAIL_SEND_CODE = "user/sendVerifyCode" //
    const val LOGIN_EMAIL_CODE = "kol/emailLogin" //
    const val LOGIN_OUT = "logout" //
    const val HOME_CALL_LIST = "home/new"
    const val HOME_RECOMMEND_LIST = "kol/recommend/users" //
    const val FILE_UPLOAD = "fileUpload" //文件上传
    const val GET_USERINFO = "getUserInfo" //获取用户ID信息
    const val GET_ME_USERINFO = "user/me" //获取用户信息
    const val GET_ME_USERINFO_V2 = "kol/me" //获取主播自己信息 新接口 和userBean的数据结构不一致 是包含关系
    const val GET_USER_LIST = "batch/user/list" //批量用户列表
    const val UPDATE_USERINFO = "kol/update" //更新用户信息
    const val POST_VIDEO = "kol/postVideo" //主播-发布视频
    const val POST_IMAGE = "kol/postImage" //主播-发布图片
    const val POST_DEL = "kol/postDelete" //帖子-删除
    const val POST_TOP = "kol/postTop" //帖子-置顶
    const val GET_ME_SERVICE_LIST = "kol/service/list" //主播服务-列表
    const val GET_CALL_END_INFO = "room/end/info" //获取通话结束信息
    const val GET_GIFT_LIST = "gift/list" //礼物-列表
    const val GET_ME_SERVICE_CREATE = "kol/service/create" //主播服务-列表
    const val GET_ME_SERVICE_DELETE = "kol/service/delete" //主播服务-删除
    const val GET_ME_SERVICE_UPDATE = "kol/service/update" //主播服务-更新
    const val FANS_LIST = "kol/fans" //主播-相册
    const val TEXT_TRANSLATE = "text/translate" //翻译（单条）
    const val TEXT_TRANSLATE_LIST = "translate/batch" //翻译（单条）
    const val ROOM_CREATE = "room/report/create" //建房上报
    const val ROOM_DESTROY = "room/report/destroy" //销毁房间上报
    const val JSON_LIST = "user/settingList" //common 配置接口
    const val COUNTRY_LIST = "countryList" //国家列表 暂时废弃 综合json 到 上面的接口上了
    const val KOL_VIDEO_HISTORY = "room/history" //通话历史列表-主播
    const val KOL_REPORT = "complaint/submit" //提交投诉
    const val KOL_REPORT_UPDATE = "complaint/update-audit-status" //更新投诉审核状态并推送消息
    const val GET_SHARE_INFO = "share/text" //获取随机分享文案
    const val GET_SYS_MSG = "system/notify/list" //获取系统消息列表
    const val KOL_INCOME_INFO = "kol/income/info" //获取主播收入信息（简化版）


    const val POST_LIST = "kol/postList" //主播-相册
    //0:审核中 PENDING 1:已通过（上架）APPROVED 2:不通过 REJECTED
    const val POST_LIST_PENDING = "0" //主播-相册 0:审核中
    const val POST_LIST_APPROVED = "1" //主播-相册 1:已通过（上架）
    const val POST_LIST_REJECTED = "2" //主播-相册 2:不通过 REJECTED
    const val POST_LIST_ALL = "" //主播-相册 查询全部不传递

    //all gift video_call text_chat kol_service
    const val INCOME_LIST = "kol/income/" //获取主播收入记录列表
    const val INCOME_LIST_ALL               = "all"
    const val INCOME_LIST_GIFT              = "gift"
    const val INCOME_LIST_VIDEO_CALL        = "video_call"
    const val INCOME_LIST_TEXT_CHAT         = "text_chat"
    const val INCOME_LIST_KOL_SERVICE       = "kol_service"


    const val UNION_LIST = "union/list" //获取公会列表
    const val UNION_MY = "union/my" //获取我的公会信息
    const val UNION_JOIN = "union/join" //申请加入公会(主播端/用户端)
    const val UNION_EXIT = "union/quit" //退出公会
    const val UNION_HISTORY = "union/kol/activities/list" //获取主播端公会活动列表


    /**
     * 构造 Union-Join 页面 URL
     *
     * @param baseUrl       基础地址（如：https://dev-guild.kisschat.com/union-join）
     * @param token         登录或验证用的 token
     * @param screenLanguage 用户界面语言参数，如 "en", "zh", 等
     * @return 构造好的完整 URL
     */
    fun buildUnionJoinUrl(
        baseUrl: String = BASE_URL_UNION,
        token: String,
        screenLanguage: String
    ): String {
        return Uri.parse(baseUrl).buildUpon()
            .appendQueryParameter("scene", "kissChatAndroidToUnion")
            .appendQueryParameter("token", token)
            .appendQueryParameter("screenLanguage", screenLanguage)
            .build()
            .toString()
    }
}
