package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.manager.SettingsManager
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.ToastTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.PhotoMultiItemAdapter
import com.dubu.me.databinding.ActivityMeSetPriceBinding
import com.dubu.me.vm.MineViewModel


/**
 * 重要设置 : 通话价格 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MySetPriceActivity]
 */
class MySetPriceActivity : BaseBindingActivity<ActivityMeSetPriceBinding>() {


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MySetPriceActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_set_price
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: PhotoMultiItemAdapter by lazy {
        PhotoMultiItemAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        if (HiRealCache.user?.videoGoldPrice == null || HiRealCache.user?.videoGoldPrice!! <= 0) {
            HiLog.e(Tag2Common.TAG_123XX, "key1 = ${HiRealCache.user?.videoGoldPrice}")
            val videoGoldPrice = HiRealCache.videoGoldDefaultPrice ?:100
            binding.etMsg.setText("$videoGoldPrice")
            binding.tvTip1.text = getString(R.string.set_call_price_content,videoGoldPrice.toString())
        } else {
            HiLog.e(Tag2Common.TAG_123XX, "key2 = ${HiRealCache.user?.videoGoldPrice}")
            val videoGoldPrice = HiRealCache.user?.videoGoldPrice ?: 10
            binding.etMsg.setText("$videoGoldPrice")
            binding.tvTip1.text = getString(R.string.set_call_price_content,videoGoldPrice.toString())
        }
        val videoGoldPrice = HiRealCache.videoGoldDefaultPrice ?:100
        binding.tvTip.text = getString(R.string.default_price_content,videoGoldPrice.toString())
    }


    private var userInput: String = "" //用户输入的文本 : 昵称
    private val model: MineViewModel by viewModels()
    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.tvNext) {

            try {
                val toLong = userInput.toLong()
                if (toLong <= 0) {
                    ToastTool.toastError(R.string.plz_enter_valid_price)
                } else {
                    showLoadingDialog()
                    model.updateUserPrice(userInput, success = {
                        dismissLoadingDialog()
                        binding.tvTip1.setText(getString(R.string.set_call_price_content,userInput))
                        SettingsManager.savePriceToMMKV(toLong.toString())
                        finish()
                    }, failed = { _, _ ->
                        dismissLoadingDialog()
                        ToastTool.toastError(R.string.toast_err_service)
                    })

                }
            } catch (e: Exception) {
                ToastTool.toastError(R.string.plz_enter_valid_price)
            }

        }


        //编辑文本 发送消息
        bindingApply {
            etMsg.addTextChangedListener { eb ->
                val trim = eb.toString().trim()
                val en = ((trim.length) > 0)
                userInput = if (en) {
                    eb.toString().trim()
                } else {
                    ""
                }
            }

            tvDD.setOnClickListener {
                val videoGoldPrice = HiRealCache.videoGoldDefaultPrice ?:100
                etMsg.setText("$videoGoldPrice")
                etMsg.setSelection("$videoGoldPrice".length)
            }
        }

    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (!isTouchInsideViewLine(ev, binding.etMsg)) {
                KeyboardUtils.hideSoftInput(binding.etMsg)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    /**
     * 判断点击事件的y是不是在view之上
     *  适用于底部输入框 消息键盘
     * @param [event]
     * @param [targetView]
     * @return [Boolean]
     */
    private fun isTouchAboveView(event: MotionEvent, targetView: View): Boolean {
        val location = IntArray(2)
        targetView.getLocationOnScreen(location)
        return event.rawY.toInt() < location[1]
    }

    /**
     * 判断点击的y是不是在view 所在的那一行  并不是判断是不是在内部
     * @param [event]
     * @param [view]
     * @return [Boolean]
     */
    private fun isTouchInsideViewLine(event: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val y = event.rawY.toInt()
        val viewH = Math.abs(view.top - view.bottom)
        val startY = location[1]
        val endY = location[1] + viewH
        return y in startY..endY
    }

}
