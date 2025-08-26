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
        HiLog.e(Tag2Common.TAG_12300, "####### HiRealCache.user的json是：：： ${GsonUtils.toJson(HiRealCache.user)}")
        HiLog.e(Tag2Common.TAG_12300, "####### HiRealCache.user的json是 toJson ：：： ${(HiRealCache.user)?.toJson()}")
        HiLog.e(Tag2Common.TAG_12300, "####### ${HiRealCache.sign}")
        HiLog.e(Tag2Common.TAG_12300, "####### ${HiRealCache.userId}")


        if (HiRealCache.userToken.isNullOrEmpty() || HiRealCache.user == null) {
            HiLog.e(Tag2Common.TAG_12300, "checkUserIsLogin false")
            return false
        }
        HiLog.e(Tag2Common.TAG_12300, "checkUserIsLogin true")

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
        val birthday = HiRealCache.user?.birthday
        val sex = HiRealCache.user?.sex
        HiLog.e(Tag2Common.TAG_12300, "检查用户是否需要初始化资料 (以下2个有一个为空 就认为没有初始化) birthday = $birthday , sex = $sex")
        return !((birthday.isNullOrEmpty()) || (sex.isNullOrEmpty()))
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



}