package com.opensource.svgaplayer

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.opensource.svgaplayer.utils.SVGARange
import com.opensource.svgaplayer.utils.log.LogUtils
import com.dubu.common.R
import java.lang.ref.WeakReference
import java.net.URL


/**
 * use for recycleView or fragment
 * to auto control play and pause
 * therefore saving a lot of usage
 */
class SvgAnimImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val TAG = "SvgAnimImageView"
    private val FM_BACKWARD = 0x0
    private val FM_FORWARD = 0x1
    private val FM_CLEAR = 0x2


    var isAnimating = false
        private set

    var loops = 0

    var needLifecycleObserver = true
    var autoPlayByTache = true
    var fillMode: Int = FM_FORWARD
    var callback: SVGACallback? = null

    private var mAnimator: ValueAnimator? = null
    private var mItemClickAreaListener: SVGAClickAreaListener? = null
    private var mAntiAlias = true
    private var mAutoPlay = true
    private val mAnimatorListener = AnimatorListener(this)
    private val mAnimatorUpdateListener = AnimatorUpdateListener(this)
    private var mStartFrame = 0
    private var mEndFrame = 0


    init {
        attrs?.let { loadAttrs(it) }
    }

    private fun loadAttrs(attrs: AttributeSet) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SVGAImageView, 0, 0)
        loops = typedArray.getInt(R.styleable.SVGAImageView_loopCount, 0)
        mAntiAlias = typedArray.getBoolean(R.styleable.SVGAImageView_antiAlias, true)
        mAutoPlay = typedArray.getBoolean(R.styleable.SVGAImageView_autoPlay, true)
        typedArray.getString(R.styleable.SVGAImageView_fillMode)?.let {
            when (it) {
                "0" -> {
                    fillMode = FM_BACKWARD
                }

                "1" -> {
                    fillMode = FM_FORWARD
                }

                "2" -> {
                    fillMode = FM_CLEAR
                }
            }
        }
        typedArray.getString(R.styleable.SVGAImageView_source)?.let {
            parserSource(it)
        }
        typedArray.recycle()
    }

    private fun parserSource(source: String) {
        val refImgView = WeakReference<SvgAnimImageView>(this)
        val parser = SVGAParser(context)
        if (source.startsWith("http://") || source.startsWith("https://")) {
            parser.decodeFromURL(URL(source), createParseCompletion(refImgView))
        } else {
            parser.decodeFromAssets(source, createParseCompletion(refImgView))
        }
    }

    private fun createParseCompletion(ref: WeakReference<SvgAnimImageView>): SVGAParser.ParseCompletion {
        return object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                ref.get()?.startAnimation(videoItem)
            }

            override fun onError() {}
        }
    }

    private fun startAnimation(videoItem: SVGAVideoEntity) {
        this@SvgAnimImageView.post {
            videoItem.antiAlias = mAntiAlias
            setVideoItem(videoItem)
            getSVGADrawable()?.scaleType = scaleType
            if (mAutoPlay) {
                startAnimation()
            }
        }
    }

    fun startAnimation() {
        startAnimation(null, false)
    }

    fun startAnimation(range: SVGARange?, reverse: Boolean = false) {
        stopAnimation(false)
        play(range, reverse)
        check2AddLifecycle()
    }

    private fun play(range: SVGARange?, reverse: Boolean) {
        LogUtils.info(TAG, "================ start animation ================")
        val drawable = getSVGADrawable() ?: return
        setupDrawable()
        mStartFrame = Math.max(0, range?.location ?: 0)
        val videoItem = drawable.videoItem
        mEndFrame = Math.min(
            videoItem.frames - 1,
            ((range?.location ?: 0) + (range?.length ?: Int.MAX_VALUE) - 1)
        )
        val animator = ValueAnimator.ofInt(mStartFrame, mEndFrame)
        animator.interpolator = LinearInterpolator()
        animator.duration =
            ((mEndFrame - mStartFrame + 1) * (1000 / videoItem.FPS) / generateScale()).toLong()
        animator.repeatCount = if (loops <= 0) 99999 else loops - 1
        animator.addUpdateListener(mAnimatorUpdateListener)
        animator.addListener(mAnimatorListener)
        if (reverse) {
            animator.reverse()
        } else {
            animator.start()
        }
        mAnimator = animator
    }


    fun hasAnimLoaded() = getSVGADrawable() != null

    private fun check2AddLifecycle() {
        if (!needLifecycleObserver) return
        val ctx = context
        if (ctx !is AppCompatActivity) {
            needLifecycleObserver = false
            return
        }
        needLifecycleObserver = false
        ctx.lifecycle.also { l ->
            l.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_PAUSE) {
                        if (visibility == View.VISIBLE) {
                            pauseAnimation()
                        }
                    } else if (event == Lifecycle.Event.ON_RESUME) {
                        if (visibility == View.VISIBLE) {
                            startAnimation()
                        }
                    } else if (event == Lifecycle.Event.ON_DESTROY) {
                        stopAnimation(true)
                        clear()
                    }
                }
            })
        }
    }


    private fun setupDrawable() {
        val drawable = getSVGADrawable() ?: return
        drawable.cleared = false
        drawable.scaleType = scaleType
    }

    private fun getSVGADrawable(): SVGADrawable? {
        return drawable as? SVGADrawable
    }

    private fun generateScale(): Double {
        var scale = 1.0
        try {
            val animatorClass = Class.forName("android.animation.ValueAnimator") ?: return scale
            val getMethod = animatorClass.getDeclaredMethod("getDurationScale") ?: return scale
            scale = (getMethod.invoke(animatorClass) as Float).toDouble()
            if (scale == 0.0) {
                val setMethod =
                    animatorClass.getDeclaredMethod("setDurationScale", Float::class.java)
                        ?: return scale
                setMethod.isAccessible = true
                setMethod.invoke(animatorClass, 1.0f)
                scale = 1.0
                LogUtils.info(
                    TAG,
                    "The animation duration scale has been reset to" +
                            " 1.0x, because you closed it on developer options."
                )
            }
        } catch (ignore: Exception) {
            ignore.printStackTrace()
        }
        return scale
    }

    private fun onAnimatorUpdate(animator: ValueAnimator?) {
        val drawable = getSVGADrawable() ?: return
        drawable.currentFrame = animator?.animatedValue as Int
        val percentage =
            (drawable.currentFrame + 1).toDouble() / drawable.videoItem.frames.toDouble()
        callback?.onStep(drawable.currentFrame, percentage)
    }

    private fun onAnimationEnd(animation: Animator?) {
        isAnimating = false
        stopAnimation()
        val drawable = getSVGADrawable()
        if (drawable != null) {
            when (fillMode) {
                FM_BACKWARD -> {
                    drawable.currentFrame = mStartFrame
                }

                FM_FORWARD -> {
                    drawable.currentFrame = mEndFrame
                }

                FM_CLEAR -> {
                    drawable.cleared = true
                }
            }
        }
        callback?.onFinished()
    }

    fun clear() {
        getSVGADrawable()?.cleared = true
        getSVGADrawable()?.clear()
        // 清除对 drawable 的引用
        setImageDrawable(null)
    }

    fun pauseAnimation() {
        stopAnimation(false)
        callback?.onPause()
    }

    fun stopAnimation() {
        stopAnimation(false)
    }

    fun stopAnimation(clear: Boolean) {
        mAnimator?.cancel()
        mAnimator?.removeAllListeners()
        mAnimator?.removeAllUpdateListeners()
        getSVGADrawable()?.stop()
        getSVGADrawable()?.cleared = clear
    }

    fun setVideoItem(videoItem: SVGAVideoEntity?) {
        setVideoItem(videoItem, SVGADynamicEntity())
    }

    fun setVideoItem(videoItem: SVGAVideoEntity?, dynamicItem: SVGADynamicEntity?) {
        if (videoItem == null) {
            setImageDrawable(null)
        } else {
            val drawable = SVGADrawable(videoItem, dynamicItem ?: SVGADynamicEntity())
            drawable.cleared = true
            setImageDrawable(drawable)
        }
    }

    fun stepToFrame(frame: Int, andPlay: Boolean) {
        pauseAnimation()
        val drawable = getSVGADrawable() ?: return
        drawable.currentFrame = frame
        if (andPlay) {
            startAnimation()
            mAnimator?.let {
                it.currentPlayTime = (Math.max(
                    0.0f,
                    Math.min(1.0f, (frame.toFloat() / drawable.videoItem.frames.toFloat()))
                ) * it.duration).toLong()
            }
        }
    }

    fun stepToPercentage(percentage: Double, andPlay: Boolean) {
        val drawable = drawable as? SVGADrawable ?: return
        var frame = (drawable.videoItem.frames * percentage).toInt()
        if (frame >= drawable.videoItem.frames && frame > 0) {
            frame = drawable.videoItem.frames - 1
        }
        stepToFrame(frame, andPlay)
    }

    fun setOnAnimKeyClickListener(clickListener: SVGAClickAreaListener) {
        mItemClickAreaListener = clickListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event)
        }
        val drawable = getSVGADrawable() ?: return super.onTouchEvent(event)
        for ((key, value) in drawable.dynamicItem.mClickMap) {
            if (event.x >= value[0] && event.x <= value[2] && event.y >= value[1] && event.y <= value[3]) {
                mItemClickAreaListener?.let {
                    it.onClick(key)
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }


    private class AnimatorListener(view: SvgAnimImageView) : Animator.AnimatorListener {
        private val weakReference = WeakReference<SvgAnimImageView>(view)

        override fun onAnimationRepeat(animation: Animator) {
            weakReference.get()?.callback?.onRepeat()
        }

        override fun onAnimationEnd(animation: Animator) {
            weakReference.get()?.onAnimationEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator) {
            weakReference.get()?.isAnimating = false
        }

        override fun onAnimationStart(animation: Animator) {
            weakReference.get()?.isAnimating = true
        }
    } // end of AnimatorListener


    private class AnimatorUpdateListener(view: SvgAnimImageView) :
        ValueAnimator.AnimatorUpdateListener {
        private val weakReference = WeakReference<SvgAnimImageView>(view)

        override fun onAnimationUpdate(animation: ValueAnimator) {
            weakReference.get()?.onAnimatorUpdate(animation)
        }
    } // end of AnimatorUpdateListener

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            stopAnimation(true)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (getSVGADrawable() != null && autoPlayByTache && !isAnimating) {
            startAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (getSVGADrawable() != null && autoPlayByTache && isAnimating) {
            pauseAnimation()
        }
    }
}