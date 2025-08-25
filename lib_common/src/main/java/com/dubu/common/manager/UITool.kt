package com.dubu.common.manager

import android.view.View
import android.widget.TextView
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.dubu.common.beans.UserBean
import com.dubu.common.utils.DisplayUiTool
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.UiUtils
import com.ruffian.library.widget.RFrameLayout

/*
* 处理登录相关的业务管理器
* */

object UITool {
    private const val STATE_ON = "online"
    private const val STATE_ON_INT = 0

    private const val STATE_OFF = "offline"
    private const val STATE_OFF_INT = 1

    private const val STATE_BUSY = "busy"
    private const val STATE_BUSY_INT = 2


    //active 就是正确走完整个流程的 miss:未接  reject 是拒接， busy 是通话中
    private const val STATE_VIDEO_ACTIVE = "active"
    private const val STATE_VIDEO_CLOSED = "reject"
    private const val STATE_VIDEO_MISS   = "miss"

    private const val STATUS_SETTLING           = "settling"           // 结算中
    private const val STATUS_PENDING_ARRIVAL    = "pending_arrival"    // 待到账
    private const val STATUS_WITHDRAWABLE       = "withdrawable"       // 可提现
    private const val STATUS_REFUNDED           = "refunded";          // 已退款

    private const val STATUS_WITHDRAW_SUCCESS   = "withdraw_success"   // 提现成功
    private const val STATUS_WITHDRAW_FAILED    = "withdraw_failed"     // 提现失败



    //closed -> 正常通话
    //miss  -> 未接
    //miss  -> 拒绝


    /**
     * 判断用户是否在线
     * online offline busy
     * @param [userBean]
     * @return [Boolean]
     */
    @JvmStatic
    fun isOnline(userBean: UserBean): Boolean {

        userBean.onlineStatus ?: return false

        if (userBean.onlineStatus == STATE_ON)
            return true

        return false
    }


    /**
     * 0         1       2    若无数据 默认1
     * online offline busy
     * @param [userBean]
     * @return [Int]
     */
    @JvmStatic
    fun getOnlineState(userBean: UserBean): Int {
        return getOnlineState(userBean.onlineStatus)
    }

    /**
     * 0         1       2    若无数据 默认1
     * online offline busy
     * @param [onlineStatus]
     * @return [Int]
     */
    @JvmStatic
    fun getOnlineState(onlineStatus: String?): Int {

        onlineStatus ?: return STATE_OFF_INT

        return when (onlineStatus) {
            STATE_ON -> {
                STATE_ON_INT
            }
            STATE_BUSY -> {
                STATE_BUSY_INT
            }
            else -> {
                STATE_OFF_INT
            }
        }

    }

    @JvmStatic
    fun getColorIdByOnlineState(onlineStatus: String?): Int {

        onlineStatus ?: return STATE_OFF_INT

        return when (onlineStatus) {
            STATE_ON -> {
                R.color.cl3CD504
            }
            STATE_BUSY -> {
                R.color.clFA434F
            }
            else -> {
                R.color.clCCCCCC
            }
        }

    }

    /**
     * 改变状态对应的点 的颜色
     * @param [onlineStatus]
     * @param [flDot]
     */
    @JvmStatic
    fun changeOnlineStateUI(onlineStatus: String?, flDot: RFrameLayout) {
        if (onlineStatus == null) {
            flDot.visibility = View.VISIBLE
            flDot.helper.apply {
                backgroundColorNormal = DisplayUiTool.getColor(R.color.clCCCCCC)
            }
            return
        }

        flDot.visibility = View.VISIBLE
        val colorIdByOnlineState = getColorIdByOnlineState(onlineStatus)
        flDot.helper.apply {
            backgroundColorNormal = DisplayUiTool.getColor(colorIdByOnlineState)
        }
    }

