package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.util.ArrayMap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.base.BaseFragment
import com.dubu.common.constant.Tag2Common
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.ext.isValid
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.TabLayoutCustomTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.databinding.ActivityMePhotoListBinding
import com.dubu.me.fragment.MePhotoListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


/**
 * 相册页面
 *
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MyPhotoListActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_PHOTO_LIST)
class MyPhotoListActivity : BaseBindingActivity<ActivityMePhotoListBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyPhotoListActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_photo_list
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    override fun onStart() {
        super.onStart()


        //刷新
        EventManager.registerSticky<Int>(EventKey.ACTION_REFRESH) { index ->
            fragments.forEach { (t, u) ->
                HiLog.e(Tag2Common.TAG_12311, "刷新 需要刷新$t = 不需要刷新下标 $index ")
                (u as MePhotoListFragment).onRefreshData()
                (u as MePhotoListFragment).setEnterEditActionType(enterEditActionType)
            }


        }
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        val arrayOf = mutableListOf<String>(
            BaseApp.instance.getString(R.string.photo_tab_all),
            BaseApp.instance.getString(R.string.photo_tab_published),
            BaseApp.instance.getString(R.string.photo_tab_under_review),
            BaseApp.instance.getString(R.string.photo_tab_not_passed),
        )
        titles = arrayOf as ArrayList<String>
        binding.ivTopPost.visibility = View.GONE
        initVp(binding.vpHome, binding.tlHome)
    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        /*
        * 上面四个按钮
        * */

        //置顶点击了 向上的图片
        ClickUtils.applySingleDebouncing(binding.ivTopPost) {
            isEnterEditAction = true
            enterEditActionType = 0
            binding.ivTopPost.visibility = View.GONE
            binding.ivEditPost.visibility = View.GONE
            binding.tvOver.visibility = View.GONE  // VISIBLE
            binding.tvAll.visibility = View.GONE
            binding.llEdit.visibility = View.GONE
            binding.llTop.visibility = View.VISIBLE

            EventManager.postSticky(EventKey.ACTION_ENTER_TOP, lastIndex)

            toggleAllFgEditUi()
        }
        //编辑点击了 横岗的图片
        ClickUtils.applySingleDebouncing(binding.ivEditPost) {
            isEnterEditAction = true
            enterEditActionType = 1
            binding.ivTopPost.visibility = View.GONE
            binding.ivEditPost.visibility = View.GONE
            binding.tvOver.visibility = View.GONE //VISIBLE
            binding.tvAll.visibility = View.VISIBLE
            binding.llEdit.visibility = View.VISIBLE
            binding.llTop.visibility = View.GONE

            EventManager.postSticky(EventKey.ACTION_ENTER_EDIT, lastIndex)

            toggleAllFgEditUi()
        }


        //全选
        ClickUtils.applySingleDebouncing(binding.tvAll) {

            EventManager.postSticky(EventKey.ACTION_ALL_SELECTED, lastIndex)

        }
        //完成
        ClickUtils.applySingleDebouncing(binding.tvOver) {
            isEnterEditAction = false
            enterEditActionType = -1
            EventManager.postSticky(EventKey.ACTION_OVER, lastIndex)
            exitEditAction()
            toggleAllFgEditUi()
        }

        /*
        * 下面四个按钮
        * */

        //取消删除
        ClickUtils.applySingleDebouncing(binding.tvCancleDel) {
            isEnterEditAction = false
            enterEditActionType = -1
            exitEditAction()
            toggleAllFgEditUi()
        }
        //删除
        ClickUtils.applySingleDebouncing(binding.tvSureDel) {
            EventManager.postSticky(EventKey.ACTION_DEL, lastIndex)
        }
        //取消置顶
        ClickUtils.applySingleDebouncing(binding.tvCancleTop) {
            EventManager.postSticky(EventKey.ACTION_UN_TOP, lastIndex)
        }
        //置顶
        ClickUtils.applySingleDebouncing(binding.tvSureTop) {
            EventManager.postSticky(EventKey.ACTION_TOP, lastIndex)
        }

    }

    fun exitEditAction() {
        isEnterEditAction = false
        enterEditActionType = -1
        handleShowTopBt()
        binding.ivEditPost.visibility = View.VISIBLE
        binding.tvOver.visibility = View.GONE
        binding.tvAll.visibility = View.GONE
        binding.llEdit.visibility = View.GONE
        binding.llTop.visibility = View.GONE
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var titles: ArrayList<String>? = null
    private var isEnterEditAction = false

    // 0 置顶编辑动作 1删除编辑动作  -1无
    private var enterEditActionType = -1
    private var lastIndex = 0 //这个是标识 上一次选中的tab是哪一个 但是目前暂时没用
    private var currentSelectedTab = 0
    private var fragments = ArrayMap<Int, BaseFragment>(4)

    private fun initVp(vpHome: ViewPager2, tlHome: TabLayout) {
        lastIndex = 0
        val fragmentStateAdapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return titles!!.size
                }

                @Suppress("KotlinConstantConditions")
                override fun createFragment(position: Int): Fragment {

                    return MePhotoListFragment.newInstance(position, position)
                        .also {
                            it.setCurVisible(position == lastIndex)
                            fragments[position] = it
                        }

                }
            }


        val fs = supportFragmentManager.fragments
        if (fs.isValid() && fragments.isEmpty()) {
            fs.filterIsInstance<BaseFragment>()
                .mapIndexed { i, f ->
                    fragments[i] = f
                }
        }


        vpHome.apply {
            adapter = fragmentStateAdapter
            offscreenPageLimit = 4
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    fragments[position]?.changeVisible(true)
                    fragments[lastIndex]?.changeVisible(false)
                    lastIndex = position
                }
            })
        }


        TabLayoutMediator(tlHome, vpHome) { tab, position ->
            TabLayoutCustomTool.setupBoldCustomView(layoutInflater, titles!![position], tab)
        }.attach()

        tlHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TabLayoutCustomTool.unselectTab(tab)


            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                TabLayoutCustomTool.selectTab(tab)
                currentSelectedTab = tab?.position ?: 0
                HiLog.e(Tag2Common.TAG_12311, "onTabSelected $currentSelectedTab : $lastIndex")
                handleShowTopBt()

                setAllFgEnterEditAction()

            }
        })
        TabLayoutCustomTool.selectTab(tlHome.getTabAt(lastIndex))
        fragmentStateAdapter.notifyItemRangeInserted(0, titles!!.size)

        CommonTool.changeViewPager2TouchSlop(vpHome, 1)
    }

    private fun toggleAllFgEditUi() {


        //编辑模式下 就需要打开 所有FG的
        fragments.forEach { (t, u) ->
            HiLog.e(
                Tag2Common.TAG_12311,
                "toggleAllFgEditUi isEnterEditAction=$isEnterEditAction  "
            )

            if (isEnterEditAction) {
                (u as MePhotoListFragment).showEditUi()
            } else {
                (u as MePhotoListFragment).hideEditUi()
            }
        }
    }

    private fun setAllFgEnterEditAction() {
        //编辑模式下 就需要打开 所有FG的
        fragments.forEach { (t, u) ->
            HiLog.e(
                Tag2Common.TAG_12311,
                "toggleAllFgEditUi isEnterEditAction=$isEnterEditAction  "
            )
            (u as MePhotoListFragment).setEnterEditActionType(enterEditActionType)
        }
    }

    private fun handleShowTopBt() {
        if (currentSelectedTab == 1 && !isEnterEditAction) {
            //只有已发布页面可以置顶
            binding.ivTopPost.visibility = View.VISIBLE
        } else {
            binding.ivTopPost.visibility = View.GONE
        }
    }

}
