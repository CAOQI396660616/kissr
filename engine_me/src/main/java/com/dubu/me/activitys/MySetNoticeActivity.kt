package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.manager.SettingsManager
import com.dubu.common.utils.PermissionsTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.databinding.ActivityMeSetNoticeBinding
import com.permissionx.guolindev.PermissionX


/**
 * 重要设置 : 通知设置 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MySetNoticeActivity]
 */
class MySetNoticeActivity : BaseBindingActivity<ActivityMeSetNoticeBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MySetNoticeActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_set_notice
    }

    override fun onCreated() {

        initView()
        initClick()
    }


    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        val grantedNotification = PermissionsTool.grantedNotification(this@MySetNoticeActivity)


        changeUi(grantedNotification)

        /* PermissionX.init(this@MySetNoticeActivity)
             .permissions(PermissionX.permission.POST_NOTIFICATIONS)
             .request { allGranted, grantedList, deniedList ->
                 if (allGranted) {
                     binding.tvNext.text = getString(R.string.app_19_007)
                     ImportSettingsManager.saveNotifyToMMKV("1")
                 }
             }*/

    }

    private fun changeUi(grantedNotification: Boolean) {

        if (grantedNotification) {
            binding.tvNext.helper.apply {
                backgroundColorNormal =
                    com.dubu.common.utils.DisplayUiTool.getColor(R.color.clE6E6E6)
            }
            binding.tvNext.setTextColor(com.dubu.common.utils.DisplayUiTool.getColor(R.color.clADADAD))
            binding.tvNext.text = getString(R.string.opened)


            ClickUtils.applySingleDebouncing(binding.tvNext) {

            }
        } else {
            binding.tvNext.helper.apply {
                backgroundColorNormal =
                    com.dubu.common.utils.DisplayUiTool.getColor(R.color.cl2C2E33)
            }
            binding.tvNext.setTextColor(com.dubu.common.utils.DisplayUiTool.getColor(R.color.white))
            binding.tvNext.text = getString(R.string.open_tip)


            ClickUtils.applySingleDebouncing(binding.tvNext) {
                PermissionX.init(this@MySetNoticeActivity)
                    .permissions(PermissionX.permission.POST_NOTIFICATIONS)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            toastOk(R.string.opened_system_notifications)
                            SettingsManager.saveNotifyToMMKV("1")
                            changeUi(true)
                        } else {
                            changeUi(false)
                        }
                    }

            }
        }

    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }


    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
