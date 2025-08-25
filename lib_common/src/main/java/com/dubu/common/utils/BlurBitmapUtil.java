package com.dubu.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurBitmapUtil {

    private static final int BLUR_MAX_SIZE = 60;

    private static BlurBitmapUtil sInstance;

    private BlurBitmapUtil() {
    }

    public static BlurBitmapUtil instance() {
        if (sInstance == null) {
            synchronized (BlurBitmapUtil.class) {
                if (sInstance == null) {
                    sInstance = new BlurBitmapUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * @param context   上下文对象
     * @param image     需要模糊的图片
     * @param outWidth  输入出的宽度
     * @param outHeight 输出的高度
     * @return 模糊处理后的Bitmap
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blurBitmap(Context context, Bitmap image, float blurRadius, int outWidth, int outHeight) {

        int destWidth = 0;
        int destHeight = 0;

        if (outWidth <= BLUR_MAX_SIZE || outHeight <= BLUR_MAX_SIZE) {
            destWidth = outWidth;
            destHeight = outHeight;
        } else {
            float widthScale = outWidth / BLUR_MAX_SIZE;
            float heightScale = outHeight / BLUR_MAX_SIZE;
            float scale = Math.min(widthScale, heightScale);
            destWidth = (int) (outWidth / scale);
            destHeight = (int) (outHeight / scale);

        }
        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, destWidth, destHeight, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context.getApplicationContext());
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // 设置渲染的模糊程度, 25f是最大模糊度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript.setRadius(blurRadius);
        }
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        inputBitmap.recycle();

        tmpIn.destroy();

        tmpOut.destroy();

        blurScript.destroy();

        rs.destroy();

        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.parseColor("#90000000"));
        return outputBitmap;
    }
}
