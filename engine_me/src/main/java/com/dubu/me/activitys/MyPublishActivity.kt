package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.PhotoMultiItemAdapter
import com.dubu.me.databinding.ActivityMePublishBinding


/**
 * 发布视频或图片主页面
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MyPublishActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_PUBLISH)
class MyPublishActivity : BaseBindingActivity<ActivityMePublishBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyPublishActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_publish
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: PhotoMultiItemAdapter by lazy {
        PhotoMultiItemAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)



    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }
        ClickUtils.applySingleDebouncing(binding.rootVideo) {
            Router.toMyPublishVideoActivity()
        }
        ClickUtils.applySingleDebouncing(binding.rootPhoto) {
            Router.toMyPublishPhotoActivity()
        }
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
