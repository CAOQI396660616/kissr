package com.dubu.common.web

import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.collection.ArrayMap
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.snackbar.Snackbar
import com.dubu.common.R
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.databinding.ActivityWebviewBinding
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.hi.HiStatusBarTool


@Route(path = RouteConst.ACTIVITY_SIMPLE_WEB)
class WebViewActivity : BaseBindingActivity<ActivityWebviewBinding>() {

    override fun getContentLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun isNeedDefaultScreenConfig() = false

    override fun onCreated() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.white, true)

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

    private fun initClient() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBar.let {
                    if (newProgress >= 100) {
                        it.visibility = View.GONE
                    } else {
                        if (it.visibility == View.GONE) {
                            it.visibility = View.VISIBLE
                        }
                        it.progress = newProgress
                    }
                }

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

        }
    }

}