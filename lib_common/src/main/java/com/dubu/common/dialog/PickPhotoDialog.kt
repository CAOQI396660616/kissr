package com.dubu.common.dialog

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.dubu.common.R

/**
 * Author:v
 * Time:2020/11/25
 */
class PickPhotoDialog : BaseBottomDialog() {
    override val TAG: String= "PickPhotoDialog"


    private var onActionChoose: ((action: Int) -> Unit)? = null
    private var cameraText = R.string.open_camera
    private var albumText = R.string.open_album


    override fun getLayoutId(): Int {
        return R.layout.dialog_pick_photo
    }



    fun withTypeChooseListener(listener: ((action: Int) -> Unit)): PickPhotoDialog {
        onActionChoose = listener
        return this
    }

    fun withCameraText(@StringRes id: Int): PickPhotoDialog {
        this.cameraText = id
        return this
    }


    fun withAlbumText(@StringRes id: Int): PickPhotoDialog {
        this.albumText = id
        return this
    }

    override fun initView(root: View) {
        root.findViewById<TextView>(R.id.dd_tv_open_album).apply {
            setText(albumText)
            setOnClickListener {
                onActionChoose?.invoke(0)
                dismiss()
            }
        }

        root.findViewById<TextView>(R.id.dd_tv_open_camera).apply {
            setText(cameraText)
            setOnClickListener {
                onActionChoose?.invoke(1)
                dismiss()
            }
        }

        root.findViewById<View>(R.id.dd_tv_cancel).setOnClickListener {
            dismiss()
        }

    }


}