package com.dubu.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field


object FontTool {

    // 为单个 TextView 设置字体
    fun applyFont(context: Context, textView: TextView, fontPath: String) {
        val typeface = FontCache.getTypeface(context, fontPath)
        typeface?.let { textView.typeface = it }
    }


}