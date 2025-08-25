package com.dubu.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubu.common.views.cview.DrawableTextView
import com.dubu.home.R

/**
 * Author:Allen
 * Time:2021/11/21
 *  自定义view
 *           语音房 麦位
 *           3d 意思是不规则摆放麦位 暂时做一个 *
 */
class HomeMultiRvLayout : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context, attrs, defStyleAttr, defStyleRes
    ) {
        LayoutInflater.from(context).inflate(R.layout.home_mult_rv_layout, this)
        initView(context)
    }

    //================================================  初始化

    private var tvTitle1: DrawableTextView? = null
    private var rvOne: RecyclerView? = null
    private var tvTitle2: DrawableTextView? = null
    private var rvTwo: RecyclerView? = null

    private fun initView(context: Context?) {
        context ?: return
        tvTitle1 = findViewById<DrawableTextView>(R.id.tvTitle1)
        tvTitle2 = findViewById<DrawableTextView>(R.id.tvTitle2)
        rvOne = findViewById<RecyclerView>(R.id.rvOne)
        rvTwo = findViewById<RecyclerView>(R.id.rvTwo)

    }

}