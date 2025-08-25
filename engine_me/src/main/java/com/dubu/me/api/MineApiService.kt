package com.dubu.main.api

import com.dubu.common.beans.UserBean
import com.dubu.common.beans.base.BaseResponseBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.beans.me.*
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.BaseClient
import com.dubu.common.http.NetConfig
import com.dubu.common.http.NetConfig.RS_DATA_ERROR
import com.dubu.common.http.NetWorkConst
import com.dubu.common.http.OnFailed
import com.dubu.common.manager.LoginManager
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.ToastTool
import com.dubu.me.R
import com.dubu.rtc.api.CommonPageBean
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

/**
 *  登录模块接口服务
 */
interface MineApiService {

    //上传图片
    @Multipart
    @POST(NetWorkConst.FILE_UPLOAD)
    suspend fun fileUpload(
        @Part("path") path: RequestBody,
        @Part file: MultipartBody.Part
    ): BaseResponseBean<UploadResultBean>


    @GET(NetWorkConst.GET_USERINFO)
    suspend fun getUserInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>


    @GET(NetWorkConst.UNION_MY)
    suspend fun getMyUnionInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<KolUnionInfoBean>

    @GET(NetWorkConst.GET_ME_USERINFO_V2)
    suspend fun getMeUserInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<UserBean>

    @GET(NetWorkConst.COUNTRY_LIST)
    suspend fun getCountryList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<List<CountryBean>>

    @GET(NetWorkConst.GET_ME_SERVICE_LIST)
    suspend fun getMeServiceList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<List<KolServiceBean>>


