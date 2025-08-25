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
// import com.effectsar.labcv.demo.task.UnzipTask // 注释掉不存在的导入
// ByteBeauty相关导入暂时注释掉，避免编译错误
// import com.effectsar.rtceffect.byteBeauty.ByteBeautyActivity
// ByteRTC相关导入暂时注释掉，避免编译错误
// import com.ss.bytertc.engine.*
// import com.ss.bytertc.engine.data.RemoteStreamKey
// import com.ss.bytertc.engine.data.StreamIndex
// import com.ss.bytertc.engine.data.StreamSycnInfoConfig
// import com.ss.bytertc.engine.handler.IRTCRoomEventHandler
// import com.ss.bytertc.engine.handler.IRTCVideoEventHandler
// import com.ss.bytertc.engine.type.ChannelProfile
// import com.ss.bytertc.engine.type.MediaStreamType
// import com.ss.bytertc.engine.type.PublicStreamErrorCode
// import com.ss.bytertc.engine.type.StreamRemoveReason
// import com.ss.bytertc.engine.UserInfo
// import com.ss.bytertc.engine.RTCRoomConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.nio.ByteBuffer

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
        //        initWhenUserLogin()
        initView()
        initClick()


        //打开页面 就做一些清楚操作

        // FiChat71909
        //QuickReplyManager.clearQuickReply("0:1:10841:11516")

    }

    private fun initView() {
        findViewById<View>(R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = true, isKeyboardEnable = false)
        }


        HiLog.e(
            Tag2Common.TAG_RTC,
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

    private fun checkResourceReady() {
        // 注释掉UnzipTask相关代码，因为UnzipTask类不存在
        // val assetPath = this@TestActivity.getExternalFilesDir("assets")
        // val dstFile = File(assetPath, UnzipTask.DIR)
        // if (!dstFile.exists()) {
        //     val task = UnzipTask(this)
        //     task.execute(UnzipTask.DIR)
        // } else {
        //     // checkLicenseReady()
        // }
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

        if (TestData.isCanTest && BuildConfig.DEBUG) {
            binding.ivBack.postDelayed({
                //默认直接加入直播房快速测试
                //Router.toRtcCallActivity("",0) //直播页面
                //Router.toMyInitActivity()  //注册填资料页面
                //Router.toMyPhotoListActivity()
                //BIMManager.instance.createSingleConversationAndEnter(20000)//直接去和 20000id的用户聊天页面
            }, 500)
        }


        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }


        //用户输入的id: 进入聊天页面
        ClickUtils.applySingleDebouncing(binding.bt1) {
            // BIM相关功能暂时注释掉，避免编译错误
            /*
            BIMClient.getInstance()
                .createSingleConversation(
                    userInputChatUserId.toLong(),
                    object : BIMResultCallback<BIMConversation?>() {
                        override fun onSuccess(bimConversation: BIMConversation?) {
                            HiLog.e(
                                Tag2Common.TAG_RTC_IM,
                                "onSuccess  createSingleConversation ： ${bimConversation?.conversationID}"
                            )
                            bimConversation?.conversationID?.let { Router.toBimChatActivity(it, 0) }
                        }

                        override fun onFailed(bimErrorCode: BIMErrorCode) {
                            HiLog.e(
                                Tag2Common.TAG_RTC_IM,
                                "onFailed  createSingleConversation ： $bimErrorCode"
                            )
                        }
                    })
            */
            ToastTool.toast("BIM功能暂时不可用")
        }


        //测试对系统消息:提现和举报结果等自定义消息
        ClickUtils.applySingleDebouncing(binding.bt2) {
            // BIM相关功能暂时注释掉，避免编译错误
            /*
            BIMClient.getInstance()
                .createSingleConversation(
                    userInputChatUserId.toLong(),
                    object : BIMResultCallback<BIMConversation?>() {
                        override fun onSuccess(bimConversation: BIMConversation?) {
                            HiLog.e(
                                Tag2Common.TAG_RTC_IM,
                                "onSuccess  createSingleConversation ： ${bimConversation?.conversationID}"
                            )
                            BIMManager.instance.sendMsg(
                                "一条来自安卓的测试消息",
                                bimConversation?.conversationID.toString()
                            )
                        }

                        override fun onFailed(bimErrorCode: BIMErrorCode) {
                            HiLog.e(
                                Tag2Common.TAG_RTC_IM,
                                "onFailed  createSingleConversation ： $bimErrorCode"
                            )
                        }
                    })
            */
            ToastTool.toast("BIM功能暂时不可用")
        }


        //将Token本地存储时间无效
        ClickUtils.applySingleDebouncing(binding.bt3) {
            //val l = HiRealCache.exTokenTime * 10
            //LoginManager.saveUserTokenTimeToMMKV((System.currentTimeMillis() - l).toString())
            //HiRealCache.user = null
            HiRealCache.userToken = ""
        }


        //测试接口
        ClickUtils.applySingleDebouncing(binding.bt4) {
            // ByteBeautyActivity暂时注释掉，避免编译错误
            // startActivity(Intent(this@TestActivity, ByteBeautyActivity::class.java))

            /*val mutableListOf = mutableListOf<String>()
            mutableListOf.add("10517")
            mutableListOf.add("10508")
            mutableListOf.add("10516")
            model.getUserList(mutableListOf, failed = { _, _ ->

            }, success = {

            })*/
        }


        //测试Toast
        ClickUtils.applySingleDebouncing(binding.bt5) {

            try {

                var userString= "{\n" +
                        "\"id\":10700,\n" +
                        "\"kol_id\":62,\n" +
                        "\"avatar\":\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/6876680ac7b4e.png\",\n" +
                        "\"token\":\"4932|NTeriglzn8pxWU0JruNX9AfRIiwoQmJ1odJvBNTpe30706ce\",\n" +
                        "\"gender\":\"M\",\n" +
                        "\"name\":\"\",\n" +
                        "\"nickname\":\"sun\",\n" +
                        "\"age\":25,\n" +
                        "\"email\":\"wlf-21@163.com\",\n" +
                        "\"created_at\":\"2025-07-15T14:37:46.000000Z\",\n" +
                        "\"updated_at\":\"2025-08-21T03:56:41.000000Z\",\n" +
                        "\"uuid\":\"FiChat:25503\",\n" +
                        "\"invite_code\":\"25503\",\n" +
                        "\"birthday\":\"2000-07-16\",\n" +
                        "\"country_code\":\"US\",\n" +
                        "\"lang_codes\":\"id,zh,en,ar\",\n" +
                        "\"im_token\":\"AceqTTSAcJ1z0sPHXunI19tHan5EPkC21RY7KLnOUG1PEKYzL6Gozg\",\n" +
                        "\"rtc_token\":\"00168778226b996380175060aa0NgBkGgEA+pimaPolzmgBACoFADEwNzAwBQAAAAAAAAABAAqnpmgCAAqnpmgDAAqnpmgEAAqnpmggAAHDoqgnBCP01sx3MtB0dlNw7pykWw7qNTm5ptYUoY/S\",\n" +
                        "\"images\":[\n" +
                        "\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/68766824b4993.png\",\n" +
                        "\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/687668236f0b0.png\",\n" +
                        "\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/6876680338b06.png\",\n" +
                        "\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/6876680b87142.png\",\n" +
                        "\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/68766803e39b5.png\",\n" +
                        "\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250715/687667ec9fc01.png\"\n" +
                        "],\n" +
                        "\"country\":{\n" +
                        "\"country_code\":\"US\",\n" +
                        "\"country_image\":\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250331/67e9fd04ef84b.png\",\n" +
                        "\"country_name\":\"美国\"\n" +
                        "},\n" +
                        "\"is_fresh\":0,\n" +
                        "\"info\":\"yo\",\n" +
                        "\"online_status\":\"online\",\n" +
                        "\"video_gold_price\":100,\n" +
                        "\"service_status\":1,\n" +
                        "\"isClicked\":false,\n" +
                        "\"post_videos\":[\n" +
                        "{\n" +
                        "\"files\":\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/video/20250808/689557b6b0272.mp4\",\n" +
                        "\"id\":342,\n" +
                        "\"is_top\":0,\n" +
                        "\"kol_id\":62,\n" +
                        "\"type\":\"video\"\n" +
                        "}\n" +
                        "],\n" +
                        "\"video_call_count\":106,\n" +
                        "\"money\":119278,\n" +
                        "\"vip\":0,\n" +
                        "\"kol_rank\":\"\"\n" +
                        "}"
                val fromJson = userString.fromJson<UserBean>()
                if (fromJson != null) {
                    // RtcVideoManager暂时注释掉，避免编译错误
                    // RtcVideoManager.instance.sendInviteVideoCallMsg(fromJson)
                }
            } catch (e: Exception) {
                ToastTool.toastError("发送失败")
            }


        }


        //测试投诉接口
        ClickUtils.applySingleDebouncing(binding.bt6) {
            model.report(
                "色情内容",
                "色情内容",
                HiRealCache.user?.kolId.toString(),
                success = { report ->


                },
                failed = { _, _ ->

                })
        }


        //测试处理投诉接口(在输入框输入提交投诉的id)
        ClickUtils.applySingleDebouncing(binding.bt7) {
            model.reportUpdate(userInputChatUserId, "1", success = {

                ToastTool.toast("处理投诉成功")

            }, failed = { _, _ ->

            })
        }


        //播放视频铃声
        ClickUtils.applySingleDebouncing(binding.bt8) {
            // RtcVideoManager暂时注释掉，避免编译错误
            // RtcVideoManager.instance.initMediaPlayerAndPlay()
        }


        //发送一条文本带视频或图片的消息
        ClickUtils.applySingleDebouncing(binding.bt9) {


            // 10130 mark
            if (HiRealCache.user?.images.isNullOrEmpty()) {
                ToastTool.toast(com.dubu.home.R.string.toast_send_fail)
                return@applySingleDebouncing
            }

            val imageUrl = HiRealCache.user?.images?.get(0) ?: ""

            //https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/video/20250717/687862d50fa17.mp4
            val videoUrl = if (HiRealCache.user?.userVideo.isNullOrEmpty()) {
                ""
            } else {
                HiRealCache.user?.userVideo?.get(0)?.files ?: ""
            }

            showLoadingDialog()

            val hiStr = if (HiRealCache.helloList.isNullOrEmpty()) {
                getString(com.dubu.home.R.string.hello_default)
            } else {
                val random = NumberTool.random(0, HiRealCache.helloList!!.size)
                HiRealCache.helloList!![random]
            }

            model.getUserInfo(userInputChatUserId, success = { data ->

                // BIMManager暂时注释掉，避免编译错误
                /*
                BIMManager.instance.createSingleConversationAndSendCustomHiMsg(
                    data.userSn,
                    hiStr,
                    imageUrl,
                    videoUrl,
                    0,
                    data,
                    success = { _, _ ->
                        dismissLoadingDialog()
                        ToastTool.toast(com.dubu.home.R.string.toast_send_ok)

                        BIMManager.instance.createSingleConversationAndEnter(userInputChatUserId.toLong())

                    },
                    failed = { _, _ ->
                        dismissLoadingDialog()
                        ToastTool.toast(com.dubu.home.R.string.toast_send_fail)
                    },
                )
                */
                dismissLoadingDialog()
                ToastTool.toast("BIM功能暂时不可用")


            }, failed = { _, _ ->
                dismissLoadingDialog()
                ToastTool.toast(com.dubu.home.R.string.toast_send_fail)
            })


        }


        //RTC
        ClickUtils.applySingleDebouncing(binding.bt10) {
            //登录 进房间
            // initRtc() // 暂时注释掉RTC初始化
        }


        //点击返回首页走升级弹框业务逻辑 强制
        ClickUtils.applySingleDebouncing(binding.bt11) {
            HiRealCache.appConfigBean =
                AppConfigBean(
                    "1",
                    "1",
                    "https://fichat.qiepian.vip/fichat/assets/fichat111.apk",
                    "修复若干BUG",
                    "无",
                    "100",
                    "5",
                    "1000"
                )
            EventManager.postSticky(EventKey.IS_CAN_GOTO_TEST_UPDATE_APP, 1)
            finish()
        }


        //点击返回首页走升级弹框业务逻辑 可选
        ClickUtils.applySingleDebouncing(binding.bt12) {
            HiRealCache.appConfigBean =
                AppConfigBean(
                    "1",
                    "0",
                    "https://fichat.qiepian.vip/fichat/assets/fichat111.apk",
                    "修复若干BUG",
                    "无",
                    "100",
                    "5",
                    "1000"
                )
            EventManager.postSticky(EventKey.IS_CAN_GOTO_TEST_UPDATE_APP, 0)
            finish()
        }


        //清空前50条会话
        ClickUtils.applySingleDebouncing(binding.bt13) {
            // clearConversation暂时注释掉，避免编译错误
            // clearConversation()
            ToastTool.toast("清空会话功能暂时不可用")
        }
    }

    // BIM相关方法暂时注释掉，避免编译错误
    /*
    private fun clearConversation() {
        // BIM会话清理代码
    }
    */

    /*
        ╔════════════════════════════════════════════════════════════════════════════════════════╗
        ║   PS: 火山sdk API调用
        ╚════════════════════════════════════════════════════════════════════════════════════════╝
       */
    // RTC SDK相关变量暂时注释掉，避免编译错误
    /*
    private var rtcVideo: RTCVideo? = null
    private var callback: RtcVideoSimCallback? = null
    fun setCallback(cb: RtcVideoSimCallback?) {
        callback = cb
    }
    private var login: Int = -1
    */

    // RTC视频事件处理器暂时注释掉，避免编译错误
    /*
    private var rtcVideoEventHandler: IRTCVideoEventHandler = object : IRTCVideoEventHandler() {
        // RTC事件处理代码
    }
    */


    // 用户登录初始化方法暂时注释掉，避免编译错误
    /*
    private fun initWhenUserLogin() {
        // RTC登录初始化代码
    }
    */


    /*
      ╔════════════════════════════════════════════════════════════════════════════════════════╗
      ║   PS:
      ╚════════════════════════════════════════════════════════════════════════════════════════╝
   */


    //rtc sdk
    // RTC相关变量暂时注释掉，避免编译错误
    // private var rtcVideo: RTCVideo? = null
    // private var rtcRoom: RTCRoom? = null
    // private var roomId: String = "123456001DB123456002"
    // private var userId: String = "123456002"
    // private var rtcToken: String = "..."

    // RTC相关方法暂时注释掉，避免编译错误
    /*
    private fun initRtc() {
        // RTC初始化代码
    }

    private fun setLocalRenderView() {
        // 本地渲染视图设置
    }

    private fun setRemoteRenderView(uid: String) {
        // 远端渲染视图设置
    }

    private fun joinRoom(roomId: String) {
        // 加入房间
    }

    private fun leaveRoom() {
        // 离开房间
    }
    */

    override fun onDestroy() {
        super.onDestroy()
        // leaveRoom() // 暂时注释掉
    }


    // RTC事件处理器暂时注释掉，避免编译错误
    /*
    private var rtcRoomEventHandler: IRTCRoomEventHandler = object : IRTCRoomEventHandler() {

        override fun onUserPublishStream(uid: String, type: MediaStreamType) {
            super.onUserPublishStream(uid, type)
            HiLog.e(
                Tag2Common.TAG_RTC,
                "           onUserPublishStream ::: uid = $uid , type = $type"
            )
            runOnUiThread { setRemoteRenderView(uid) }
        }

        override fun onUserLeave(uid: String?, reason: Int) {
            super.onUserLeave(uid, reason)
            HiLog.e(
                Tag2Common.TAG_RTC,
                "           onUserLeave ::: uid = ${uid} , reason = $reason"
            )

        }

    }
    */

    // 移除UnzipTask.IUnzipViewCallback接口的实现方法
    // override fun getContext(): Context {
    //     return applicationContext
    // }
    //
    // override fun onStartTask() {
    //     HiLog.e(
    //         Tag2Common.TAG_12316,
    //         " onStartTask ZIP"
    //     )
    // }
    //
    // override fun onEndTask(result: Boolean) {
    //     HiLog.e(
    //         Tag2Common.TAG_12316,
    //         " onEndTask ZIP result = $result"
    //     )
    // }


}
