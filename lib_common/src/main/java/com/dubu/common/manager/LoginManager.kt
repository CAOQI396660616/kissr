package com.dubu.common.manager

import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.constant.SpKey2Common
import com.dubu.common.constant.Tag2Common
import com.dubu.common.ext.fromJson
import com.dubu.common.ext.toJson
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.tencent.mmkv.MMKV

/*
* 处理登录相关的业务管理器
* */

object LoginManager {


    /**
     * 用户重新打开app校验用户是否登录 同时处理数据到HiRealCache
     * @return [Boolean]
     */
    @JvmStatic
    fun checkUserIsLogin(): Boolean {
        HiRealCache.userToken = getUserTokenToMMKV()
        HiRealCache.user = getUserByMMKV()
        HiRealCache.userId = (getUserSnToMMKV() ?: "0").toLong()


        HiLog.e(Tag2Common.TAG_12300, "####### ${HiRealCache.userToken}")
        HiLog.e(Tag2Common.TAG_12300, "####### rtcToken：：： ${HiRealCache.user?.rtcToken}")
        HiLog.e(Tag2Common.TAG_12300, "####### HiRealCache.user的json是：：： ${GsonUtils.toJson(HiRealCache.user)}")
        HiLog.e(Tag2Common.TAG_12300, "####### HiRealCache.user的json是 toJson ：：： ${(HiRealCache.user)?.toJson()}")
        HiLog.e(Tag2Common.TAG_12300, "####### ${HiRealCache.sign}")
        HiLog.e(Tag2Common.TAG_12300, "####### ${HiRealCache.userId}")

        HiLog.e(
            Tag2Common.TAG_12300,
            "checkUserIsLogin  languageCodes = ${HiRealCache.user?.languageCodes}"
        )
        if (HiRealCache.userToken.isNullOrEmpty() || HiRealCache.user == null) {
            HiLog.e(Tag2Common.TAG_12300, "false")
            return false
        }
        HiLog.e(Tag2Common.TAG_12300, "true")

        return true

    }

    /**
     * token是否有效
     * @return [Boolean]
     */
    @JvmStatic
    fun checkUserTokenIsValid(): Boolean {
        val userTokenTimeMMKV = getUserTokenTimeToMMKV()
        HiLog.e(Tag2Common.TAG_12300, "token checkUserTokenIsValid")
        if (userTokenTimeMMKV.isNullOrEmpty()) {
            HiLog.e(Tag2Common.TAG_12300, "token没有保存")
            return true
        }

        val userTokenTime = userTokenTimeMMKV.toLong()
        HiLog.e(Tag2Common.TAG_12300, "#######userTokenTime =  ${userTokenTime}")
        HiLog.e(Tag2Common.TAG_12300, "#######syssTokenTime =  ${System.currentTimeMillis()}")

        val desHourTimeFromTimestampCountDown =
            EaseDateUtils.getDescriptionTimeFromTimestampV2(userTokenTime)
        HiLog.e(Tag2Common.TAG_12300, "本地存储Token时刻是在 : $desHourTimeFromTimestampCountDown")


        if (System.currentTimeMillis() - userTokenTime < HiRealCache.exTokenTime) {

            return true
        }
        HiLog.e(Tag2Common.TAG_12300, "token是无效")

        return false

    }


    /**
     * 用户填写过资料吗
     * @return [Boolean] false没填
     */
    @JvmStatic
    fun checkUserIsInited(): Boolean {
        val needInit = HiRealCache.user?.isNeedInit
        HiLog.e(Tag2Common.TAG_12300, "####### needInit = ${needInit}")

        if (needInit == 1) {
            return false
        }
        return true
    }

    //登录成功以后 处理业务逻辑
    @JvmStatic
    fun initLoginSuccess(user: UserBean?) {
        user ?: return
        HiRealCache.user = user
        HiRealCache.userToken = user.userToken
        HiRealCache.userId = user.userSn

        saveUserTokenToMMKV(user.userToken ?: "")
        saveUserToMMKV(user)
        saveUserSnToMMKV(user.userSn.toString())
        saveUserTokenTimeToMMKV((System.currentTimeMillis()).toString())

    }


