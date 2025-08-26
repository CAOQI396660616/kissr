package com.dubu.common.http

import androidx.collection.ArrayMap
import com.dubu.common.R
import com.dubu.common.beans.base.BaseResponseBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.utils.AppFactoryTool
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.ToastTool
import kotlin.reflect.KClass

/**
 * Author:v
 * Time:2022/5/13
 */
abstract class BaseClient<T : Any>(clz: KClass<T>) {
    private val TAG = "BaseClient"
    protected val service by lazy {
        AppFactoryTool.instance.provideRetrofit().create(clz.java)
    }

    fun newParamMap() = ArrayMap<String, Any>()


    protected fun checkCodeFailed(
        res: BaseResponseBean<*>,
        onError: OnFailed,
        needShowToast: Boolean? = true
    ): Boolean {
        val code = res.code
        HiLog.e(Tag2Common.TAG_12300, "checkCodeFailed = $res")
        if (code != NetConfig.RS_SUCCESS) {
            //只要不是成功200 就是错误 回调错误
            val s = res.message ?: failServer
            onError(code, s)
            if (needShowToast == true)
                ToastTool.toastError(s)

            //然后针对不同的码做不同的处理

            //Token 失效
            if (code == NetConfig.RS_TOKEN_INVALID) {
                EventManager.post(EventKey.TOKEN_INVALIDED, true)
            }
            HiLog.e(Tag2Common.TAG_12300, "接口通用校验异常: checkCodeFailed  = ${res.code} : ${res.message} ")
            return true
        }

        HiLog.e(Tag2Common.TAG_12300, "接口通用校验正常: checkCodeFailed  = ${res.code} ")
        return false
    }


    protected val failEmpty by lazy {
        com.dubu.common.base.BaseApp.instance.getString(R.string.toast_err_empty_data)
    }

    protected val failServer by lazy {
        com.dubu.common.base.BaseApp.instance.getString(R.string.toast_err_service)
    }
}
