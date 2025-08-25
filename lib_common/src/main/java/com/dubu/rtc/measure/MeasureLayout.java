package com.dubu.rtc.measure;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class MeasureLayout extends FrameLayout {
    private int height;
    private onHeightChangedListener listener;

    public MeasureLayout(@NonNull Context context, onHeightChangedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        long c = bottom - top;
        if (height != c) {
            height = (bottom - top);
            listener.onHeightChanged(height);
        }

    }

    interface onHeightChangedListener {
        void onHeightChanged(int height);
    }
}
