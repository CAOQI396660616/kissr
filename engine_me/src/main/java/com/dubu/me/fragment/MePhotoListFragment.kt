package com.dubu.me.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingFragment
import com.dubu.common.constant.Tag2Common
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.http.NetWorkConst
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.ToastTool
import com.dubu.me.R
import com.dubu.me.activitys.MyPhotoListActivity
import com.dubu.me.activitys.MyPhotoListPreviewActivity
import com.dubu.me.adapters.PhotoListAdapter
import com.dubu.me.databinding.FragmentMePhotoListBinding
import com.dubu.me.vm.MineViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 我的相册
 *
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MePhotoListFragment]
 */
class MePhotoListFragment : BaseBindingFragment<FragmentMePhotoListBinding>(),
    OnRefreshLoadMoreListener {


    private val model: MineViewModel by viewModels()

    //下面2个字段 都是差不多的
    //数据类型 对应的是tab的下标 0全部 1已发布 2审核中 3未通过
    //0:审核中 PENDING 1:已通过（上架）APPROVED 2:不通过 REJECTED  后台对应数据
    private var dataType = 0

    //对应后台数据请求时候的类型
    private var dataHttpStr = ""

    //数据类型 对应的是tab的下标 0全部 1已发布 2审核中 3未通过
    private var lastIndex = 0

    companion object {
        const val TAG = "MineFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        private const val DEFAULT_TYPE = "DEFAULT_TYPE"
        fun newInstance(index: Int = 0, type: Int = 0) = MePhotoListFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
                putInt(DEFAULT_TYPE, type)
            }
        }
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_me_photo_list
    }


    private val mAdapter: PhotoListAdapter by lazy {
        PhotoListAdapter()
    }

    override fun onViewCreated(root: View) {
        lastIndex = arguments?.getInt(DEFAULT_INDEX) ?: 0
        dataType = arguments?.getInt(DEFAULT_TYPE) ?: 0
        HiLog.e(Tag2Common.TAG_12311, " 当前下标 $lastIndex")
        //数据类型 对应的是tab的下标 0全部 1已发布 2审核中 3未通过
        //0:审核中 PENDING 1:已通过（上架）APPROVED 2:不通过 REJECTED  后台对应数据

        dataHttpStr = when (dataType) {
            0 -> {
                NetWorkConst.POST_LIST_ALL
            }
            1 -> {
                NetWorkConst.POST_LIST_APPROVED
            }
            2 -> {
                NetWorkConst.POST_LIST_PENDING
            }
            3 -> {
                NetWorkConst.POST_LIST_REJECTED
            }
            else -> {
                NetWorkConst.POST_LIST_ALL
            }
        }

        bindingApply {
            with(recyclerView) {
                layoutManager =
                    GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }

            smartRefresh.setOnRefreshLoadMoreListener(this@MePhotoListFragment)

        }

        initClick()

    }

    private fun initClick() {

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val kolPostBean = mAdapter.data[position]

            when (view.id) {
                R.id.root -> {
                    //            if (kolPostBean.type == "video") {
                    //                kolPostBean.files?.let { Router.toVideoPlayActivity(it, 0) }
                    //            } else {
                    //                kolPostBean.files?.let { Router.toImagePreviewChatActivity(it) }
                    //            }

                    activity?.let { MyPhotoListPreviewActivity.startAction(it) }
                    HiRealCache.photoList = mAdapter.data
                    HiRealCache.photoListShowIndex = position

                    HiLog.e(Tag2Common.TAG_12311, "audit_status = ${kolPostBean.audit_status}")
                }
                R.id.ivChoose -> {

                    if (enterEditActionType == 0 && lastIndex != 1) {
                        ToastTool.toastError(R.string.toast_err_only_published)
                    } else {
                        mAdapter.toggleChoose(position)
                    }

                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        HiRealCache.photoList = null
        HiRealCache.photoListShowIndex = 0
    }

    override fun onResume() {
        super.onResume()
        registerEvent()
    }


    override fun onVisibleStateChanged(visible: Boolean) {
        if (visible) {
            check2LoadData()
            HiLog.e(Tag2Common.TAG_12314, "onVisibleStateChanged = $lastIndex")
        }
    }

    override fun onStart() {
        super.onStart()
        if (visibleCurRecord && !isActivityParent) {
            check2LoadData()
        }
    }

    private var loading = false
    private fun check2LoadData() {
        if (loading || mAdapter == null) return
        if (mAdapter.data.isEmpty()) {
            onRefreshData()
        }
    }


    // 0 置顶编辑动作 1删除编辑动作  -1无
    private var enterEditActionType = -1

    fun setEnterEditActionType(type: Int){
        enterEditActionType = type

    }

    private fun registerEvent() {

        //全选
        EventManager.registerSticky<Int>(EventKey.ACTION_ALL_SELECTED) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "全选 选中下标 $index 当前下标 $lastIndex")

                mAdapter.selectedAllOrNot()
            }
        }

        //置顶
        EventManager.registerSticky<Int>(EventKey.ACTION_ENTER_TOP) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "置顶 选中下标 $index 当前下标 $lastIndex")
                enterEditActionType = 0
                mAdapter.showEditUi()
            }
        }

        //编辑
        EventManager.registerSticky<Int>(EventKey.ACTION_ENTER_EDIT) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "删除 选中下标 $index 当前下标 $lastIndex")
                enterEditActionType = 1
                mAdapter.showEditUi()
            }
        }


        //完成
        EventManager.registerSticky<Int>(EventKey.ACTION_OVER) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "完成 选中下标 $index 当前下标 $lastIndex")
                enterEditActionType = -1
                mAdapter.hideEditUi()
            }
        }


        //==============================================================================

        //删除
        EventManager.registerSticky<Int>(EventKey.ACTION_DEL) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "删除 选中下标 $index 当前下标 $lastIndex")

                val checkUserChooseList = mAdapter.checkUserChooseList()
                if (checkUserChooseList.isNotEmpty()) {
                    //走删除业务逻辑

                    val imageList = mutableListOf<Long>()
                    checkUserChooseList.forEachIndexed { index, kolPostBean ->
                        kolPostBean.id?.let { imageList.add(it) }
                    }

                    showLoadingDialog()
                    model.postDel(
                        images = imageList,
                        success = { _ ->
                            dismissLoadingDialog()
                            setActivityExitEditAction()
                            enterEditActionType = -1
                            mAdapter.exitEditAction()
                            EventManager.postSticky(EventKey.ACTION_REFRESH, lastIndex)
                        }) { code, msg ->
                        ToastTool.toastError(R.string.toast_err_service)
                        dismissLoadingDialog()
                    }
                } else {
                    //提示用户
                    ToastTool.toastError(R.string.plz_select_data)

                }
            }
        }


        //置顶
        EventManager.registerSticky<Int>(EventKey.ACTION_TOP) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "置顶 选中下标 $index 当前下标 $lastIndex")

                val checkUserChooseList = mAdapter.checkUserChooseList()
                if (checkUserChooseList.isNotEmpty()) {

                    if (checkUserChooseList.size <= 2) {

                        //走删除业务逻辑

                        val imageList = mutableListOf<Long>()
                        checkUserChooseList.forEachIndexed { index, kolPostBean ->
                            kolPostBean.id?.let { imageList.add(it) }
                        }

                        showLoadingDialog()
                        //1:置顶，2:取消置顶
                        model.postToggleTop(
                            1,
                            images = imageList,
                            success = { _ ->
                                dismissLoadingDialog()
                                setActivityExitEditAction()
                                enterEditActionType = -1
                                mAdapter.exitEditAction()
                                EventManager.postSticky(EventKey.ACTION_REFRESH, lastIndex)

                            }) { _, _ ->
                            ToastTool.toastError(R.string.toast_err_service)
                            dismissLoadingDialog()
                        }
                    } else {
                        ToastTool.toastError(R.string.toast_err_only_top)
                    }

                } else {
                    //提示用户
                    ToastTool.toastError(R.string.plz_select_data)

                }
            }
        }


        //取消置顶
        EventManager.registerSticky<Int>(EventKey.ACTION_UN_TOP) { index ->
            if (index == lastIndex) {
                HiLog.e(Tag2Common.TAG_12311, "取消置顶 选中下标 $index 当前下标 $lastIndex")

                val checkUserCanUpTopList = mAdapter.checkUserCanUnTop()
                if (checkUserCanUpTopList.isNotEmpty() && mAdapter.isUserCanUpTop()) {


                    //走删除业务逻辑

                    val imageList = mutableListOf<Long>()
                    checkUserCanUpTopList.forEachIndexed { index, kolPostBean ->
                        kolPostBean.id?.let { imageList.add(it) }
                    }

                    showLoadingDialog()
                    //1:置顶，0:取消置顶
                    model.postToggleTop(
                        0,
                        images = imageList,
                        success = { _ ->
                            dismissLoadingDialog()
                            setActivityExitEditAction()
                            enterEditActionType = -1
                            mAdapter.exitEditAction()
                            EventManager.postSticky(EventKey.ACTION_REFRESH, lastIndex)
                        }) { _, _ ->
                        ToastTool.toastError(R.string.toast_err_service)
                        dismissLoadingDialog()
                    }
                } else {
                    //提示用户
                    ToastTool.toastError(R.string.toast_err_plz_select_data)
                }
            }
        }


    }

    private fun setActivityExitEditAction() {
        val myPhotoListActivity = activity as MyPhotoListActivity
        myPhotoListActivity.exitEditAction()
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


    fun onRefreshData() {

        page = 1
        loadMsgData(page, true)
    }

    private fun loadMsgData(page: Int, isRefresh: Boolean? = false) {
        loading = true
        if (isRefresh == true) {
//            showLoadingDialog()
        }


        if (isRefresh == true) {
            finishRefresh()

            model.homeShortsList(dataHttpStr, "$page", "20", success = {
                HiLog.e(Tag2Common.TAG_12301, "homeShortsList = ${GsonUtils.toJson(it)}")
                HiLog.e(Tag2Common.TAG_12301, "11111111111enterEditActionType = $enterEditActionType")

                dismissLoadingDialog()
                val nextPageUrl = it.next_page_url ?: ""
                hasMore = nextPageUrl.isNotEmpty()

                val list = it.data
                if (list?.isEmpty() == true) {
                    mAdapter.setList(null)
                    mAdapter.setEmptyView(R.layout.layout_default_empty)
                    binding?.smartRefresh?.setEnableLoadMore(false)
                } else {
                    if (enterEditActionType != -1){
                        mAdapter.enterEditAction()
                    }else{
                        mAdapter.exitEditAction()
                    }

                    mAdapter.setList(list)
                    binding?.smartRefresh?.setEnableLoadMore(true)
                }
                loading = false

            }, failed = { _, _ ->
                dismissLoadingDialog()
                loading = false
            })

        } else {

            if (hasMore) {


                model.homeShortsList("", "$page", "20", success = {
                    HiLog.e(Tag2Common.TAG_12301, "homeShortsList = ${GsonUtils.toJson(it)}")
                    val nextPageUrl = it.next_page_url ?: ""
                    hasMore = nextPageUrl.isNotEmpty()

                    val list = it.data
                    if (list != null && list.isNotEmpty()) {
                        mAdapter.addData(list)
                    }
                    finishLoadMore()
                    loading = false
                }, failed = { _, _ ->
                    finishLoadMore()
                    loading = false
                })

            } else {

                //加载更多
                finishRefreshWithNoMoreData()
                loading = false
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

    fun showEditUi() {
        mAdapter.showEditUi()
    }

    fun hideEditUi() {
        mAdapter.hideEditUi()
    }

}