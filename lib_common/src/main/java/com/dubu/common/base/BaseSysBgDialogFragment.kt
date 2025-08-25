package com.dubu.common.base

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import com.dubu.common.databinding.DialogTitleBarBinding
import com.dubu.common.dialog.LoadingDialog
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *  @author  Even
 *  @date   2021/11/10
 *  基础Dialog封装
 *  这个是沿用系统的背景色 也就是透明黑色背景
 */
abstract class BaseSysBgDialogFragment<VM : BaseViewModel, T : ViewDataBinding>(
    private val layoutId: Int,
    private val variableId: Int
) : BaseViewModelDialogFragment<VM, T>(layoutId, variableId) {
    override fun getTitleBarView(): View {
        val inflate =
            DialogTitleBarBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        return inflate.root
    }

    protected var loadingDialog: LoadingDialog? = null
    fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
                .withCancelOutside(false)
                .withCancelable(true)
        }
        loadingDialog?.show(childFragmentManager)
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    /**
     * 设置刷新框架，需要时调用即可
     */
     fun initRefreshLayout(smartRefreshLayout: SmartRefreshLayout) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setEnableRefresh(true)
            smartRefreshLayout.setEnableLoadMore(true)
            smartRefreshLayout.setEnableAutoLoadMore(true)
            smartRefreshLayout.setDisableContentWhenLoading(true)
            smartRefreshLayout.setDisableContentWhenRefresh(true)
        }
    }
}