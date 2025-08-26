package com.dubu.common.web

import android.webkit.JavascriptInterface
import android.widget.Toast
import com.dubu.common.constant.Tag2Common
import com.dubu.common.utils.HiLog

class AppToH5Interface(private val mContext: WebViewUnionActivity) {
    companion object {
        private const val ROUTE_MAIN_ME = "kisschat://main/me"

    }

    @JavascriptInterface
    fun showToast(msg: String?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
    }

    // --------------------------------------------------------> JS调用方法
    @JavascriptInterface
    fun route(appScheme: String) {
        if (appScheme.isNullOrEmpty())
            return
        HiLog.e(Tag2Common.TAG_12300, "JS route  -> $appScheme")
        //JS直接去回调安卓方法
        handleCallNative(appScheme)
    }
    // --------------------------------------------------------> JS调用方法 根据方法名 不同处理
    /**
     * 处理各种JS回调原生方法的业务逻辑
     * @param appScheme 路由参数
     */
    private fun handleCallNative(appScheme: String) {
        when (appScheme) {
            ROUTE_MAIN_ME -> {
                //关闭页面 返回到 主页的me
                mContext.finish()
            }
        }
    }
}