package com.dubu.rtc.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.R
import com.dubu.common.beans.common.ShareBean
import com.dubu.common.constant.Consts2Common
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.BaseBottomDialog
import com.dubu.common.helper.LinkHelper
import com.dubu.common.utils.*
import com.dubu.common.views.cview.BubblePop
import com.dubu.me.vm.CommonViewModel
import com.hikennyc.view.MultiStateAiView
import java.net.URLEncoder

/**
 * 分享
 * Author:v
 * Time:2020/11/25
 * @author cq
 * @date 2025/06/25
 * @constructor 创建[ShareDialog]
 */
class ShareDialog : BaseBottomDialog() {
    override val TAG: String = "EditTextDialog"

    private var shareBean: ShareBean? = null
    private var copyStr: String? = null


    private var multiStateView: MultiStateAiView? = null
    private var clLoading: ConstraintLayout? = null
    private var clChange: ConstraintLayout? = null
    private var tvDesSum: TextView? = null
    private var tvLink: TextView? = null

    private var onActionChoose: ((action: String) -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_share
    }


    fun withTypeChooseListener(listener: ((action: String) -> Unit)): ShareDialog {
        onActionChoose = listener
        return this
    }

    // 使用 DialogFragment 作用域的 ViewModel
    private val model: CommonViewModel by viewModels()


    override fun initView(root: View) {


        root.findViewById<View>(R.id.ivClose).setOnClickListener {
            dismiss()
        }


        multiStateView = root.findViewById<MultiStateAiView>(R.id.multiStateView)

        clLoading = root.findViewById<ConstraintLayout>(R.id.clLoadingRoot)
        clChange = root.findViewById<ConstraintLayout>(R.id.clChange)

        val tvChange = root.findViewById<TextView>(R.id.tvChange)
        val tvCopy = root.findViewById<TextView>(R.id.tvCopy)
        tvLink = root.findViewById(R.id.tvLink)

        tvDesSum = root.findViewById(R.id.tvDesSum)


        val customFont = Typeface.createFromAsset(context?.assets, "font/Ubuntu-Medium.ttf")
        tvChange.typeface = customFont

        val customFont1 = Typeface.createFromAsset(context?.assets, "font/Ubuntu-Medium.ttf")
        tvCopy.typeface = customFont1

        httpEngine()


        initClick(root)

        //点击


    }


