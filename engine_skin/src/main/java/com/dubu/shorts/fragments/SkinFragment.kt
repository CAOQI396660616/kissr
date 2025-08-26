package com.dubu.shorts.fragments

import android.os.Bundle
import android.view.View
import com.dubu.common.base.BaseBindingFragment
import com.dubu.skin.R
import com.dubu.skin.databinding.FragmentSkinBinding


class SkinFragment : BaseBindingFragment<FragmentSkinBinding>() {

    companion object {
        const val TAG = "ChatFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        fun newInstance(index: Int = 0) = SkinFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
            }
        }
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_skin
    }


    override fun onViewCreated(root: View) {

    }

}