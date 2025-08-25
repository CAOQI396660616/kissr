package com.dubu.main.home

import android.graphics.Typeface
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.dubu.common.utils.FontTool
import com.dubu.main.R


/**
 * Author:v
 * Time:2021/8/10
 */
class MainBottomBar(private var _bar: ConstraintLayout? = null) {

    private val bar: ConstraintLayout = _bar!!
    private var currentIndex: Int = 0
    private var listener: ((Int) -> Unit)? = null

    private fun changeUi(last: Int, cur: Int) {
        for (v in bar.children) {
            val index = v.tag as? Int ?: continue
            if (index == last) {
                v.isSelected = false
            } else if (index == cur) {
                v.isSelected = true
            }
        }

        changeTv(last, cur)

        currentIndex = cur
        listener?.invoke(cur)
    }

    private fun changeTv(last: Int, cur: Int) {

        val textViewLast = when (last) {
            0 -> {bar.findViewById<TextView>(R.id.tv_home)}
            1 -> {bar.findViewById<TextView>(R.id.tv_discovery)}
            2 -> {bar.findViewById<TextView>(R.id.tv_msg)}
            3 -> {bar.findViewById<TextView>(R.id.tv_mine)}
            else -> {bar.findViewById<TextView>(R.id.tv_home)}
        }
        FontTool.applyFont(context = textViewLast.context , textViewLast , "font/Ubuntu-Regular.ttf")
//        textViewLast.setTextColor(ContextCompat.getColor(textViewLast.context, com.dubu.common.R.color.cl847F97))


        val textViewCur = when (cur) {
            0 -> {bar.findViewById<TextView>(R.id.tv_home)}
            1 -> {bar.findViewById<TextView>(R.id.tv_discovery)}
            2 -> {bar.findViewById<TextView>(R.id.tv_msg)}
            3 -> {bar.findViewById<TextView>(R.id.tv_mine)}
            else -> {bar.findViewById<TextView>(R.id.tv_home)}
        }
        FontTool.applyFont(context = textViewCur.context , textViewCur , "font/Ubuntu-Bold.ttf")
//        textViewLast.setTextColor(ContextCompat.getColor(textViewCur.context, com.dubu.common.R.color.color3E77F9))




    }


    fun changeTab(index: Int) {
        if (index == currentIndex) return
        changeUi(currentIndex, index)
    }


    fun initTab(index: Int): MainBottomBar {
        currentIndex = index
        for (v in bar.children) {
            when (v.id) {
                R.id.tv_home,
                R.id.fl1,
                R.id.iv_home -> setupView(v, 0)

                R.id.tv_discovery,
                R.id.fl2,
                R.id.iv_discovery -> setupView(v, 1)

                R.id.tv_msg,
                R.id.fl3,
                R.id.iv_msg -> setupView(v, 2)

                R.id.tv_mine,
                R.id.fl4,
                R.id.iv_mine -> setupView(v, 3)
            }
        }
        return this
    }


    private fun setupView(view: View, index: Int) {
        view.tag = index
        view.setOnClickListener {
            changeTab(index)
        }
        if (currentIndex == index) {
            view.isSelected = true
            changeTv(currentIndex, currentIndex)
        }
    }


    fun withTabChangeListener(listener: (Int) -> Unit): MainBottomBar {
        this.listener = listener
        return this
    }


    fun showMsg(num: Int) {
        val flMsg = bar.findViewById<View>(R.id.flMsg)
        val tvMsg = bar.findViewById<TextView>(R.id.tvMsg)

        if (num > 0){
            flMsg.visibility = View.VISIBLE
            tvMsg.text = "$num"
        }else{
            flMsg.visibility = View.GONE
        }
    }


    fun detach() {
        _bar = null
        listener = null
    }
}