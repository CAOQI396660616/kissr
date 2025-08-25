package com.dubu.rtc.dialogs

import android.view.View
import android.widget.TextView
import com.dubu.common.R
import com.dubu.common.dialog.BaseBottomDialog
import com.dubu.common.utils.TextViewUtils
import com.dubu.common.views.tv.ToggleBoldTextView


/**
 * 评级
 * @author cq
 * @date 2025/06/12
 * @constructor 创建[LvDialog]
 */
class LvDialog : BaseBottomDialog() {
    override val TAG: String = "EditTextDialog"


    private var onActionChoose: ((action: String) -> Unit)? = null


    override fun getLayoutId(): Int {
        return R.layout.dialog_lv
    }


    fun withTypeChooseListener(listener: ((action: String) -> Unit)): LvDialog {
        onActionChoose = listener
        return this
    }


    override fun initView(root: View) {


        root.findViewById<View>(R.id.ivClose).setOnClickListener {
            dismiss()
        }

        root.findViewById<TextView>(R.id.tvTitle).apply {
            TextViewUtils.setMidBold(this)
        }
        root.findViewById<TextView>(R.id.tv1).apply {
            TextViewUtils.setMidBold(this)
        }

        root.findViewById<TextView>(R.id.tv6).apply {
            TextViewUtils.setMidBold(this)
        }

        root.findViewById<ToggleBoldTextView>(R.id.tv2).apply {
            setMixedText(Pair(getString(R.string.dialog_lv_connection_rate), true),Pair(getString(R.string.dialog_lv_connection_rate_tip), false))
        }
        root.findViewById<ToggleBoldTextView>(R.id.tv3).apply {
            setMixedText(Pair(getString(R.string.dialog_lv_num_calls), true),Pair(getString(R.string.dialog_lv_num_calls_tip), false))
        }
        root.findViewById<ToggleBoldTextView>(R.id.tv4).apply {
            setMixedText(Pair(getString(R.string.dialog_lv_average_call_duration), true),Pair(getString(R.string.dialog_lv_average_call_duration_tip), false))
        }
        root.findViewById<ToggleBoldTextView>(R.id.tv5).apply {
            // 使用示例
            setMixedText(Pair(getString(R.string.dialog_lv_report_num), true),Pair(getString(R.string.dialog_lv_report_num_tip), false))
        }


    }


}