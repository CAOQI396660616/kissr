package com.dubu.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.dubu.common.base.BaseBindingFragment
import com.dubu.common.base.BaseFragment
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.home.R
import com.dubu.home.adapters.HomeNewUserAdapter
import com.dubu.home.databinding.FragmentHomeBinding
import com.dubu.me.vm.CommonViewModel
import com.hikennyc.view.MultiStateAiView


class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    private var multiStateView: MultiStateAiView? = null

    private val mAdapter: HomeNewUserAdapter by lazy {
        HomeNewUserAdapter()
    }

    private val commonViewModel: CommonViewModel by lazy {
        CommonViewModel()
    }

    companion object {
        const val TAG = "HomeFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        fun newInstance(index: Int = 0) = HomeFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
            }
        }
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_home
    }


    override fun onViewCreated(root: View) {

        multiStateView = root.findViewById<MultiStateAiView>(R.id.multiStateView)
        val vStatus = root.findViewById<View>(R.id.vStatus)
        val statusLayoutParams = vStatus.layoutParams
        statusLayoutParams.height =
            HiStatusBarTool.getCompatOptStatusBarHeight(requireContext())
        vStatus.layoutParams = statusLayoutParams

        initClick(root)
        httpEngine()
    }


    private fun initClick(root: View) {
        // 测试设置用户性别接口
        testSetUserGender()
    }

    private fun testSetUserGender() {
        // 测试设置用户性别为"1"（男性）
        commonViewModel.setUserGender(
            sex = "1",
            success = {
                Log.d(TAG, "设置用户性别成功")
            },
            failed = { code, message ->
                Log.e(TAG, "设置用户性别失败: code=$code, message=$message")
            }
        )
    }


    override fun onVisibleStateChanged(visible: Boolean) {

    }


    /*
    ╔════════════════════════════════════════════════════════════════════════════════════════╗
    ║   PS:
    ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


    private fun getData() {
        showSuccessEngine()
    }

    private fun httpEngine() {
        getData()
    }

    private fun showErrorEngine() {
        multiStateView?.viewState = MultiStateAiView.ViewState.ERROR
        multiStateView?.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        multiStateView?.viewState = MultiStateAiView.ViewState.CONTENT
    }


}