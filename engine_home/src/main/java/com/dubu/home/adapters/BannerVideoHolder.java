package com.dubu.home.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dubu.home.R;
import com.ruffian.library.widget.RConstraintLayout;

public
class BannerVideoHolder extends RecyclerView.ViewHolder {
    public RConstraintLayout rootView;
    public ImageView ivBanner;

    public BannerVideoHolder(@NonNull View view) {
        super(view);
        this.rootView = (RConstraintLayout) view;
        ivBanner = (ImageView)rootView.findViewById(R.id.ivBanner);
    }
}
