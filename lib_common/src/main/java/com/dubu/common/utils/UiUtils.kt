package com.dubu.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.*
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dubu.common.base.BaseApp
import com.dubu.common.views.CenteredImageSpan

/**
 * @author  Created by Even on 2019/8/14
 *  Email: emailtopan@163.com
 *
 */
object UiUtils {
    /**
     * 获取color
     */
    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(BaseApp.instance, resId)
    }

    /**
     * 获取String
     */
    fun getString(resId: Int): String {
        return BaseApp.instance.getString(resId)
    }

    /**
     *  获取格式化之后的文字
     */
    fun getStringFormat(resId: Int, vararg value: Any): String {
        return String.format(getString(resId), *value)
    }

    /**
     * 获取StringArray
     */
    fun getStringArray(resId: Int): Array<String> {
        return BaseApp.instance.resources.getStringArray(resId)
    }

    /**
     * 获取TextSize
     */
    fun getTextSize(resId: Int): Float {
        return BaseApp.instance.resources.getDimension(resId)
    }

    /**
     * 获取Drawable
     */
    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(BaseApp.instance.applicationContext, resId)
    }

    /**
     * 设置window透明度
     */
    fun setWindowBgAlpha(activity: Activity, bgAlpha: Float) {
        val window = activity.window
        val attributes = window.attributes
        attributes.alpha = bgAlpha
        activity.window.attributes = attributes
    }

    /**
     * 设置TextView部分颜色
     */
    fun setTextPartColor(
        textValue: String,
        colorId: Int,
        startPosition: Int,
        endPosition: Int
    ): SpannableString {
        val spannableString = SpannableString(textValue)
        if (startPosition == -1 || endPosition == -1) {
            return spannableString
        }
        val colorSpan = ForegroundColorSpan(getColor(colorId))
        spannableString.setSpan(
            colorSpan,
            startPosition,
            endPosition,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun getTextPartColor(
        textValue: String,
        colorId: Int,
        startPosition: Int,
        endPosition: Int
    ): SpannableStringBuilder {
        val spannableString = SpannableStringBuilder(textValue)
        if (startPosition == -1 || endPosition == -1) {
            return spannableString
        }
        val colorSpan = ForegroundColorSpan(getColor(colorId))
        spannableString.setSpan(
            colorSpan,
            startPosition,
            endPosition,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    /**
     * @description 获取网络图片 加载到TV的末尾
     * @param context
     * @param url
     * @param view
     * @param contentStringBuilder 文本
     * @author Allen
     * @time 2021/12/13 14:52
     */
    fun getBitmapSetTextViewEnd(
        context: Context?,
        url: String?,
        view: TextView?,
        contentStringBuilder: SpannableStringBuilder,
        addStringBuilder: SpannableStringBuilder
    ) {
        if (context == null || view == null) {
            return
        }
        if (!TextUtils.isEmpty(url)) {
            getDrawableGlide(context, url!!, object :
                CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    //默认尾部一个 占位字符  用来替换图片
                    contentStringBuilder.append("1")
                    //目前宽高是写死的 后面修改一下
                    resource?.setBounds(
                        0,
                        0,
                        DisplayMetricsTool.dp2px(BaseApp.instance, 18f).toInt(),
                        DisplayMetricsTool.dp2px(BaseApp.instance, 26f).toInt()
                    )
                    val span = CenteredImageSpan(context, resource, view)
                    //var span = ImageSpan(resource, ImageSpan.ALIGN_CENTER)
                    contentStringBuilder.setSpan(
                        span,
                        contentStringBuilder.length - 1,
                        contentStringBuilder.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
                    )

                    contentStringBuilder.append(addStringBuilder)

                    view.post(Runnable {
                        view.text = contentStringBuilder
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })
        }
    }

    fun getDrawableGlide(context: Context, url: String, customTarget: CustomTarget<Drawable>) {
        Glide.with(context).asDrawable().load(url).into(customTarget);
    }



    /**
     * @description 获取网络图片 加载到TV的末尾
     * @param context
     * @param url
     * @param view
     * @param contentStringBuilder 文本
     * @author Allen
     * @time 2021/12/13 14:52
     */
    fun setBitmapIntoTextView(
        context: Context?,
        url: String?,
        view: TextView?,
        contentStringBuilder: SpannableStringBuilder,
        addStringBuilder: SpannableStringBuilder
    ) {
        if (context == null || view == null) {
            return
        }
        if (!TextUtils.isEmpty(url)) {
            getDrawableGlide(context, url!!, object :
                CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    //默认尾部一个 占位字符  用来替换图片
                    contentStringBuilder.append("1")
                    //目前宽高是写死的 后面修改一下
                    resource?.setBounds(
                        0,
                        0,
                        DisplayMetricsTool.dp2px(BaseApp.instance, 14f).toInt(),
                        DisplayMetricsTool.dp2px(BaseApp.instance, 14f).toInt()
                    )
                    val span = CenteredImageSpan(context, resource, view)
                    //var span = ImageSpan(resource, ImageSpan.ALIGN_CENTER)
                    contentStringBuilder.setSpan(
                        span,
                        contentStringBuilder.length - 1,
                        contentStringBuilder.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
                    )

                    contentStringBuilder.append(addStringBuilder)

                    view.post(Runnable {
                        view.text = contentStringBuilder
                    })
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })
        }
    }


    // 带缓存的优化版本（避免重复计算）
    val lineCountCache = mutableMapOf<Pair<Int, String>, Int>()

    fun calculateTextLinesCached(textView: TextView, text: CharSequence? = null): Int {
        val measureText = text?.toString() ?: textView.text.toString()
        val widthKey = textView.width - textView.paddingLeft - textView.paddingRight
        val cacheKey = widthKey to measureText

        // 使用缓存避免重复计算
        return lineCountCache.getOrPut(cacheKey) {
            if (TextUtils.isEmpty(measureText)) return 0
            if (widthKey <= 0) return 1

            StaticLayout.Builder.obtain(
                measureText,
                0,
                measureText.length,
                textView.paint,
                widthKey
            ).build().lineCount
        }
    }



}