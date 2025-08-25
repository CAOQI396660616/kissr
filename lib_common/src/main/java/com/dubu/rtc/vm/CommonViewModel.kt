package com.dubu.me.vm

import com.dubu.common.base.BaseViewModel
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.common.ReportBean
import com.dubu.common.beans.common.HttpSysMsgBean
import com.dubu.common.beans.common.ShareBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.beans.me.UploadResultBean
import com.dubu.common.beans.rtc.VideoRtcResultBean
import com.dubu.common.http.OnFailed
import com.dubu.rtc.api.CommonClient
import com.dubu.rtc.api.CommonPageBean
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

    fun roomDestroy(
        hangup_by: String,
        room_state_id: String, success: (data: Any) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.roomDestroy( hangup_by, room_state_id, failed)
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

    fun getMeUserInfoV2(success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getMeUserInfoV2(failed)
            if (u != null) {
                success(u)
            }
        }
    }
    fun report(
        reason: String,
        content: String,
        kol_id: String,
        success: (user: ReportBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.report(reason,content,kol_id,failed)
            if (u != null) {
                success(u)
            }
        }
    }
    fun reportUpdate(
        id: String,
        audit_status: String,
        success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.reportUpdate(id,audit_status,failed)
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

    fun fansList(
        page: String,
        pageSize: String,
        success: (user: CommonPageBean<UserBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.fansList( page, pageSize, failed)

            if (u != null) {
                success(u)
            }
        }
    }

    //今天:today 昨天yesterday 全部:不传或者ALL
    fun videoRtcHistory(
        date_type: String,
        page: String,
        pageSize: String,
        success: (user: CommonPageBean<VideoRtcResultBean>) -> Unit, failed: OnFailed
    ) {
        launch {
            val u = client.videoRtcHistory( date_type,page, pageSize, failed)

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
    fun getSysMsg(page: String,
                  pageSize: String, success: (data: CommonPageBean<HttpSysMsgBean>) -> Unit, failed: OnFailed) {
        launch {
            val u = client.getSysMsg(page,pageSize,failed)
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
            val u = client.updateUserInfo(kol_name,email,mobile,avatar,gender,birthday,images,failed)
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
            val u = client.getUserList(ids,failed)
            if (u != null) {
                success(u)
            }
        }
    }


}