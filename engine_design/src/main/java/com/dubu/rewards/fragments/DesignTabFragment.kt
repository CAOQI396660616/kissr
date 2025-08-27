package com.dubu.rewards.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingFragment
import com.dubu.common.constant.Tag2Common
import com.dubu.common.utils.AdvancedClickUtils
import com.dubu.common.utils.HiLog
import com.dubu.design.R
import com.dubu.design.databinding.FragmentDesignTabBinding
import com.dubu.me.vm.CommonViewModel
import com.dubu.rewards.adapters.DesignListAdapter
import com.dubu.test.testdata.TestData
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 粉丝列表
 * @author cq
 * @date 2025/06/20
 * @constructor 创建[DesignTabFragment]
 */
class DesignTabFragment : BaseBindingFragment<FragmentDesignTabBinding>(),
    OnRefreshLoadMoreListener {

    private val model: CommonViewModel by viewModels()

    //数据类型
    private var dataType = 0

    //当前fg在vp中的下标
    private var lastIndex = 0

    companion object {
        const val TAG = "DesignTabFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        private const val DEFAULT_TYPE = "DEFAULT_TYPE"
        fun newInstance(index: Int = 0, type: Int = 0) = DesignTabFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
                putInt(DEFAULT_TYPE, type)
            }
        }
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_design_tab
    }


    private val mAdapter: DesignListAdapter by lazy {
        DesignListAdapter()
    }

    override fun onViewCreated(root: View) {
        lastIndex = arguments?.getInt(DEFAULT_INDEX) ?: 0
        dataType = arguments?.getInt(DEFAULT_TYPE) ?: 0

        bindingApply {
            with(recyclerView) {
                layoutManager =
                    GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }
            smartRefresh.setOnRefreshLoadMoreListener(this@DesignTabFragment)
        }
        mAdapter.setList(TestData.getUserList())
        initClick()


    }


    override fun onResume() {
        super.onResume()
        onRefreshData()
    }


    private fun initClick() {

        mAdapter.setOnItemClickListener { adapter, view, position ->


        }


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

//            model.fansList("$page", "20", success = {
//
//                it.data?.forEachIndexed { index, userBean ->
//                    HiLog.e(Tag2Common.TAG_12301, "fansList $index = ${GsonUtils.toJson(userBean)}")
//                }
//
//                dismissLoadingDialog()
//                val nextPageUrl = it.next_page_url ?: ""
//                hasMore = nextPageUrl.isNotEmpty()
//
//                val list = it.data
//                if (list?.isEmpty() == true) {
//                    mAdapter.setEmptyView(R.layout.layout_default_empty)
//                } else {
//                    mAdapter.setList(list)
//                }
//
//
//            }, failed = { _, _ ->
//                dismissLoadingDialog()
//            })

        } else {

            if (hasMore) {


//                model.fansList("$page", "20", success = {
//                    HiLog.e(Tag2Common.TAG_12301, "homeShortsList = ${GsonUtils.toJson(it)}")
//                    val nextPageUrl = it.next_page_url ?: ""
//                    hasMore = nextPageUrl.isNotEmpty()
//
//                    val list = it.data
//                    if (list != null && list.isNotEmpty()) {
//                        mAdapter.addData(list)
//                    }
//                    finishLoadMore()
//
//                }, failed = { _, _ ->
//                    finishLoadMore()
//                })

            } else {

                //加载更多
                finishRefreshWithNoMoreData()

            }

        }


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
      ║   PS: 会话列表回调监听
      ╚════════════════════════════════════════════════════════════════════════════════════════╝
   */

}