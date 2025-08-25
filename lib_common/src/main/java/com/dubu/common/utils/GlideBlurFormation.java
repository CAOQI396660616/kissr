package com.dubu.common.utils;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class GlideBlurFormation extends BitmapTransformation {

    private Context context;

    public String url;

    public GlideBlurFormation(Context context, String url) {
        this.context = context.getApplicationContext();
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return url.equals(((GlideBlurFormation)o).url);
    }


    @Override
    public int hashCode() {
        int code = url.hashCode();
        return code;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return BlurBitmapUtil.instance().blurBitmap(context, toTransform, 10, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}

