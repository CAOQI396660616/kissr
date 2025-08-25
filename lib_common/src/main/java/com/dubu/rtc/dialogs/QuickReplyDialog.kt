package com.dubu.rtc.dialogs

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.dubu.common.R
import com.dubu.common.dialog.BaseBottomDialog
import com.dubu.common.utils.HiRealCache
import com.dubu.me.vm.CommonViewModel
import com.dubu.rtc.adapter.QuickReplyAdapter

/**
 * 分享
 * Author:v
 * Time:2020/11/25
 * @author cq
 * @date 2025/06/25
 * @constructor 创建[QuickReplyDialog]
 */
class QuickReplyDialog : BaseBottomDialog() {
    override val TAG: String = "EditTextDialog"

    private var recyclerView: RecyclerView? = null
    private var distanceDeliverToDialog = 0
    private var deliverToDialogForView= 0
    private var onActionChoose: ((action: String,type: Int) -> Unit)? = null
    private var onActionClose: (() -> Unit)? = null
    private var onActionCloseActivity: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_quick_reply
    }


    fun withTypeChooseListener(listener: ((action: String,type: Int) -> Unit)): QuickReplyDialog {
        onActionChoose = listener
        return this
    }
    fun withCloseListener(listener: (() -> Unit)): QuickReplyDialog {
        onActionClose = listener
        return this
    }

    fun withCloseActivityListener(listener: (() -> Unit)): QuickReplyDialog {
        onActionCloseActivity = listener
        return this
    }

    fun withGap(gap: Int): QuickReplyDialog {
        distanceDeliverToDialog = gap
        return this
    }
    fun withViewHeight(gap: Int): QuickReplyDialog {
        deliverToDialogForView = gap
        return this
    }

    // 使用 DialogFragment 作用域的 ViewModel
    private val model: CommonViewModel by viewModels()

    private val mAdapter: QuickReplyAdapter by lazy {
        QuickReplyAdapter()
    }

    override fun isFullScreen() = true

    override fun initView(root: View) {
        root.findViewById<View>(R.id.ivClose).setOnClickListener {
            onActionClose?.invoke()
            dismiss()
        }

        root.findViewById<View>(R.id.viewBack).setOnClickListener {
            onActionCloseActivity?.invoke()
            dismiss()
        }

        root.findViewById<View>(R.id.clRoot).let {clRoot ->
            val params = clRoot.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = distanceDeliverToDialog
            clRoot.layoutParams = params
        }

        root.findViewById<View>(R.id.clRootView).let {clRoot ->
            val navBarHeight = BarUtils.getNavBarHeight()
            val screenHeight = ScreenUtils.getScreenHeight()
            val params = clRoot.layoutParams as ViewGroup.MarginLayoutParams
            params.height = deliverToDialogForView /*+ navBarHeight*/
            clRoot.layoutParams = params
        }

        recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)

        with(recyclerView!!){
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mAdapter
        }

        mAdapter.setList(HiRealCache.getRandomChatMessages(3))


        mAdapter.setOnItemChildClickListener { adapter, view, position ->

            // 1 点击了文字 直接发送
            // 0 点击了编辑 展示文本到输入框
            when(view.id){
                R.id.tvTitle -> {
                    onActionChoose?.invoke(mAdapter.data[position],1)
                    dismiss()
                }
                R.id.viewEdit -> {
                    onActionChoose?.invoke(mAdapter.data[position],0)
                }
            }
        }
    }



}