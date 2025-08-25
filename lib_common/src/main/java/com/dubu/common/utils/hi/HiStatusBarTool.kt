@file:Suppress("DEPRECATION")

package com.dubu.common.utils.hi

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dubu.common.utils.DisplayUiTool
import com.gyf.immersionbar.ImmersionBar


object HiStatusBarTool {
    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS: immersionbar 相关API
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    /**
     * 兼容性获取状态栏高度
     *
     * @return
     */
    fun getCompatOptStatusBarHeight(context: Context): Int {
        return ImmersionBar.getStatusBarHeight(context)
    }


    /**
     * 获取当前底部栏高度（隐藏后高度为0）
     *
     * @param activity
     * @return
     */
    fun getCurrentOptNavigationBarHeight(context: Context):Int{
        return ImmersionBar.getNavigationBarHeight(context)
    }


    //设置状态栏颜色
    fun setStatusBarOptColor(activity: Activity, colorId: Int, isLight: Boolean) {
        ImmersionBar.with(activity).fitsSystemWindows(true).statusBarDarkFont(isLight)
            .statusBarColor(colorId).init()
    }

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS: 常用的沉浸式状态栏API 老项目是用的自己编写的api 新项目主要是用第三方库 immersionbar
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    fun setTransparent(
        window: Window,
        isShowStatusBar: Boolean = true,
        isShowNavigationBar: Boolean = true
    ) {
        var uiOptions: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //设置状态栏颜色为浅色调
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (!isShowStatusBar) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (!isShowNavigationBar) {
                uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                navigationBarColor = Color.TRANSPARENT
            } else {
                navigationBarColor = Color.TRANSPARENT
            }
            decorView.systemUiVisibility = uiOptions
            statusBarColor = Color.TRANSPARENT
        }
    }



    fun hideNavigation(window: Window) {
        ViewCompat.getWindowInsetsController(window.decorView)?.let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
        }

        val uiOptions: Int = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        window.decorView.systemUiVisibility = uiOptions
    }

    //设置状态栏颜色
    fun setStatusBarOptColorIn(activity: Activity, colorId: Int, isLight: Boolean) {
        activity.window.statusBarColor = DisplayUiTool.getColor(colorId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller =
                ViewCompat.getWindowInsetsController(activity.window.decorView)
            controller?.isAppearanceLightStatusBars = isLight
        } else {
            activity.window.decorView.systemUiVisibility = if (isLight) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    fun setNavigationOptColor(window: Window, color: Int) {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            navigationBarColor = color
        }
    }

    fun setLightOptStatusBar(activity: Activity, light: Boolean) {
        val decor: View = activity.window.decorView
        if (light) {
            decor.systemUiVisibility = decor.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            val flags = decor.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            decor.systemUiVisibility = flags or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

}