    // active 接通
    // closed 挂断
    // miss   未接
    fun changeVideoDurationUI(videoDuration: Int, status: String, tvDuration: TextView) {

        when (status) {

            STATE_VIDEO_CLOSED->{
                val color = UiUtils.getColor(R.color.clF4496A)
                tvDuration.setTextColor(color)
                tvDuration.text = tvDuration.context.getString(R.string.reject_call_t)

            }
            STATE_VIDEO_MISS->{
                val color = UiUtils.getColor(R.color.clF4496A)
                tvDuration.setTextColor(color)
                tvDuration.text = tvDuration.context.getString(R.string.missed_calls)
            }
            else->{
                //STATE_VIDEO_ACTIVE
                val color = UiUtils.getColor(R.color.cl6F7480)
                tvDuration.setTextColor(color)
                val toTimeBySecond = EaseDateUtils.toTimeBySecond(videoDuration)
                tvDuration.text = tvDuration.context.getString(R.string.talk_time_an , toTimeBySecond)
            }
        }


    }


    /*
 private const val STATUS_SETTLING           = "settling"           // 结算中
 private const val STATUS_PENDING_ARRIVAL    = "pending_arrival"    // 待到账
 private const val STATUS_WITHDRAWABLE       = "withdrawable"       // 可提现 已到账
 private const val STATUS_REFUNDED           = "refunded";          // 已退款
    * */
    fun changeIncomeUI(status: String?, statusTxt: String?, textView: TextView) {

        when (status) {

            //待到账
            STATUS_PENDING_ARRIVAL->{
                textView.setTextColor(UiUtils.getColor(R.color.clEF8135))
            }
            //可提现 已到账
            STATUS_WITHDRAWABLE->{
                textView.setTextColor(UiUtils.getColor(R.color.cl27B94B))
            }

            //已退款
            STATUS_REFUNDED->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
            }

            //结算中
            else->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
            }
        }


        textView.text = statusTxt


    }


    //approved pending rejected   通过 待审核 拒绝
    const val UNION_STATUS_NONE           = "none"            // 没有提交过任何加入公会申请 (后台没有这个状态)
    const val UNION_STATUS_APPROVED       = "approved"        // 通过
    const val UNION_STATUS_REVIEW         = "pending"         // 审核中
    const val UNION_STATUS_REJECTED       = "rejected"        // 拒绝
    const val UNION_STATUS_EXIT_REVIEW        = "quit_pending"        // 退会中
    const val UNION_STATUS_EXIT_INVALID        = "expired"        // 加入公会 申请失效

    fun changeUnionUI(status: String?, textView: TextView) {

        when (status) {

            //审核中
            UNION_STATUS_REVIEW->{
                textView.text = BaseApp.instance.getString(R.string.union_status_review)
            }

            //没有提交过任何加入公会申请 其他
            else->{
                textView.text = BaseApp.instance.getString(R.string.union_status_join)
            }
        }
    }
    fun changeUnionOutUI(status: String?, textView: TextView) {

        when (status) {

            //审核中
            UNION_STATUS_EXIT_REVIEW->{
                textView.text = BaseApp.instance.getString(R.string.union_status_out_pending)
            }

            //没有提交过任何加入公会申请 其他
            else->{
                textView.text = BaseApp.instance.getString(R.string.union_status_out)
            }
        }
    }


    /*
    *
    *   //approved pending rejected   通过 待审核 拒绝
    const val UNION_STATUS_NONE           = "none"            // 没有提交过任何加入公会申请 (后台没有这个状态)
    const val UNION_STATUS_APPROVED       = "approved"        // 通过
    const val UNION_STATUS_REVIEW         = "pending"         // 审核中
    const val UNION_STATUS_REJECTED       = "rejected"        // 拒绝
    const val UNION_STATUS_EXIT_REVIEW        = "quit_pending"        // 退会中
*/

    fun changeUnionHistoryUI(status: String?, textView: TextView) {

        when (status) {

            UNION_STATUS_APPROVED->{
                textView.setTextColor(UiUtils.getColor(R.color.cl27B94B))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_agree)
            }

            UNION_STATUS_REJECTED->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_reject)
            }

            UNION_STATUS_EXIT_INVALID->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_invalid)
            }

            UNION_STATUS_REVIEW->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_pending)
            }

            else->{
                textView.setTextColor(UiUtils.getColor(R.color.cl9AA1B3))
                textView.text = BaseApp.instance.getString(R.string.join_union_states_pending)
            }
        }




    }

}