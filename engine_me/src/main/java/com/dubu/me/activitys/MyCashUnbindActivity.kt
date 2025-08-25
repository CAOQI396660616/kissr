package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.PhotoMultiItemAdapter
import com.dubu.me.databinding.ActivityMeCashUnbindBinding


/**
 * 提现: 解绑 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyCashUnbindActivity]
 */
class MyCashUnbindActivity : BaseBindingActivity<ActivityMeCashUnbindBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyCashUnbindActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_cash_unbind
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


    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
