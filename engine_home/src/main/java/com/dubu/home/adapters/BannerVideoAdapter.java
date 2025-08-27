package com.dubu.home.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.dubu.home.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 默认实现的图片适配器，图片加载需要自己实现
 */
public abstract class BannerVideoAdapter<T> extends BannerAdapter<T, BannerVideoHolder> {

    public BannerVideoAdapter(List<T> mData) {
        super(mData);
    }

    @Override
    public BannerVideoHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = parent.inflate(parent.getContext(),R.layout.item_home_top_video, null);
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        BannerVideoHolder bannerVideoHolder = new BannerVideoHolder(view);

        return bannerVideoHolder;
    }

}
