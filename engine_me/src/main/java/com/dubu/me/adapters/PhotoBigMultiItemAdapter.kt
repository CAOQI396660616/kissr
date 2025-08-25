package com.dubu.me.adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.PhotoMultiTypeBean
import com.dubu.common.utils.GlideTool
import com.dubu.me.R

/**
 *  添加图片多类型布局适配器
 */
class PhotoBigMultiItemAdapter :
    BaseMultiItemQuickAdapter<PhotoMultiTypeBean, BaseViewHolder>() { //end

    init {

        addItemType(type = PhotoMultiTypeBean.ITEM_TYPE_ADD, R.layout.item_photo_add_2)
        addItemType(type = PhotoMultiTypeBean.ITEM_TYPE_PHOTO, R.layout.item_photo_2)

        addChildClickViewIds(
            //R.id.rlRootArea,
        )
    }

    override fun convert(holder: BaseViewHolder, item: PhotoMultiTypeBean) {


        when (holder.itemViewType) {
            //文本消息
            PhotoMultiTypeBean.ITEM_TYPE_ADD -> {

            }

            //礼物消息
            PhotoMultiTypeBean.ITEM_TYPE_PHOTO -> {
                val ivPhoto = holder.getView<ImageView>(R.id.ivPhoto)
                GlideTool.loadImage(item.data.image,ivPhoto,R.color.cl1C1C1C)
            }


            //
            else -> {
            }
        }

    }


    override fun convert(
        holder: BaseViewHolder,
        item: PhotoMultiTypeBean,
        payloads: List<Any>
    ) {
        super.convert(holder, item, payloads)


        when (payloads[0]) {
        }


    }

}