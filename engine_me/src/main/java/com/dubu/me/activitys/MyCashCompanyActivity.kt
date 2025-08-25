package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.utils.AdvancedClickUtils
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.CashCompanyListAdapter
import com.dubu.me.databinding.ActivityMeCashCompanyBinding
import com.dubu.rtc.dialogs.CashUnbindDialog
import com.dubu.test.testdata.TestData


/**
 * 提现公司绑定 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyCashCompanyActivity]
 */
class MyCashCompanyActivity : BaseBindingActivity<ActivityMeCashCompanyBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyCashCompanyActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_cash_company
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: CashCompanyListAdapter by lazy {
        CashCompanyListAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        bindingApply {
            with(recyclerView) {
                layoutManager =
                    LinearLayoutManager(this@MyCashCompanyActivity)
                adapter = mAdapter
            }
        }
        mAdapter.setList(TestData.getStringList())

        mAdapter.setOnItemClickListener { adapter, view, position ->

            if (!AdvancedClickUtils.canClick(view))
                return@setOnItemClickListener

            MyCashBindActivity.startAction(this@MyCashCompanyActivity)



        }
    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }
        ClickUtils.applySingleDebouncing(binding.tvTitle) {
            CashUnbindDialog().withTypeChooseListener {

                MyCashUnbindActivity.startAction(this@MyCashCompanyActivity)

            }.show(supportFragmentManager)
        }


    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
