package com.dubu.rtc.measure

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.dubu.common.constant.Tag2Common
import com.dubu.common.utils.HiLog

class KeyBoardHeightHelper(private val activity: Activity) {

    companion object {
        private const val TAG = "KeyBoardHeightHelper"
        private var keyBoardHeight = 0
        private const val KEYBOARD_VISIBLE_THRESHOLD_DP = 100 // 键盘可见的最小高度阈值
    }

    private var rootView: View? = null
    private var listener: OnMeasureCompleteListener? = null
    private var initialHeight = -1
    private var isKeyboardShowing = false
    private var keyboardVisibleThreshold = 0
    private var lastKeyboardHeight = 0
    private var consecutiveSameHeightCount = 0
    private var isHarmonyOS = false

    interface OnMeasureCompleteListener {
        fun onKeyBoardShow(keyBoardHeight: Int)
        fun onKeyBoardHide()
    }

    init {
        // 检测是否是鸿蒙系统
        isHarmonyOS = false
    }

    fun setOnMeasureCompleteListener(listener: OnMeasureCompleteListener) {
        this.listener = listener
    }

    fun start() {
        rootView = activity.window.decorView
        keyboardVisibleThreshold = (KEYBOARD_VISIBLE_THRESHOLD_DP * activity.resources.displayMetrics.density).toInt()

        // 鸿蒙系统使用不同的监听策略
        if (isHarmonyOS) {
            setupHarmonyOSListener()
        } else {
            setupStandardListener()
        }
    }

    private fun setupStandardListener() {
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun setupHarmonyOSListener() {
        // 鸿蒙系统需要更频繁的监听
        rootView?.viewTreeObserver?.addOnDrawListener(drawListener)

        // 额外添加焦点变化监听作为补充
        rootView?.viewTreeObserver?.addOnWindowFocusChangeListener { hasFocus ->
//            if (hasFocus) {
                checkKeyboardHeight()
//            }
        }
    }

    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        HiLog.e(Tag2Common.TAG_12301, "globalLayoutListener = globalLayoutListener")
        checkKeyboardHeight()
    }

    private val drawListener = ViewTreeObserver.OnDrawListener {
        HiLog.e(Tag2Common.TAG_12301, "drawListener = drawListener")
        // 鸿蒙系统在onDraw中检查键盘状态
        checkKeyboardHeight()
    }

    private fun checkKeyboardHeight() {
        val rect = Rect()
        rootView?.getWindowVisibleDisplayFrame(rect)

        val screenHeight = rootView?.height ?: 0
        val statusBarHeight = rect.top
        val visibleHeight = rect.bottom - statusBarHeight
        val keyboardHeight = screenHeight - visibleHeight - statusBarHeight
        HiLog.e(Tag2Common.TAG_12301, "111 $screenHeight $statusBarHeight $visibleHeight $keyboardHeight")
        // 鸿蒙系统特殊处理：检测高度是否稳定
        if (isHarmonyOS) {
            handleHarmonyOSKeyboard(keyboardHeight, screenHeight)
            return
        }

        // 标准处理逻辑
        handleStandardKeyboard(keyboardHeight, screenHeight)
    }

    private fun handleStandardKeyboard(keyboardHeight: Int, screenHeight: Int) {
        if (initialHeight == -1) {
            initialHeight = screenHeight
            return
        }

        // 键盘显示逻辑
        if (keyboardHeight > keyboardVisibleThreshold) {
            if (!isKeyboardShowing || lastKeyboardHeight != keyboardHeight) {
                isKeyboardShowing = true
                keyBoardHeight = keyboardHeight
                lastKeyboardHeight = keyboardHeight
                listener?.onKeyBoardShow(keyboardHeight)
            }
        }
        // 键盘隐藏逻辑
        else if (keyboardHeight <= keyboardVisibleThreshold) {
            if (isKeyboardShowing) {
                isKeyboardShowing = false
                listener?.onKeyBoardHide()
            }
        }
    }

    private fun handleHarmonyOSKeyboard(keyboardHeight: Int, screenHeight: Int) {
        HiLog.e(Tag2Common.TAG_12301, "222 $initialHeight")
        if (initialHeight == -1) {
            initialHeight = screenHeight
            return
        }

        // 鸿蒙系统：检测高度变化趋势
        if (keyboardHeight == lastKeyboardHeight) {
            consecutiveSameHeightCount++
        } else {
            consecutiveSameHeightCount = 0
            lastKeyboardHeight = keyboardHeight
        }

        // 鸿蒙系统：需要连续多次相同高度才确认状态
        val isKeyboardVisible = keyboardHeight > keyboardVisibleThreshold
        val isStableHeight = consecutiveSameHeightCount > 2
        HiLog.e(Tag2Common.TAG_12301, "222 $isKeyboardVisible ")
        HiLog.e(Tag2Common.TAG_12301, "222 $isStableHeight")
        when {
            // 键盘稳定显示
            isKeyboardVisible && isStableHeight -> {
                if (!isKeyboardShowing || keyBoardHeight != keyboardHeight) {
                    isKeyboardShowing = true
                    keyBoardHeight = keyboardHeight
                    listener?.onKeyBoardShow(keyboardHeight)
                }
            }

            // 键盘稳定隐藏
            !isKeyboardVisible && isStableHeight -> {
                if (isKeyboardShowing) {
                    isKeyboardShowing = false
                    listener?.onKeyBoardHide()
                }
            }

            // 键盘状态变化中（不触发回调）
            else -> {
                // 不稳定的状态，等待稳定
            }
        }
    }

    fun stop() {
        rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(globalLayoutListener)
        rootView?.viewTreeObserver?.removeOnDrawListener(drawListener)
        rootView = null
        listener = null
    }

    fun getKeyBoardHeight(): Int {
        return keyBoardHeight
    }

    // 辅助方法：强制刷新键盘状态
    fun forceCheckKeyboardState() {
        checkKeyboardHeight()
    }
}