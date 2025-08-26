package com.dubu.main.test


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.TextureView
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ThreadUtils
// BIM相关导入暂时注释掉，避免编译错误
// import com.bytedance.im.core.api.BIMClient
// import com.bytedance.im.core.api.enums.BIMErrorCode
// import com.bytedance.im.core.api.interfaces.BIMResultCallback
// import com.bytedance.im.core.api.interfaces.BIMSimpleCallback
// import com.bytedance.im.core.api.model.BIMConversation
// import com.bytedance.im.core.api.model.BIMConversationListResult
import com.dubu.common.base.BaseApp
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.apk.AppConfigBean
import com.dubu.common.beans.rtc.RTCCallMsgBean
import com.dubu.common.constant.Constants
import com.dubu.common.constant.Tag2Common
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.common.ext.MoshiExt
import com.dubu.common.ext.fromJson
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.*
import com.dubu.main.BuildConfig
import com.dubu.main.R
import com.dubu.main.databinding.ActivityTestTestBinding
import com.dubu.me.vm.CommonViewModel
import com.dubu.test.testdata.TestData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * 测试页面
 * @author cq
 * @date 2025/06/11
 * @constructor 创建[TestActivity]
 */
@Route(path = RouteConst.ACTIVITY_TEST)
class TestActivity : BaseBindingActivity<ActivityTestTestBinding>() { //end


    private var chatType: Int = -1 //-1标识无效  0全能通话Ui 1标识客服Ui需要精简一些ui
    private var conversationID: String? = null //会话id

    private var userInputChatUserId: String =
        Constants.RTC_UID_FOR_SYSTEM_MSG //用户输入的文本 : 发送自定义系统消息的id

    private val model: CommonViewModel by viewModels()

    companion object {

        fun startAction(context: Activity) {
            val intent = Intent(context, TestActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_test_test
    }

    override fun onCreated() {
        chatType = intent.getIntExtra(RouteConst.P_TYPE, -1)
        conversationID = intent.getStringExtra(RouteConst.P_JSON)
        initView()
        initClick()


    }

    private fun initView() {
        findViewById<View>(R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = true, isKeyboardEnable = false)
        }


        HiLog.e(
            Tag2Common.TAG_12300,
            "获取设备的deviceId ： ${HiRealCache.deviceId}"
        )

        /*lifecycleScope.launch(Dispatchers.IO) {
            checkResourceReady()
        }*/


        lifecycleScope.launch(Dispatchers.IO) {
            val rootPath =
                File("/storage/emulated/0/Android/data/com.dabai.kiss.chat/files/assets/resource/cvlab/")
            FileTreePrinter.printFileTreeWithMaxDepth(rootPath)
        }
    }


    private fun initClick() {

        //编辑文本 发送消息
        bindingApply {
            etMsg.addTextChangedListener { eb ->
                val trim = eb.toString().trim()
                val en = ((trim.length) > 0)
                userInputChatUserId = if (en) {
                    eb.toString().trim()
                } else {
                    ""
                }
            }
        }


        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }


    }

}
