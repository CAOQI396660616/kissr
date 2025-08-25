package com.dubu.me.vm

import com.dubu.common.base.BaseViewModel
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.beans.me.*
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.OnFailed
import com.dubu.common.utils.HiLog
import com.dubu.main.api.MineClient
import com.dubu.rtc.api.CommonPageBean
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 *  @author  Even
 *  @date   2021/11/12
 */
class MineViewModel : BaseViewModel() {

    private val client by lazy {
        MineClient()
    }

    fun homeShortsList(
        search: String,
        page: String,
        pageSize: String,
        success: (user: CommonPageBean<KolPostBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.homeCallList(search, page, pageSize, failed)

            if (u != null) {
                success(u)
            }
        }
    }
    fun incomeList(
        search: String,
        page: String,
        pageSize: String,
        success: (user: CommonPageBean<KolIncomeListBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.incomeList(search, page, pageSize, failed)

            if (u != null) {
                success(u)
            }
        }
    }
    fun unionHistoryList(
        search: String,
        page: String,
        pageSize: String,
        success: (user: CommonPageBean<KolUnionHistoryListBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.unionHistoryList(search, page, pageSize, failed)

            if (u != null) {
                success(u)
            }
        }
    }
    fun unionList(
        search: String,
        page: String,
        pageSize: String,
        success: (user: CommonPageBean<KolUnionListBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.unionList(search, page, pageSize, failed)

            if (u != null) {
                success(u)
            }
        }
    }

    /**
     * 上传图片
     */
    fun fileUpload(
        path: RequestBody,
        file: MultipartBody.Part,
        failed: OnFailed,
        success: (data: UploadResultBean) -> Unit
    ) {
        launch {
            client.fileUpload(path, file, failed)?.run(success)
        }
    }


