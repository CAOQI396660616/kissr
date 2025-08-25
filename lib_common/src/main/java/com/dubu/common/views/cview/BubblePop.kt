package com.dubu.common.views.cview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.dubu.common.R

/**
 * Author:v
 * Time:2021/7/22
 */
class BubblePop private constructor() {

    @IntDef(SHORT, LONG)
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Duration

    companion object {
        const val SHORT = 0
        const val LONG = 1
        const val TAG = "Bubble"


        @JvmStatic
        val instance = Holder.holder
    }

    private object Holder {
        val holder = BubblePop()
    }

    private var mWorker: BubbleWorker? = null


    fun showText(context: Context, msg: String, @Duration duration: Int = SHORT) {
        showIcon(context, BubbleWorker.TYPE_TEXT, msg, duration)
    }

    fun showWarn(context: Context, msg: String, @Duration duration: Int = SHORT) {
        showIcon(context, BubbleWorker.TYPE_WARN, msg, duration)
    }

    fun showError(context: Context, msg: String, @Duration duration: Int = LONG) {
        showIcon(context, BubbleWorker.TYPE_ERROR, msg, duration)
    }

    fun showCheck(context: Context, msg: String, @Duration duration: Int = SHORT) {
        showIcon(context, BubbleWorker.TYPE_CHECK, msg, duration)
    }

    fun showText(context: Context, @StringRes res: Int, @Duration duration: Int = SHORT) {
        showIcon(context, BubbleWorker.TYPE_TEXT, context.getString(res), duration)
    }

    fun showWarn(context: Context, @StringRes res: Int, @Duration duration: Int = SHORT) {
        showIcon(context, BubbleWorker.TYPE_WARN, context.getString(res), duration)
    }

    fun showError(context: Context, @StringRes res: Int, @Duration duration: Int = LONG) {
        showIcon(context, BubbleWorker.TYPE_ERROR, context.getString(res), duration)
    }

    fun showCheck(context: Context, @StringRes res: Int, @Duration duration: Int = SHORT) {
        showIcon(context, BubbleWorker.TYPE_CHECK, context.getString(res), duration)
    }



    // TODO: remove automatically with fragment's life cycle
    fun removeNow() {
        mWorker?.let {
            it.destroy()
            it.listener = null
            mWorker = null
        }
    }


    private fun showIcon(context: Context, type: Byte, msg: String, @Duration duration: Int) {
        if (mWorker == null && !hireWorker(context)) {
            return
        }

        mWorker!!.showBubble(msg, type, duration)
    }

    private fun hireWorker(context: Context): Boolean {
        mWorker = BubbleWorker.createWorker(context)

        if (mWorker == null) {
            Log.e(BubblePop.TAG, "Hire BubbleWorker Failed")
            return false
        }

        if (context is AppCompatActivity) {
            addLifeCycleObserver(context)
        }

        addWorkerStateListener()
        return true
    }

    private fun addLifeCycleObserver(activity: AppCompatActivity) {
        activity.lifecycle.also { l ->
            l.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    /*  if (event == Lifecycle.Event.ON_STOP) {
                          Log.d(TAG, "on activity stop")
                          mWorker?.destroy()
                          l.removeObserver(this)
                      } else*/ if (event == Lifecycle.Event.ON_DESTROY) {
//                        Log.d(TAG, "on activity destroy")
                        mWorker?.destroy()
                        mWorker = null
                        l.removeObserver(this)
                    }
                }
            })
        }
    }

    private fun addWorkerStateListener() {
        mWorker?.listener = object : WorkerStateListener {
            override fun onReset() {
                //nothing
            }

            override fun onDestroy() {
                mWorker?.let {
//                    Log.e(TAG, "onWorkerState Destroy")
                    it.destroy()
                    it.listener = null
                    mWorker = null
                }
            }
        }
    }

}


private class BubbleWorker {

    var listener: WorkerStateListener? = null
    private var manager: WindowManager
    private var context: Context
    private var animator: ObjectAnimator? = null

