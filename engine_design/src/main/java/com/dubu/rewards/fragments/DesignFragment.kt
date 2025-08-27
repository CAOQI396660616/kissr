package com.dubu.rewards.fragments

import android.util.ArrayMap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.BarUtils
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseFragment
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.ext.isValid
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.TabLayoutCustomTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.design.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DesignFragment : BaseFragment() {

    companion object {
        const val TAG = "DesignFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        fun newInstance() = DesignFragment()
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_design
    }

    override fun onViewCreated(root: View) {

        val vStatus = root.findViewById<View>(R.id.vStatus)
        val statusLayoutParams = vStatus.layoutParams
        statusLayoutParams.height =
            HiStatusBarTool.getCompatOptStatusBarHeight(requireContext())
        vStatus.layoutParams = statusLayoutParams

        val vpHome = root.findViewById<ViewPager2>(R.id.vp_home)
        val tlHome = root.findViewById<TabLayout>(R.id.tl_home)
        initVp(vpHome, tlHome)

    }

    override fun onStart() {
        super.onStart()
        registerEvent()
    }

    private fun registerEvent() {

        EventManager.registerSticky<Int>(EventKey.ROUTER_VIDEOS_HISTORY_IN) { _ ->
            vpHome?.currentItem = 1
        }

    }


    /*
      ╔════════════════════════════════════════════════════════════════════════════════════════╗
      ║   PS:
      ╚════════════════════════════════════════════════════════════════════════════════════════╝
      */
    private val titles by lazy {
        arrayOf(
            BaseApp.instance.getString(R.string.tab_msg),
            BaseApp.instance.getString(R.string.tab_call),
            BaseApp.instance.getString(R.string.tab_fans)
        )
    }
    private var lastIndex = 0
    private var fragments = ArrayMap<Int, BaseFragment>(3)
    private var vpHome: ViewPager2? = null
    private fun initVp(vpHome: ViewPager2, tlHome: TabLayout) {

        lastIndex = arguments?.getInt(DEFAULT_INDEX) ?: 0
        val fragmentStateAdapter =
            object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return titles.size
                }

                @Suppress("KotlinConstantConditions")
                override fun createFragment(position: Int): Fragment {

                    return DesignTabFragment.newInstance(position, position)
                        .also {
                            it.setCurVisible(position == lastIndex)
                            fragments[position] = it
                        }


                }
            }


        val fs = childFragmentManager.fragments
        if (fs.isValid() && fragments.isEmpty()) {
            fs.filterIsInstance<BaseFragment>()
                .mapIndexed { i, f ->
                    fragments[i] = f
                }
        }


        vpHome.apply {
            adapter = fragmentStateAdapter
            offscreenPageLimit = 3
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    fragments[position]?.changeVisible(true)
                    fragments[lastIndex]?.changeVisible(false)
                    lastIndex = position
                }
            })
        }


        TabLayoutMediator(tlHome, vpHome) { tab, position ->
            TabLayoutCustomTool.setupMessageCustomView(layoutInflater, titles[position], tab)
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



}