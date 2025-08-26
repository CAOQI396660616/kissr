package com.dubu.common.api

import com.dubu.common.beans.UserBean
import com.dubu.common.beans.base.BaseResponseBean
import com.dubu.common.http.BaseClient
import com.dubu.common.http.NetConfig.RS_DATA_ERROR
import com.dubu.common.http.OnFailed
import com.dubu.common.manager.LoginManager
import com.dubu.common.utils.HiRealCache
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 *  登录模块接口服务
 */
interface CommonApiService {

    // 访客登录接口
    @FormUrlEncoded
    @POST("user/guest/login")
    suspend fun guestLogin(
        @Field("deviceId") deviceId: String
    ): BaseResponseBean<UserBean>

    // 设置用户性别接口
    @PUT("user/gender")
    suspend fun setUserGender(
        @Body genderRequest: Map<String, String>
    ): BaseResponseBean<Any>

}


class CommonClient : BaseClient<CommonApiService>(CommonApiService::class) {

    suspend fun guestLogin(failed: OnFailed): UserBean? {
        val ret = try {
            service.guestLogin(HiRealCache.deviceId)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        // 保存用户信息
        LoginManager.initLoginSuccess(ret.data)

        return ret.data
    }

    suspend fun setUserGender(sex: String, failed: OnFailed): Boolean {
        val genderRequest = mapOf("sex" to sex)

        val ret = try {
            service.setUserGender(genderRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return false
        }

        if (checkCodeFailed(ret, failed)) {
            return false
        }

        return true
    }
}





