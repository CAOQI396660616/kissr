package com.dubu.common.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.dubu.common.R
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.LoadingDialog
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.router.Router
import com.dubu.common.utils.HiLog
import com.dubu.common.views.cview.BubblePop
import com.gyf.immersionbar.ImmersionBar
import com.hjq.language.MultiLanguages
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.utils.ScreenUtils


abstract class BaseActivity : AppCompatActivity() {
    protected var loadingDialog: LoadingDialog? = null


    override fun attachBaseContext(newBase: Context?) {
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(newBase))
    }

    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        // 防止app返回桌面重新打开 新起一个应用
        if (!isTaskRoot) {
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent
                    .ACTION_MAIN
            ) {
                finish()
                return
            }
        }


        onCreateActivity(savedInstanceState)
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            }
        )

        /*if (savedInstanceState != null) {
            //TODO allen  App在后台时，在设置中手动拒绝某项权限，在返回App造成的闪退或者数据异常 https://blog.csdn.net/freekaiye/article/details/125621048
        }*/


        if (isNeedDefaultScreenConfig()) {
            initOptBar()
        }

    }

    open fun onCreateActivity(savedInstanceState: Bundle?) {
        setContentView(getContentLayoutId())
        onCreated()
    }

    /**
     * 屏幕适配
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        AutoSize.autoConvertDensity(this, 375F,true);
        //获取当前屏幕的宽高
//        val screenSize = ScreenUtils.getScreenSize(this)
//        AutoSizeConfig.getInstance().screenWidth = screenSize[0]
//        AutoSizeConfig.getInstance().screenHeight = screenSize[1]
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { //竖屏
//            AutoSize.autoConvertDensityOfGlobal(this)
//        } else { //横屏
//            AutoSize.autoConvertDensityBaseOnHeight(this, 375F)
//        }
    }

    override fun onResume() {
        super.onResume()
        HiLog.e(Tag2Common.TAG_12316, "接口通用校验正常: onResume 111111")
        EventManager.registerNew<Boolean>(EventKey.TOKEN_INVALIDED) {
            Router.toLoginActivity()
            HiLog.e(Tag2Common.TAG_12316, "接口通用校验正常: onResume 222222")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(EventKey.TOKEN_INVALIDED)
    }

    /*
          ╔════════════════════════════════════════════════════════════════════════════════════════╗
          ║   PS: 子类按需覆写初始化方法
          ╚════════════════════════════════════════════════════════════════════════════════════════╝
       */
    open fun preCreate(savedInstanceState: Bundle?) {}

    open fun onCreated() {}

    open fun getContentLayoutId(): Int = 0

    open fun onBack() {
        finish()
    }


    fun toast(@StringRes id: Int) {
        //BubblePop.instance.showText(actThis, id)
        Toast.makeText(this,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun toast(txt: String) {
        //BubblePop.instance.showText(actThis, txt)
        Toast.makeText(this,txt, Toast.LENGTH_SHORT).show()
    }


    fun toastOk(id: Int) {
        //BubblePop.instance.showCheck(actThis, id)
        Toast.makeText(this,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun toastOk(txt: String) {
        //BubblePop.instance.showCheck(actThis, txt)
        Toast.makeText(this,txt, Toast.LENGTH_SHORT).show()
    }
    fun toastError(id: Int) {
        //BubblePop.instance.showError(actThis, id)
        Toast.makeText(this,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun toastError(txt: String) {
        // BubblePop.instance.showError(actThis, txt)
        Toast.makeText(this,txt, Toast.LENGTH_SHORT).show()
    }

    fun commonToast(id: Int) {
        Toast.makeText(this,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun commonToast(txt: String) {
        Toast.makeText(this,txt, Toast.LENGTH_SHORT).show()
    }

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS: 沉浸式状态栏
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    //是否初始化默认的 沉浸式导航栏 全屏 等配置
    open fun isNeedDefaultScreenConfig() = true

    open fun initOptBar() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true) //解决状态栏和布局重叠问题，
            .statusBarDarkFont(true)
            .statusBarColor(R.color.transparent)
            .init()
    }


    /**
     * 沉浸式状态栏
     */
    open fun initOptBar(view: View, isStatusBarDarkFont: Boolean, isKeyboardEnable: Boolean) {
        ImmersionBar.with(this).statusBarView(view).statusBarDarkFont(isStatusBarDarkFont)
            .keyboardEnable(isKeyboardEnable).init()
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS: 通用api
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    /**
     * Activity是否已被销毁
     */
    open fun isActivityOptEnable(): Boolean {
        return !(isDestroyed || isFinishing)
    }


    fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
                .withCancelOutside(false)
                .withCancelable(true)
        }
        loadingDialog?.show(supportFragmentManager)
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

}