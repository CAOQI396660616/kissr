package com.dubu.me.dialogs

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.R
import com.dubu.common.dialog.BaseBottomDialog
import com.dubu.common.beans.me.KolServiceBean

/**
 * 编辑服务Dialog
 * @author cq
 * @date 2025/01/01
 */
class BottomEditServiceDialog : BaseBottomDialog() {
    override val TAG: String = "BottomEditServiceDialog"

    private var etDes: EditText? = null
    private var etDesSum: EditText? = null
    private var tvDesSum: TextView? = null
    private var tvNext: TextView? = null
    private var ivClose: View? = null
    private var tvSerOne: TextView? = null
    private var tvSerTwo: TextView? = null

    private var operateListener: ((serviceName: String, servicePrice: String) -> Unit)? = null
    private var kolServiceBean: KolServiceBean? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_service
    }

    override fun initView(root: View) {
        super.initView(root)
        
        etDes = root.findViewById(R.id.etDes)
        etDesSum = root.findViewById(R.id.etDesSum)
        tvDesSum = root.findViewById(R.id.tvDesSum)
        tvNext = root.findViewById(R.id.tvNext)
        ivClose = root.findViewById(R.id.ivClose)
        tvSerOne = root.findViewById(R.id.tvSerOne)
        tvSerTwo = root.findViewById(R.id.tvSerTwo)

        initClick()
        initTextWatcher()
        
        // 如果有数据，填充到界面
        kolServiceBean?.let { bean ->
            etDes?.setText(bean.service_name ?: "")
            etDesSum?.setText(bean.gold_price?.toString() ?: "")
        }
    }

    private fun initClick() {
        ClickUtils.applySingleDebouncing(ivClose) {
            dismiss()
        }

        ClickUtils.applySingleDebouncing(tvNext) {
            val serviceName = etDes?.text?.toString()?.trim() ?: ""
            val servicePrice = etDesSum?.text?.toString()?.trim() ?: ""
            
            if (serviceName.isEmpty()) {
                // 可以添加toast提示
                return@applySingleDebouncing
            }
            
            if (servicePrice.isEmpty()) {
                // 可以添加toast提示
                return@applySingleDebouncing
            }
            
            operateListener?.invoke(serviceName, servicePrice)
            dismiss()
        }

        ClickUtils.applySingleDebouncing(tvSerOne) {
            etDes?.setText(tvSerOne?.text?.toString() ?: "")
        }

        ClickUtils.applySingleDebouncing(tvSerTwo) {
            etDes?.setText(tvSerTwo?.text?.toString() ?: "")
        }
    }

    private fun initTextWatcher() {
        etDes?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                tvDesSum?.text = "$length/50"
            }
        })
    }

    fun setOperateListener(listener: (serviceName: String, servicePrice: String) -> Unit) {
        this.operateListener = listener
    }

    fun setData(bean: KolServiceBean) {
        this.kolServiceBean = bean
    }
}