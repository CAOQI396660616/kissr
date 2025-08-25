package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.config.LanguageInfo
import com.dubu.common.constant.Tag2Common
import com.dubu.common.manager.SettingsManager
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.ToastTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.LanguageListAdapter
import com.dubu.me.databinding.ActivityMeSetLanguageBinding
import com.dubu.me.vm.MineViewModel
import com.hikennyc.view.MultiStateAiView


/**
 * 重要设置 : 擅长语言 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MySetLanguageActivity]
 */
class MySetLanguageActivity : BaseBindingActivity<ActivityMeSetLanguageBinding>() {

    private val model: MineViewModel by viewModels()

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MySetLanguageActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val mAdapter: LanguageListAdapter by lazy {
        LanguageListAdapter()
    }

    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_set_language
    }

    override fun onCreated() {
        httpEngine()
        initView()
        initClick()
    }


    private fun initView() {

        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        bindingApply {
            with(recyclerView) {
                layoutManager =
                    GridLayoutManager(this@MySetLanguageActivity, 2, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            mAdapter.changeUi(position)
        }


    }

    private var userInputLanguage: String = "" //用户输入的文本 : 语言
    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }
        ClickUtils.applySingleDebouncing(binding.tvNext) {
            if ((mAdapter.checkUserChooseList().isNotEmpty())) {
                showLoadingDialog()
                userInputLanguage = mAdapter.checkUserChooseListToStr()

                model.updateUserLang(
                    language = userInputLanguage,
                    success = { _ ->
                        HiLog.e(
                            Tag2Common.TAG_12303,
                            "更新用户信息 成功 }"
                        )

                        dismissLoadingDialog()
                        ToastTool.toastOk(R.string.update_ok)
                        //todo allen:server (对接后台数据)
                        //allen 目前这里暂时做 注册过后的语言保存
                        SettingsManager.saveLanguageToMMKV("lang")
                    }) { code, msg ->
                    ToastTool.toastError(R.string.toast_err_service)
                    dismissLoadingDialog()
                }

            } else {
                ToastTool.toastError(getString(R.string.toast_err_must_complete_cur_step))
            }
        }

    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    private fun httpEngine() {
        getData()

    }

    private var languageList: MutableList<LanguageInfo>? = null
    private fun getData() {


        if (HiRealCache.languageList.isNullOrEmpty()) {
            model.getJsonList(success = { cList ->
                languageList = HiRealCache.languageList
                showSuccessEngine()
            }, failed = { code, msg ->
                showErrorEngine()
            })

        } else {
            languageList = HiRealCache.languageList
            showSuccessEngine()
        }

    }

    private fun showErrorEngine() {
        binding.multiStateView.viewState = MultiStateAiView.ViewState.ERROR
        binding.multiStateView.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        binding.multiStateView.viewState = MultiStateAiView.ViewState.CONTENT
        mAdapter.setList(languageList)
    }

}
