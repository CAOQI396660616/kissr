package com.dubu.rtc.dialogs

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.dubu.common.R
import com.dubu.common.dialog.BaseBottomDialog


/**
 *
 * 提现公司解绑dialog
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[CashUnbindDialog]
 */
class CashUnbindDialog : BaseBottomDialog() {
    override val TAG: String= "CashUnbindDialog"


    private var onActionChoose: ((action: String) -> Unit)? = null


    override fun getLayoutId(): Int {
        return R.layout.dialog_unbind
    }



    fun withTypeChooseListener(listener: ((action: String) -> Unit)): CashUnbindDialog {
        onActionChoose = listener
        return this
    }


    override fun initView(root: View) {

        root.findViewById<TextView>(R.id.tvTitle).setOnClickListener {
            onActionChoose?.invoke("ss")
        }
        root.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            dismiss()
        }

    }


}