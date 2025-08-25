package com.dubu.common.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

/**
 * @Description
 * @Author Created by AF on 2022/12/28
 */
public class CenteredImageSpan extends ImageSpan {
    private Drawable mDrawable;
    private TextView textView;

    public CenteredImageSpan(Context context, final int drawableRes) {
        super(context, drawableRes);
    }

    public CenteredImageSpan(Context context, Drawable drawable) {
        super(drawable, 0);
        this.mDrawable = drawable;
    }

    public CenteredImageSpan(Context context, Drawable drawable, TextView tv) {
        super(drawable, 0);
        this.mDrawable = drawable;
        this.textView = tv;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // image to draw
        Drawable b = getDrawable();
        if (b == null) {
            b = mDrawable;
        }
        // font metrics of text to be replaced
        Paint.FontMetricsInt fm;
        if (textView == null) {
            fm = paint.getFontMetricsInt();
        } else {
            fm = textView.getPaint().getFontMetricsInt();
        }
        int transY = (y + fm.descent + y + fm.ascent) / 2
                - b.getBounds().bottom / 2;

        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