    @POST(NetWorkConst.GET_ME_SERVICE_CREATE)
    suspend fun createService(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<String>

    @POST(NetWorkConst.UNION_JOIN)
    suspend fun joinUnion(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<KolUnionListBean>

    @POST(NetWorkConst.UNION_EXIT)
    suspend fun exitUnion(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<KolUnionListBean>

    @POST(NetWorkConst.LOGIN_OUT)
    suspend fun loginOut(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<String>


    @POST(NetWorkConst.GET_ME_SERVICE_DELETE)
    suspend fun deleteService(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<String>

    @POST(NetWorkConst.GET_ME_SERVICE_UPDATE)
    suspend fun editService(@Body map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<String>


    //    @POST(NetWorkConst.POST_VIDEO)
    //    suspend fun publishVideoV2(@Body request: List<PhotoDataBean>): BaseResponseBean<String>
    // 修改接口
    @POST(NetWorkConst.POST_VIDEO)
    suspend fun publishVideoV2(
        @Body request: VideoRequest
    ): BaseResponseBean<Any>

    // 请求体数据类
    @JsonClass(generateAdapter = true)
    data class VideoRequest(
        @Json(name = "videos")
        val videos: List<VideoDataBean>
    )

    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun initUserInfo(
        @Field("kol_name") kol_name: String,
        @Field("birthday") birthday: String,
        @Field("country_code") country: String,
        @Field("lang_codes") language: String,
        @Field("info") des: String,
        @Field("images[]") imageUrls: List<String>
    ): BaseResponseBean<UserBean>

    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserOpenService(
        @Field("service_status") status: Int,
    ): BaseResponseBean<UserBean>

    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserLang(
        @Field("lang_codes") language: String,
    ): BaseResponseBean<UserBean>

    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserPrice(
        @Field("video_gold_price") price: String,
    ): BaseResponseBean<UserBean>


    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserInfo(
        @Field("kol_name") kol_name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("avatar") avatar: String,
        @Field("birthday") birthday: String,
        @Field("info") info: String,
        @Field("country_code") country_code: String,
        @Field("online_status") online_status: String,
        @Field("language") language: String,
        @Field("images[]") imageUrls: List<String>
    ): BaseResponseBean<UserBean>

    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserInfoV2(
        @Field("kol_name") kol_name: String,
        @Field("avatar") avatar: String,
        @Field("country_code") country: String,
        @Field("birthday") birthday: String,
        @Field("info") des: String,
        @Field("images[]") imageUrls: List<String>
    ): BaseResponseBean<UserBean>


    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserInfoV1(
        @Field("kol_name") kol_name: String,
        @Field("country_code") country: String,
        @Field("birthday") birthday: String,
        @Field("info") des: String,
        @Field("images[]") imageUrls: List<String>
    ): BaseResponseBean<UserBean>


    @FormUrlEncoded
    @POST(NetWorkConst.UPDATE_USERINFO)
    suspend fun updateUserInfoV3(
        @Field("kol_name") kol_name: String,
        @Field("country_code") country: String,
        @Field("birthday") birthday: String,
        @Field("info") des: String,
    ): BaseResponseBean<UserBean>


    @FormUrlEncoded
    @POST(NetWorkConst.POST_VIDEO)
    suspend fun publishVideo(
        @Field("videos[]") videoUrls: List<String>
    ): BaseResponseBean<String>

    @FormUrlEncoded
    @POST(NetWorkConst.POST_IMAGE)
    suspend fun publishPhoto(
        @Field("images[]") imageUrls: List<String>
    ): BaseResponseBean<String>


    @GET(NetWorkConst.POST_LIST)
    suspend fun homeCallList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<KolPostBean>>

    @GET(NetWorkConst.INCOME_LIST)
    suspend fun incomeList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<KolIncomeListBean>>

    @GET(NetWorkConst.UNION_HISTORY)
    suspend fun unionHistoryList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<KolUnionHistoryListBean>>

    @GET(NetWorkConst.UNION_LIST)
    suspend fun unionList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<CommonPageBean<KolUnionListBean>>


    @FormUrlEncoded
    @POST(NetWorkConst.POST_DEL)
    suspend fun postDel(
        @Field("ids[]") ids: List<Long>
    ): BaseResponseBean<String>

    @FormUrlEncoded
    @POST(NetWorkConst.POST_TOP)
    suspend fun postToggleTop(
        @Field("is_top") is_top: Int, //1:置顶，2:取消置顶
        @Field("ids[]") ids: List<Long>
    ): BaseResponseBean<String>


    @GET(NetWorkConst.JSON_LIST)
    suspend fun getJsonList(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<JsonList>

    @GET(NetWorkConst.KOL_INCOME_INFO)
    suspend fun getKolIncomeInfo(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): BaseResponseBean<KolIncomeBean>

}


class MineClient : BaseClient<MineApiService>(MineApiService::class) {
    suspend fun getKolIncomeInfo(failed: OnFailed): KolIncomeBean? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getKolIncomeInfo(params)
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

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.initJsonSuccess(ret.data)

        return ret.data
    }

    suspend fun homeCallList(
        search: String, //0审核中   1通过   2拒绝  不传递这个字段则是获取全部
        page: String,
        pageSize: String,
        failed: OnFailed
    ): CommonPageBean<KolPostBean>? {
        val params = newParamMap().apply {
            if (search.isNotEmpty())
                put("audit_status", search)

            put("kol_id", (HiRealCache.user?.kolId ?: 0L).toString())
            put("page", page)
            put("page_size", pageSize)
        }

        val ret = try {
            service.homeCallList(params)
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

    suspend fun incomeList(
        search: String, //all gift video_call text_chat kol_service
        page: String,
        pageSize: String,
        failed: OnFailed
    ): CommonPageBean<KolIncomeListBean>? {
        val params = newParamMap().apply {
            put("type", search)
            //put("kol_id", (HiRealCache.user?.kolId ?: 0L).toString())
            put("page", page)
            put("page_size", pageSize)
        }

        val ret = try {
            service.incomeList(params)
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

    suspend fun unionHistoryList(
        search: String, //all gift video_call text_chat kol_service
        page: String,
        pageSize: String,
        failed: OnFailed
    ): CommonPageBean<KolUnionHistoryListBean>? {
        val params = newParamMap().apply {
            put("activity_type", search)
            //put("kol_id", (HiRealCache.user?.kolId ?: 0L).toString())
            put("page", page)
            put("page_size", pageSize)
        }

        val ret = try {
            service.unionHistoryList(params)
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

    suspend fun unionList(
        search: String, //all gift video_call text_chat kol_service
        page: String,
        pageSize: String,
        failed: OnFailed
    ): CommonPageBean<KolUnionListBean>? {
        val params = newParamMap().apply {
            put("search", search)
            //put("kol_id", (HiRealCache.user?.kolId ?: 0L).toString())
            put("page", page)
            put("page_size", pageSize)
        }

        val ret = try {
            service.unionList(params)
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
            HiLog.l(Tag2Common.TAG_12308, "e.printStackTrace() = ${e.message}")
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

    suspend fun getMyUnionInfo( failed: OnFailed): KolUnionInfoBean? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getMyUnionInfo(params)
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

    suspend fun createService(service_name: String, gold_price: String, failed: OnFailed): String? {
        val params = newParamMap().apply {
            put("service_name", service_name)
            put("gold_price", gold_price)
        }

        val ret = try {
            service.createService(params)
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
    suspend fun joinUnion(id: String, failed: OnFailed): KolUnionListBean? {
        val params = newParamMap().apply {
            put("union_id", id)
        }

        val ret = try {
            service.joinUnion(params)
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
    suspend fun exitUnion(id: String, failed: OnFailed): KolUnionListBean? {
        val params = newParamMap().apply {
            put("union_id", id)
        }

        val ret = try {
            service.exitUnion(params)
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

    suspend fun loginOut(failed: OnFailed): String? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.loginOut(params)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.removeUserInfo()

        return ret.data
    }

    suspend fun deleteService(id: String, failed: OnFailed): String? {
        val params = newParamMap().apply {
            put("id", id)
        }

        val ret = try {
            service.deleteService(params)
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

    suspend fun editService(
        id: String,
        service_name: String,
        gold_price: String,
        failed: OnFailed
    ): String? {
        val params = newParamMap().apply {
            put("id", id)
            put("service_name", service_name)
            put("gold_price", gold_price)
        }

        val ret = try {
            service.editService(params)
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

    suspend fun getMeServiceList(failed: OnFailed): List<KolServiceBean>? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getMeServiceList(params)
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

    suspend fun getCountryList(failed: OnFailed): List<CountryBean>? {
        val params = newParamMap().apply {
        }

        val ret = try {
            service.getCountryList(params)
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

    suspend fun initUserInfo(
        kol_name: String,
        birthday: String,
        country: String,
        language: String,
        des: String,
        images: MutableList<String>,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.initUserInfo(kol_name, birthday, country, language, des, images)
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

    suspend fun updateUserLang(
        language: String,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserLang(language)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.updateUserLang(ret.data)

        return ret.data
    }

    suspend fun updateUserOpenService(
        language: Int,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserOpenService(language)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.updateUserOpenService(ret.data)

        return ret.data
    }

    suspend fun updateUserPrice(
        language: String,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserPrice(language)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        LoginManager.updateUserPrice(ret.data)

        return ret.data
    }

    suspend fun updateUserInfo(
        kol_name: String,
        email: String,
        mobile: String,
        avatar: String,
        birthday: String,
        info: String,
        country_code: String,
        online_status: String,
        language: String,
        images: MutableList<String>,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserInfo(
                kol_name,
                email,
                mobile,
                avatar,
                birthday,
                info,
                country_code,
                online_status,
                language,
                images,
            )
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

    suspend fun updateUserInfoV2(
        kol_name: String,
        avatar: String,
        country: String,
        birthday: String,
        des: String,
        images: MutableList<String>,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserInfoV2(kol_name, avatar, country, birthday, des, images)
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

    suspend fun updateUserInfoV1(
        kol_name: String,
        country: String,
        birthday: String,
        des: String,
        images: MutableList<String>,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserInfoV1(kol_name, country, birthday, des, images)
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

    suspend fun updateUserInfoV3(
        kol_name: String,
        country: String,
        birthday: String,
        des: String,
        failed: OnFailed
    ): UserBean? {

        val ret = try {
            service.updateUserInfoV3(kol_name, country, birthday, des)
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

    suspend fun publishVideo(
        images: MutableList<String>,
        failed: OnFailed
    ): String? {

        val ret = try {
            service.publishVideo(images)
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

    suspend fun publishVideoV2(
        images: MutableList<VideoDataBean>,
        failed: OnFailed
    ): Any? {

        val ret = try {
            val videoRequest = MineApiService.VideoRequest(videos = images)
            service.publishVideoV2(videoRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            failed(RS_DATA_ERROR, e.message ?: failServer)
            return null
        }

        if (checkCodeFailed(ret, failed)) {
            return null
        }

        return "ok"
    }

    suspend fun publishPhoto(
        images: MutableList<String>,
        failed: OnFailed
    ): String? {

        val ret = try {
            service.publishPhoto(images)
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

    suspend fun postDel(
        images: MutableList<Long>,
        failed: OnFailed
    ): String? {

        val ret = try {
            service.postDel(images)
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

    suspend fun postToggleTop(
        top: Int,
        images: MutableList<Long>,
        failed: OnFailed
    ): String? {

        val ret = try {
            service.postToggleTop(top, images)
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


    @JsonClass(generateAdapter = true)
    data class KolUserBean(
        val kol_name: String? = "",
        val email: String? = "",
        val mobile: String? = "",
        val avatar: String? = "",
        val birthday: String? = "",
        val images: String? = "",
        val info: String? = "",
        val country_code: String? = "",
        val online_status: String? = "",
        val language: String? = "",
    )

    data class UploadResult(
        val originalFile: File,  // 原始文件
        val url: String?,        // 上传后的URL
        val success: Boolean    // 是否成功
    )

    suspend fun uploadFilesAndUpdateUser(
        files: List<String>,
        avatarPath: String,
        userInfo: KolUserBean,
        failed: OnFailed,
        onProgress: (current: Int, total: Int) -> Unit
    ): List<UploadResult> = coroutineScope {
        val results = mutableListOf<UploadResult>()
        val totalFiles = files.size

        // 使用通道控制并发数
        val channel = Channel<Deferred<UploadResult>>(3) // 最多3个并发上传

        // 生产者协程 - 发送上传任务
        launch {
            files.forEachIndexed { index, fileStr ->
                val deferred = async {
                    try {

                        val file = File(fileStr)
                        val fileBody = file.asRequestBody("multipart/form-data".toMediaType())
                        val formData =
                            MultipartBody.Part.createFormData("file", file.name, fileBody)

                        val fileUpload = fileUpload(fileBody, formData, failed)
                        UploadResult(file, fileUpload?.path ?: "", fileUpload != null)
                    } catch (e: Exception) {
                        val file = File(fileStr)
                        UploadResult(file, null, false)
                    } finally {
                        // 更新进度
                        onProgress(index + 1, totalFiles)
                    }
                }
                channel.send(deferred)
            }
            channel.close()
        }

        // 消费者协程 - 按顺序接收结果
        launch {
            for (deferred in channel) {
                results.add(deferred.await())
            }
        }.join() // 等待所有任务完成

        // 后续处理与之前相同...
        results


    }
}





