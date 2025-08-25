package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.UserBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.*
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.common.views.cview.DrawableTextView
import com.dubu.me.R
import com.dubu.me.adapters.MeBannerAdapter
import com.dubu.me.adapters.PhotoListAdapterV2
import com.dubu.me.databinding.ActivityMeProfileBinding
import com.dubu.me.vm.MineViewModel
import com.dubu.rtc.tools.HorizontalPageLayoutManager
import com.dubu.rtc.tools.PagingScrollHelper
import com.google.android.material.appbar.AppBarLayout
import com.hikennyc.view.MultiStateAiView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zhpan.indicator.enums.IndicatorSlideMode
import kotlin.math.abs


/**
 * 个人主页
 *
 * @author cq
 * @date 2025/06/05
 * @constructor 创建[MyProfileActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_PROFILE)
class MyProfileActivity : BaseBindingActivity<ActivityMeProfileBinding>(),
    PagingScrollHelper.onPageChangeListener, OnRefreshLoadMoreListener {

    private val model: MineViewModel by viewModels()

    private var tvBio: TextView? = null
    private var btnToggle: DrawableTextView? = null
    private var isExpanded = false
    private var maxCollapsedLines = 2 // 折叠时最大行数


    private val mAdapterImage: MeBannerAdapter by lazy {
        MeBannerAdapter()
    }

    private val mAdapter: PhotoListAdapterV2 by lazy {
        PhotoListAdapterV2()
    }

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyProfileActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_profile
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private var horizontalPageLayoutManager: HorizontalPageLayoutManager? = null
    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    private fun initView() {

        val compatOptStatusBarHeight =
            HiStatusBarTool.getCompatOptStatusBarHeight(this@MyProfileActivity)
        val dp2px = DisplayUiTool.dp2px(this@MyProfileActivity, 50f)
        val dp2px40 = DisplayUiTool.dp2px(this@MyProfileActivity, 40f)
        mOffset = (compatOptStatusBarHeight + dp2px + dp2px40).toInt()

        findViewById<View>(R.id.vStatusHi)?.let {
            initOptBar(it, isStatusBarDarkFont = false, isKeyboardEnable = false)
        }

        tvBio = binding.tvInfo
        btnToggle = binding.tvToggle


        // 切换按钮点击事件
        btnToggle?.setOnClickListener { toggleText() }


        //使用通用RecyclerView组件
        val scrollHelper = PagingScrollHelper() //初始化横向管理器

        horizontalPageLayoutManager = HorizontalPageLayoutManager(1, 1) //这里两个参数是行列，这里实现的是三行四列

        scrollHelper.setUpRecycleView(binding.recyclerView) //将横向布局管理器和recycler view绑定到一起

        scrollHelper.setOnPageChangeListener(this) //设置滑动监听

        binding.recyclerView.layoutManager = horizontalPageLayoutManager //设置为横向

        scrollHelper.updateLayoutManger()
        scrollHelper.scrollToPosition(0) //默认滑动到第一页

        binding.recyclerView.isHorizontalScrollBarEnabled = true

        binding.recyclerView.apply {
            adapter = mAdapterImage
        }


        bindingApply {
            with(recyclerViewBot) {
                layoutManager =
                    GridLayoutManager(this@MyProfileActivity, 2, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }

            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                handleAppBarLayoutOffset(appBarLayout, verticalOffset)
            }

            smartRefresh.setOnRefreshLoadMoreListener(this@MyProfileActivity)

        }

        httpEngine()
    }

    private fun addGlobalLayoutListener() {

        // 添加布局监听器检测文本行数
        onGlobalLayoutListener = object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tvBio?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                val text = HiRealCache.user?.info ?:""
                val lineCount = UiUtils.calculateTextLinesCached(tvBio!!, text)
                checkTextLines(lineCount)
            }
        }
        tvBio?.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)

        tvBio?.addTextChangedListener {
            UiUtils.lineCountCache.clear()
        }

    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }
        ClickUtils.applySingleDebouncing(binding.ivBackAn) {
            finish()
        }
        ClickUtils.applySingleDebouncing(binding.viewEdit) {
            Router.toMeEditProfileActivity()
        }
        ClickUtils.applySingleDebouncing(binding.viewEditAn) {
            Router.toMeEditProfileActivity()
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            MyPhotoListPreviewActivity.startAction(this@MyProfileActivity)
            HiRealCache.photoList = mAdapter.data
            HiRealCache.photoListShowIndex = position

        }

    }

    override fun onResume() {
        super.onResume()
        HiRealCache.user?.let { handleUi(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        tvBio?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        HiRealCache.photoList = null
        HiRealCache.photoListShowIndex = 0
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var mOffset: Int? = 0 //顶部滑动变化试图的 距离

    /**
     * @description 处理上滑下滑顶部view动画背景api
     * @param appBarLayout 顶部view
     * @param verticalOffset 滑动偏移量
     * @author Allen appBarLayout.totalScrollRange
     * @time 2021/11/18 8:57
     */
    private fun handleAppBarLayoutOffset(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (mOffset == null) {
            return
        }
        if (abs(verticalOffset) <= mOffset!!) {
            /*总滑动百分比*/
            val range = abs(verticalOffset * 1.0f) / mOffset!!
            DisplayUiTool.setAlphaAllView(binding.toolbar, (range))
        } else {
            DisplayUiTool.setAlphaAllView(binding.toolbar, (1.0F))
        }

    }


    override fun onPageChange(index: Int) {
        binding.magicIndicator1.onPageSelected(index)
    }


    // 检测文本行数决定按钮显示
    private fun checkTextLines(lineCount: Int) {
        if (lineCount <= maxCollapsedLines){
            btnToggle?.visibility = View.GONE
        }else{
            btnToggle?.visibility = View.VISIBLE
        }


        btnToggle?.text = if (isExpanded) {
            btnToggle?.setDrawableRight(R.drawable.ic_tv_toggle_u)
            //收起
            getString(R.string.collapse)
        } else {
            btnToggle?.setDrawableRight(R.drawable.ic_tv_toggle_d)
            //展开
            getString(R.string.expand)
        }


        // 初始设置为折叠状态
        tvBio?.maxLines = maxCollapsedLines
        tvBio?.ellipsize = TextUtils.TruncateAt.END
    }

    // 切换展开/折叠状态
    private fun toggleText() {
        isExpanded = !isExpanded

        // 添加平滑过渡动画
        //TransitionManager.beginDelayedTransition(tvBio?.parent as ViewGroup)


        tvBio?.post {
            if (isExpanded) {
                // 展开状态：显示所有行
                tvBio?.maxLines = Integer.MAX_VALUE
                tvBio?.ellipsize = null
                btnToggle?.text = getString(R.string.collapse)
                btnToggle?.setDrawableRight(R.drawable.ic_tv_toggle_u)
            } else {
                // 折叠状态：限制行数+省略号
                tvBio?.maxLines = maxCollapsedLines
                tvBio?.ellipsize = TextUtils.TruncateAt.END
                btnToggle?.text = getString(R.string.expand)
                btnToggle?.setDrawableRight(R.drawable.ic_tv_toggle_d)
            }
        }
    }
    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private fun showUserInfo() {

        model.getMeUserInfo(success = { data ->
            HiLog.e(Tag2Common.TAG_12302, "用户信息最新数据 成功: ${GsonUtils.toJson(data)}")
            showSuccessEngine()
            handleUi(data)
        }, failed = { code, msg ->
            showErrorEngine()
            HiLog.e(Tag2Common.TAG_12302, "用户信息最新数据 失败: $code = $msg")
        })
    }

    private fun handleUi(data: UserBean) {

        mAdapterImage.setList(data.images)

        changeUI(data)

        binding.recyclerView.postDelayed({
            val pageSize = horizontalPageLayoutManager?.pageSize ?: 0

            binding.magicIndicator1.apply {
                setIndicatorGap(DisplayMetricsTool.dp2px(context, 6F).toInt())
                setSlideMode(IndicatorSlideMode.NORMAL)
                setIndicatorDrawable(
                    com.dubu.common.R.drawable.common_banner_indicator_nornal,
                    com.dubu.common.R.drawable.common_banner_indicator_focus
                )
                setPageSize(pageSize)
                notifyDataChanged()
            }
            binding.magicIndicator1.onPageSelected(0)
        }, 100)
    }


    private fun changeUI(user: UserBean) {
        bindingApply {
            tvUserName.text = user.nickname
            tvAge.text = user.age.toString()
            tvCountry.text = user.countryCode
            tvBio?.text = user.info
            GlideTool.loadImage(user.country?.country_image,ivCountry)
        }
        addGlobalLayoutListener()
        //checkTextLines()
    }

    private fun httpEngine() {
        showUserInfo()
        onRefreshData()
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

    /*
      ╔════════════════════════════════════════════════════════════════════════════════════════╗
      ║   PS: 处理下拉刷新 上拉加载
      ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    //0:审核中 PENDING 1:已通过（上架）APPROVED 2:不通过 REJECTED  后台对应数据
    private var dataHttpStr = "1"
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

            model.homeShortsList(dataHttpStr, "$page", "10", success = {

                dismissLoadingDialog()
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
            })

        } else {

            if (hasMore) {
                model.homeShortsList("", "$page", "10", success = {
                    val nextPageUrl = it.next_page_url ?: ""
                    hasMore = nextPageUrl.isNotEmpty()

                    val list = it.data
                    if (list != null && list.isNotEmpty()) {
                        mAdapter.addData(list)
                    }
                    finishLoadMore()
                }, failed = { _, _ ->
                    finishLoadMore()
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


}