    private fun initClick(root: View) {
        val tvFb = root.findViewById<View>(R.id.tvFb)
        ClickUtils.applySingleDebouncing(tvFb) {
            val fb = getString(R.string.chat_title_vr_share_fb)
            val act = requireActivity()
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_FB, act.packageManager)) {
                BubblePop.instance.showText(act, act.getString(R.string.no_install, fb))
                return@applySingleDebouncing
            }
            val channelType = Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_FB
            gotoShare(channelType)

        }


        val tvWs = root.findViewById<View>(R.id.tvWs)
        ClickUtils.applySingleDebouncing(tvWs) {
            val fb = getString(R.string.chat_title_vr_share_whats)
            val act = requireActivity()
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_WA, act.packageManager)) {
                BubblePop.instance.showText(act, act.getString(R.string.no_install, fb))
                return@applySingleDebouncing
            }
            val channelType = Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_WHATS
            gotoShare(channelType)
        }


        val tvLi = root.findViewById<View>(R.id.tvLi)
        ClickUtils.applySingleDebouncing(tvLi) {
            val fb = getString(R.string.chat_title_vr_share_line)
            val act = requireActivity()
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_LN, act.packageManager)) {
                BubblePop.instance.showText(act, act.getString(R.string.no_install, fb))
                return@applySingleDebouncing
            }
            val channelType = Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_LINE
            gotoShare(channelType)
        }


        val tvTw = root.findViewById<View>(R.id.tvTw)
        ClickUtils.applySingleDebouncing(tvTw) {
            val fb = getString(R.string.chat_title_vr_share_twitter)
            val act = requireActivity()
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_TT, act.packageManager)) {
                BubblePop.instance.showText(act, act.getString(R.string.no_install, fb))
                return@applySingleDebouncing
            }
            val channelType = Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_TWITTER
            gotoShare(channelType)
        }


        val tvIg = root.findViewById<View>(R.id.tvIg)
        ClickUtils.applySingleDebouncing(tvIg) {
            val fb = getString(R.string.chat_title_vr_share_telegram)
            val act = requireActivity()
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_TAM, act.packageManager)) {
                BubblePop.instance.showText(act, act.getString(R.string.no_install, fb))
                return@applySingleDebouncing
            }
            val channelType = Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_TELEGRAM
            gotoShare(channelType)
        }

        val clCopy = root.findViewById<View>(R.id.clCopy)
        ClickUtils.applySingleDebouncing(clCopy) {
            copy()
        }

        ClickUtils.applySingleDebouncing(clChange) {
            httpEngine()
        }


    }

    private fun gotoShare(channelType: Int) {

        val c = copyStr ?:getString(R.string.app_name)
        //FB
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_FB) {

            //val test = "https://fanyi.baidu.com/#en/zh/variable"
            //val test = "https://www.sojson.com/"
            // com.vc.hichat.dev    com.chat.yolo
            //val test = "https://play.google.com/store/apps/details?id=com.chat.yolo/zl"
            share(Consts2Common.PACK_NAME_FB, c)
        }

        // line
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_LINE) {
            /*shareToLine(
                "https://fanyi.baidu.com/#en/zh/variable",
                "Hello",
                " I am playing this APP, click to download"
            )*/
            share(Consts2Common.PACK_NAME_LN, c)
        }

        //MG
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_MG) {
            share(Consts2Common.PACK_NAME_MSG, c)
        }
        //WHATS
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_WHATS) {
            share(Consts2Common.PACK_NAME_WA, c)
        }
        //TWITTER
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_TWITTER) {
            share(Consts2Common.PACK_NAME_TT, c)
        }
        //telegram
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_TELEGRAM) {
            share(Consts2Common.PACK_NAME_TAM, c)
        }


        //copy link
        if (channelType == Consts2Common.COMMON_V_SHARE_CHANNEL_TYPE_LINK) {
            copy()
        }


    }


    private fun shareToLine(uriString: String?, title: String?, text: String?) {
        val urlStr = StringBuilder("line://msg/")
        urlStr.append("text/")
        urlStr.append(
            URLEncoder.encode(
                title + "\n" +
                        text + "\n"
                        + uriString, "UTF-8"
            )
        )
        val uri = Uri.parse(urlStr.toString())
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun copy() {
        val content = copyStr
        val clipBoard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText("text", content)
        clipBoard.setPrimaryClip(data)
        ToastTool.toast(R.string.copied_to_clipboard)
    }

    private fun share(pkg: String, content: String) {
        val shareIntent = Intent()
        shareIntent.setPackage(pkg)
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"

        try {
            startActivity(Intent.createChooser(shareIntent, "Share To"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun httpEngine() {
        showLoading()
        model.getShareInfo(success = {
            dismissLoading()
            shareBean = it
            changeUI()
            showSuccessEngine()
        }, failed = { _, _ ->
            dismissLoading()
            showErrorEngine()
            ToastTool.toastError(R.string.toast_err_service)
        })
    }

    private fun changeUI() {
        val title: String = shareBean?.shareText ?: getString(R.string.app_name)
        tvDesSum?.text = title
        val id = (HiRealCache.user?.kolId ?:0).toString()
        val inviteCode = (HiRealCache.user?.inviteCode ?:"0")
        val toWebUserInfo = LinkHelper.toWebUserInfo(id, inviteCode)

        val sp= StringBuilder(title)
        sp.append("\n")
        sp.append(toWebUserInfo)
        copyStr = sp.toString()

        HiLog.e(Tag2Common.TAG_12300, "toWebUserInfo = $toWebUserInfo")
        HiLog.e(Tag2Common.TAG_12300, "copyStr = $copyStr")

        TextViewUtils.setDrawableFirstTxt(
            tvLink,
            toWebUserInfo,
            R.drawable.ic_link,
            ImageSpan.ALIGN_BASELINE
        )


    }

    private fun showLoading() {
        clLoading?.visibility = View.VISIBLE
        clLoading?.setOnClickListener {  }
    }
    private fun dismissLoading() {
        clLoading?.visibility = View.GONE
        clLoading?.setOnClickListener(null)
    }

    private fun showErrorEngine() {
        multiStateView?.viewState = MultiStateAiView.ViewState.ERROR
        multiStateView?.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        multiStateView?.viewState = MultiStateAiView.ViewState.CONTENT
    }
}