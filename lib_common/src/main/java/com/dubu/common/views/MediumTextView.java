package com.dubu.common.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @Description
 * @Author Created by AF on 2023/4/24
 */
public class MediumTextView extends AppCompatTextView {
    public MediumTextView(@NonNull Context context) {
        this(context, null);
    }

    public MediumTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediumTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        getPaint().setFakeBoldText(true);
        setIncludeFontPadding(false);
    }
}
