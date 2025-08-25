package com.dubu.rtc.api

import com.dubu.common.beans.UserBean
import com.dubu.common.beans.base.BaseResponseBean
import com.dubu.common.beans.common.ReportBean
import com.dubu.common.beans.common.HttpSysMsgBean
import com.dubu.common.beans.common.ShareBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.beans.me.UploadResultBean
import com.dubu.common.beans.rtc.VideoRtcResultBean
import com.dubu.common.http.BaseClient
import com.dubu.common.http.NetConfig.RS_DATA_ERROR
import com.dubu.common.http.NetWorkConst
import com.dubu.common.http.OnFailed
import com.dubu.common.manager.LoginManager
import com.dubu.common.utils.HiRealCache
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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


    @GET(NetWorkConst.FANS_LIST)
    suspend fun fansList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<UserBean>>

    @GET(NetWorkConst.KOL_VIDEO_HISTORY)
    suspend fun videoRtcHistory(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<VideoRtcResultBean>>

    @GET(NetWorkConst.JSON_LIST)
    suspend fun getJsonList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<JsonList>


    @GET(NetWorkConst.GET_ME_USERINFO_V2)
    suspend fun getMeUserInfoV2(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>

    @POST(NetWorkConst.ROOM_DESTROY)
    suspend fun roomDestroy(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<Any>


    @FormUrlEncoded
    @POST(NetWorkConst.GET_USER_LIST)
    suspend fun getUserList(
        @Field("ids[]") imageUrls: List<String>
    ): BaseResponseBean<List<UserBean>>


    @POST(NetWorkConst.KOL_REPORT)
    suspend fun report(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<ReportBean>

    @POST(NetWorkConst.KOL_REPORT_UPDATE)
    suspend fun reportUpdate(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>


    @GET(NetWorkConst.GET_SHARE_INFO)
    suspend fun getShareInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<ShareBean>

    @GET(NetWorkConst.GET_SYS_MSG)
    suspend fun getSysMsg(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<HttpSysMsgBean>>


    @GET(NetWorkConst.GET_ME_USERINFO)
    suspend fun getMeUserInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>

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

    suspend fun getSysMsg(page: String,
                          pageSize: String,failed: OnFailed): CommonPageBean<HttpSysMsgBean>? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getSysMsg(params)
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


    suspend fun roomDestroy(
        hangup_by: String,
        room_state_id: String,
        failed: OnFailed
    ): Any? {
        val params = newParamMap().apply {
            put("hangup_by", hangup_by) //kol :主播 user:用户 谁挂断的
            put("room_state_id", room_state_id) // 房间id

            put("end_time", 0) //这里0 标识我挂断
        }

        val ret = try {
            service.roomDestroy(params)
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

    suspend fun getMeUserInfoV2(failed: OnFailed): UserBean? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getMeUserInfoV2(params)
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


    suspend fun report(reason: String,
                       content: String,
                       kol_id: String,failed: OnFailed): ReportBean? {
        val params = newParamMap().apply {
            put("reason", reason)
            put("content", content)
            put("kol_id", kol_id)
        }

        val ret = try {
            service.report(params)
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
    suspend fun reportUpdate( id: String,
                              audit_status: String,failed: OnFailed): UserBean? {
        val params = newParamMap().apply {
            put("id", id)
            put("audit_status", audit_status)
        }

        val ret = try {
            service.reportUpdate(params)
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

        if (checkCodeFailed(ret, failed,false)) {
            return null
        }

        LoginManager.initJsonSuccess(ret.data)
        return ret.data
    }

    suspend fun fansList(
        page: String,
        pageSize: String,
        failed: OnFailed
    ): CommonPageBean<UserBean>? {
        val params = newParamMap().apply {
            put("kol_id", (HiRealCache.user?.kolId ?: 0L).toString())
            put("page", page)
            put("page_size", pageSize)
        }

        val ret = try {
            service.fansList(params)
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

    suspend fun videoRtcHistory(
        date_type: String,
        page: String,
        pageSize: String,
        failed: OnFailed
    ): CommonPageBean<VideoRtcResultBean>? {
        val params = newParamMap().apply {
            put("kol_id", (HiRealCache.user?.kolId ?: 0L).toString())
            if (date_type.isNotEmpty())
                put("date_type", date_type)
            put("page", page)
            put("page_size", pageSize)
        }

        val ret = try {
            service.videoRtcHistory(params)
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
}





