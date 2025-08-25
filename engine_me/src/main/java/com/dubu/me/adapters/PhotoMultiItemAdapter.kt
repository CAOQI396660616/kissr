package com.dubu.me.adapters

import android.graphics.Color
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.PhotoMultiTypeBean
import com.dubu.common.utils.GlideTool
import com.dubu.me.R
import java.util.Collections

/**
 *  添加图片多类型布局适配器
 */
class PhotoMultiItemAdapter :
    BaseMultiItemQuickAdapter<PhotoMultiTypeBean, BaseViewHolder>() { //end

    init {

        addItemType(type = PhotoMultiTypeBean.ITEM_TYPE_ADD, R.layout.item_photo_add)
        addItemType(type = PhotoMultiTypeBean.ITEM_TYPE_PHOTO, R.layout.item_photo)

        addChildClickViewIds(
            //R.id.rlRootArea,
        )
    }

    // 定义回调接口
    interface OnItemMoveListener {
        fun onItemMove(fromPosition: Int, toPosition: Int)
    }

    private var onItemMoveListener: OnItemMoveListener? = null

    fun setOnItemMoveListener(listener: OnItemMoveListener) {
        onItemMoveListener = listener
    }


    override fun convert(holder: BaseViewHolder, item: PhotoMultiTypeBean) {


        when (holder.itemViewType) {
            //文本消息
            PhotoMultiTypeBean.ITEM_TYPE_ADD -> {

            }

            //礼物消息
            PhotoMultiTypeBean.ITEM_TYPE_PHOTO -> {
                val ivPhoto = holder.getView<ImageView>(R.id.ivPhoto)
                GlideTool.loadImageS(item.data.image,ivPhoto,R.color.cl1C1C1C)
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


    // 改进的交换方法：直接交换元素位置
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) return

        // 直接交换元素位置
        Collections.swap(data, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)


        // 通知数据变化
        onItemMoveListener?.onItemMove(fromPosition, toPosition)
    }

}





class DragCallback(private val adapter: PhotoMultiItemAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 使用 absoluteAdapterPosition 替代 adapterPosition
        if (isLastItem(viewHolder.absoluteAdapterPosition)) {
            return 0
        }

        return when (recyclerView.layoutManager) {
            is GridLayoutManager -> {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                makeMovementFlags(dragFlags, 0)
            }
            else -> {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                makeMovementFlags(dragFlags, 0)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 使用 absoluteAdapterPosition 替代 adapterPosition
        val fromPosition = viewHolder.absoluteAdapterPosition
        val toPosition = target.absoluteAdapterPosition

        if (isLastItem(toPosition) || isLastItem(fromPosition)) {
            return false
        }

        adapter.swapItems(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 不需要实现
    }

    override fun isLongPressDragEnabled() = true

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        /*if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.takeIf { !isLastItem(it.absoluteAdapterPosition) }?.itemView?.setBackgroundColor(Color.LTGRAY)
        }*/
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        /*if (!isLastItem(viewHolder.absoluteAdapterPosition)) {
            viewHolder.itemView.setBackgroundColor(Color.WHITE)
        }*/
    }

    // 检查是否是最后一项
    private fun isLastItem(position: Int): Boolean {
        return position == adapter.itemCount - 1
    }
}