    private var bubbleView: View? = null
    private var tvMsg: DrawableTextView? = null


    private val messageList: ArrayList<BubbleMessage> = ArrayList(MAX_MSG_SIZE)
    private var state = STATE_WAITING
    private var lastShowedType: Byte = TYPE_TEXT


    companion object {
        const val TYPE_WARN: Byte = 0
        const val TYPE_TEXT: Byte = 1
        const val TYPE_ERROR: Byte = 2
        const val TYPE_CHECK: Byte = 3

        private const val DURATION_START_ANIM = 800L
        private const val DURATION_SHOW_SHORT = 1500L
        private const val DURATION_SHOW_LONG = 2500L
        private const val DURATION_HIDE_ANIM = 500L
        private const val TIME_RESET = 4_000L
        private const val MAX_MSG_SIZE = 5

        private const val WHAT_SHOW = 0x1
        private const val WHAT_HIDE = 0x2
        private const val WHAT_HID = 0x3
        private const val WHAT_RESET = 0x4
        private const val WHAT_DESTROY = 0x5


        private const val STATE_SHOWING: Byte = 0
        private const val STATE_HIDING: Byte = 1
        private const val STATE_HIDDEN: Byte = 2
        private const val STATE_DETACH: Byte = 3
        private const val STATE_WAITING: Byte = 4


        fun createWorker(context: Context): BubbleWorker? {
            val manager = try {
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } ?: return null
            return BubbleWorker(manager, context)
        }

    }


    private constructor(manager: WindowManager, context: Context) {
        this.manager = manager
        this.context = context
    }

    private var handler: Handler? = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                WHAT_SHOW -> {
                    show()
                }

                WHAT_HIDE -> {
                    state = STATE_HIDING
                    startHideAnimation()
                }

                WHAT_HID -> {
                    if (messageList.size > 0) {
                        state = STATE_WAITING
                        sendEmptyMessage(WHAT_SHOW)
                    } else {
                        state = STATE_HIDDEN
                        sendEmptyMessageDelayed(WHAT_RESET, TIME_RESET)
                    }
                }

