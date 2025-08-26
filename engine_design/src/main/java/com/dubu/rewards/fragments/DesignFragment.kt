package com.dubu.rewards.fragments

import android.view.View
import com.dubu.common.base.BaseFragment
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.design.R


class DesignFragment : BaseFragment() {

    companion object {
        const val TAG = "MessageFragment"
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


    }

    override fun onStart() {
        super.onStart()
        registerEvent()
    }

    private fun registerEvent() {


    }


}