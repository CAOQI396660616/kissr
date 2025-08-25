package com.dubu.me.fragment

import android.view.View
import androidx.fragment.app.viewModels
import com.dubu.common.base.BaseBindingFragment
import com.dubu.me.R
import com.dubu.me.databinding.FragmentMeBinding
import com.dubu.me.vm.CommonViewModel


class MeFragment : BaseBindingFragment<FragmentMeBinding>() {

    companion object {
        const val TAG = "MineFragment"
        fun newInstance() = MeFragment()
    }


    private val model: CommonViewModel by viewModels()

    override fun getRootViewId(): Int {
        return R.layout.fragment_me
    }

    override fun onViewCreated(root: View) {
        initClick()
    }


    private fun initClick() {

    }


}