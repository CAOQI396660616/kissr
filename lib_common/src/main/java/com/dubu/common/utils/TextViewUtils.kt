package com.dubu.common.utils

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.*
import android.text.style.*
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.dubu.common.base.BaseApp
import com.dubu.common.utils.UiUtils.getColor
import com.dubu.common.views.CenteredImageSpan
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 *  @author  Even
 *  @date   2021/11/15
 *  文本工具类
 */
object TextViewUtils {

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
        val colorSpan = ForegroundColorSpan(
            getColor(
                colorId
            )
        )
        spannableString.setSpan(
            colorSpan,
            startPosition,
            endPosition,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }


    /*文字头部带图片*/
    fun setDrawableInTxt(
        tv: TextView?,
        str: String? = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_BOTTOM
    ) {
        val ss = SpannableString(str)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""
        tv?.append(ss)
        tv?.append(" $str")
    }

    /*文字头部带图片*/
    fun setDrawableInTxt(
        mContext: Context,
        tv: TextView?,
        str: String? = "",
        drawable: Drawable,
        gravity: Int = ImageSpan.ALIGN_BASELINE
    ): TextView {
        val ss = SpannableString(str)
        //val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        //d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)

        var d = drawable
        val span = d?.let { CenteredImageSpan(mContext, d) }//用这个drawable对象代替字符串easy
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""
        tv?.append(ss)
        tv?.append(" $str")
        var newSp: SpannableStringBuilder = tv?.text as SpannableStringBuilder
        return tv
    }

