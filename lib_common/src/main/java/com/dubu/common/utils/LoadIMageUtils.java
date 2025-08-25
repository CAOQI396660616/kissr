package com.dubu.common.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LoadIMageUtils {

    public static boolean loadLocal(String localPath,Uri uri, ImageView imageView) {
        if (!TextUtils.isEmpty(localPath)) {
                //优先加载本地
            Glide.with(imageView.getContext()).load(localPath).into(imageView);
            return true;
        } else if (uri != null) {
            Glide.with(imageView.getContext()).load(uri).into(imageView);
            return true;
        }
        return false;
    }
}
