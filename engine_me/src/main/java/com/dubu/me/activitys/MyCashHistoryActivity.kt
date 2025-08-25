package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.CashHistoryListAdapter
import com.dubu.me.databinding.ActivityMeCashHistoryBinding
import com.dubu.test.testdata.TestData


/**
 * 提现历史 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyCashHistoryActivity]
 */
class MyCashHistoryActivity : BaseBindingActivity<ActivityMeCashHistoryBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyCashHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_cash_history
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: CashHistoryListAdapter by lazy {
        CashHistoryListAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        bindingApply {
            with(recyclerView) {
                layoutManager =
                    LinearLayoutManager(this@MyCashHistoryActivity)
                adapter = mAdapter
            }
        }
        mAdapter.setList(TestData.getStringList())
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