    fun getUserInfo(id: String, success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getUserInfo(id, failed)
            if (u != null) {
                success(u)
            }
        }
    }


    fun getMyUnionInfo(success: (data: KolUnionInfoBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getMyUnionInfo(failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun createService(
        service_name: String,
        gold_price: String,
        success: (data: String) -> Unit,
        failed: OnFailed
    ) {
        launch {
            val u = client.createService(service_name, gold_price, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun joinUnion(
        id: String,
        success: (data: KolUnionListBean) -> Unit,
        failed: OnFailed
    ) {
        launch {
            val u = client.joinUnion(id, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun exitUnion(
        id: String,
        success: (data: KolUnionListBean) -> Unit,
        failed: OnFailed
    ) {
        launch {
            val u = client.exitUnion(id, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun loginOut(
        success: (data: String) -> Unit,
        failed: OnFailed
    ) {
        launch {
            val u = client.loginOut( failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun deleteService(id: String, success: (data: String) -> Unit, failed: OnFailed) {
        launch {
            val u = client.deleteService(id, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun editService(
        id: String,
        service_name: String,
        gold_price: String,
        success: (data: String) -> Unit,
        failed: OnFailed
    ) {
        launch {
            val u = client.editService(id, service_name, gold_price, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun getMeServiceList(success: (data: List<KolServiceBean>) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getMeServiceList(failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun getMeUserInfo(success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getMeUserInfo(failed)
            if (u != null) {
                success(u)
            }
        }
    }
    fun getCountryList(success: (user: List<CountryBean>) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getCountryList(failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun getJsonList(success: (data: JsonList) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getJsonList(failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun getKolIncomeInfo(success: (data: KolIncomeBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getKolIncomeInfo(failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun initUserInfo(
        kol_name: String,
        birthday: String,
        country: String,
        language: String,
        des: String,
        images: MutableList<String>,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.initUserInfo(
                kol_name,
                birthday,
                country,
                language,
                des, images, failed
            )
            if (u != null) {
                success(u)
            }
        }
    }
    fun updateUserLang(
        language: String,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.updateUserLang(
                language, failed
            )
            if (u != null) {
                success(u)
            }
        }
    }
    fun updateUserOpenService(
        language: Int,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.updateUserOpenService(
                language, failed
            )
            if (u != null) {
                success(u)
            }
        }
    }
    fun updateUserPrice(
        language: String,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.updateUserPrice(
                language, failed
            )
            if (u != null) {
                success(u)
            }
        }
    }


    //    kol_name
    //    email
    //    mobile
    //    avatar
    //    birthday
    //    info
    //    country_code
    //    online_status
    //    language
    //    images


    fun updateUserInfo(
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
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.updateUserInfo(
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
                failed
            )
            if (u != null) {
                success(u)
            }
        }
    }

    fun updateUserInfoV2(
        kol_name: String,
        avatar: String,
        country: String,
        birthday: String,
        des: String,
        images: MutableList<String>,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u =
                client.updateUserInfoV2(kol_name, avatar, country, birthday, des, images, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun updateUserInfoV1(
        kol_name: String,
        country: String,
        birthday: String,
        des: String,
        images: MutableList<String>,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u =
                client.updateUserInfoV1(kol_name, country, birthday, des, images, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun updateUserInfoV3(
        kol_name: String,
        country: String,
        birthday: String,
        des: String,
        success: (user: UserBean) -> Unit,
        failed: OnFailed
    ) {
        launch {
            val u =
                client.updateUserInfoV3(kol_name, country, birthday, des, failed)
            if (u != null) {
                success(u)
            }
        }
    }


    fun publishVideo(
        images: MutableList<String>,
        success: (user: String) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.publishVideo(images, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun publishVideoV2(
        images: MutableList<VideoDataBean>,
        success: (user: Any) -> Unit, failed: OnFailed
    ) {
        launch {


            val u = client.publishVideoV2(images, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun publishPhoto(
        images: MutableList<String>,
        success: (user: String) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.publishPhoto(images, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun postDel(
        images: MutableList<Long>,
        success: (user: String) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.postDel(images, failed)
            if (u != null) {
                success(u)
            }
        }
    }
    fun postToggleTop(
        top: Int,
        images: MutableList<Long>,
        success: (user: String) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.postToggleTop(top,images, failed)
            if (u != null) {
                success(u)
            }
        }
    }


    suspend fun uploadFilesAndUpdateUser(
        files: List<String>,
        imageNotBgList: List<String>,
        avatarPath: String,
        userInfo: MineClient.KolUserBean,
        failedForUploadImage: OnFailed,
        failedForUpdateUserInfo: OnFailed,
        success: (user: String) -> Unit,
        onProgress: (current: Int, total: Int) -> Unit
    ): List<MineClient.UploadResult> = coroutineScope {
        val results = mutableListOf<MineClient.UploadResult>()
        val totalFilesUploadList = mutableListOf<String>()

        if (avatarPath.isNotEmpty())
            totalFilesUploadList.add(avatarPath)

        totalFilesUploadList.addAll(files)

        val totalFiles = totalFilesUploadList.size

        // 使用通道控制并发数
        val channel = Channel<Deferred<MineClient.UploadResult>>(7) // 最多3个并发上传

        // 生产者协程 - 发送上传任务
        launch {
            totalFilesUploadList.forEachIndexed { index, fileStr ->
                val deferred = async {
                    try {

                        val file = File(fileStr)
                        val fileBody = file.asRequestBody("multipart/form-data".toMediaType())
                        val formData =
                            MultipartBody.Part.createFormData("file", file.name, fileBody)
                        HiLog.e(Tag2Common.TAG_12309, "上传前 $index = $fileStr")
                        val fileUpload = client.fileUpload(fileBody, formData, failedForUploadImage)
                        MineClient.UploadResult(file, fileUpload?.path ?: "", fileUpload != null)
                    } catch (e: Exception) {
                        val file = File(fileStr)
                        MineClient.UploadResult(file, null, false)
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


        // 检查是否全部成功
        val allSuccess = results.all { it.success }

        if (allSuccess) {

            HiLog.e(
                Tag2Common.TAG_12309,
                " 开始更新的时候 avatarPath = $avatarPath"
            )

            // 更新用户信息
            val imageListNeedUpload = mutableListOf<String>()
            var avatarPathUpload = ""
            results.forEachIndexed { index, uploadResult ->
                HiLog.e(
                    Tag2Common.TAG_12309,
                    " 上传后 $index = ${uploadResult.originalFile.absolutePath} , ${uploadResult.url}"
                )
                //
                if (avatarPath != null && uploadResult.originalFile.absolutePath == avatarPath) {
                    uploadResult.url?.let { avatarPathUpload = (it) }
                } else {
                    uploadResult.url?.let {
                        imageListNeedUpload.add(it)

                        HiLog.e(
                            Tag2Common.TAG_12309,
                            " 上传后 $index =  imageListNeedUpload.add(it) =  $it"
                        )

                    }
                }
            }

            imageListNeedUpload.addAll(imageNotBgList)

            HiLog.e(
                Tag2Common.TAG_12309,
                " imageListNeedUpload imageListNeedUpload imageListNeedUpload =  $imageListNeedUpload"
            )
            HiLog.e(
                Tag2Common.TAG_12309,
                " imageListNeedUpload imageListNeedUpload imageListNeedUpload  imageNotBgList =  $imageNotBgList"
            )


            val updateUserInfo = client.updateUserInfo(
                kol_name = userInfo.kol_name ?: "",
                email = userInfo.email ?: "",
                mobile = userInfo.mobile ?: "",
                avatar = avatarPathUpload ?: "",
                birthday = userInfo.birthday ?: "",
                info = userInfo.info ?: "",
                country_code = userInfo.country_code ?: "",
                online_status = userInfo.online_status ?: "",
                language = userInfo.language ?: "",
                images = imageListNeedUpload,
                failedForUpdateUserInfo
            )

            if (updateUserInfo != null) {
                success("sos")

                //这里更新完成了以后 还是给 本地的数据做一个更新吧
//                val userBean = UserBean()
//
//                if (userInfo.kol_name?.isNotEmpty() == true){
//                    userBean.nickname = userInfo.kol_name
//                    userBean.name = userInfo.kol_name
//                }
//                if (userInfo.kol_name?.isNotEmpty() == true){
//                    userBean.avatar = userInfo.kol_name
//                    userBean.name = userInfo.kol_name
//                }


            }

        }


        // 后续处理与之前相同...
        results

    }


}