    /*文字头部带2张图片，已做居中处理*/
    fun setDrawableSpInTxt(
        mContext: Context,
        tv: TextView?,
        str: String? = "",
        drawable0: Drawable,
        drawable: Drawable,
        gravity: Int = ImageSpan.ALIGN_BASELINE
    ) {
        val ss = SpannableString(str)
        var ss1 = SpannableString(str)
        //val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片

        //d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        var d0 = drawable0

        val span0 = d0?.let { CenteredImageSpan(mContext, d0) }//用这个drawable对象代替字符串easy

        var d = drawable
        val span = d?.let { CenteredImageSpan(mContext, d) }//用这个drawable对象代替字符串easy
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span0, 0, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        ss1.setSpan(span, 0, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""
        tv?.append(ss)
        tv?.append(" ")
        tv?.append(ss1)
        tv?.append(" $str")
        //return ss
    }

    fun setMidBold(tv: TextView) {
        tv.paint.style = Paint.Style.FILL_AND_STROKE
        tv.paint.strokeWidth = 0.7f
    }
    fun setCommon(tv: TextView) {
        tv.paint.style = Paint.Style.FILL
        tv.paint.strokeWidth = 0f
    }


    fun setTextColorGradient(
        textView: TextView?,
        @ColorRes startColor: Int,
        @ColorRes endColor: Int
    ) {
        if (textView == null || textView.context == null) {
            return
        }
        val context = textView.context
        @ColorInt val sc = context.resources.getColor(startColor)
        @ColorInt val ec = context.resources.getColor(endColor)
        val x = textView.paint.textSize * textView.text.length.toFloat()
        val gradient: LinearGradient = LinearGradient(0F, 0F, x, 0F, sc, ec, Shader.TileMode.CLAMP)
        textView.paint.shader = gradient
        textView.invalidate()
    }


    /*文字末尾带图片 玫瑰 */
    fun setDrawableEndInTxtForRose(
        tv: TextView?,
        str: String? = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_CENTER,
        width: Float = 15f,
        height: Float = 19f,
    ) {
        /*val ss = SpannableString(str + "1")
        val d = UiUtils.getDrawable(drawable)
        d?.setBounds(0, 0, dp2px(15f), dp2px(19f))
        val span = d?.let { ImageSpan(it, gravity) }
        ss.setSpan(span, str?.length!! - 1, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""
        tv?.append(ss)*/

        val ss = SpannableString(str)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, dp2px(width), dp2px(height))
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        ss.setSpan(span, 0, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""
        tv?.append(" $str")
        tv?.append(ss)
    }


    /*文字末尾带图片 玫瑰 */
    fun setDrawableMidInTxtForRose(
        tv: TextView?,
        strPartS: String? = "",
        strPartE: String? = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_CENTER,
        width: Float = 15f,
        height: Float = 19f,
    ) {
        val ss = SpannableString(strPartS)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, dp2px(width), dp2px(height))
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        ss.setSpan(span, 0, strPartS?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""

        tv?.append(" $strPartS")
        tv?.append(ss)
        tv?.append(" $strPartE")
    }


    //特殊方法
    fun setDrawableEndInTxtForRoomTip(
        tv: TextView?,
        strPartS: SpannableString,
        strPartE: String? = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_CENTER,
        width: Float = 15f,
        height: Float = 19f,
    ) {

        val ss = SpannableString(strPartE)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, dp2px(width), dp2px(height))
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        ss.setSpan(span, 0, strPartE?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""

        tv?.append(strPartS)
        tv?.append(ss)
        tv?.append("$strPartE")
    }

    //作用 是替换 rose 为 玫瑰图标
    fun replaceTextToDrawable(
        tv: TextView,
        oldStr: String = "",
        needReplaceText: String = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_CENTER,
        width: Float = 15f,
        height: Float = 19f,
    ) {
        val builder = SpannableStringBuilder(oldStr)
        val pattern: Pattern = Pattern.compile(needReplaceText)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, dp2px(width), dp2px(height))
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        val matcher: Matcher = pattern.matcher(oldStr)
        while (matcher.find()) {
            builder.setSpan(
                span,
                matcher.start() - 1,
                matcher.end() + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        tv.text = builder
    }

    //作用 是替换 rose 为 玫瑰图标
    fun replaceTextToDrawable(
        tv: TextView,
        oldStr: SpannableString,
        needReplaceText: String = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_CENTER,
        width: Float = 15f,
        height: Float = 19f,
    ) {
        val builder = SpannableStringBuilder(oldStr)
        val pattern: Pattern = Pattern.compile(needReplaceText)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, dp2px(width), dp2px(height))
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        val matcher: Matcher = pattern.matcher(oldStr)
        while (matcher.find()) {
            builder.setSpan(
                span,
                matcher.start() - 1,
                matcher.end() + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        tv.text = builder
    }

    fun setRightIcon(
        content: String,
        drawable: Int,
        size: Int,
        context: Context,
        tv: TextView
    ) {
        val s = content
        val d = ContextCompat.getDrawable(context, drawable)
        if (d != null) {
            val h = size

            d.setBounds(0, 0, h, h)

            val eis = CenteredImageSpan(context, d)
            val ssb = SpannableStringBuilder(s.plus(" *")).apply {
                setSpan(eis, length - 1, length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }

            tv.text = ssb
        } else {
            tv.text = s
        }
    }

    private fun dp2px(dp:Float): Int {
        return DisplayMetricsTool.dp2px(BaseApp.instance,dp).toInt()
    }


    fun setDrawableForMsg(
        tv: TextView?,
        str: String? = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_BOTTOM
    ) {
        val ss = SpannableString(str)
        val d = UiUtils.getDrawable(drawable)//得到drawable对象，即所要插入的图片
        d?.setBounds(0, 0, dp2px(14F), dp2px(14F))
        val span = d?.let { ImageSpan(it, gravity) }//用这个drawable对象代替字符串easy
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, str?.length!!, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv?.text = ""
        tv?.append(ss)
        tv?.append(" $str")
    }



    fun appendTextPartColorCommon(
        textValue: String,
        colorId: Int,
    ): SpannableString {
        val spannableString = SpannableString(textValue)
        val colorSpan = ForegroundColorSpan(
            getColor(
                colorId
            )
        )
        spannableString.setSpan(
            colorSpan,
            0,
            textValue.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }


    fun appendTextPartColor(
        textValue: String,
        colorId: Int,
        isBold: Boolean = false // 添加是否加粗的参数，默认加粗
    ): SpannableString {
        val spannableString = SpannableString(textValue)

        // 设置颜色
        val colorSpan = ForegroundColorSpan(
            getColor(
                colorId
            )
        )
        spannableString.setSpan(
            colorSpan,
            0,
            textValue.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        // 设置加粗
        if (isBold) {
            val boldSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(
                boldSpan,
                0,
                textValue.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }else{
            val boldSpan = StyleSpan(Typeface.NORMAL)
            spannableString.setSpan(
                boldSpan,
                0,
                textValue.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }

        return spannableString
    }

    fun appendTwoTextParts(
        context: Context,
        text1: String,
        colorRes1: Int,
        isBold1: Boolean,
        text2: String,
        colorRes2: Int,
        isBold2: Boolean
    ): SpannableStringBuilder {
        return appendTextParts(
            context,
            TextPart(text1, colorRes1, isBold1,false,14f),
            TextPart(text2, colorRes2, isBold2,false,13f)
        )
    }


    // 更新 appendTextParts 方法
    fun appendTextParts(context: Context, vararg parts: TextPart): SpannableStringBuilder {
        val spannableBuilder = SpannableStringBuilder()

        for (part in parts) {
            val start = spannableBuilder.length
            spannableBuilder.append(part.text)
            val end = spannableBuilder.length

            // 颜色
            part.colorRes?.let { colorRes ->
                val color = ContextCompat.getColor(context, colorRes)
                spannableBuilder.setSpan(
                    ForegroundColorSpan(color),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // 字体样式
            val style = when {
                part.isBold && part.isItalic -> Typeface.BOLD_ITALIC
                part.isBold -> Typeface.BOLD
                part.isItalic -> Typeface.ITALIC
                else -> Typeface.NORMAL
            }

            if (style != Typeface.NORMAL) {
                spannableBuilder.setSpan(
                    StyleSpan(style),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // 字体大小
            part.textSizeSp?.let { sizeSp ->
                val pxSize = sizeSp.spToPx(context).toInt()
                spannableBuilder.setSpan(
                    AbsoluteSizeSpan(pxSize),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // 下划线
            if (part.underline) {
                spannableBuilder.setSpan(
                    UnderlineSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // 删除线
            if (part.strikethrough) {
                spannableBuilder.setSpan(
                    StrikethroughSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannableBuilder
    }

    // sp转px的扩展函数
    private fun Float.spToPx(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            context.resources.displayMetrics
        )
    }



    fun setDrawableFirstTxt(
        tv: TextView?,
        str: String? = "",
        drawable: Int,
        gravity: Int = ImageSpan.ALIGN_BOTTOM
    ) {
        tv ?: return
        val text = str ?: ""
        val d = UiUtils.getDrawable(drawable)
        d?.let {
            d?.setBounds(0, 0, dp2px(10f), dp2px(10f))
            // 4. 创建带图片的Spannable
            val ss = SpannableString("  $text") // 添加空格作为图片占位符
            val span = ImageSpan(it, gravity)

            // 5. 设置Span位置 (只替换第一个字符位置)
            ss.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            // 6. 设置文本
            tv.text = ss
        }
    }



    /**
     * 将后台传来的文本转成可显示的格式
     * 支持 \n 换行 和 HTML <br> 换行
     */
    fun formatServerText(rawText: String?): CharSequence {
        if (rawText.isNullOrEmpty()) return ""

        // 1. 把转义的 \n 替换成真实换行符
        var processedText = rawText.replace("\\n", "\n")

        // 2. 如果包含 HTML 标签，转成 Spanned
        return if (containsHtml(processedText)) {
            HtmlCompat.fromHtml(processedText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            processedText
        }
    }

    /**
     * 将多行文本（用 \n 分隔）转成带序号的美化文本
     */
    fun formatAsNumberedList(rawText: String?): String {
        if (rawText.isNullOrEmpty()) return ""

        // 替换转义的 \n
        val lines = rawText.replace("\\n", "\n")
            .split("\n")
            .filter { it.isNotBlank() }

        return buildString {
            lines.forEachIndexed { index, line ->
                append("${index + 1}. ${line.trim()}\n")
            }
        }.trim()
    }

    /**
     * 检测字符串是否包含 HTML 标签
     */
    private fun containsHtml(text: String): Boolean {
        val htmlTags = listOf("<br>", "<p>", "&nbsp;", "<b>", "<i>", "<u>")
        return htmlTags.any { tag -> text.contains(tag, ignoreCase = true) }
    }


}



data class TextPart(
    val text            : String,
    val colorRes            : Int? = null,
    val isBold          : Boolean = false,
    val isItalic            : Boolean = false,
    val textSizeSp      : Float? = null,
    val underline           : Boolean = false,
    val strikethrough       : Boolean = false
)


// 自定义 TypefaceSpan
class CustomTypefaceSpan() : MetricAffectingSpan() {
    override fun updateDrawState(ds: TextPaint) {
        applyTypeface(ds)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyTypeface(paint)
    }

    private fun applyTypeface(paint: Paint) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 0.7f
    }
}