                WHAT_RESET -> {
                    reset()
                }
                WHAT_DESTROY -> {
                    listener?.onDestroy()
                }
            }
        }
    }

    private fun show() {
        if (state == STATE_SHOWING || state == STATE_HIDING) {
            Log.d(BubblePop.TAG, "last bubble working,state=$state")
            return
        }

        state = STATE_SHOWING
        var data: BubbleMessage? = null
        synchronized(messageList) {
            if (messageList.size > 0) {
                data = messageList.removeAt(0)
            }
        }

        if (data == null) {
            hideTemporarily()
            return
        }

        loadContent(data!!.msg, data!!.type)
        startShowAnimation()
        scheduleHide(data!!.duration)
    }


    private fun loadContent(msg: String, type: Byte) {
//        Log.d(Bubble.TAG, "loadContent :$msg")

        tvMsg?.run {
            if (type == TYPE_TEXT) {
                if (lastShowedType != TYPE_TEXT) {
                    clearDrawable()
                }
            } else {
                changeIcon(type, this)
            }
            text = msg
        }

        lastShowedType = type
    }



    private fun scheduleHide(@BubblePop.Duration durationType: Int) {
        val duration = if (durationType == BubblePop.SHORT) {
            DURATION_SHOW_SHORT
        } else DURATION_SHOW_LONG
        handler?.sendEmptyMessageDelayed(WHAT_HIDE, duration)
    }


    private fun getParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            format = PixelFormat.TRANSLUCENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            title = "Toast"
            flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            gravity = Gravity.TOP
            verticalMargin = 0.2f
        }
    }


    fun showBubble(msg: String, type: Byte, @BubblePop.Duration duration: Int) {
        enqueueMessage(msg, type, duration)

        if (bubbleView == null) {
            if (!tryInitView()) {
                Log.e(BubblePop.TAG, "try add BubbleView to window Failed,see log before")
                return
            }
            handler?.sendEmptyMessage(WHAT_SHOW)
        } else {
            when (state) {
                STATE_HIDING -> {
                }
                STATE_WAITING -> {
                }
                STATE_HIDDEN -> {
                    handler?.removeMessages(WHAT_RESET)
                    handler?.removeMessages(WHAT_DESTROY)
                    handler?.sendEmptyMessage(WHAT_SHOW)
                }
                STATE_DETACH -> {
                    handler?.removeMessages(WHAT_DESTROY)

                    attach()
                    handler?.sendEmptyMessage(WHAT_SHOW)
                }
                else -> {
                }
            }
        }
    }


    private fun tryInitView(): Boolean {
        LayoutInflater.from(context).inflate(R.layout.layout_bubble, null).also { view ->
            bubbleView = view
            tvMsg = view.findViewById(R.id.bubble_text)
            view.alpha = 0.0f
        }
        return attach()
    }


    private fun attach() = try {
        manager.addView(bubbleView, getParams())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }


    private fun changeIcon(type: Byte, iv: DrawableTextView) {
        when (type) {
            TYPE_CHECK -> {
                iv.setDrawableLeft(R.drawable.ic_bubble_check)
            }
            TYPE_ERROR -> {
                iv.setDrawableLeft(R.drawable.ic_bubble_fail)
            }
            TYPE_WARN -> {
                iv.setDrawableLeft(R.drawable.ic_bubble_warn)
            }
        }
    }


    /**
     * put message in queue
     * NOTE if there are more than [MAX_MSG_SIZE] messages
     * only the last message will be show
     */
    fun enqueueMessage(msg: String, type: Byte, @BubblePop.Duration duration: Int) {
        synchronized(messageList) {
            if (messageList.size >= MAX_MSG_SIZE) {
                messageList.removeLast()
            }
            messageList.add(BubbleMessage(msg, type, duration))
        }
    }


    private fun startShowAnimation() {
        animator = ObjectAnimator.ofFloat(bubbleView!!, View.ALPHA, 0f, 1f).also {
            it.duration = DURATION_START_ANIM
            it.interpolator = DecelerateInterpolator()
        }
//        handler?.sendEmptyMessageDelayed(WHAT_SHOW, DURATION_START_ANIM)
        animator!!.start()
    }


    fun startHideAnimation() {
        bubbleView?.let { view ->
            animator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).apply {
                duration = if (messageList.size > 0) {
                    DURATION_HIDE_ANIM / 2
                } else DURATION_HIDE_ANIM

                interpolator = DecelerateInterpolator()
                start()
                handler?.sendEmptyMessageDelayed(WHAT_HID, duration)
            }
        }
    }


    fun hideTemporarily() {
        state = STATE_HIDING
        bubbleView?.alpha = 0f
        handler?.sendEmptyMessageDelayed(WHAT_HID, DURATION_HIDE_ANIM)
    }

    fun hasMsgToShow(): Boolean {
        synchronized(messageList) {
            if (messageList.size > 0) {
                handler?.sendEmptyMessage(WHAT_SHOW)
                return true
            } else {
                return false
            }
        }
    }


    fun reset() {
        if (hasMsgToShow()) {
            return
        }
        synchronized(this) {
            bubbleView?.let { view ->
                try {
                    state = STATE_DETACH
                    manager.removeView(view)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            listener?.onReset()
            handler?.sendEmptyMessageDelayed(WHAT_DESTROY, TIME_RESET * 2)
        }
    }

    /**
     * destroy,no bubble will show anymore
     */
    fun destroy() {
        synchronized(this) {
            handler?.let {
                it.removeCallbacksAndMessages(null)
                handler = null
            }

            animator?.let {
                it.cancel()
                animator = null
            }

            bubbleView?.let { view ->
                try {
                    if (view.isAttachedToWindow) {
                        manager.removeViewImmediate(view)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                bubbleView = null
            }
        }
    }
}

private data class BubbleMessage(
    var msg: String,
    var type: Byte,
    @BubblePop.Duration var duration: Int
)

private interface WorkerStateListener {
    fun onReset()
    fun onDestroy()
}
