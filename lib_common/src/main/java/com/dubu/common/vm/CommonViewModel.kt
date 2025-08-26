package com.dubu.me.vm

import com.dubu.common.api.CommonClient
import com.dubu.common.base.BaseViewModel
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.common.ShareBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.beans.me.UploadResultBean
import com.dubu.common.http.OnFailed
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 *  @author  Even
 *  @date   2021/11/12
 */
class CommonViewModel : BaseViewModel() {

    private val client by lazy {
        CommonClient()
    }


    fun getMeUserInfo(success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getMeUserInfo(failed)
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

    fun getShareInfo(success: (data: ShareBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getShareInfo(failed)
            if (u != null) {
                success(u)
            }
        }
    }


    fun updateUserInfo(
        kol_name: String,
        email: String,
        mobile: String,
        avatar: String,
        gender: String,
        birthday: String,
        images: MutableList<String>,
        success: (user: UserBean) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.updateUserInfo(
                kol_name,
                email,
                mobile,
                avatar,
                gender,
                birthday,
                images,
                failed
            )
            if (u != null) {
                success(u)
            }
        }
    }

    fun getUserList(
        ids: MutableList<String>,
        success: (data: List<UserBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.getUserList(ids, failed)
            if (u != null) {
                success(u)
            }
        }
    }


}