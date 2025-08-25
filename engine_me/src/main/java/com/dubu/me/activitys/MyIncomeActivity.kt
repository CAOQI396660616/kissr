package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.util.ArrayMap
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.base.BaseFragment
import com.dubu.common.beans.me.KolIncomeBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.TipDialogV2
import com.dubu.common.ext.isValid
import com.dubu.common.ext.toJson
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.TabLayoutCustomTool
import com.dubu.common.utils.TextViewUtils
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.databinding.ActivityMeIncomeBinding
import com.dubu.me.fragment.MeIncomeFragment
import com.dubu.me.vm.MineViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hikennyc.view.MultiStateAiView


/**
 * 我的收入页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyIncomeActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_INCOME)
class MyIncomeActivity : BaseBindingActivity<ActivityMeIncomeBinding>() {
    private val model: MineViewModel by viewModels()

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyIncomeActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_income
    }

    override fun onCreated() {
        initView()
        initClick()

    }

    override fun onResume() {
        super.onResume()

        httpEngine()
    }

    private fun changeUI(data: KolIncomeBean) {

//        data.today_income = 1.23f
//        data.total_income = 2.23f
//        data.withdrawable_amount = 3.23f
//        data.pending_amount = 4.23f


        /*val todayIncome = data.today_income.toString()
        binding.tv2.text = "\$$todayIncome"

        val totalIncome = data.total_income.toString()
        binding.tv11.text = "\$$totalIncome"
        val withdrawableAmount = data.withdrawable_amount.toString()
        binding.tv12.text = "\$$withdrawableAmount"
        val pendingAmount = data.pending_amount.toString()
        binding.tv13.text = "\$$pendingAmount"*/

        binding.tv2.text = data.today_income.toString()
        binding.tv11.text = data.total_income.toString()
        binding.tv12.text = data.withdrawable_amount.toString()
        binding.tv13.text = data.pending_amount.toString()
    }


    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        bindingApply {
            TextViewUtils.setMidBold(tv1)
            TextViewUtils.setMidBold(tvH0)
            TextViewUtils.setMidBold(tvH1)
            TextViewUtils.setMidBold(tvH2)
        }

        val arrayOf = mutableListOf<String>(
            BaseApp.instance.getString(R.string.income_tab_details),
            BaseApp.instance.getString(R.string.income_tab_call_income),
            BaseApp.instance.getString(R.string.income_tab_chat_income),
            BaseApp.instance.getString(R.string.income_tab_gift_income),
            BaseApp.instance.getString(R.string.income_tab_service_income),
        )
        titles = arrayOf as ArrayList<String>
        initVp(binding.vpHome, binding.tlHome)

    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.tvH1) {
            TipDialogV2()
                .withTitle(R.string.waiting_received_tip)
                .withContent(R.string.waiting_received_content)
                .show(supportFragmentManager, "TipDialogV2")
        }

        ClickUtils.applySingleDebouncing(binding.tvH2) {
            TipDialogV2()
                .withTitle(R.string.can_cash_tip)
                .withContent(R.string.can_cash_content)
                .show(supportFragmentManager, "TipDialogV2")
        }

        ClickUtils.applySingleDebouncing(binding.tvGoto) {
            MyCashActivity.startAction(this@MyIncomeActivity)
        }


    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var titles: ArrayList<String>? = null


    private var lastIndex = 0
    private var fragments = ArrayMap<Int, BaseFragment>(2)

    private fun initVp(vpHome: ViewPager2, tlHome: TabLayout) {
        lastIndex = 0
        val fragmentStateAdapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return titles!!.size
                }

                @Suppress("KotlinConstantConditions")
                override fun createFragment(position: Int): Fragment {

                    return MeIncomeFragment.newInstance(position,position)
                        .also {
                            it.setCurVisible(position == lastIndex)
                            fragments[position] = it
                        }

                }
            }


        val fs = supportFragmentManager.fragments
        if (fs.isValid() && fragments.isEmpty()) {
            fs.filterIsInstance<BaseFragment>()
                .mapIndexed { i, f ->
                    fragments[i] = f
                }
        }


        vpHome.apply {
            adapter = fragmentStateAdapter
            offscreenPageLimit = 2
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    fragments[position]?.changeVisible(true)
                    fragments[lastIndex]?.changeVisible(false)
                    lastIndex = position
                }
            })
        }


        TabLayoutMediator(tlHome, vpHome) { tab, position ->
            TabLayoutCustomTool.setupBoldCustomView(layoutInflater, titles!![position], tab)
        }.attach()

        tlHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TabLayoutCustomTool.unselectTab(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                TabLayoutCustomTool.selectTab(tab)
            }
        })
        TabLayoutCustomTool.selectTab(tlHome.getTabAt(lastIndex))
        fragmentStateAdapter.notifyItemRangeInserted(0, titles!!.size)

        CommonTool.changeViewPager2TouchSlop(vpHome, 4)
    }

    /*
   ╔════════════════════════════════════════════════════════════════════════════════════════╗
   ║   PS:
   ╚════════════════════════════════════════════════════════════════════════════════════════╝
*/

    private fun httpEngine() {
        model.getKolIncomeInfo(success = {
            HiLog.e(Tag2Common.TAG_12301, "kol income = ${it.toJson()}")
            showSuccessEngine()
            changeUI(it)
        }, failed = {_,_ ->
            showErrorEngine()
        })
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
