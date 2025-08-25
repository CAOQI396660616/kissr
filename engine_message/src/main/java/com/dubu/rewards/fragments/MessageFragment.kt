package com.dubu.rewards.fragments

import android.util.ArrayMap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseFragment
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.ext.isValid
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.TabLayoutCustomTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.message.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MessageFragment : BaseFragment() {

    companion object {
        const val TAG = "MessageFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        fun newInstance() = MessageFragment()
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_message
    }

    override fun onViewCreated(root: View) {

        val vStatus = root.findViewById<View>(R.id.vStatus)
        val statusLayoutParams = vStatus.layoutParams
        statusLayoutParams.height =
            HiStatusBarTool.getCompatOptStatusBarHeight(requireContext())
        vStatus.layoutParams = statusLayoutParams


    }

    override fun onStart() {
        super.onStart()
        registerEvent()
    }

    private fun registerEvent() {


    }


}