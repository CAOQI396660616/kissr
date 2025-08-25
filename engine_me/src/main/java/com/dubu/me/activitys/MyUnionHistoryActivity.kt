package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.UnionHistoryListAdapter
import com.dubu.me.databinding.ActivityMeUnionHistoryBinding
import com.dubu.me.vm.MineViewModel
import com.hikennyc.view.MultiStateAiView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 我的公会加入记录
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyUnionHistoryActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_UNION_HISTORY)
class MyUnionHistoryActivity : BaseBindingActivity<ActivityMeUnionHistoryBinding>(),
    OnRefreshLoadMoreListener {
    private val model: MineViewModel by viewModels()

    private val mAdapter: UnionHistoryListAdapter by lazy {
        UnionHistoryListAdapter()
    }

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyUnionHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_union_history
    }

    override fun onCreated() {
        initView()
        initClick()

        httpEngine()
    }


    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)
        bindingApply {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = mAdapter
            }

            smartRefresh.setOnRefreshLoadMoreListener(this@MyUnionHistoryActivity)

        }
        mAdapter.setEmptyView(R.layout.layout_default_empty)
    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
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

            model.unionHistoryList("join", "$page", "10", success = {


                it.data?.forEachIndexed { index, kolIncomeListBean ->
                    HiLog.e(Tag2Common.TAG_12301, "$index = ${GsonUtils.toJson(kolIncomeListBean)}")
                }

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


                model.unionHistoryList("join", "$page", "10", success = {
                    it.data?.forEachIndexed { index, kolIncomeListBean ->
                        HiLog.e(Tag2Common.TAG_12301, "$index = ${GsonUtils.toJson(kolIncomeListBean)}")
                    }
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
