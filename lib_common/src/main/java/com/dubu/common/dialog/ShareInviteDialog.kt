package com.dubu.common.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.annotation.Keep
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.constant.Consts2Common
import com.dubu.common.databinding.DialogShareInviteBinding
import com.dubu.common.utils.CommonTool
import com.dubu.common.utils.DisplayMetricsTool
import com.dubu.common.views.cview.BubblePop

/**
 * Author:v
 * Time:2020/11/25
 */
class ShareInviteDialog : DialogFragment(), View.OnClickListener {
    private val TAG = "ShareInviteDialog"

    private var cancelOut = true
    private var canCancel = true
    private var binding: DialogShareInviteBinding? = null

    private var lastIndex = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogShareInviteBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            clickListener = this@ShareInviteDialog
        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dd_btn_change -> changeContent()
            R.id.dd_btn_copy -> copyContent()
            R.id.iv_fb -> share2Fb()
            R.id.iv_twitter -> share2Twitter()
            R.id.iv_share_phone -> shareSystem()
            R.id.iv_share_link -> copyLink()
            R.id.iv_close -> dismiss()
        }
    }

    private fun copyLink() {

    }


    private fun shareSystem() {
        activity?.let {
            val link = "list[lastIndex]"
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, link)
            sendIntent.type = "text/plain"
            it.startActivity(Intent.createChooser(sendIntent, "share to"))
        }
    }

    private fun share2Twitter() {
        activity?.let { act ->
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_TT, act.packageManager)) {
                BubblePop.instance.showText(act, R.string.no_twitter)
                return
            }
            val c = "list[lastIndex]"
            share(Consts2Common.PACK_NAME_TT, c)
        }
    }

    private fun share2Fb() {
        activity?.let { act ->
            if (!CommonTool.isPackageInstalled(Consts2Common.PACK_NAME_FB, act.packageManager)) {
                BubblePop.instance.showText(act, R.string.no_facebook)
                return
            }
            val c = "list[lastIndex]"
            share(Consts2Common.PACK_NAME_FB, c)
        }

    }

    private fun copyContent() {
        val c = "list[lastIndex]"
        copy(c, false)
    }

    private fun changeContent() {

    }

    fun withCancelOutside(cancel: Boolean): ShareInviteDialog {
        cancelOut = cancel
        return this
    }

    fun withCancelable(cancel: Boolean): ShareInviteDialog {
        canCancel = cancel
        return this
    }


    override fun onStart() {
        super.onStart()
        setWindow()
    }


    private fun setWindow() {
        dialog?.apply {
            val w = (DisplayMetricsTool.getScreenWidth(context) * 1.00f).toInt()
            window?.run {
                attributes = attributes.apply {
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    width = w
                    dimAmount = 0.6f
                }
                setGravity(Gravity.BOTTOM)
                setBackgroundDrawableResource(android.R.color.transparent)
            }
            setCanceledOnTouchOutside(cancelOut)
            setCancelable(canCancel)

        }
    }


    fun show(manager: FragmentManager) {
        if (isActivityDead()) return
        try {
            manager.beginTransaction().remove(this).commitAllowingStateLoss()
            super.show(manager, TAG)
        } catch (e: Exception) {
        }
    }


    private fun isActivityDead(): Boolean {
        activity?.let {
            return it.isFinishing || it.isDestroyed
        }
        return false
    }

    override fun dismiss() = try {
        BubblePop.instance.removeNow()
        dismissAllowingStateLoss()
    } catch (e: Exception) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun share(pkg: String, content: String) {
        val shareIntent = Intent()
        shareIntent.setPackage(pkg)
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"

        try {
            context?.startActivity(Intent.createChooser(shareIntent, "Share To"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copy(content: String, isLink: Boolean) {
        activity?.let {
            val clipBoard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("text", content)
            clipBoard.setPrimaryClip(data)
            BubblePop.instance.showCheck(
                it,
                if (isLink) R.string.copied_invite_link
                else R.string.copied_to_clipboard
            )
        }
    }

}