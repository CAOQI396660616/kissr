package com.dubu.main.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.base.BaseFragment
import com.dubu.common.constant.Tag2Common
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.PermissionsTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.home.fragment.HomeFragment
import com.dubu.main.R
import com.dubu.main.databinding.ActivityMainBinding
import com.dubu.me.fragment.MeFragment
import com.dubu.me.vm.CommonViewModel
import com.dubu.rewards.fragments.MessageFragment
import com.dubu.shorts.fragments.ChatFragment
import com.hikennyc.view.MultiStateAiView
import com.permissionx.guolindev.PermissionX


@Route(path = RouteConst.ACTIVITY_MAIN)
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    private val tags = arrayOf(
        HomeFragment.TAG,
        MessageFragment.TAG,
        ChatFragment.TAG,
        MeFragment.TAG,
    )


    private val model: CommonViewModel by viewModels()

    //上次选中的下标 及初始化页面下标
    private var lastIndex = 0
    private var bottomBar: MainBottomBar? = null
    private var activityVisible = false

    override fun getContentLayoutId() = R.layout.activity_main


    override fun isNeedDefaultScreenConfig() = false

    override fun onCreated() {
        httpEngine()
        check2ApplyPermission()
    }

    private fun httpEngine() {
        showSuccessEngine()
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
        initView()
        initFragment()
        HiLog.e(
            Tag2Common.TAG_RTC,
            "获取设备的deviceId ： ${HiRealCache.deviceId}"
        )
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tryChangeIndex(intent)
    }


    private fun check2ApplyPermission() {
        val pl = PermissionsTool.neededPermissionsV2(this)
        if (pl.isEmpty()) {

        } else {

            PermissionX.init(this@MainActivity)
                .permissions(pl)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {

                    } else {

                    }
                }

        }

    }


    private fun initView() {
        //Main记录一下状态栏高度 用于其他页面计算滑动距离等
        HiRealCache.phoneStatusBarHeight =
            HiStatusBarTool.getCompatOptStatusBarHeight(this@MainActivity)

        findViewById<View>(R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = true, isKeyboardEnable = false)
        }

        findViewById<ConstraintLayout>(R.id.cl_main_bottom_bar)?.let {
            bottomBar = MainBottomBar(it)
                .withTabChangeListener { index ->
                    showFragment(index)
                }
        }

        val parent = findViewById<ConstraintLayout>(R.id.cl_container)
        ViewCompat.setOnApplyWindowInsetsListener(parent) { _, insets ->
            //allen 暂时隐藏 底部间距的增加
            //            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            //            bottomBar?.adapterBottom(systemBars.bottom)
            return@setOnApplyWindowInsetsListener insets
        }

    }


    override fun onResume() {
        super.onResume()
        activityVisible = true
    }


    override fun onPause() {
        activityVisible = false
        super.onPause()
    }


    override fun onStart() {
        super.onStart()
        registerEvent()
    }


    private fun registerEvent() {
        EventManager.registerSticky<Int>(EventKey.LOGIN_OUT) { msgNum ->
            //finish()
        }
    }

    private fun defaultFragment(index: Int, fragment: BaseFragment, trans: FragmentTransaction) {
        fragment.setCurVisible(true)
        trans.show(fragment).commitAllowingStateLoss()
        lastIndex = index
    }

    private fun showFragment(index: Int) {
        val manager = supportFragmentManager
        val trans = manager.beginTransaction()
        val fragment: BaseFragment = findFragmentByTag(tags[index], manager, trans)

        (manager.findFragmentByTag(tags[lastIndex]) as? BaseFragment)?.let { lastF ->
            trans.hide(lastF)
            lastF.changeVisible(false)
        }

        trans.show(fragment).commitAllowingStateLoss()
        fragment.changeVisible(true)
        lastIndex = index
    }


    private fun findFragmentByTag(
        tag: String, manager: FragmentManager, trans: FragmentTransaction
    ): BaseFragment {
        return manager.findFragmentByTag(tag) as? BaseFragment ?: when (tag) {
            HomeFragment.TAG -> HomeFragment.newInstance().also {
                trans.add(R.id.fl_content, it, tag)
            }

            MessageFragment.TAG -> MessageFragment.newInstance().also {
                trans.add(R.id.fl_content, it, tag)
            }

            ChatFragment.TAG -> ChatFragment.newInstance().also {
                trans.add(R.id.fl_content, it, tag)
            }

            MeFragment.TAG -> MeFragment.newInstance().also {
                trans.add(R.id.fl_content, it, tag)
            }

            else -> throw IllegalArgumentException("no such fragment")
        }
    }


    private fun initFragment() {
        val fragments: Array<BaseFragment> = arrayOf(
            HomeFragment.newInstance(),
            MessageFragment.newInstance(),
            ChatFragment.newInstance(),
            MeFragment.newInstance(),
        )

        val manager = supportFragmentManager
        val trans = manager.beginTransaction()
        for (i in tags.indices) {
            val m = manager.findFragmentByTag(tags[i]) as? BaseFragment
            if (m == null) {
                fragments[i].let {
                    trans.add(R.id.fl_content, it, tags[i]).hide(it)
                }
            } else {
                trans.hide(m)
                fragments[i] = m
            }
        }


        lastIndex = intent?.getIntExtra(RouteConst.P_INDEX, 0) ?: 0

        defaultFragment(lastIndex, fragments[lastIndex], trans)
        bottomBar?.initTab(lastIndex)
    }


    override fun onDestroy() {
        bottomBar?.detach()
        super.onDestroy()
        EventManager.clear()
    }


    companion object {
        private const val TAG = "MainActivity"
    }


    private fun tryChangeIndex(intent: Intent?) {
        val index = intent?.getIntExtra(RouteConst.P_INDEX, -1) ?: -1
        if (index in 0..3) {
            bottomBar?.changeTab(index)
        }
    }


}