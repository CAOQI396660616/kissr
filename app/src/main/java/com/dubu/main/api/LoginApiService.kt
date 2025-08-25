package com.dubu.main.api

import com.dubu.common.beans.base.BaseResponseBean
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.http.BaseClient
import com.dubu.common.http.NetConfig.RS_DATA_ERROR
import com.dubu.common.http.NetWorkConst
import com.dubu.common.http.OnFailed
import com.dubu.common.manager.LoginManager
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.ToastTool
import com.dubu.main.R
import retrofit2.http.*

/**
 *  登录模块接口服务
 */
interface LoginApiService {

    @POST(NetWorkConst.LOGIN_VISITOR)
    suspend fun visitorLogin(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>

    @POST(NetWorkConst.LOGIN_EMAIL_SEND_CODE)
    suspend fun emailSendCode(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<Any>

    @POST(NetWorkConst.LOGIN_EMAIL_CODE)
    suspend fun emailCodeLogin(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>


    @GET(NetWorkConst.JSON_LIST)
    suspend fun getJsonList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<JsonList>

}


class LoginClient : BaseClient<LoginApiService>(LoginApiService::class) {

    suspend fun getJsonList(failed: OnFailed): JsonList? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getJsonList(params)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.initJsonSuccess(ret.data)

        return ret.data
    }

    suspend fun visitorLogin(uuid: String, failed: OnFailed): UserBean? {
        val params = newParamMap().apply {
            put("type", "normal")
            put("uuid", uuid)
        }

        val ret = try {
            service.visitorLogin(params)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        /*
        * 这里登录成功以后 处理
        * token  user  秘钥
        * */
        LoginManager.initLoginSuccess(ret.data)
        return ret.data
    }

    suspend fun emailSendCode(email: String, failed: OnFailed): Any? {
        val params = newParamMap().apply {
            put("email", email)
            put("uuid", HiRealCache.deviceId)
        }

        val ret = try {
            service.emailSendCode(params)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        return ret.data
    }

    suspend fun emailCodeLogin(email: String, code: String, failed: OnFailed): UserBean? {
        val params = newParamMap().apply {
            put("email", email)
            put("code", code)
            //            put("uuid", HiRealCache.deviceId)
        }

        val ret = try {
            service.emailCodeLogin(params)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        /*
          * 这里登录成功以后 处理
          * token  user  秘钥
          * */
        LoginManager.initLoginSuccess(ret.data)

        return ret.data
    }


}





