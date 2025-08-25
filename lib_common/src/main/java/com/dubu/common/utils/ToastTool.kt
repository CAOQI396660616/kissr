package com.dubu.common.utils

import android.view.Gravity
import androidx.annotation.StringRes
import com.dubu.common.BuildConfig
import com.dubu.common.R
import com.hjq.toast.Toaster

object ToastTool {

    fun toast(@StringRes txt: Int) {
        Toaster.show(txt)
    }

    fun toast(txt: String) {
        Toaster.show(txt)
    }

    fun toastOk(@StringRes txt: Int) {
        Toaster.show(txt)
    }

    fun toastOk(txt: String) {
        Toaster.show(txt)

    }

    fun toastError(@StringRes txt: Int) {
        Toaster.show(txt)

    }

    fun toastError(txt: String) {
        Toaster.show(txt)
    }

    fun toastWhenDebug(txt: String) {
        if (BuildConfig.DEBUG)
            Toaster.show(txt)
    }
    fun toastWhenDebug(@StringRes txt: Int) {
        Toaster.show(txt)
    }

    private fun customGlobalToastStyle() {
        Toaster.setView(R.layout.toast_common)
        Toaster.setGravity(Gravity.CENTER)
    }

}