package com.dubu.common.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.utils.screenHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var mContext: Context? = null
    private var havePeekHeight = false
    var bottomSheetHeight: Int = 0
    private var bottomSheet: FrameLayout? = null
    var behavior: BottomSheetBehavior<FrameLayout>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        val view = inflater.inflate(getLayoutId(), container, false)
        bottomSheetHeight = screenHeight / 10 * 4
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)
        loadData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetStyle)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return bottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        dialog?.window?.findViewById<View>(R.id.design_bottom_sheet)?.background = ColorDrawable(Color.TRANSPARENT)
        bottomSheet = dialog?.delegate?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams = bottomSheet?.layoutParams as CoordinatorLayout.LayoutParams
            if (havePeekHeight) {
                layoutParams.height = bottomSheetHeight
            }
            //            else {
            //                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            //            }
            bottomSheet?.layoutParams = layoutParams
            behavior = BottomSheetBehavior.from<FrameLayout>(bottomSheet!!)
            if (havePeekHeight) {
                behavior?.peekHeight = bottomSheetHeight
            }
            //            else {
            //                behavior?.peekHeight = ScreenUtil.getScreenHeight(mContext)
            //            }
            // 初始为展开状态
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
    }


    /**
     * 设置弹出高度
     */
    fun setPeekHeight(height: Int = screenHeight / 7 * 6) {
        havePeekHeight = true
        bottomSheetHeight = height
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initialize(view: View)

    abstract fun loadData()

}