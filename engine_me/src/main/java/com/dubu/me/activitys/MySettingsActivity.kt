package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.BuildConfig
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.CancelSureDialog
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.manager.SettingsManager
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.ToastTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.databinding.ActivityMeSettingsBinding
import com.dubu.me.vm.MineViewModel


/**
 * 我的设置
 *
 * @author cq
 * @date 2025/06/05
 * @constructor 创建[MySettingsActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_SETTINGS)
class MySettingsActivity : BaseBindingActivity<ActivityMeSettingsBinding>() {
    private val model: MineViewModel by viewModels()
    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MySettingsActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_settings
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        binding.tv1.text = BuildConfig.versionName
    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }
        //登出
        ClickUtils.applySingleDebouncing(binding.tvNext) {

            loginOut()

        }


        ClickUtils.applySingleDebouncing(binding.clRoot1) {
            CancelSureDialog()
                .withTitle(R.string.delete_account)
                .withContent(R.string.delete_account_tip)
                .withEndListener {
                    loginOut()//todo allen:server (对接后台数据) 这里需要出接口
                }
                .show(supportFragmentManager,"CancelSureDialog")
        }


        binding.switchChat.isChecked = !SettingsManager.checkUserCloseInteractive()
        binding.switchChat.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                SettingsManager.saveInteractiveToMMKV("")
            }else{
                SettingsManager.saveInteractiveToMMKV("close")
            }
        }

    }

    private fun loginOut() {
        showLoadingDialog()

        model.loginOut(success = {
            ToastTool.toastOk(R.string.login_out_ok)
            dismissLoadingDialog()
            HiLog.e(Tag2Common.TAG_12302, "loginOut 成功: $it")
            finish()
            Router.toLoginActivity {  }
            EventManager.postSticky(EventKey.LOGIN_OUT, 0)
        }, failed = { code, msg ->
            dismissLoadingDialog()
            HiLog.e(Tag2Common.TAG_12302, "loginOut 失败: $code = $msg")
        })
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

}
