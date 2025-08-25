package com.dubu.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<BD : ViewDataBinding> : BaseFragment() {
    protected var binding: BD? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            (DataBindingUtil.inflate(inflater, getRootViewId(), container, false) as BD).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        return binding!!.root
    }


    protected inline fun bindingApply(block: BD.() -> Unit) {
        binding?.apply(block)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}