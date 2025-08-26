package com.dubu.common.http

import android.net.Uri

object NetWorkConst {

    /*
    * - 测试环境: http://api.kissr.love
      - 正式环境: https://api.kissr.ai
    * */

    const val BASE_URL_DEBUG: String = "http://api.kissr.love/" //主域名地址 测试环境
    const val BASE_URL_RELEASE: String = "https://api.kissr.ai/" //主域名地址 正式环境

    const val BASE_URL_PRIVACY: String = "https://fichat.qiepian.vip/fichat/agreement/userAgreement.html"


    const val POST: String = "POST"
    const val GET: String = "GET"








    //const val LOGIN_VISITOR = "user/guestLogin" //游客登录
    const val LOGIN_VISITOR = "kol/guestLogin" //游客登录
    const val LOGIN_EMAIL_SEND_CODE = "user/sendVerifyCode" //
    const val LOGIN_EMAIL_CODE = "kol/emailLogin" //
    const val FILE_UPLOAD = "fileUpload" //文件上传
    const val GET_USERINFO = "getUserInfo" //获取用户ID信息
    const val GET_ME_USERINFO = "user/me" //获取用户信息
    const val GET_ME_USERINFO_V2 = "kol/me" //获取主播自己信息 新接口 和userBean的数据结构不一致 是包含关系
    const val GET_USER_LIST = "batch/user/list" //批量用户列表
    const val UPDATE_USERINFO = "kol/update" //更新用户信息
    const val FANS_LIST = "kol/fans" //主播-相册
    const val ROOM_DESTROY = "room/report/destroy" //销毁房间上报
    const val JSON_LIST = "user/settingList" //common 配置接口
    const val KOL_VIDEO_HISTORY = "room/history" //通话历史列表-主播
    const val KOL_REPORT = "complaint/submit" //提交投诉
    const val KOL_REPORT_UPDATE = "complaint/update-audit-status" //更新投诉审核状态并推送消息
    const val GET_SHARE_INFO = "share/text" //获取随机分享文案
    const val GET_SYS_MSG = "system/notify/list" //获取系统消息列表


}
