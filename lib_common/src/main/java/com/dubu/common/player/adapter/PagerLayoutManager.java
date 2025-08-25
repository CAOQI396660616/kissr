package com.dubu.common.player.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dubu.common.player.listener.OnViewPagerListener;

/**
 * 横向滑动 主要用于相册
 * @author cq
 * @date 2025/06/28
 */
public class PagerLayoutManager extends LinearLayoutManager {
    private static final String TAG = "HorizontalPagerLayout";
    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;
    private RecyclerView mRecyclerView;
    private int mDrift; // 位移量，用于判断滑动方向

    // 强制设置为水平方向
    public PagerLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        init();
    }

    private void init() {
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        this.mRecyclerView = view;
        mRecyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onLayoutChildren: IndexOutOfBoundsException", e);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View snapView = mPagerSnapHelper.findSnapView(this);
                if (snapView != null) {
                    int position = getPosition(snapView);
                    if (mOnViewPagerListener != null) {
                        mOnViewPagerListener.onPageSelected(
                                position,
                                position == getItemCount() - 1,
                                snapView
                        );
                    }
                }
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:
                // 拖动状态处理
                break;

            case RecyclerView.SCROLL_STATE_SETTLING:
                // 自动滚动状态处理
                break;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    // 禁用垂直滚动（因为我们是水平滑动）
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return 0;
    }

    public void setOnViewPagerListener(OnViewPagerListener listener) {
        this.mOnViewPagerListener = listener;
    }

    private final RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener =
            new RecyclerView.OnChildAttachStateChangeListener() {

                @Override
                public void onChildViewAttachedToWindow(@NonNull View view) {
                    if (mOnViewPagerListener != null && getChildCount() == 1) {
                        mOnViewPagerListener.onInitComplete();
                    }
                }

                @Override
                public void onChildViewDetachedFromWindow(@NonNull View view) {
                    if (mOnViewPagerListener != null) {
                        boolean isNext = mDrift >= 0; // 正数表示向右滑(下一页)
                        mOnViewPagerListener.onPageRelease(isNext, getPosition(view), view);
                    }
                }
            };
}