    @JvmStatic
    fun updateUserInfo(user: UserBean?) {
        user ?: return

        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 images: ${user.images} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 avatar: ${user.avatar} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 nickname: ${user.nickname} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 countryCode: ${user.countryCode} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 videoCallCount: ${user.videoCallCount} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 userVideo: ${user.userVideo} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 money: ${user.money} "
        )

        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 user : ${GsonUtils.toJson(user)} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 user : ================== "
        )
        HiRealCache.user?.nickname = user.nickname
        HiRealCache.user?.name = user.name
        HiRealCache.user?.avatar = user.avatar
        HiRealCache.user?.images = user.images
        HiRealCache.user?.birthday = user.birthday
        HiRealCache.user?.country = user.country
        HiRealCache.user?.age = user.age
        HiRealCache.user?.userSn = user.userSn
        HiRealCache.user?.info = user.info
        HiRealCache.user?.countryCode = user.countryCode
        HiRealCache.user?.videoCallCount = user.videoCallCount
        HiRealCache.user?.userVideo = user.userVideo
        HiRealCache.user?.money = user.money
        HiRealCache.user?.unionRole = user.unionRole
        HiRealCache.user?.unionStatus = user.unionStatus

        HiRealCache.userId = user.userSn

        HiRealCache.user?.let { saveUserToMMKV(it) }
        saveUserSnToMMKV(user.userSn.toString())

    }


    @JvmStatic
    fun removeUserInfo() {

        HiLog.e(
            Tag2Common.TAG_12300,
            "用户登出 移除本地缓存 user : ================== "
        )
        val userBean = UserBean()
        HiRealCache.user = userBean
        HiRealCache.userId = -1L
        HiRealCache.userToken = ""

        saveUserToMMKV(userBean)
        saveUserSnToMMKV("")
        saveUserTokenToMMKV("")
        saveUserTokenTimeToMMKV("")
    }

    //----------------------------------------------------------------------------------------------


    @JvmStatic
    fun saveUserToMMKV(user: UserBean) {
        val toJson = user.toJson()
        HiLog.e(Tag2Common.TAG_12300, "saveUserToMMKV 数据= $toJson")
        MMKV.defaultMMKV().encode(SpKey2Common.CURRENT_USER_INFO, toJson)
    }

    @JvmStatic
    fun getUserByMMKV(): UserBean? {
        val decodeString = MMKV.defaultMMKV().decodeString(SpKey2Common.CURRENT_USER_INFO, "")

        HiLog.e(Tag2Common.TAG_12300, "getUserByMMKV 数据= $decodeString")
        return if (decodeString.isNullOrBlank()) {
            null
        } else {
            decodeString.fromJson<UserBean>()
        }
    }


    //----------------------------------------------------------------------------------------------


    @JvmStatic
    fun saveUserTokenTimeToMMKV(userTokenTime: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveUserTokenTimeToMMKV 数据= $userTokenTime")
        MMKV.defaultMMKV().encode(SpKey2Common.CURRENT_USER_TOKEN_TIME, userTokenTime)
    }

    @JvmStatic
    fun getUserTokenTimeToMMKV(): String? {
        val decodeString = MMKV.defaultMMKV().decodeString(SpKey2Common.CURRENT_USER_TOKEN_TIME, "")
        HiLog.e(Tag2Common.TAG_12300, "getUserTokenTimeToMMKV 数据= $decodeString")
        return decodeString
    }
    //----------------------------------------------------------------------------------------------


    @JvmStatic
    fun saveUserTokenToMMKV(userToken: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveUserTokenToMMKV 数据= $userToken")
        MMKV.defaultMMKV().encode(SpKey2Common.CURRENT_USER_TOKEN, userToken)
    }

    @JvmStatic
    fun getUserTokenToMMKV(): String? {
        val decodeString = MMKV.defaultMMKV().decodeString(SpKey2Common.CURRENT_USER_TOKEN, "")
        HiLog.e(Tag2Common.TAG_12300, "getUserTokenToMMKV 数据= $decodeString")
        return decodeString
    }


    //----------------------------------------------------------------------------------------------
    @JvmStatic
    fun saveUserSnToMMKV(userSn: String) {
        HiLog.e(Tag2Common.TAG_12300, "saveUserSnToMMKV 数据= $userSn")

        MMKV.defaultMMKV().encode(SpKey2Common.CURRENT_USER_SN, userSn)
    }

    @JvmStatic
    fun getUserSnToMMKV(): String? {
        val decodeString = MMKV.defaultMMKV().decodeString(SpKey2Common.CURRENT_USER_SN, "")
        HiLog.e(Tag2Common.TAG_12300, "getUserSnToMMKV 数据= $decodeString")
        return if (decodeString.isNullOrBlank()) {
            "0"
        } else {
            decodeString
        }
    }

    /**
     *
     * 第一次登录获取用户信息以后 会直接去获取json配置
     * 后续N次重新登录也是一样 会直接去获取json配置
     * 若在Token有效期内自动登录 会在SplashAc中获取json配置
     * 这里有3个json
     * 国家列表 用户会修改信息时候用到
     * 语言列表 主播修改自己擅长的语言会用到 多选 本地会存储一个 zh,zh的数据结构 下面需要恢复这个
     * @param [data]
     */
    fun initJsonSuccess(data: JsonList?) {


        if (data == null) {
            return
        }
        HiRealCache.countryList = data.country_list
        HiRealCache.languageList = data.language_list
        HiLog.e(Tag2Common.TAG_12300, "countryList = ${HiRealCache.countryList}")
        HiLog.e(Tag2Common.TAG_12300, "languageList = ${HiRealCache.languageList}")

        val languageCodes = HiRealCache.user?.languageCodes
        HiLog.e(Tag2Common.TAG_12300, "languageCodes = ${HiRealCache.user?.languageCodes}")
        if (languageCodes.isNullOrEmpty()) {
            HiLog.e(Tag2Common.TAG_12300, "languageCodes.isNullOrEmpty()")
        } else {
            //zh,zh 拆分

            val splitAndDistinct = splitAndDistinct(languageCodes)
            HiLog.e(Tag2Common.TAG_12300, "splitAndDistinct = $splitAndDistinct")
            if (!HiRealCache.languageList.isNullOrEmpty()) {
                HiRealCache.languageList!!.forEachIndexed { _, languageInfo ->
                    if (splitAndDistinct.contains(languageInfo.country_code)) {
                        languageInfo.isSelected = true
                        HiLog.e(
                            Tag2Common.TAG_12300,
                            "languageInfo.country_code = ${languageInfo.country_code}"
                        )
                    }

                }
            }

        }


    }

    /**
     * //zh,zh 拆分
     * @param [input]
     * @return [List<String>]
     */
    private fun splitAndDistinct(input: String): List<String> {
        return input.split(",")
            .map { it.trim() }         // 去除每个元素两端的空格
            .filter { it.isNotBlank() } // 过滤掉空字符串
            .distinct()                 // 去重（保留顺序）
            .toList()                   // 转换为 List
    }

    fun updateUserLang(data: UserBean?) {
        data ?: return

        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserLang user : ${GsonUtils.toJson(data)} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserLang languageCodes : ${data.languageCodes} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 user : ================== "
        )
        HiRealCache.user?.languageCodes = data.languageCodes
        HiRealCache.user?.let { saveUserToMMKV(it) }
        getUserByMMKV()
    }

    fun updateUserPrice(data: UserBean?) {
        data ?: return

        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserPrice user : ${GsonUtils.toJson(data)} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserPrice videoGoldPrice : ${data.videoGoldPrice} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 user : ================== "
        )
        HiRealCache.user?.videoGoldPrice = data.videoGoldPrice
        HiRealCache.user?.let { saveUserToMMKV(it) }
        getUserByMMKV()
    }

    fun updateUserOpenService(data: UserBean?) {
        data ?: return

        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserOpenService user : ${GsonUtils.toJson(data)} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserOpenService serviceStatus : ${data.serviceStatus} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 user : ================== "
        )
        HiRealCache.user?.serviceStatus = data.serviceStatus
        HiRealCache.user?.let { saveUserToMMKV(it) }
        getUserByMMKV()
    }

    fun updateUserOnline(data: UserBean?) {
        data ?: return

        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserOnline user : ${GsonUtils.toJson(data)} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 updateUserOnline serviceStatus : ${data.onlineStatus} "
        )
        HiLog.e(
            Tag2Common.TAG_12300,
            "更新用户信息 user : ================== "
        )
        HiRealCache.user?.onlineStatus = data.onlineStatus
        HiRealCache.user?.let { saveUserToMMKV(it) }
        getUserByMMKV()
    }

    fun initHelloList() {
        val mutableListOf = mutableListOf<String>()

        mutableListOf.add(BaseApp.instance.getString(R.string.hello_1))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_2))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_3))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_4))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_5))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_6))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_7))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_8))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_9))
        mutableListOf.add(BaseApp.instance.getString(R.string.hello_10))

        HiRealCache.helloList = mutableListOf

    }

    fun initChatList() {
        val mutableListOf = mutableListOf<String>()

        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_1))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_2))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_3))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_4))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_5))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_6))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_7))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_8))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_9))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_10))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_11))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_12))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_13))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_14))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_15))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_16))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_17))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_18))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_19))
        mutableListOf.add(BaseApp.instance.getString(R.string.chat_msg_20))

        HiRealCache.chatList = mutableListOf
    }

    //----------------------------------------------------------------------------------------------


}