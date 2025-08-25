package com.dubu.me.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.config.LanguageInfo
import com.dubu.common.utils.HiLog
import com.dubu.me.R

/**
 * 我的擅长语言适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[LanguageListAdapter]
 */
class LanguageListAdapter :
    BaseQuickAdapter<LanguageInfo, BaseViewHolder>(R.layout.item_language_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: LanguageInfo) {
        val tvName = holder.getView<TextView>(R.id.tvName)
        tvName.text = item.native_name

        val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
        ivChoose.visibility = View.VISIBLE
        if (item.isSelected == true ) {
            ivChoose.setImageResource(R.drawable.ic_red_choose)
        } else {
            ivChoose.setImageResource(R.drawable.ic_statu_down)
        }

    }

    override fun convert(holder: BaseViewHolder, item: LanguageInfo, payloads: List<Any>) {
        super.convert(holder, item, payloads)


        when (payloads[0]) {
            "click" ->{
                val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
                ivChoose.visibility = View.VISIBLE
                if (item.isSelected == true ) {
                    ivChoose.setImageResource(R.drawable.ic_red_choose)
                } else {
                    ivChoose.setImageResource(R.drawable.ic_statu_down)
                }
            }
        }

    }

    fun changeUi(position: Int) {

        val languageInfo = data[position]

        languageInfo.isSelected = !(languageInfo.isSelected ?: false)

        notifyItemChanged(position, "click")

    }

    fun checkUserChooseList(): MutableList<LanguageInfo> {
        val listOf = mutableListOf<LanguageInfo>()

        data.forEachIndexed { index, item ->

            if (item.isSelected == true) {
                listOf.add(item)
            }

        }


        return listOf
    }

    fun checkUserChooseListToStr(): String {

        val checkUserChooseList = checkUserChooseList()
        if (checkUserChooseList.isEmpty())
            return ""

        // 构建签名字符串
        val sb = StringBuilder()
        checkUserChooseList.forEachIndexed { index, languageInfo ->

            if (index == (checkUserChooseList.size -1 )){
                sb.append(languageInfo.country_code)
            }else{
                sb.append(languageInfo.country_code)
                sb.append(",")
            }
        }


        return sb.toString()
    }

}