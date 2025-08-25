package com.dubu.me.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.KolPostBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.NetWorkConst
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.TextViewUtils
import com.dubu.me.R

/**
 * 我的相册适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[PhotoListAdapterV2]
 */
class PhotoListAdapterV2 :
    BaseQuickAdapter<KolPostBean, BaseViewHolder>(R.layout.item_photo_list_v2),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            R.id.ivChoose,
            R.id.root,
        )
    }

    override fun convert(holder: BaseViewHolder, item: KolPostBean) {

        val ivPlay = holder.getView<ImageView>(R.id.ivPlay)
        val ivPhoto = holder.getView<ImageView>(R.id.ivPhoto)
        val ivTop = holder.getView<ImageView>(R.id.ivTop)

        HiLog.e(Tag2Common.TAG_12306, "item.files = ${item.files}")

        val type = item.type ?: ""
        if (type == "video") {
            ivPlay.visibility = View.VISIBLE
            GlideTool.loadVideoFrameV2(item.files, ivPhoto, 100)
        } else {
            ivPlay.visibility = View.GONE
            GlideTool.loadImage(item.files, ivPhoto)
        }

        val tvY = holder.getView<TextView>(R.id.tvY)
        TextViewUtils.setMidBold(tvY)
        val tvNo = holder.getView<TextView>(R.id.tvNo)
        TextViewUtils.setMidBold(tvNo)

        val auditStatus = item.audit_status
        when (auditStatus.toString()) {
            NetWorkConst.POST_LIST_PENDING -> {
                tvY.visibility = View.VISIBLE
                tvNo.visibility = View.GONE
            }
            NetWorkConst.POST_LIST_REJECTED -> {
                tvY.visibility = View.GONE
                tvNo.visibility = View.VISIBLE
            }
            NetWorkConst.POST_LIST_APPROVED -> {
                tvY.visibility = View.GONE
                tvNo.visibility = View.GONE
            }
            else -> {
                tvY.visibility = View.GONE
                tvNo.visibility = View.GONE
            }
        }

        HiLog.e(Tag2Common.TAG_12315, "1isEnterEditAction = $isEnterEditAction  ${item.id}")
        if (isEnterEditAction) {
            val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
            ivChoose.visibility = View.VISIBLE
            HiLog.e(Tag2Common.TAG_12315, "2isEnterEditAction = $isEnterEditAction  ${item.id}")
            if (item.isSelected == true || isUserSelectedAll) {
                ivChoose.setImageResource(R.drawable.ic_red_choose)
            } else {
                ivChoose.setImageResource(R.drawable.ic_un_seletced_w)
            }

            item.isSelected = isUserSelectedAll
        }else{
            val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
            ivChoose.visibility = View.GONE
        }



        if (item.is_top == 1) {
            ivTop.visibility = View.VISIBLE
        } else {
            ivTop.visibility = View.GONE
        }


    }

    override fun convert(holder: BaseViewHolder, item: KolPostBean, payloads: List<Any>) {
        super.convert(holder, item, payloads)

        when (payloads[0]) {
            "selected" -> {
                val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
                ivChoose.visibility = View.VISIBLE
                if (item.isSelected == true) {
                    ivChoose.setImageResource(R.drawable.ic_red_choose)
                } else {
                    ivChoose.setImageResource(R.drawable.ic_un_seletced_w)
                }


            }
            "showEditUi" -> {
                val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
                ivChoose.visibility = View.VISIBLE
                ivChoose.setImageResource(R.drawable.ic_un_seletced_w)
            }
            "hideEditUi" -> {
                val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
                ivChoose.visibility = View.GONE
                ivChoose.setImageResource(R.drawable.ic_un_seletced_w)
            }
            "toggle" -> {
                val ivChoose = holder.getView<ImageView>(R.id.ivChoose)
                ivChoose.visibility = View.GONE
                ivChoose.setImageResource(R.drawable.ic_un_seletced_w)
            }
        }
    }

    private var isUserSelectedAll: Boolean = false
    private var isEnterEditAction: Boolean = false
    fun selectedAllOrNot() {
        if (data.isNullOrEmpty())
            return
        isUserSelectedAll = !isUserSelectedAll
        data.forEachIndexed { index, kolPostBean ->
            kolPostBean.isSelected = isUserSelectedAll
            notifyItemChanged(index, "selected")
        }

    }
    fun enterEditAction() {
        isEnterEditAction = true
    }
    fun exitEditAction() {
        isEnterEditAction = false
    }
    fun showEditUi() {
        if (data.isNullOrEmpty())
            return
        isUserSelectedAll = false
        isEnterEditAction = true
        data.forEachIndexed { index, kolPostBean ->
            kolPostBean.isSelected = isUserSelectedAll
            notifyItemChanged(index, "showEditUi")
        }


    }

    fun hideEditUi() {
        if (data.isNullOrEmpty())
            return
        isUserSelectedAll = false
        isEnterEditAction = false
        data.forEachIndexed { index, kolPostBean ->
            kolPostBean.isSelected = isUserSelectedAll
            notifyItemChanged(index, "hideEditUi")
        }


    }

    fun checkUserChooseList(): MutableList<KolPostBean> {
        val listOf = mutableListOf<KolPostBean>()

        data.forEachIndexed { index, kolPostBean ->

            if (kolPostBean.isSelected == true) {
                listOf.add(kolPostBean)
            }

        }


        return listOf
    }
    fun checkUserCanUnTop(): MutableList<KolPostBean> {
        val listOf = mutableListOf<KolPostBean>()

        data.forEachIndexed { index, kolPostBean ->

            if (kolPostBean.isSelected == true && kolPostBean.is_top == 1) {
                listOf.add(kolPostBean)
            }

        }


        return listOf
    }

    fun isUserCanUpTop(): Boolean {

        var isCanUnTop = true
        data.forEachIndexed { index, kolPostBean ->

            if (kolPostBean.isSelected == true ) {
                if (kolPostBean.is_top != 1) {
                    isCanUnTop = false
                }
            }

        }


        return isCanUnTop
    }


    fun toggleChoose(position: Int) {
        if (data.isNullOrEmpty())
            return
        val kolPostBean = data[position]

        kolPostBean.isSelected = !(kolPostBean.isSelected ?: false)

        notifyItemChanged(position, "selected")
    }

    fun removeAllChoosed() {
        if (data.isNullOrEmpty())
            return

        val mutableListOf = mutableListOf<Int>()

        for (index in 0 until (data.size)) {
            val kolPostBean = data[index]
            if (kolPostBean.isSelected == true) {
                mutableListOf.add(index)
            }
        }

        mutableListOf.forEachIndexed { index, i ->

            removeAt(i)
        }


        hideEditUi()

    }

}