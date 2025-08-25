@file:Suppress("DEPRECATION")

package com.dubu.common.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


/**
 * Author:v
 * Time:2022/5/16
 */
object StatusBarTool {
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



    fun setNavigationColor(window: Window, color: Int) {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            navigationBarColor = color
        }
    }


}