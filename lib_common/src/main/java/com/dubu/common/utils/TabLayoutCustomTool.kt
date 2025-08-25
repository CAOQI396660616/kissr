package com.dubu.common.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.dubu.common.R

/**
 * Author:v
 * Time:2023/5/12
 */
object TabLayoutCustomTool {

    fun setupBoldCustomView(inflater: LayoutInflater, title: String, tab: TabLayout.Tab) {
        if (tab.customView == null) {
            val cv = inflater.inflate(R.layout.layout_tabview_home, null) as ViewGroup
            tab.customView = cv
            (cv.getChildAt(0) as TextView).text = title
            (cv.getChildAt(1) as TextView).text = title
            (cv.parent as? View)?.background = null
        } else {
            (tab.customView as? ViewGroup)?.let { fl ->
                (fl.getChildAt(1) as TextView).text = title
                (fl.getChildAt(1) as TextView).text = title
            }
        }
    }

    inline fun setupNormalCustomView(
        inflater: LayoutInflater,
        tab: TabLayout.Tab,
        selectApply: TextView.() -> Unit,
        unselectApply: TextView.() -> Unit
    ) {
        if (tab.customView == null) {
            val cv = inflater.inflate(R.layout.layout_tabview_sub, null) as ViewGroup
            tab.customView = cv
            (cv.getChildAt(0) as TextView).apply(selectApply)
            (cv.getChildAt(1) as TextView).apply(unselectApply)
            (cv.parent as? View)?.background = null
        } else {
            (tab.customView as? ViewGroup)?.let { fl ->
                (fl.getChildAt(0) as TextView).apply(selectApply)
                (fl.getChildAt(1) as TextView).apply(unselectApply)
            }
        }
    }


    fun selectTab(tab: TabLayout.Tab?) {
        (tab?.customView as? ViewGroup)?.let { fl ->
            fl.getChildAt(1).visibility = View.INVISIBLE
            fl.getChildAt(0).visibility = View.VISIBLE
        }
    }

    fun unselectTab(tab: TabLayout.Tab?) {
        (tab?.customView as? ViewGroup)?.let { fl ->
            fl.getChildAt(0).visibility = View.INVISIBLE
            fl.getChildAt(1).visibility = View.VISIBLE
        }
    }


    fun setupMessageCustomView(inflater: LayoutInflater, title: String, tab: TabLayout.Tab) {
        if (tab.customView == null) {
            val cv = inflater.inflate(R.layout.layout_tabview_short, null) as ViewGroup
            tab.customView = cv
            (cv.getChildAt(0) as TextView).text = title
            (cv.getChildAt(1) as TextView).text = title
            (cv.parent as? View)?.background = null
        } else {
            (tab.customView as? ViewGroup)?.let { fl ->
                (fl.getChildAt(1) as TextView).text = title
                (fl.getChildAt(1) as TextView).text = title
            }
        }
    }

    fun setupWalletView(inflater: LayoutInflater, title: String, tab: TabLayout.Tab) {
        if (tab.customView == null) {
            val cv = inflater.inflate(R.layout.layout_tabview_wallet, null) as ViewGroup
            tab.customView = cv
            (cv.getChildAt(0) as TextView).text = title
            (cv.getChildAt(1) as TextView).text = title
            (cv.parent as? View)?.background = null
        } else {
            (tab.customView as? ViewGroup)?.let { fl ->
                (fl.getChildAt(1) as TextView).text = title
                (fl.getChildAt(1) as TextView).text = title
            }
        }
    }


    fun setupAccDataView(inflater: LayoutInflater, title: String, tab: TabLayout.Tab) {
        if (tab.customView == null) {
            val cv = inflater.inflate(R.layout.layout_tabview_acc, null) as ViewGroup
            tab.customView = cv
            (cv.getChildAt(0) as TextView).text = title
            (cv.getChildAt(1) as TextView).text = title
            (cv.parent as? View)?.background = null
        } else {
            (tab.customView as? ViewGroup)?.let { fl ->
                (fl.getChildAt(1) as TextView).text = title
                (fl.getChildAt(1) as TextView).text = title
            }
        }
    }


    fun setupGiftNewCustomView(inflater: LayoutInflater, title: String, tab: TabLayout.Tab) {
        if (tab.customView == null) {
            val cv = inflater.inflate(R.layout.layout_tabview_gift_new, null) as ViewGroup
            tab.customView = cv
            (cv.getChildAt(0) as TextView).text = title
            (cv.getChildAt(1) as TextView).text = title
            (cv.parent as? View)?.background = null
        } else {
            (tab.customView as? ViewGroup)?.let { fl ->
                (fl.getChildAt(1) as TextView).text = title
                (fl.getChildAt(1) as TextView).text = title
            }
        }
    }
}