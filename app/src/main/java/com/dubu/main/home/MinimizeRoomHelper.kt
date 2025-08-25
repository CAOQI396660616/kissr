package com.dubu.main.home

import android.animation.ObjectAnimator
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dubu.common.beans.common.VoiceRoomEventBean
import com.dubu.common.ext.getDimen
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.RotateAnimHelper
import com.dubu.main.R
import java.lang.ref.WeakReference

/**
 * Author:v
 * Time:2023/4/26
 */
class MinimizeRoomHelper {
    private val TAG = MinimizeRoomHelper::class.java.simpleName
    private var rootRef: WeakReference<ViewGroup>? = null
    private var ivAvatar: ImageView? = null
    private var eventInfo: VoiceRoomEventBean? = null
    private var animHelper: RotateAnimHelper? = null
    var detached = false

    fun attachActivity(activity: Activity) {

    }

    fun attachParent(parent: ConstraintLayout): MinimizeRoomHelper {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_minimize_room, null, false) as ViewGroup
        val dp5 = parent.context.getDimen(R.dimen.dp_5)
        parent.addView(layout, dp5 * 25, dp5 * 9)

        layout.apply {
            layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                bottomMargin = context.getDimen(R.dimen.dp_104)
            }

            ivAvatar = findViewById<ImageView>(R.id.iv_avatar)
            ivAvatar?.setOnClickListener {
                enterVoiceRoom()
            }
            findViewById<ImageView>(R.id.iv_volume).also { iv ->
                iv?.isSelected = true
                iv?.setOnClickListener {
                    toggleVoice(iv)
                }
            }
            findViewById<ImageView>(R.id.iv_close).setOnClickListener {
                exitVoiceRoom()
            }

        }
        rootRef = WeakReference(layout)
        return this
    }

    fun loadAvatar(url: String?): MinimizeRoomHelper {
        ivAvatar?.let { iv ->
            GlideTool.loadAvatar(url, iv)
            if (animHelper == null) {
                animHelper = RotateAnimHelper().apply {
                    setup(iv)
                    start()
                }
            }
        }
        return this
    }

    fun withData(info: VoiceRoomEventBean?): MinimizeRoomHelper {
        eventInfo = info
        return this
    }


    fun show(visible: Boolean): MinimizeRoomHelper {
        if (!visible) {
            animHelper?.pauseAnim()
            return this
        }
        rootRef?.get()?.let { vg ->
            ObjectAnimator.ofFloat(vg, View.ALPHA, 0.0f, 0.95f).apply {
                duration = 300L
                interpolator = DecelerateInterpolator()
                start()
            }
        }
        animHelper?.resumeAnim()
        return this
    }


    private fun toggleVoice(iv: ImageView) {
        if (iv.isSelected) {
            iv.isSelected = false
            iv.setImageResource(R.drawable.ic_min_volume_off)
            muteVoice()
        } else {
            iv.isSelected = true
            iv.setImageResource(R.drawable.ic_min_volume_on)
            resumeVoice()
        }
    }


    fun tryEnterVoiceRoom(): Boolean {
        val info = eventInfo ?: return false
        return true
    }


    private fun enterVoiceRoom() {
        eventInfo ?: return
        detach()
    }

    private fun exitVoiceRoom() {
        detach()
    }

    private fun resumeVoice() {
    }

    private fun muteVoice() {
    }

    fun pauseRotate() {
        animHelper?.pauseAnim()
    }

    fun resumeRotate() {
        animHelper?.resumeAnim()
    }


    fun detach() {
        detached = true
        ivAvatar = null
        eventInfo = null
        animHelper?.destory()
        animHelper = null
        rootRef?.run {
            get()?.let { vg ->
                tryAnimExit(vg)
            }
            clear()
            rootRef = null
        }
    }

    private fun tryAnimExit(vg: ViewGroup) {
        ObjectAnimator.ofFloat(vg, View.ALPHA, 0.95f, 0f).apply {
            duration = 500L
            interpolator = DecelerateInterpolator()
            start()
        }
        vg.postDelayed({
            (vg.parent as ViewGroup).removeView(vg)
        }, 510L)
    }
}