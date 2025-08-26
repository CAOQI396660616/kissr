package com.dubu.common.api

import com.dubu.common.beans.UserBean
import com.dubu.common.beans.base.BaseResponseBean
import com.dubu.common.beans.common.ShareBean
import com.dubu.common.beans.me.UploadResultBean
import com.dubu.common.http.BaseClient
import com.dubu.common.http.NetConfig.RS_DATA_ERROR
import com.dubu.common.http.NetWorkConst
import com.dubu.common.http.OnFailed
import com.dubu.common.manager.LoginManager
import com.dubu.common.utils.HiRealCache
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.QueryMap

/**
 *  登录模块接口服务
 */
interface CommonApiService {

    //上传图片
    @Multipart
    @POST(NetWorkConst.FILE_UPLOAD)
    suspend fun fileUpload(
        @Part("path") path: RequestBody,
        @Part file: MultipartBody.Part
    ): BaseResponseBean<UploadResultBean>


    @GET(NetWorkConst.GET_USERINFO)
    suspend fun getUserInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>

    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserInfo(
        @Field("kol_name") kol_name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("avatar") avatar: String,
        @Field("gender") gender: String,
        @Field("birthday") birthday: String,
        @Field("images[]") imageUrls: List<String>
    ): BaseResponseBean<UserBean>



    @FormUrlEncoded
    @POST(NetWorkConst.GET_USER_LIST)
    suspend fun getUserList(
        @Field("ids[]") imageUrls: List<String>
    ): BaseResponseBean<List<UserBean>>


    @GET(NetWorkConst.GET_SHARE_INFO)
    suspend fun getShareInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<ShareBean>


    @GET(NetWorkConst.GET_ME_USERINFO)
    suspend fun getMeUserInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>

    // 访客登录接口
    @FormUrlEncoded
    @POST("user/guest/login")
    suspend fun guestLogin(
        @Field("deviceId") deviceId: String
    ): BaseResponseBean<UserBean>

}


class CommonClient : BaseClient<CommonApiService>(CommonApiService::class) {

    suspend fun getMeUserInfo(failed: OnFailed): UserBean? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getMeUserInfo(params)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }
        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.updateUserInfo(ret.data)

        return ret.data
    }


    suspend fun getShareInfo(failed: OnFailed): ShareBean? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getShareInfo(params)
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




    suspend fun fileUpload(
        path: RequestBody,
        file: MultipartBody.Part,
        failed: OnFailed
    ): UploadResultBean? {
        val ret = try {
            service.fileUpload(path, file)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null;
        }
        if (checkCodeFailed(ret, failed)) {
            return null
        }
        return ret.data
    }

    suspend fun getUserInfo(id: String, failed: OnFailed): UserBean? {
        val params = newParamMap().apply {
            put("id", id)
        }

        val ret = try {
            service.getUserInfo(params)
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

    suspend fun updateUserInfo(
        kol_name: String,
        email: String,
        mobile: String,
        avatar: String,
        gender: String,
        birthday: String,
        images: MutableList<String>,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserInfo(kol_name, email, mobile, avatar, gender, birthday, images)
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


    suspend fun getUserList(
        ids: MutableList<String>,
        failed: OnFailed
    ): List<UserBean>? {

        val ret = try {
            service.getUserList(ids)
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
        LoginManager.updateUserInfo(ret.data)

        return ret.data
    }
}





