package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.me.HelpMultiTypeBean
import com.dubu.common.constant.Constants
import com.dubu.common.router.Router
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.HelpMultiItemAdapter
import com.dubu.me.databinding.ActivityMeHelpCenterBinding
import com.dubu.test.testdata.TestData


/**
 * 帮助中心 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyHelpCenterActivity]
 */
class MyHelpCenterActivity : BaseBindingActivity<ActivityMeHelpCenterBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyHelpCenterActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_help_center
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: HelpMultiItemAdapter by lazy {
        HelpMultiItemAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        bindingApply {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = mAdapter
            }
        }
        mAdapter.setList(TestData.getHelpList())

        mAdapter.setOnItemClickListener { adapter, view, position ->

            val helpMultiTypeBean = mAdapter.data[position]

            if (helpMultiTypeBean.type == HelpMultiTypeBean.ITEM_TYPE_ALL) {
                binding.recyclerView.scrollToPosition(mAdapter.itemCount - 1)
            }

        }
    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        //客服中心
        ClickUtils.applySingleDebouncing(binding.tvEnd) {

            //BIMManager.instance.createSingleConversationAndEnter(Constants.RTC_UID_FOR_KE_FU.toLong())

            Router.toBimChatActivity("0:1:${HiRealCache.user?.userSn.toString()}:${Constants.RTC_UID_FOR_KE_FU}", 1)
        }
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
