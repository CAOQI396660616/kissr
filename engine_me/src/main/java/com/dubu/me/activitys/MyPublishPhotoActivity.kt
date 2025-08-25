package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.me.PhotoDataBean
import com.dubu.common.beans.me.PhotoMultiTypeBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.CancelSureDialog
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.ToastTool
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.common.utils.picker.helper.GlideEngine
import com.dubu.me.R
import com.dubu.me.adapters.PhotoBigMultiItemAdapter
import com.dubu.me.databinding.ActivityMePublishPhotoBinding
import com.dubu.me.vm.MineViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


/**
 * 发布图片页面
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MyPublishPhotoActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_PUBLISH_PHOTO)
class MyPublishPhotoActivity : BaseBindingActivity<ActivityMePublishPhotoBinding>() {

    private val model: MineViewModel by viewModels()

    private var urlFile: String? = ""


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyPublishPhotoActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_publish_photo
    }

    override fun onCreated() {

        initView()
        initClick()
    }

    private val mAdapter: PhotoBigMultiItemAdapter by lazy {
        PhotoBigMultiItemAdapter()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)


        bindingApply {
            with(recyclerView) {
                layoutManager =
                    GridLayoutManager(this@MyPublishPhotoActivity, 2, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }
        }
        val mutableListOf = mutableListOf<PhotoMultiTypeBean>()

        val photoMultiTypeBean =
            PhotoMultiTypeBean(PhotoMultiTypeBean.ITEM_TYPE_ADD, PhotoDataBean(""))
        mutableListOf.add(photoMultiTypeBean)


        mAdapter.setList(mutableListOf)


        mAdapter.setOnItemClickListener { adapter, view, position ->
            val photoTypeBean = mAdapter.data[position]

            if (photoTypeBean.type == PhotoMultiTypeBean.ITEM_TYPE_ADD) {


                PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .setMaxSelectNum(6)
                    .setMinSelectNum(1)
                    .setImageEngine(GlideEngine.create())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {
                            result?.forEachIndexed { index, localMedia ->
                                val picPath = result[index]?.realPath ?: ""
                                HiLog.e(
                                    Tag2Common.TAG_RTC_IM,
                                    "获取相册  $index =  $picPath"
                                )

                                HiLog.e(Tag2Common.TAG_12309, "获取相册 $index = $picPath")

                                val photo = PhotoMultiTypeBean(
                                    PhotoMultiTypeBean.ITEM_TYPE_PHOTO,
                                    PhotoDataBean(picPath)
                                )
                                mAdapter.addData(0, photo)

                            }

                        }

                        override fun onCancel() {
                            HiLog.e(
                                Tag2Common.TAG_RTC_IM,
                                "获取相册 onCancel"
                            )
                        }
                    })



            } else {


                CancelSureDialog()
                    .withTitle(R.string.kind_tips)
                    .withContent(R.string.kind_tip_remove_image)
                    .withEndListener {
                        mAdapter.removeAt(position)
                    }
                    .show(supportFragmentManager, "CancelSureDialog")

            }

        }


    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.tvNext) {

            if (mAdapter.data.size <= 1) {
                ToastTool.toast(R.string.no_selected_any_pictures)
            } else {
                updateUserInfo()
            }

        }

    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    /**
     *
     * 开始完善用户资料
     */
    private var completeCount = 0
    private fun updateUserInfo() {
        lifecycleScope.launch(Dispatchers.IO) {

            HiLog.e(Tag2Common.TAG_12303, "文件上传: urlFile = $urlFile")
            runOnUiThread {
                showLoadingDialog()
            }
            completeCount = 0
            val imageList = mutableListOf<String>()
            mAdapter.data.forEachIndexed { index, photoMultiTypeBean ->
                if (photoMultiTypeBean.type == PhotoMultiTypeBean.ITEM_TYPE_PHOTO) {
                    photoMultiTypeBean.data.image?.let { s ->
                        val file: File = File(s)
                        val fileBody = file.asRequestBody("multipart/form-data".toMediaType())

                        val formData =
                            MultipartBody.Part.createFormData("file", file.name, fileBody)

                        model.fileUpload(fileBody, formData, success = {

                            it.path?.let { pathStr -> imageList.add(pathStr) }

                            completeCount++


                            HiLog.e(
                                Tag2Common.TAG_12303,
                                "fileUpload 成功  $completeCount }"
                            )

                            if (completeCount + 1 == mAdapter.data.size) {
                                runOnUiThread {
                                    dismissLoadingDialog()
                                }
                                HiLog.e(
                                    Tag2Common.TAG_12303,
                                    "fileUpload 成功  到最后一个 }"
                                )
                                model.publishPhoto(
                                    images = imageList,
                                    success = { _ ->
                                        ToastTool.toast(R.string.published_successfully)
                                        Router.toMyPhotoListActivity()
                                        finish()

                                    }) { code, msg ->
                                    ToastTool.toastError(R.string.toast_err_service)
                                    runOnUiThread {
                                        dismissLoadingDialog()
                                    }
                                }
                            }
                        }, failed = { code, msg ->
                            ToastTool.toastError(R.string.toast_err_service)
                            runOnUiThread {
                                dismissLoadingDialog()
                            }
                        })
                    }
                }
            }

        }
    }


}
