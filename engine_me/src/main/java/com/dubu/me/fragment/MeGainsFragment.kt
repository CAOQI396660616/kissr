package com.dubu.me.fragment

import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseBindingFragment
import com.dubu.common.base.BaseFragment
import com.dubu.common.ext.isValid
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.TabLayoutCustomTool
import com.dubu.me.R
import com.dubu.me.databinding.FragmentMeGainsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MeGainsFragment : BaseBindingFragment<FragmentMeGainsBinding>() {


    companion object {
        const val TAG = "MeGainsFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        private const val DEFAULT_TYPE = "DEFAULT_TYPE"
        fun newInstance(index: Int = 0, type: Int = 0) = MeGainsFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
                putInt(DEFAULT_TYPE, type)
            }
        }
    }

    //数据类型 收益或是消费   0gains收益  1consumption消费
    private var dataType = 0

    override fun getRootViewId(): Int {
        return R.layout.fragment_me_gains
    }

    override fun onViewCreated(root: View) {

        dataType = arguments?.getInt(DEFAULT_TYPE) ?: 0

        initClick()

        bindingApply {


            //            vpHome.isUserInputEnabled = false

            initVp(vpHome, tlHome)
        }
    }

    private fun initClick() {

    }

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    private val titles by lazy {
        arrayOf(
            BaseApp.instance.getString(R.string.app_name),
            BaseApp.instance.getString(R.string.app_name),
            BaseApp.instance.getString(R.string.app_name)
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

                    return MePhotoListFragment.newInstance(position, dataType)
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
            TabLayoutCustomTool.setupAccDataView(layoutInflater, titles[position], tab)
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


    override fun onDestroyView() {
        fragments.clear()
        super.onDestroyView()
    }

}