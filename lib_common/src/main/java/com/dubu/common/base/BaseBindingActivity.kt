package com.dubu.common.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


abstract class BaseBindingActivity<BD : ViewDataBinding> : BaseActivity() {
    protected lateinit var binding: BD

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        binding = (DataBindingUtil.setContentView(this, getContentLayoutId()) as BD).apply {
            lifecycleOwner = this@BaseBindingActivity
        }
        onCreated()
    }

    protected inline fun bindingApply(block: BD.() -> Unit) {
        binding.apply(block)
    }

    protected fun <VM : ViewModel> initModel(clazz: Class<VM>): VM {
        return ViewModelProvider(this)[clazz]
    }

}