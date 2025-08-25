package com.dubu.me.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingFragment
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.NetWorkConst
import com.dubu.common.utils.HiLog
import com.dubu.me.R
import com.dubu.me.adapters.IncomeListAdapter
import com.dubu.me.databinding.FragmentMeIncomeBinding
import com.dubu.me.databinding.FragmentMePhotoListBinding
import com.dubu.me.vm.MineViewModel
import com.dubu.test.testdata.TestData
import com.hikennyc.view.MultiStateAiView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 我的收入
 *
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MeIncomeFragment]
 */
class MeIncomeFragment : BaseBindingFragment<FragmentMeIncomeBinding>(),
    OnRefreshLoadMoreListener {
    //对应后台数据请求时候的类型  all gift video_call text_chat kol_service
    private var dataHttpStr = ""
    //数据类型
    private var dataType = 0

    //数据查找的类型 0all所有 1coins金币 2bouns积分
    private var lastIndex = 0

    private val model: MineViewModel by viewModels()

    companion object {
        const val TAG = "MineFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        private const val DEFAULT_TYPE = "DEFAULT_TYPE"
        fun newInstance(index: Int = 0, type: Int = 0) = MeIncomeFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
                putInt(DEFAULT_TYPE, type)
            }
        }
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_me_income
    }


    private val mAdapter: IncomeListAdapter by lazy {
        IncomeListAdapter()
    }

    override fun onViewCreated(root: View) {
        lastIndex = arguments?.getInt(DEFAULT_INDEX) ?: 0
        dataType = arguments?.getInt(DEFAULT_TYPE) ?: 0

        dataHttpStr = when (dataType) {
            0 -> {
                NetWorkConst.INCOME_LIST_ALL
            }
            1 -> {
                NetWorkConst.INCOME_LIST_VIDEO_CALL
            }
            2 -> {
                NetWorkConst.INCOME_LIST_TEXT_CHAT
            }
            3 -> {
                NetWorkConst.INCOME_LIST_GIFT
            }
            4 -> {
                NetWorkConst.INCOME_LIST_KOL_SERVICE
            }
            else -> {
                NetWorkConst.INCOME_LIST_ALL
            }
        }

        bindingApply {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = mAdapter
            }

            smartRefresh.setOnRefreshLoadMoreListener(this@MeIncomeFragment)

        }
        mAdapter.setEmptyView(R.layout.layout_default_empty)
        initClick()

    }

    private fun initClick() {

    }

    override fun onResume() {
        super.onResume()
        httpEngine()
    }


    /*
      ╔════════════════════════════════════════════════════════════════════════════════════════╗
      ║   PS: 处理下拉刷新 上拉加载
      ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var page = 1 //拉取数据的页数
    private var hasMore = true //还有更多数据吗?

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefreshData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        loadMsgData(page, false)
    }


    private fun onRefreshData() {
        page = 1
        loadMsgData(page, true)
    }

    private fun loadMsgData(page: Int, isRefresh: Boolean? = false) {
        if (isRefresh == true) {
            //            showLoadingDialog()
        }


        if (isRefresh == true) {
            finishRefresh()

            model.incomeList(dataHttpStr, "$page", "10", success = {


                /*it.data?.forEachIndexed { index, kolIncomeListBean ->
                    HiLog.e(Tag2Common.TAG_12301, "$index = ${GsonUtils.toJson(kolIncomeListBean)}")
                }*/

                dismissLoadingDialog()
                showSuccessEngine()
                val nextPageUrl = it.next_page_url ?: ""
                hasMore = nextPageUrl.isNotEmpty()

                val list = it.data
                if (list?.isEmpty() == true) {
                    mAdapter.setList(null)
                    mAdapter.setEmptyView(R.layout.layout_default_empty)
                    binding?.smartRefresh?.setEnableLoadMore(false)
                } else {

                    mAdapter.setList(list)
                    binding?.smartRefresh?.setEnableLoadMore(true)
                }

            }, failed = { _, _ ->
                dismissLoadingDialog()
                showErrorEngine()
            })

        } else {

            if (hasMore) {


                model.incomeList(dataHttpStr, "$page", "10", success = {
//                    HiLog.e(Tag2Common.TAG_12301, "incomeList = ${GsonUtils.toJson(it)}")
                    showSuccessEngine()
                    val nextPageUrl = it.next_page_url ?: ""
                    hasMore = nextPageUrl.isNotEmpty()

                    val list = it.data
                    if (list != null && list.isNotEmpty()) {
                        mAdapter.addData(list)
                    }
                    finishLoadMore()
                }, failed = { _, _ ->
                    finishLoadMore()
                    showErrorEngine()
                })

            } else {
                //加载更多
                finishRefreshWithNoMoreData()
            }

        }


        //        mAdapter.setEmptyView(R.layout.layout_default_empty)
        //        val tvRetry =
        //            mAdapter.emptyLayout?.findViewById<TextView>(R.id.tvRetry)
        //        tvRetry?.setOnClickListener {
        //            onRefreshData()
        //        }
        //        if (isRefresh == true) {
        //            finishRefresh()
        //        } else {
        //            finishLoadMore()
        //        }

    }


    private fun finishRefresh() {


        binding?.smartRefresh?.finishRefresh()
    }

    private fun finishLoadMore() {
        binding?.smartRefresh?.finishLoadMore()
    }

    private fun finishRefreshWithNoMoreData() {
        binding?.smartRefresh?.finishRefreshWithNoMoreData()
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private fun httpEngine() {
        onRefreshData()
    }

    private fun showErrorEngine() {
        binding?.multiStateView?.viewState = MultiStateAiView.ViewState.ERROR
        binding?.multiStateView?.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        binding?.multiStateView?.viewState = MultiStateAiView.ViewState.CONTENT
    }
}