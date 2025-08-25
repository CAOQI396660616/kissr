package com.dubu.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.even.commonrv.utils.DisplayUtil
import java.io.File


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

object GlideTool {


    fun loadImage(url: String?, iv: ImageView, @DrawableRes holder: Int) {
        Glide.with(iv.context)
            .load(url)
            .placeholder(holder)
            .error(holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }
    fun loadImageS(url: String?, iv: ImageView, @DrawableRes holder: Int) {
        Glide.with(iv.context)
            .load(url)
            .override(200, 200)
            .apply(RequestOptions().dontTransform()) // 禁用变换
            .placeholder(holder)
            .error(holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }

    fun loadImage(url: String?, iv: ImageView) {
        Glide.with(iv.context)
            .load(url)
            .placeholder(R.color.cl2C2E33)
            .error(R.color.cl2C2E33)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }

    fun loadImageWithDefault(url: String?, iv: ImageView) {
        Glide.with(iv.context)
            .load(url)
            .placeholder(R.drawable.common_radius_9_1d1e26)
            .error(R.drawable.common_radius_9_1d1e26)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }

    fun loadImage(uri: Uri?, iv: ImageView) {
        Glide.with(iv.context)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }


    fun loadAvatar(url: String?, iv: ImageView) {
        Glide.with(iv.context)
            .load(url)
            .override(500, 500)
            .apply(RequestOptions().dontTransform()) // 禁用变换
            .placeholder(R.drawable.ic_default_avatar)
            .error(R.drawable.ic_default_avatar)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(iv)
    }
    fun loadAvatarForRtc(url: String?, iv: ImageView) {
        Glide.with(iv.context)
            .load(url)
            .override(500, 500)
            .apply(RequestOptions().dontTransform()) // 禁用变换
            .placeholder(R.drawable.ic_default_avatar_r)
            .error(R.drawable.ic_default_avatar_r)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(iv)
    }


    /**
     * 加载圆角图片
     *
     * @param url      图片资源
     * @param iv          控件
     * @param radius       圆角度
     */
    fun loadRoundedImg(url: String?, iv: ImageView, radius: Float) {
        getRequestOptions(true)?.let {
            Glide.with(iv.context).asDrawable()
                .load(url)
                .apply(it)
                .error(R.color.color999999)
                .placeholder(R.color.color999999) //会影响圆角
                //.transition(withCrossFade())
                .transform(RoundedCorners(DisplayUiTool.dp2px(iv.context, radius).toInt()))
                .into(iv)
        }
    }

    /**
     * 1.DiskCacheStrategy.NONE：表示不缓存任何内容。
     * 2.DiskCacheStrategy.DATA：表示只缓存原始图片。
     * 3.DiskCacheStrategy.RESOURCE：表示只缓存转换过后的图片。
     * 4.DiskCacheStrategy.ALL：表示既缓存原始图片，也缓存转换过后的图片。
     * 5.DiskCacheStrategy.AUTOMATIC：表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     *
     * @return 配置
     */
    private fun getRequestOptions(isCrop: Boolean): RequestOptions? {
        return if (isCrop) {
            RequestOptions() // 填充方式
                .centerCrop() //优先级
                .priority(Priority.HIGH) //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        } else {
            RequestOptions() //优先级
                .priority(Priority.HIGH) //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        }
    }

    @SuppressLint("CheckResult")
    fun loadVideoFrame(
        url: String?,
        iv: ImageView,
        inPoint: Long = 100000,
        @DrawableRes holder: Int = R.drawable.iv_default_error_data
    ) {
        Glide.with(iv.context)
            .load(url)
            .apply {
                frame(inPoint)
                    .override(500, 500)
                    .centerCrop()
            }
            .placeholder(holder)
            .error(holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }
    @SuppressLint("CheckResult")
    fun loadVideoFrameV2(
        url: String?,
        iv: ImageView,
        inPoint: Long = 100000,
    ) {
        Glide.with(iv.context)
            .load(url)
            .apply {
                frame(inPoint)
                    .override(500, 500)
                    .centerCrop()
            }
            .placeholder(R.color.color999999)
            .error(R.color.color999999)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    @WorkerThread
    suspend fun getCachePicture(url: String): File? {
        if (url.isBlank()) return null

        val f = Glide.with(BaseApp.instance)
            .downloadOnly()
            .load(url)
            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)


        return try {
            f.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 高斯模糊 直播间背景图
     */
    fun blurUserBg(
        context: Context?,
        view: ImageView?,
        url: String?,
        width: Int? = DisplayUtil.getScreenWidth(),
        height: Int? = DisplayUtil.getScreenHeight(),
    ) {
        if (context == null || view == null) {
            return
        }
        val bitmapTransformation = GlideBlurFormation(context, url)
        Glide.with(context)
            .load(url)
            .placeholder(R.color.black)
            .error(R.color.black)
            .override(width!!, height!!)
            .apply(
                RequestOptions.bitmapTransform(bitmapTransformation)
                    .format(DecodeFormat.PREFER_ARGB_8888)
            )
            .into(view)
    }





    @SuppressLint("CheckResult")
    fun loadVideoFrame(
        url: String?,
        iv: ImageView,
        inPoint: Long = 100000,
        @DrawableRes holder: Int = R.drawable.iv_default_error_data,
        onSizeReady: (width: Int, height: Int) -> Unit = { _, _ -> } // 添加宽高回调
    ) {
        Glide.with(iv.context)
            .asBitmap() // 明确指定加载为Bitmap
            .load(url)
            .apply {
                frame(inPoint)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) // 获取原始尺寸
                    .centerCrop()
            }
            .placeholder(holder)
            .error(holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirstResource: Boolean
                ): Boolean {
                    // 加载失败时回调默认尺寸
                    onSizeReady(0, 0)
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 资源加载成功时获取宽高
                    resource?.let {
                        onSizeReady(it.width, it.height)
                    } ?: run {
                        onSizeReady(0, 0)
                    }
                    return false
                }
            })
            .into(object : CustomViewTarget<ImageView, Bitmap>(iv) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    iv.setImageDrawable(errorDrawable)
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    iv.setImageBitmap(resource)
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    iv.setImageDrawable(placeholder)
                }
            })
    }

}