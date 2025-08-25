package com.dubu.me.adapters

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.HelpDataBean
import com.dubu.common.beans.me.HelpMultiTypeBean
import com.dubu.common.beans.me.QuestionDataBean
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.TextViewUtils
import com.dubu.me.R
import java.util.*

class HelpMultiItemAdapter :
    BaseMultiItemQuickAdapter<HelpMultiTypeBean, BaseViewHolder>() { //end

    init {

        addItemType(type = HelpMultiTypeBean.ITEM_TYPE_ALL, R.layout.item_help_all)
        addItemType(type = HelpMultiTypeBean.ITEM_TYPE_ONE, R.layout.item_help_one)

        addChildClickViewIds(
            //R.id.rlRootArea,
        )
    }

    override fun convert(holder: BaseViewHolder, item: HelpMultiTypeBean) {
        val tvTime = holder.getView<TextView>(R.id.tvTime)


        when (holder.itemViewType) {
            HelpMultiTypeBean.ITEM_TYPE_ALL -> {
                tvTime.visibility = View.GONE
                val tvText = holder.getView<TextView>(R.id.tvText)
                tvText.text = item.data.des ?: ""
                val linearLayout = holder.getView<LinearLayout>(R.id.llQuestionList)

                addTextViewsToLayout(linearLayout, item)

            }

            HelpMultiTypeBean.ITEM_TYPE_ONE -> {
                tvTime.visibility = View.VISIBLE
                val tvText = holder.getView<TextView>(R.id.tvText)
                val tvTextR = holder.getView<TextView>(R.id.tvTextR)
                val ivAvatarR = holder.getView<ImageView>(R.id.ivAvatarR)
                tvTextR.text = item.data.questionList?.get(0)?.question ?: ""
                tvText.text = item.data.questionList?.get(0)?.answer ?: ""

                GlideTool.loadAvatar(HiRealCache.user?.avatar?:"",ivAvatarR)

                tvTime.text = EaseDateUtils.getTimestampString(
                    context,
                    Date(System.currentTimeMillis())
                )
            }

            else -> {
            }
        }

    }


    override fun convert(
        holder: BaseViewHolder,
        item: HelpMultiTypeBean,
        payloads: List<Any>
    ) {
        super.convert(holder, item, payloads)


        when (payloads[0]) {
        }


    }


    private var myChooseData: QuestionDataBean? = null

    fun getChooseData() = myChooseData

    // 假设在 Activity 或 Fragment 中
    private fun addTextViewsToLayout(container: LinearLayout, item: HelpMultiTypeBean) {
        // 1. 准备文本数据集合
        val textList = mutableListOf<String>()

        val questionList = item.data.questionList
        questionList?.forEachIndexed { index, questionDataBean ->
            textList.add(questionDataBean.question ?: "")
        }

        // 3. 设置布局方向（纵向）
        container.orientation = LinearLayout.VERTICAL

        // 4. 遍历文本集合并创建 TextView
        textList.forEachIndexed { index, text ->
            // 创建新的 TextView
            val textView = TextView(context).apply {
                // 设置文本内容
                this.text = text

                // 设置文本大小
                textSize = 14f
                TextViewUtils.setMidBold(this)

                // 设置文本颜色
                setTextColor(ContextCompat.getColor(context, R.color.cl5F77EF))

                // 设置布局参数（宽度、高度、外边距）
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    // 设置外边距（顶部间距）
                    topMargin = if (index == 0) 0 else dipToPx(0) // 首项无上边距
                }

                // 设置内边距
                setPadding(dipToPx(0), dipToPx(8), dipToPx(8), dipToPx(0))

                // 设置点击事件
                setOnClickListener {
                    myChooseData = questionList?.get(index)

                    val mutableListOf = mutableListOf<QuestionDataBean>()
                    myChooseData?.let { it1 -> mutableListOf.add(it1) }
                    val helpDataBean = HelpDataBean("", mutableListOf)
                    val element = HelpMultiTypeBean(HelpMultiTypeBean.ITEM_TYPE_ONE, helpDataBean)
                    addData(element)

                    getOnItemClickListener()?.onItemClick(this@HelpMultiItemAdapter, it, 0)
                }
            }

            // 5. 将 TextView 添加到容器
            container.addView(textView)
        }
    }

    // dp 转 px 的扩展函数
    private fun dipToPx(dp: Int): Int {
        return (com.dubu.common.utils.DisplayUiTool.dp2px(context, dp.toFloat())).toInt()
    }

}