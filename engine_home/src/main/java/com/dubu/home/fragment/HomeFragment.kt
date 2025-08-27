package com.dubu.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dubu.common.base.BaseBindingFragment
import com.dubu.common.base.BaseFragment
import com.dubu.common.beans.home.HomeBannerDataBean
import com.dubu.common.beans.home.HomeTopVideoBean
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.home.R
import com.dubu.home.adapters.BannerVideoAdapter
import com.dubu.home.adapters.BannerVideoHolder
import com.dubu.home.adapters.HomeNewUserAdapter
import com.dubu.home.databinding.FragmentHomeBinding
import com.dubu.me.vm.CommonViewModel
import com.dubu.test.testdata.TestData
import com.hikennyc.view.MultiStateAiView
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnPageChangeListener


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

        initView()
        initClick(root)

        httpEngine()
    }

    private fun initView() {
        handleBanner(TestData.getHomeTopVideoList())
    }


    private fun initClick(root: View) {
        // 测试设置用户性别接口
//        testSetUserGender()
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
       ║   PS: banner
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var mBannerAdapter: BannerVideoAdapter<HomeBannerDataBean?>? = null

    private fun handleBanner(list: List<HomeTopVideoBean>) {

        //加载网络图片  banner
        val convertData = HomeBannerDataBean.convertOptData(list)
        binding?.bannerTopVideo?.isAutoLoop(false)
        binding?.bannerTopVideo?.viewPager2?.offscreenPageLimit = 1
        binding?.bannerTopVideo?.setIntercept(false)
        binding?.bannerTopVideo?.setUserInputEnabled(true)

        mBannerAdapter = object :
            BannerVideoAdapter<HomeBannerDataBean?>(
                convertData
            ) {
            override fun onBindView(
                holder: BannerVideoHolder?,
                data: HomeBannerDataBean?,
                position: Int,
                size: Int
            ) {

                holder?.ivBanner

            }
        }


        binding?.bannerTopVideo?.setAdapter(mBannerAdapter)
        binding?.bannerTopVideo?.indicator = CircleIndicator(requireContext())


        binding?.bannerTopVideo?.addOnPageChangeListener(object :
            OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }



    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS: http
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