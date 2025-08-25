package com.dubu.common.beans.me

// import com.bytedance.im.core.api.model.BIMMessage // 暂时注释掉，缺少字节跳动IM SDK依赖
import com.chad.library.adapter.base.entity.MultiItemEntity


data class PhotoMultiTypeBean(var type: Int, var data: PhotoDataBean) :
    MultiItemEntity {
    override val itemType: Int
        get() = type

    companion object {
        const val ITEM_TYPE_PHOTO = 1 //真实图片
        const val ITEM_TYPE_ADD = 2 // add 按钮
    }
}
