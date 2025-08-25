package com.dubu.common.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.dubu.common.utils.StatusBarTool
import com.dubu.rtc.country.CountryInfo
import com.v.wheel.WheelView

/**
 * Author:v
 * Time:2023/4/4
 */
class PickTextDialog : DialogFragment() {
    private val TAG = "PickTextDialog"

    private var title: String? = null
    private var content: String? = null

    private var onDateChooseListener: ((action: String) -> Unit)? = null
    private var mTextList: MutableList<String>? = null

    fun withDateChooseListener(listener: ((date: String) -> Unit)): PickTextDialog {
        onDateChooseListener = listener
        return this
    }

    fun withInitDate(textList: MutableList<String>): PickTextDialog {
        mTextList = textList
        return this
    }


    fun withTitle(title: String): PickTextDialog {
        this.title = title
        return this
    }


    fun withContent(content: String): PickTextDialog {
        this.content = content
        return this
    }


    fun withTitle(title: Int): PickTextDialog {
        this.title = BaseApp.instance.getString(title)
        return this
    }

    fun withContent(content: Int): PickTextDialog {
        this.content = BaseApp.instance.getString(content)
        return this
    }

    override fun onStart() {
        super.onStart()
        setWindow()
    }

    private fun setWindow() {
        dialog?.apply {
            window?.run {
                attributes = attributes.apply {
                    gravity = Gravity.BOTTOM
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }

                StatusBarTool.setNavigationColor(this, Color.WHITE)
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.Theme_HiReal_BottomDialog
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_pick_text, container, false)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {

        val tvTitle: TextView = root.findViewById(R.id.tvTitle)
        val tvContent: TextView = root.findViewById(R.id.tvTitleOther)

        root.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            dismissAllowingStateLoss()
        }

        root.findViewById<View>(R.id.tv_confirm).setOnClickListener {
            onDateChooseListener?.invoke(selectText2)
            dismissAllowingStateLoss()
        }
        initWheelView(root)

        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        } else {
            tvTitle.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(content)) {
            tvContent.text = content
            tvContent.visibility = View.VISIBLE
        }else {
            tvContent.visibility = View.GONE
        }

    }

    private var selectText2: String = ""


    private lateinit var wvText2: WheelView
    private fun initWheelView(root: View) {
        wvText2 = root.findViewById<WheelView>(R.id.wv_month)
        setup(wvText2)
    }

    private fun setup(wvMonth: WheelView) {

        val i = (mTextList?.size ?: 2) / 2

        selectText2 = mTextList?.get(i) ?: ""
        wvMonth.apply {
            mTextList?.let { setupStringItems(it) }
            setCurrentSelect(i)
            setOnItemSelectedListener { item ->
                selectText2 = item.text()
            }
        }
    }


    fun show(manager: FragmentManager) {
        activity?.let {
            if (it.isFinishing || it.isDestroyed) return
        }

        val f = manager.findFragmentByTag(TAG)
        if (f?.isAdded == true) {
            manager.beginTransaction().show(f)
        } else {
            super.show(manager, TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDateChooseListener = null
    }

}