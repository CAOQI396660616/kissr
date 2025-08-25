package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.util.ArrayMap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.base.BaseFragment
import com.dubu.common.ext.isValid
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.TabLayoutCustomTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.databinding.ActivityMyWalletBinding
import com.dubu.me.fragment.MeGainsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


/**
 * 我的钱包
 *
 * @author cq
 * @date 2025/06/05
 * @constructor 创建[MyWalletActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_WALLET)
class MyWalletActivity : BaseBindingActivity<ActivityMyWalletBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyWalletActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val titles by lazy {
        arrayOf(
            BaseApp.instance.getString(R.string.app_name),
            BaseApp.instance.getString(R.string.app_name)
        )
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_my_wallet
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.cl1C1C1C, false)

        bindingApply {


            //            vpHome.isUserInputEnabled = false

            initVp(vpHome, tlHome)
        }
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

    private var lastIndex = 0
    private var fragments = ArrayMap<Int, BaseFragment>(2)
    private var vpHome: ViewPager2? = null
    private fun initVp(vpHome: ViewPager2, tlHome: TabLayout) {
        lastIndex = 0
        val fragmentStateAdapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return titles.size
                }

                @Suppress("KotlinConstantConditions")
                override fun createFragment(position: Int): Fragment {

                    return if (0 == position) {
                        MeGainsFragment.newInstance(0,0)
                            .also {
                                it.setCurVisible(position == lastIndex)
                                fragments[position] = it
                            }
                    } else {
                        MeGainsFragment.newInstance(0,1)
                            .also {
                                it.setCurVisible(position == lastIndex)
                                fragments[position] = it
                            }
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
            TabLayoutCustomTool.setupWalletView(layoutInflater, titles[position], tab)
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
        fragmentStateAdapter.notifyItemRangeInserted(0, titles.size)

        CommonTool.changeViewPager2TouchSlop(vpHome, 4)
        this.vpHome = vpHome
    }


    override fun onDestroy() {
        fragments.clear()
        super.onDestroy()
    }

}
