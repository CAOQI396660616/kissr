package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.PhotoMultiItemAdapter
import com.dubu.me.databinding.ActivityMeImportantSettingBinding


/**
 * 重要设置页面
 *
 * @author cq
 * @date 2025/06/05
 * @constructor 创建[MyImportantSettingActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_IMPORTANT_SETTING)
class MyImportantSettingActivity : BaseBindingActivity<ActivityMeImportantSettingBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyImportantSettingActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_important_setting
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: PhotoMultiItemAdapter by lazy {
        PhotoMultiItemAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)


    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.clRoot1) {
           Router.toRtcEffectActivity("",0)
        }

        ClickUtils.applySingleDebouncing(binding.clRoot2) {
            MySetPriceActivity.startAction(this@MyImportantSettingActivity)
        }

        ClickUtils.applySingleDebouncing(binding.clRoot3) {
            MySetNoticeActivity.startAction(this@MyImportantSettingActivity)
        }

        ClickUtils.applySingleDebouncing(binding.clRoot4) {
            MySetLanguageActivity.startAction(this@MyImportantSettingActivity)
        }

        ClickUtils.applySingleDebouncing(binding.clRoot5) {
            MySetServiceActivity.startAction(this@MyImportantSettingActivity)
        }


    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
