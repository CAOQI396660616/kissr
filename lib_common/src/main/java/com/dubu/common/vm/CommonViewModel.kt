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

    fun guestLogin(success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val ret = client.guestLogin(failed)
            ret?.let {
                success(it)
            }
        }
    }


    fun setUserGender(
        sex: String,
        success: () -> Unit,
        failed: OnFailed
    ) {
        launch {
            val result = client.setUserGender(sex, failed)
            if (result) {
                success()
            }
        }
    }


}