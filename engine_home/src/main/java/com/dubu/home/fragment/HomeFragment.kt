package com.dubu.home.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubu.common.base.BaseFragment
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.home.R
import com.dubu.home.adapters.HomeNewUserAdapter
import com.hikennyc.view.MultiStateAiView


class HomeFragment : BaseFragment() {
    private var multiStateView: MultiStateAiView? = null
    private val mAdapter: HomeNewUserAdapter by lazy {
        HomeNewUserAdapter()
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


    private fun getData() {
        showSuccessEngine()
    }


    private fun initClick(root: View) {


    }


    override fun onVisibleStateChanged(visible: Boolean) {

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


    /*
    ╔════════════════════════════════════════════════════════════════════════════════════════╗
    ║   PS:
    ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


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