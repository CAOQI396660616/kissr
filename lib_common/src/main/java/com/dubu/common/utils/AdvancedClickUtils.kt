package com.dubu.common.utils

import android.view.View

object AdvancedClickUtils {
    private const val MIN_CLICK_DELAY = 1000L
    private val clickRecords = mutableMapOf<Int, Long>() // 使用ViewID作为key

    fun canClick(view: View): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastClickTime = clickRecords[view.id] ?: 0

        if (currentTime - lastClickTime < MIN_CLICK_DELAY) {
            return false
        }

        clickRecords[view.id] = currentTime
        return true
    }
}
