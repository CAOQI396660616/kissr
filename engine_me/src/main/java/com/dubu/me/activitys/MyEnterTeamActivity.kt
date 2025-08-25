package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.me.KolUnionInfoBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.CancelSureDialog
import com.dubu.common.manager.UITool
import com.dubu.common.router.Router
import com.dubu.common.utils.DisplayUiTool
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.ToastTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.TeamListAdapter
import com.dubu.me.databinding.ActivityMeSetTeamBinding
import com.dubu.me.vm.MineViewModel
import com.hikennyc.view.MultiStateAiView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 加入工会页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MyEnterTeamActivity]
 */
class MyEnterTeamActivity : BaseBindingActivity<ActivityMeSetTeamBinding>(),
    OnRefreshLoadMoreListener {
    private val model: MineViewModel by viewModels()

    //搜索
    private var userInput = ""
    //用户加入了某一个工会 在审核 那么就不可以点击 其他工会申请加入
    private var isUserUnionJoining = false
    //用户退出了某一个工会 在审核 那么就需要变化UI以及点击事件的处理
    private var isUserUnionExiting = false
    //我有工会
    private var isUserHasUnion = false

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyEnterTeamActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_set_team
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: TeamListAdapter by lazy {
        TeamListAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)
        httpEngine()
    }

    private fun initClick() {

        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBackAn) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.tvDesSum) {
            //搜索
            onRefreshData()
        }

        ClickUtils.applySingleDebouncing(binding.tvTitleEnd) {
            //入会记录
            Router.toMyUnionHistoryActivity()
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

            smartRefresh.setOnRefreshLoadMoreListener(this@MyEnterTeamActivity)


            etDes.addTextChangedListener { eb ->
                val trim = eb.toString().trim()
                val en = ((trim.length) > 0)
                userInput = if (en) {
                    eb.toString().trim()
                } else {
                    ""
                }
            }
            /*etDes.setOnEditorActionListener { _, _, _ ->
                true
            }*/
        }


        mAdapter.setOnItemClickListener { _, _, position ->

            if (isUserHasUnion) {
                ToastTool.toastError(R.string.joined_union_owner)
                return@setOnItemClickListener
            }

            if (isUserUnionJoining) {
                ToastTool.toastError(R.string.joined_union_review)
                return@setOnItemClickListener
            }

            CancelSureDialog()
                .withTitle(R.string.bind_guild)
                .withContent(R.string.bind_guild_tip)
                .withEndListener {
                    mAdapter.data[position].id?.let { uId ->
                        model.joinUnion(uId.toString(), success = {
                            ToastTool.toast(R.string.joined_union)
                            isUserUnionJoining = true
                            mAdapter.changUiForJoin(position)

                        }, failed = { _, _ ->
                            ToastTool.toast(R.string.toast_err_service)
                        })
                    }
                }
                .show(supportFragmentManager, "CancelSureDialog")


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

            model.unionList(userInput, "$page", "10", success = {


                it.data?.forEachIndexed { index, item ->
                    HiLog.e(Tag2Common.TAG_12301, "$index = ${GsonUtils.toJson(item)}")
                }

                dismissLoadingDialog()
                showSuccessEngine()
                val nextPageUrl = it.next_page_url ?: ""
                hasMore = nextPageUrl.isNotEmpty()

                val list = it.data
                if (list?.isEmpty() == true) {
                    mAdapter.setList(null)
                    mAdapter.setEmptyView(R.layout.layout_default_empty)
                    binding.smartRefresh.setEnableLoadMore(false)
                } else {

                    mAdapter.setList(list)
                    binding.smartRefresh.setEnableLoadMore(true)
                }

            }, failed = { _, _ ->
                dismissLoadingDialog()
                showErrorEngine()
            })

        } else {

            if (hasMore) {


                model.unionList(userInput, "$page", "10", success = {
                    it.data?.forEachIndexed { index, item ->
                        HiLog.e(Tag2Common.TAG_12301, "$index = ${GsonUtils.toJson(item)}")
                    }
                    showSuccessEngine()
                    val nextPageUrl = it.next_page_url ?: ""
                    hasMore = nextPageUrl.isNotEmpty()

                    val list = it.data
                    if (!list.isNullOrEmpty()) {
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
        binding.smartRefresh.finishRefresh()
    }

    private fun finishLoadMore() {
        binding.smartRefresh.finishLoadMore()
    }

    private fun finishRefreshWithNoMoreData() {
        binding.smartRefresh.finishRefreshWithNoMoreData()
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private fun httpEngine() {
        model.getMyUnionInfo(success = {
            /*if (it.union == null) {
                //没有加入过工会
                onRefreshData()
            } else {

                if (it.role == "owner") {
                    //自己有工会
                    isUserHasUnion = true
                    //加入过工会 展示具体工会信息
                    showSuccessEngine()
                    showAnUI(it,true)
                } else {

                    if (it.member?.status == UITool.UNION_STATUS_EXIT_REVIEW || it.member?.status == UITool.UNION_STATUS_APPROVED) {
                        //加入过工会 展示具体工会信息
                        showSuccessEngine()
                        showAnUI(it)
                    } else {
                        if (it.member?.status == UITool.UNION_STATUS_REVIEW){
                            //加入工会 待审核
                            isUserUnionJoining = true
                        }
                        onRefreshData()
                    }

                }
            }*/










            val unionInfo = it // 使用一个更具描述性的变量名
            val memberStatus = unionInfo.member?.status

            if (unionInfo.union == null) {
                // 没有加入过工会
                onRefreshData()
                return@getMyUnionInfo // 如果 onRefreshData() 后没有其他逻辑，可以考虑提前返回
            }

            // 用户已加入或曾经加入过工会
            if (unionInfo.role == "owner") {
                // 自己有工会
                isUserHasUnion = true
                showSuccessEngine()
                showAnUI(unionInfo, true)
            } else {
                // 非会长，判断成员状态
                when (memberStatus) {
                    UITool.UNION_STATUS_EXIT_REVIEW, UITool.UNION_STATUS_APPROVED -> {
                        // 加入过工会且状态为已通过或退出审核中，展示具体工会信息
                        showSuccessEngine()
                        showAnUI(unionInfo)
                    }
                    UITool.UNION_STATUS_REVIEW -> {
                        // 加入工会 待审核
                        isUserUnionJoining = true
                        onRefreshData() // 根据业务逻辑，可能也需要 onRefreshData()
                    }
                    else -> {
                        // 其他成员状态 (例如：被拒绝, 已退出等)，或者 member 为 null 的情况
                        // 根据业务逻辑，这里可能也需要 onRefreshData() 或其他处理
                        onRefreshData()
                    }
                }
            }


        }, failed = { _, _ ->
            showErrorEngine()
        })
    }


    private fun showErrorEngine() {
        binding.multiStateView.viewState = MultiStateAiView.ViewState.ERROR
        binding.multiStateView.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        binding.multiStateView.viewState = MultiStateAiView.ViewState.CONTENT
    }


    private fun showAnUI(data: KolUnionInfoBean, isOwner:Boolean = false) {
        if ((data.member?.status?:"") == UITool.UNION_STATUS_EXIT_REVIEW || (data.member?.status?:"") == UITool.UNION_STATUS_APPROVED || isOwner) {
            //同时要改变UI
            //展示 加入工会的UI
            binding.clTopRootAn.visibility = View.VISIBLE
            binding.clTopRoot.visibility = View.GONE
            //返回按键
            ClickUtils.applySingleDebouncing(binding.ivBackAnAn) {
                finish()
            }

            GlideTool.loadImageWithDefault(data.union?.cover,binding.ivAvatar)
            binding.tvTeamName.text = data.union?.name
            binding.tvTeamDesc.text = getString(R.string.union_join_time,data.union?.approved_at.toString())

            val status = data.union?.member?.status
            UITool.changeUnionOutUI(status,binding.tvNext)

            if (status == UITool.UNION_STATUS_EXIT_REVIEW){
                binding.tvNext.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clCCCCCC)
                }
                binding.tvNext.text = getString(R.string.union_status_out_pending)
            }else{
                binding.tvNext.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clFF4D6F)
                }
                binding.tvNext.text = getString(R.string.union_status_out)

                ClickUtils.applySingleDebouncing(binding.tvNext) {

                    if (isUserUnionExiting) {
                        ToastTool.toastError(R.string.exit_union_pending)
                        return@applySingleDebouncing
                    }

                    //可以退出公会
                    CancelSureDialog()
                        .withTitle(R.string.union_status_out)
                        .withContent(R.string.exit_guild_tip)
                        .withEndListener {
                            model.exitUnion(data.union?.id.toString(), success = {
                                ToastTool.toast(R.string.exit_union_pending)
                                isUserUnionExiting = true
                                binding.tvNext.helper.apply {
                                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clCCCCCC)
                                }
                                binding.tvNext.text = getString(R.string.union_status_out_pending)
                            }, failed = { _, _ ->
                                ToastTool.toast(R.string.toast_err_service)
                                isUserUnionExiting = false
                            })
                        }
                        .show(supportFragmentManager, "CancelSureDialog")

                }

                ClickUtils.applySingleDebouncing(binding.tvTitleEndAN) {
                    Router.toMyUnionHistoryActivity()
                }


            }

            if (isOwner)
                binding.tvNext.visibility = View.GONE

        }else{

            isUserUnionJoining = true
            onRefreshData()
        }
    }

}
