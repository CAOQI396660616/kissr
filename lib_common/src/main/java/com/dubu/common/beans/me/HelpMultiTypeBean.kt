package com.dubu.common.beans.me

import com.chad.library.adapter.base.entity.MultiItemEntity


data class HelpMultiTypeBean(var type: Int, var data: HelpDataBean) :
    MultiItemEntity {
    override val itemType: Int
        get() = type

    companion object {
        const val ITEM_TYPE_ALL = 1 //顶部所有的问题列表
        const val ITEM_TYPE_ONE = 2 //下面一问一答
    }
}
