package com.dubu.common.manager

import android.view.View
import android.widget.TextView
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.dubu.common.beans.UserBean
import com.dubu.common.utils.DisplayUiTool
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.UiUtils
import com.ruffian.library.widget.RFrameLayout

/*
* 处理登录相关的业务管理器
* */

object UITool {
    private const val STATE_ON = "online"

    fun changeUI(status: String?, textView: TextView) {

        when (status) {

            STATE_ON->{
                textView.setTextColor(UiUtils.getColor(R.color.cl27B94B))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_agree)
            }


            else->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_pending)
            }
        }

    }

}