package com.dubu.rtc.dialogs

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.dubu.common.R
import com.dubu.common.dialog.BaseBottomDialog

/**
 * 无用
 *
 * Author:v
 * Time:2020/11/25
 */
class EditTextDialog : BaseBottomDialog() {
    override val TAG: String= "EditTextDialog"


    private var onActionChoose: ((action: String) -> Unit)? = null


    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_text
    }



    fun withTypeChooseListener(listener: ((action: String) -> Unit)): EditTextDialog {
        onActionChoose = listener
        return this
    }


    override fun initView(root: View) {


        root.findViewById<View>(R.id.ivClose).setOnClickListener {
            dismiss()
        }

    }


}