package com.dubu.common.views;

/**
 * @Description
 * @Author Created by AF on 2022/12/8
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @Description 圆角图片
 * @Author Created by AF on 2021/9/16.
 */
public class RoundImageViewWidget extends AppCompatImageView {
    private float mWidth, mHeight;

    private final Path mPath;
    private int mLeftTopRadio = 30;
    private int mRightTopRadio = 30;
    private int mLeftBottomRadio = 30;
    private int mRightBottomRadio = 30;

    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;

    public RoundImageViewWidget(Context context) {
        this(context, null);
    }

    public RoundImageViewWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageViewWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPath = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        mPath.moveTo(mRightTopRadio, 0);
        mPath.lineTo(mWidth - mRightTopRadio, 0);
        mPath.quadTo(mWidth, 0, mWidth, mRightTopRadio);
        mPath.lineTo(mWidth, mHeight - mRightBottomRadio);
        mPath.quadTo(mWidth, mHeight, mWidth - mRightBottomRadio, mHeight);
        mPath.lineTo(mLeftBottomRadio, mHeight);
        mPath.quadTo(0, mHeight, 0, mHeight - mLeftBottomRadio);
        mPath.lineTo(0, mLeftTopRadio);
        mPath.quadTo(0, 0, mLeftTopRadio, 0);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }

    public void setRadio(int radio) {
        this.mLeftTopRadio = radio;
        this.mRightTopRadio = radio;
        this.mLeftBottomRadio = radio;
        this.mRightBottomRadio = radio;
        postInvalidate();
    }

    public void setRadio(int leftTopRadio, int rightTopRadio, int leftBottomRadio, int rightBottomRadio) {
        this.mLeftTopRadio = leftTopRadio;
        this.mRightTopRadio = rightTopRadio;
        this.mLeftBottomRadio = leftBottomRadio;
        this.mRightBottomRadio = rightBottomRadio;
        postInvalidate();
    }
}
