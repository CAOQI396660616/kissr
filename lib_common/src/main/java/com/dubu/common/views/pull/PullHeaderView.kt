//package com.weiyun.firebirdcommon.views.pull
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.AttributeSet
//import android.view.Gravity
//import android.view.View
//import android.widget.LinearLayout
//import androidx.annotation.NonNull
//import com.airbnb.lottie.LottieAnimationView
//import com.airbnb.lottie.LottieDrawable
//import com.ay.stickermaker.R
//import com.ay.stickermaker.core.tool.getDimen
//import com.scwang.smart.refresh.layout.api.RefreshHeader
//import com.scwang.smart.refresh.layout.api.RefreshKernel
//import com.scwang.smart.refresh.layout.api.RefreshLayout
//import com.scwang.smart.refresh.layout.constant.RefreshState
//import com.scwang.smart.refresh.layout.constant.SpinnerStyle
//
///**
// *rewrite by v
// *@ 2021/2/26
// */
//@SuppressLint("RestrictedApi")
//class PullHeaderView : LinearLayout, RefreshHeader {
//
//    constructor(context: Context) : this(context, null)
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//        context,
//        attrs,
//        defStyleAttr
//    ) {
//        initView(context)
//    }
//
//    private val tag = "PullHeaderView"
//
//    //    private var bar: ProgressBar? = null
//    private var lav: LottieAnimationView? = null
//
//    private fun initView(context: Context) {
//        gravity = Gravity.CENTER
//        val dp35 = context.getDimen(R.dimen.dp25)
//        minimumHeight = dp35
//
//        lav = LottieAnimationView(context).apply {
//            setAnimation("ic_loading.json")
//            repeatCount = LottieDrawable.INFINITE
//        }
//
//        /*   bar = ProgressBar(context).apply {
//               indeterminateTintList = ColorStateList(null, intArrayOf(Color.YELLOW))
//           }*/
//        addView(lav, dp35.shl(1), dp35)
//    }
//
//
//    override fun onStateChanged(
//        @NonNull refreshLayout: RefreshLayout,
//        @NonNull oldState: RefreshState,
//        @NonNull newState: RefreshState
//    ) {
////        VLog.d(tag, "state = $newState")
//        if (newState == RefreshState.PullDownToRefresh) {
//            //下拉过程
////            bar?.visibility = View.VISIBLE
//            lav?.resumeAnimation()
//        } else if (newState == RefreshState.None) {
//            lav?.pauseAnimation()
////            bar?.visibility = View.INVISIBLE
//        }
//    }
//
//    override fun getView(): View {
//        return this
//    }
//
//    override fun getSpinnerStyle(): SpinnerStyle {
//        return SpinnerStyle.Translate
//    }
//
//    override fun setPrimaryColors(vararg colors: Int) {
//    }
//
//    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
//    }
//
//    override fun onMoving(
//        isDragging: Boolean,
//        percent: Float,
//        offset: Int,
//        height: Int,
//        maxDragHeight: Int
//    ) {
//    }
//
//    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
//    }
//
//    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
////        HsLog.d(tag, "onStartAnimator")
////        animationBg?.start()
//    }
//
//    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
////        HsLog.d(tag, "onFinish")
////        animationBg?.stop()
//        return 100// delay 100L
//    }
//
//    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
//    }
//
//    override fun isSupportHorizontalDrag(): Boolean = false
//}