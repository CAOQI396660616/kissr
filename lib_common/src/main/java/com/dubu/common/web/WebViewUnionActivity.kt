package com.dubu.common.web

import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.collection.ArrayMap
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.snackbar.Snackbar
import com.dubu.common.R
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.databinding.ActivityWebviewBinding
import com.dubu.common.databinding.ActivityWebviewUnionBinding
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.hi.HiStatusBarTool
import com.hikennyc.view.MultiStateAiView


@Route(path = RouteConst.ACTIVITY_UNION_WEB)
class WebViewUnionActivity : BaseBindingActivity<ActivityWebviewUnionBinding>() {

    override fun getContentLayoutId(): Int {
        return R.layout.activity_webview_union
    }

    override fun isNeedDefaultScreenConfig() = false

    override fun onCreated() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        initView()
        initSetting()
        initClient()
        loadUrl()
    }

    private fun initView() {
        val title = intent.getStringExtra(RouteConst.P_TITLE) ?: getString(R.string.app_name)
        binding.tvTitle.text = title
        binding.ivBack.setOnClickListener {
            onBack()
        }
    }

    private fun loadUrl() {
        val url = intent.getStringExtra(RouteConst.P_URL)
        if (url.isNullOrEmpty()) {
            Snackbar.make(binding.webView, "无效的url", Snackbar.LENGTH_SHORT).show()
            return
        }
        val headers = ArrayMap<String, String>().apply {
//            put("Accept-Language", VcCache.language)
        }
        binding.webView.loadUrl(url, headers)

    }

    private var showContent = false
    private fun initClient() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }


           /* override fun onReceivedSslError(
                view: WebView?,
                handler: android.webkit.SslErrorHandler?,
                error: android.net.http.SslError?
            ) {
                // 🚨 忽略证书错误，继续加载（仅限测试环境）
                handler?.proceed()
            }*/
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress >= 100) {
                    if (!showContent){
                        showContent = true
                        binding.webView.postDelayed({ showSuccessEngine() }, 100)
                    }
                }

                HiLog.l(Tag2Common.TAG_12302, "newProgress 成功 url : $newProgress")
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    private fun initSetting() {
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        binding.webView.apply {

            settings.also { setting ->
                setting.javaScriptEnabled = true
                setting.javaScriptCanOpenWindowsAutomatically = true
                setting.useWideViewPort = true
                setting.setSupportZoom(true)
                setting.builtInZoomControls = true
                setting.displayZoomControls = false
                setting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                setting.loadWithOverviewMode = true
                setting.domStorageEnabled = true
                setting.cacheMode = WebSettings.LOAD_NO_CACHE
                setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            addJavascriptInterface(
                AppToH5Interface(
                    this@WebViewUnionActivity
                ), "JSCallNative"
            )

        }
    }


    private fun httpEngine() {

    }

    private fun showErrorEngine() {
        binding?.multiStateView?.viewState = MultiStateAiView.ViewState.ERROR
        binding?.multiStateView?.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        binding?.multiStateView?.viewState = MultiStateAiView.ViewState.CONTENT
    }
}