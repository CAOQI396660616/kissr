package com.dubu.common.http

import com.tencent.mmkv.MMKV
import com.dubu.common.constant.SpKey2Common

/**
 * Author:v
 * Time:2022/10/19
 */

typealias OnFailed = (code: Int, msg: String) -> Unit
typealias OnSuccess = (code: Int, msg: String) -> Unit

object NetConfig {

    var isRelease = false
        private set

    fun init() {
        isRelease = MMKV.defaultMMKV().decodeBool(SpKey2Common.ENVIRONMENT_RELEASE, isRelease)
        //allen 这里的是否是 正式版判断有点问题 暂时写死
    }

    @JvmStatic
    fun changeEnvironment(release: Boolean) {
        isRelease = release
        isRelease = MMKV.defaultMMKV().encode(SpKey2Common.ENVIRONMENT_RELEASE, release)
    }


    fun baseUrl() = if (isRelease) {
        NetWorkConst.BASE_URL_RELEASE
    } else NetWorkConst.BASE_URL_DEBUG


    const val RS_SUCCESS = 0
    const val RS_FAIL = 400
    const val RS_DUPLICATED_ORDER = 501

    const val RS_TOKEN_INVALID = 401

    //app报错 主要是框架报错
    const val RS_DATA_ERROR= -1
}