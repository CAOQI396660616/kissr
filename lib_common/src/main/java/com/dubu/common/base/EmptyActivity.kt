package com.dubu.common.base

import android.app.Activity
import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.R
import com.dubu.common.databinding.ActivityEmptyBinding
import com.dubu.common.router.RouteConst


/**
 *  @author
 *  @date
 */
//@Route(path = RouteConst.ACTIVITY_SHORT_DETAILS_PLAY)
class EmptyActivity : BaseBindingActivity<ActivityEmptyBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, EmptyActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_empty
    }

    override fun onCreated() {
        findViewById<View>(R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = false, isKeyboardEnable = false)
        }

        initView()
        initClick()
    }

    private fun initView() {
    }

    override fun onStart() {
        super.onStart()
    }


    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }


        bindingApply {


        }


    }

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
