package com.dubu.me.dialogs

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.dubu.common.R
import com.dubu.common.dialog.BaseBottomDialog
import com.ruffian.library.widget.RTextView

class BottomEditTextDialog : BaseBottomDialog() {
    override val TAG: String = "BottomEditTextDialog"
    
    private var onConfirmListener: ((text: String) -> Unit)? = null
    private var titleText: String = ""
    private var hintText: String = ""
    private var initialText: String = ""
    private var maxLength: Int = 15
    
    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_text
    }
    
    fun setTitle(title: String): BottomEditTextDialog {
        titleText = title
        return this
    }
    
    fun setHint(hint: String): BottomEditTextDialog {
        hintText = hint
        return this
    }
    
    fun setInitialText(text: String): BottomEditTextDialog {
        initialText = text
        return this
    }
    
    fun setMaxLength(length: Int): BottomEditTextDialog {
        maxLength = length
        return this
    }
    
    fun setOnConfirmListener(listener: (text: String) -> Unit): BottomEditTextDialog {
        onConfirmListener = listener
        return this
    }
    
    override fun initView(root: View) {
        val tvTitle = root.findViewById<TextView>(R.id.tvTitle)
        val etDes = root.findViewById<EditText>(R.id.etDes)
        val tvDesSum = root.findViewById<TextView>(R.id.tvDesSum)
        val tvNext = root.findViewById<RTextView>(R.id.tvNext)
        val ivClose = root.findViewById<View>(R.id.ivClose)
        
        // 设置标题
        if (titleText.isNotEmpty()) {
            tvTitle.text = titleText
        }
        
        // 设置提示文本
        if (hintText.isNotEmpty()) {
            etDes.hint = hintText
        }
        
        // 设置初始文本
        if (initialText.isNotEmpty()) {
            etDes.setText(initialText)
            etDes.setSelection(initialText.length)
        }
        
        // 设置最大长度
        etDes.filters = arrayOf(android.text.InputFilter.LengthFilter(maxLength))
        
        // 更新字符计数
        updateCharCount(etDes.text.toString(), tvDesSum)
        
        // 文本变化监听
        etDes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                updateCharCount(s?.toString() ?: "", tvDesSum)
            }
        })
        
        // 确认按钮点击
        tvNext.setOnClickListener {
            val text = etDes.text.toString().trim()
            onConfirmListener?.invoke(text)
            dismiss()
        }
        
        // 关闭按钮点击
        ivClose.setOnClickListener {
            dismiss()
        }
    }
    
    private fun updateCharCount(text: String, tvDesSum: TextView) {
        tvDesSum.text = "${text.length}/$maxLength"
    }
}