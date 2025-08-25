package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.me.VideoDataBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.ToastTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.common.utils.picker.helper.GlideEngine
import com.dubu.me.R
import com.dubu.me.databinding.ActivityMePublishVideoBinding
import com.dubu.me.vm.MineViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


/**
 * 发布视频页面
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MyPublishVideoActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_PUBLISH_VIDEO)
class MyPublishVideoActivity : BaseBindingActivity<ActivityMePublishVideoBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyPublishVideoActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_publish_video
    }

    override fun onCreated() {

        initView()
        initClick()
    }


    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)


    }

    private var path = ""
    private val model: MineViewModel by viewModels()
    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.rootVideo) {

            //相册播放
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(path), "video/*")
            startActivity(intent)

        }
        ClickUtils.applySingleDebouncing(binding.ivClose) {

            path = ""

            binding.rootAdd.visibility = View.VISIBLE
            binding.rootVideo.visibility = View.GONE

        }

        //打开本地相册 选择视频
        ClickUtils.applySingleDebouncing(binding.rootAdd) {

            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofVideo())
                .setSelectMaxFileSize(1024 * 20)
                .setMaxSelectNum(1)
                .setMinSelectNum(1)
                .setImageEngine(GlideEngine.create())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        HiLog.e(
                            Tag2Common.TAG_RTC_IM,
                            "获取相册 ${
                                result?.get(0)?.toString()
                            } = ${result?.get(0)?.realPath} = ${result?.get(0)?.path}"
                        )

                        path = result?.get(0)?.realPath ?: ""

                        showVideo(path)


                    }

                    override fun onCancel() {
                        HiLog.e(
                            Tag2Common.TAG_RTC_IM,
                            "获取相册 onCancel"
                        )
                    }
                })


        }


        ClickUtils.applySingleDebouncing(binding.tvNext) {


            if (path.isEmpty()){
                ToastTool.toast(R.string.tip_plz_select_one_video)
                return@applySingleDebouncing
            }

            showLoadingDialog()

            HiLog.e(
                Tag2Common.TAG_12305,
                "fileUpload : path = $path }"
            )
            val file: File = File(path)
            val fileBody = file.asRequestBody("multipart/form-data".toMediaType())

            val formData =
                MultipartBody.Part.createFormData("file", file.name, fileBody)


            model.fileUpload(fileBody, formData, success = { data ->


                HiLog.e(
                    Tag2Common.TAG_12305,
                    "fileUpload success : data = $data }"
                )


                val mutableListOf = mutableListOf<VideoDataBean>()
                mutableListOf.add(VideoDataBean(data.path ?: "",data.cover_path ?: ""))


                model.publishVideoV2(
                    mutableListOf,
                    success = { str ->
                        HiLog.e(
                            Tag2Common.TAG_12305,
                            "publishVideo success : str = $str }"
                        )

                        ToastTool.toast(R.string.published_successfully)
                        Router.toMyPhotoListActivity()
                        finish()
                        runOnUiThread {
                            dismissLoadingDialog()
                        }
                        path = ""

                        binding.rootAdd.visibility = View.VISIBLE
                        binding.rootVideo.visibility = View.GONE
                    },
                    failed = { _, _ ->
                        ToastTool.toastError(R.string.toast_err_service)
                        HiLog.e(
                            Tag2Common.TAG_12305,
                            "publishVideo failed  }"
                        )

                        runOnUiThread {
                            dismissLoadingDialog()
                        }

                    })

            }, failed = { code, msg ->
                ToastTool.toastError(R.string.toast_err_service)
                runOnUiThread {
                    dismissLoadingDialog()
                }

                HiLog.e(
                    Tag2Common.TAG_12305,
                    "fileUpload failed : msg = $msg }"
                )

            })

        }

    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private fun showVideo(url: String) {

        binding.rootAdd.visibility = View.GONE
        binding.rootVideo.visibility = View.VISIBLE

        GlideTool.loadVideoFrame(
            url,
            binding.ivVideo,
            0,
            R.color.cl1C1C1C
        )


    }

}
