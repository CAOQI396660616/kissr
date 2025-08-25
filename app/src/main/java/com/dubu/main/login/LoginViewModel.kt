package com.dubu.main.login

import com.dubu.common.base.BaseViewModel
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.config.JsonList
import com.dubu.common.http.OnFailed
import com.dubu.main.api.LoginClient

/**
 *  @author  Even
 *  @date   2021/11/12
 */
class LoginViewModel : BaseViewModel() {

    private val client by lazy {
        LoginClient()
    }




    fun visitorLogin(uuid: String, success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.visitorLogin(uuid, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun emailSendCode(email: String, success: (user: Any) -> Unit, failed: OnFailed) {
        launch {
            val u = client.emailSendCode(email, failed)
            if (u != null) {
                success(u)
            }
        }
    }

    fun emailCodeLogin(email: String, code: String, success: (user: UserBean) -> Unit, failed: OnFailed) {
        launch {
            val u = client.emailCodeLogin(email, code,failed)
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

}