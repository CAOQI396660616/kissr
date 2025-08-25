package com.dubu.common.utils

import android.content.pm.PackageManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field


object CommonTool {


    fun isPackageInstalled(packageName: String?, packageManager: PackageManager): Boolean {
        if (packageName.isNullOrBlank()) return false
        return try {
            packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    fun changeViewPager2TouchSlop(vp2: ViewPager2, ratio: Int = 5) {
        //动态设置ViewPager2 灵敏度
        try {
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(vp2) as RecyclerView
            val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * ratio) //6 is empirical value
        } catch (ignore: java.lang.Exception) {
        }
    }


}