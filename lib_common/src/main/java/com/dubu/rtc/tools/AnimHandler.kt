package com.dubu.rtc.tools

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.opensource.svgaplayer.SVGAImageView
import java.lang.ref.WeakReference

/*
*  动画处理 Handle
* */
class AnimHandler private constructor() : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            EVENT_TYPE_START_ANIM -> {
                val btUserComeIn = msg.obj as MaterialButton
                LiveRoomAnimUtils.startUserComeInLiveRoomAnim(btUserComeIn)
            }

            EVENT_TYPE_START_SVGA -> {
                val ivSVGA = msg.obj as SVGAImageView
                val tag = ivSVGA.tag as String
                val context = reference?.get()

                if(context is AppCompatActivity){
                    val cont = context as AppCompatActivity
                    SvgaAnimationUtil.showSVGA(tag, ivSVGA,
                        cont
                    )
                }
            }
        }
    }

    companion object {
        //开始动画
        const val EVENT_TYPE_START_ANIM = 10001
        const val EVENT_TYPE_START_SVGA = 10002
        private val HANDLER = AnimHandler()
        fun sharedInstance(): AnimHandler {
            return HANDLER
        }
    }

    private var reference: WeakReference<Context>? = null
    fun setContext(context: Context) {
        reference = WeakReference(context)
    }
}