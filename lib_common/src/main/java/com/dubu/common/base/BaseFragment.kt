package com.dubu.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.dubu.common.dialog.LoadingDialog
import com.dubu.common.views.cview.BubblePop

abstract class BaseFragment : Fragment() {
    private var loadingDialog: LoadingDialog? = null

    protected var isActivityParent = true

    protected var visibleCurRecord = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getRootViewId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isActivityParent = parentFragment == null
        val visible = savedInstanceState?.getBoolean("visible_cur_record", false) ?: false
        if (visible) setCurVisible(true)
        onViewCreated(view)
    }


    open fun getRootViewId(): Int = 0
    open fun onViewCreated(root: View) {}


    override fun onResume() {
        super.onResume()
        if (isActivityParent && visibleCurRecord) {
            onVisibleStateChanged(true)
            dispatchVisible(visible = true, false)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isActivityParent && visibleCurRecord) {
            onVisibleStateChanged(false)
            dispatchVisible(visible = false, false)
        }
    }

    fun setCurVisible(visible: Boolean) {
        this.visibleCurRecord = visible
    }

    open fun changeVisible(visible: Boolean) {
        this.visibleCurRecord = visible
        onVisibleStateChanged(visible)
        dispatchVisible(visible, false)
    }

    private fun dispatchVisible(visible: Boolean, isChild: Boolean) {
        if (!isAdded) return
        if (isChild) {
            if (visibleCurRecord) {
                onVisibleStateChanged(visible)
            }
        }
        val children = childFragmentManager.fragments
        if (children.isEmpty()) return
        for (i in 0 until children.size) {
            val f = children[i]
            if (f is BaseFragment) {
                f.dispatchVisible(visible, true)
            }
        }
    }

    protected open fun onVisibleStateChanged(visible: Boolean) {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("visible_cur_record", visibleCurRecord)
    }


    fun showLoadingDialog() {
        context?.let {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog()
                    .withCancelOutside(false)
                    .withCancelable(true)
            }
            loadingDialog?.show(parentFragmentManager)
        }
    }


    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroyView() {
        dismissLoadingDialog()
        super.onDestroyView()
        BubblePop.instance.removeNow()
    }


    fun toast(@StringRes id: Int) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        //BubblePop.instance.showText(actThis, id)
        Toast.makeText(actThis,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun toast(txt: String) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        //BubblePop.instance.showText(actThis, txt)
        Toast.makeText(actThis,txt, Toast.LENGTH_SHORT).show()
    }


    fun toastOk(id: Int) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        //BubblePop.instance.showCheck(actThis, id)
        Toast.makeText(actThis,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun toastOk(txt: String) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        //BubblePop.instance.showCheck(actThis, txt)
        Toast.makeText(actThis,txt, Toast.LENGTH_SHORT).show()
    }
    fun toastError(id: Int) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        //BubblePop.instance.showError(actThis, id)
        Toast.makeText(actThis,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun toastError(txt: String) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
       // BubblePop.instance.showError(actThis, txt)
        Toast.makeText(actThis,txt, Toast.LENGTH_SHORT).show()
    }

    fun commonToast(id: Int) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        Toast.makeText(actThis,getString(id), Toast.LENGTH_SHORT).show()
    }

    fun commonToast(txt: String) {
        val actThis = activity
        if (actThis == null || actThis.isFinishing || actThis.isDestroyed) return
        Toast.makeText(actThis,txt, Toast.LENGTH_SHORT).show()
    }

}