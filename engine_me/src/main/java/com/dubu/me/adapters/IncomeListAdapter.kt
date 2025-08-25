package com.dubu.me.adapters

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.KolIncomeListBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.manager.UITool
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.TextViewUtils
import com.dubu.me.R
import java.util.*

/**
 * 我的收入适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[IncomeListAdapter]
 */
class IncomeListAdapter :
    BaseQuickAdapter<KolIncomeListBean, BaseViewHolder>(R.layout.item_income_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: KolIncomeListBean) {

        val mTime = holder.getView<TextView>(R.id.tvTime)

        val tvTop1 = holder.getView<TextView>(R.id.tvTeamName)
        TextViewUtils.setMidBold(tvTop1)
        val tvTop2 = holder.getView<TextView>(R.id.tvStates)
        TextViewUtils.setMidBold(tvTop2)
        val tvBot1 = holder.getView<TextView>(R.id.tvBot1)
        val tvBot2 = holder.getView<TextView>(R.id.tvBot2)


        tvTop1.text =  item.display_income_title//"聊天-收到1个火箭"
        tvBot1.text = item.display_from_user_text//"来自ID: 33467  2024-03-03 12:33"
        tvBot2.text = item.usd_amount//"\$3.33"


        UITool.changeIncomeUI(item.status,item.status_text,tvTop2)




        val itemPosition = getItemPosition(item)


        HiLog.e(Tag2Common.TAG_12301, "$itemPosition = ${item.created_at}")



        val positionPre = itemPosition - 1
        if (positionPre < data.size && positionPre >= 0) {

            val stringToDate =
                EaseDateUtils.StringToDate(item.created_at, EaseDateUtils.FORMAT_YMDHMS)
            val dateStr = EaseDateUtils.getDateStr(stringToDate.time, EaseDateUtils.FORMAT_YMD)

            val preWrapper = getItem(positionPre)
            val stringToDatePre =
                EaseDateUtils.StringToDate(preWrapper.created_at, EaseDateUtils.FORMAT_YMDHMS)
            val dateStrPre = EaseDateUtils.getDateStr(stringToDatePre.time, EaseDateUtils.FORMAT_YMD)

            HiLog.e(
                Tag2Common.TAG_12310,
                "$itemPosition, ${item.created_at} , $dateStr -- $positionPre, ${preWrapper.created_at} , $dateStrPre"
            )


            if (dateStr == dateStrPre
            ) {
                mTime.visibility = View.GONE
            } else {
                mTime.visibility = View.VISIBLE
                mTime.text = dateStr
            }
        }else{
            val stringToDate =
                EaseDateUtils.StringToDate(item.created_at, EaseDateUtils.FORMAT_YMDHMS)
            val dateStr = EaseDateUtils.getDateStr(stringToDate.time, EaseDateUtils.FORMAT_YMD)

            mTime.visibility = View.VISIBLE
            mTime.text = dateStr
        }
    }

}