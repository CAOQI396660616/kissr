package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.me.PhotoDataBean
import com.dubu.common.beans.me.PhotoMultiTypeBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.CancelSureDialog
import com.dubu.common.dialog.PickDateDialog
import com.dubu.common.dialog.PickTextDialog
import com.dubu.common.router.RouteConst
import com.dubu.common.utils.*
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.common.utils.picker.helper.GlideEngine
import com.dubu.me.dialogs.BottomEditTextDialog
import com.dubu.me.dialogs.BottomEditTextDialogV2
import com.dubu.main.api.MineClient
import com.dubu.me.R
import com.dubu.me.adapters.DragCallback
import com.dubu.me.adapters.PhotoMultiItemAdapter
import com.dubu.me.databinding.ActivityMeEditProfileBinding
import com.dubu.me.vm.MineViewModel
import com.hikennyc.view.MultiStateAiView
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * 编辑个人资料
 *
 * @author cq
 * @date 2025/06/05
 * @constructor 创建[MyEditProfileActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_EDIT_PROFILE)
class MyEditProfileActivity : BaseBindingActivity<ActivityMeEditProfileBinding>() {
    private val model: MineViewModel by viewModels()


    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyEditProfileActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_edit_profile
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


        bindingApply {
            with(recyclerView) {
                layoutManager =
                    GridLayoutManager(this@MyEditProfileActivity, 4, RecyclerView.VERTICAL, false)
                adapter = mAdapter

                // 绑定拖拽功能
                val itemTouchHelper = ItemTouchHelper(DragCallback(mAdapter))
                itemTouchHelper.attachToRecyclerView(recyclerView)
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


                val size = mAdapter.data.size
                if (size >= 7) {
                    ToastTool.toast(R.string.update_ok_err)
                } else {

                    val maxChoose = 7 - size

                    PictureSelector.create(this)
                        .openGallery(SelectMimeType.ofImage())
                        .setMaxSelectNum(maxChoose)
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

                }


            } else {
                val size = mAdapter.data.size
                if (size <= 2) {
                    ToastTool.toast(R.string.update_ok_content)
                } else {

                    CancelSureDialog()
                        .withTitle(R.string.kind_tips)
                        .withContent(R.string.kind_tip_remove_image)
                        .withEndListener {
                            mPageBgPathEdit = true
                            mAdapter.removeAt(position)
                        }
                        .show(supportFragmentManager, "CancelSureDialog")

                }


            }

        }


        // 设置数据变化回调
        mAdapter.setOnItemMoveListener(object : PhotoMultiItemAdapter.OnItemMoveListener {
            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                // 在这里处理数据变化，例如提交数据到后台
                HiLog.e(Tag2Common.TAG_12309, "Item moved from $fromPosition to $toPosition")
                // 提交数据到后台的逻辑
                mPageBgPathEdit = true
            }
        })

        httpEngine()

    }

    private var countryList: MutableList<String>? = null
    private fun showUserInfo() {

        model.getMeUserInfo(success = { data ->
            HiLog.e(Tag2Common.TAG_12302, "用户信息最新数据 成功: ${GsonUtils.toJson(data)}")


            if (HiRealCache.countryList.isNullOrEmpty()){

                model.getJsonList(success = { cList ->

                    countryList = mutableListOf()
                    cList.country_list?.forEachIndexed { _, countryBean ->
                        countryBean.country_code?.let { countryList?.add(it) }
                    }

                    HiLog.e(Tag2Common.TAG_12302, "国家列表 Http 成功 cList : ${GsonUtils.toJson(cList)}")

                    bindingApply {
                        tvName.text = HiRealCache.user?.nickname ?: ""
                        tvCountry.text = HiRealCache.user?.countryCode ?: ""
                        tvBri.text = HiRealCache.user?.birthday ?: ""
                        tvDes.text = HiRealCache.user?.info ?: ""

                        GlideTool.loadAvatar(HiRealCache.user?.avatar ?: "", binding.ivAvatar)
                    }

                    val mutableListOf = mutableListOf<PhotoMultiTypeBean>()

                    data.images?.forEachIndexed { index, s ->

                        val photoMultiTypeBean =
                            PhotoMultiTypeBean(PhotoMultiTypeBean.ITEM_TYPE_PHOTO, PhotoDataBean(s, 1))
                        mutableListOf.add(photoMultiTypeBean)

                    }

                    mAdapter.addData(0, mutableListOf)

                    showSuccessEngine()
                }, failed = { code, msg ->
                    showErrorEngine()
                })

            }else{

                countryList = mutableListOf()
                HiRealCache.countryList?.forEachIndexed { _, countryBean ->
                    countryBean.country_code?.let { countryList?.add(it) }
                }

                HiLog.e(Tag2Common.TAG_12302, "国家列表 HiRealCache 成功 cList : ${GsonUtils.toJson(HiRealCache.countryList)}")

                bindingApply {
                    tvName.text = HiRealCache.user?.nickname ?: ""
                    tvCountry.text = HiRealCache.user?.countryCode ?: ""
                    tvBri.text = HiRealCache.user?.birthday ?: ""
                    tvDes.text = HiRealCache.user?.info ?: ""

                    GlideTool.loadAvatar(HiRealCache.user?.avatar ?: "", binding.ivAvatar)
                }

                val mutableListOf = mutableListOf<PhotoMultiTypeBean>()

                data.images?.forEachIndexed { index, s ->

                    val photoMultiTypeBean =
                        PhotoMultiTypeBean(PhotoMultiTypeBean.ITEM_TYPE_PHOTO, PhotoDataBean(s, 1))
                    mutableListOf.add(photoMultiTypeBean)

                }

                mAdapter.addData(0, mutableListOf)

                showSuccessEngine()
            }



        }, failed = { code, msg ->
            showErrorEngine()
            HiLog.e(Tag2Common.TAG_12302, "用户信息最新数据 失败: $code = $msg")
        })


    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.tvTitleR) {
            updateUserInfo2()
        }

        HiLog.e(Tag2Common.TAG_12305, "当前用户信息 = ${GsonUtils.toJson(HiRealCache.user)}")

        //头像编辑
        ClickUtils.applySingleDebouncing(binding.clRoot1) {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
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

                        mAvatarPath = result?.get(0)?.realPath ?: ""
                        GlideTool.loadAvatar(mAvatarPath, binding.ivAvatar)
                        mAvatarPathEdit = true
                    }

                    override fun onCancel() {
                        HiLog.e(
                            Tag2Common.TAG_RTC_IM,
                            "获取相册 onCancel"
                        )
                    }
                })

        }

        ClickUtils.applySingleDebouncing(binding.clRoot2) {
            val text = binding.tvName.text.toString() ?: ""
            val bottomDialog = BottomEditTextDialog()
                .setTitle(getString(R.string.profile_input_text))
                .setHint(getString(R.string.profile_input_text))
                .setInitialText(text)
                .setOnConfirmListener {
                    binding.tvName.text = it
                    userInputName = it
                    userInputNameEdit = true
                }
            bottomDialog.show(supportFragmentManager, "BottomEditTextDialog")
        }

        ClickUtils.applySingleDebouncing(binding.clRoot5) {
            val text = binding.tvDes.text.toString() ?: ""
            val bottomDialog = BottomEditTextDialogV2()
                .setTitle(getString(R.string.profile_input_text))
                .setHint(getString(R.string.profile_input_text))
                .setInitialText(text)
                .setMaxLength(100)
                .setOnConfirmListener {
                    binding.tvDes.text = it
                    userInputDes = it
                    userInputDesEdit = true
                }
            bottomDialog.show(supportFragmentManager, "BottomEditTextDialogV2")
        }

        ClickUtils.applySingleDebouncing(binding.clRoot4) {

            val text = binding.tvBri.text.toString() ?: ""

            //1000L  * 60 * 60 *24 一天
            val dateStr =
                EaseDateUtils.getDateStr(System.currentTimeMillis(), EaseDateUtils.FORMAT_YMD)
            val textReal = if (text.isNullOrEmpty()) {
                dateStr
            } else {
                text
            }

            PickDateDialog()
                .withInitDate(textReal)
                .withDateChooseListener { date ->
                    ToastUtils.showLong(date)
                    binding.tvBri.text = date
                    selectDate = date
                    selectDateEdit = true
                }
                .show(supportFragmentManager, "PickDateDialog")
        }

        ClickUtils.applySingleDebouncing(binding.clRoot3) {
            //            val countryList =
            //                CountryJsonManager.getInstance().getCountryList(this@MyEditProfileActivity)
            val mutableListOf = mutableListOf<String>()
            countryList?.forEachIndexed { index, countryInfo ->
                mutableListOf.add(countryInfo)
            }
            PickTextDialog()
                .withTitle(R.string.select_country)
                .withInitDate(mutableListOf)
                .withDateChooseListener { date ->
                    ToastUtils.showLong(date)
                    binding.tvCountry.text = date
                    userInputCountry = date
                    userInputCountryEdit = true
                }
                .show(supportFragmentManager, "PickTextDialog")


        }
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    private var mAvatarPath: String = ""
    private var mAvatarPathEdit: Boolean = false
    private var mAvatarPathUpload: String = ""

    private var userInputName: String = "" //用户输入的文本 : 昵称
    private var userInputNameEdit: Boolean = false

    private var selectDate: String = ""
    private var selectDateEdit: Boolean = false
    private var selectYear: String = ""
    private var selectMonth: String = ""
    private var selectDay: String = ""


    private var userInputCountry: String = "" //用户输入的文本 : 国家
    private var userInputCountryEdit: Boolean = false
    private var userInputLanguage: String = "" //用户输入的文本 : 语言


    private var userInputDes: String = "" //用户输入的文本 : 简介
    private var userInputDesEdit: Boolean = false


    private var mPageBgPathEdit: Boolean = false

    private fun changeStatus() {
        mAvatarPathEdit = false
        userInputNameEdit = false
        selectDateEdit = false
        userInputCountryEdit = false
        userInputDesEdit = false
        mPageBgPathEdit = false
        mAvatarPath = ""
        mAvatarPathUpload = ""
    }

    private fun splitSimplePath(s: String): String {

        var sttr = ""

        if (s.startsWith("http")) {


            if (s.contains("uploads")) {
                val split2 = s.split("uploads")

                val s2 = split2[1]
                val s3 = "uploads$s2"

                sttr = s3

            } else {

            }

        } else {

        }

        HiLog.e(Tag2Common.TAG_12306, "splitSimplePath    :::   $sttr")
        return sttr

    }

    private fun checkUserEditPageBgImage(): Boolean {

        var isUserEditPageBgImage = false

        mAdapter.data.forEachIndexed { index, photoMultiTypeBean ->
            if (photoMultiTypeBean.type == PhotoMultiTypeBean.ITEM_TYPE_PHOTO) {
                photoMultiTypeBean.data.image?.let { s ->
                    if (s.isNotEmpty() && !s.startsWith("http") && !s.startsWith("uploads")) {
                        isUserEditPageBgImage = true
                    }

                }
            }
        }

        return isUserEditPageBgImage

    }


    // 1 = /storage/emulated/0/DCIM/Camera/PXL_20250617_021057561.jpg
    // 2 = /storage/emulated/0/DCIM/Camera/PXL_20250617_021108094.jpg
    // 3 = /storage/emulated/0/DCIM/Camera/PXL_20250617_021119285.jpg
    // 4 = /storage/emulated/0/DCIM/Camera/PXL_20250617_021125476.jpg

    private fun updateUserInfo2() {

        if (!checkCanUpdate()) {
            ToastTool.toast(R.string.update_ok_tip)
            return
        }



        lifecycleScope.launch(Dispatchers.IO) {

            runOnUiThread {
                showLoadingDialog()
            }


            val imageListNeedUpload = mutableListOf<String>()
            if (mAvatarPathEdit && mAvatarPath.isNotEmpty()) {
                imageListNeedUpload.add(mAvatarPath)
                HiLog.e(Tag2Common.TAG_123088, "用户编辑了头像 本地数据是: $mAvatarPath")
            }

            val imageBgList = getNeedUploadBgImageList()
            val imageNotBgList = getNotNeedUploadBgImageList()

            imageListNeedUpload.addAll(imageBgList)

            val kolUserBean = MineClient.KolUserBean(
                kol_name = userInputName,
                email = "",
                mobile = "",
                avatar = "",
                birthday = selectDate,
                images = "",
                info = userInputDes,
                country_code = userInputCountry,
                online_status = "",
                language = userInputLanguage,
            )

            model.uploadFilesAndUpdateUser(
                imageBgList,
                imageNotBgList,
                mAvatarPath,
                kolUserBean,
                failedForUploadImage = { code, msg ->
                    HiLog.e(Tag2Common.TAG_12309, "failedForUploadImage $code = $msg")
                    runOnUiThread {
                        dismissLoadingDialog()
                        toastError(R.string.toast_err_service)
                    }
                },
                failedForUpdateUserInfo = { code, msg ->
                    HiLog.e(Tag2Common.TAG_12309, "failedForUpdateUserInfo $code = $msg")
                    runOnUiThread {
                        dismissLoadingDialog()
                        toastError(R.string.toast_err_service)
                    }
                },
                success = {


                    model.getMeUserInfo(success = { data ->
                        HiLog.e(Tag2Common.TAG_12302, "用户信息最新数据 成功: ${GsonUtils.toJson(data)}")
                        runOnUiThread {
                            ToastTool.toast(R.string.update_ok)
                            dismissLoadingDialog()
                            changeStatus()
                        }
                    }, failed = { code, msg ->
                        toastError(R.string.toast_err_service)
                        dismissLoadingDialog()
                        HiLog.e(Tag2Common.TAG_12302, "用户信息最新数据 失败: $code = $msg")
                    })

                },
                onProgress = { current, total ->
                    HiLog.e(Tag2Common.TAG_12309, "onProgress $current = $current")

                })


        }


        //=================================================================

    }

    private fun getNeedUploadBgImageList(): MutableList<String> {

        val imageListNeedUpload = mutableListOf<String>()

        mAdapter.data.forEachIndexed { index, photoMultiTypeBean ->
            if (photoMultiTypeBean.type == PhotoMultiTypeBean.ITEM_TYPE_PHOTO) {
                photoMultiTypeBean.data.image?.let { s ->

                    if (s.isNotEmpty() && !s.startsWith("http") && !s.startsWith("uploads")) {
                        imageListNeedUpload.add(s)
                        HiLog.e(Tag2Common.TAG_12309, "适配器中本地图片是: $s")
                    }
                }
            }
        }

        return imageListNeedUpload
    }

    private fun getNotNeedUploadBgImageList(): MutableList<String> {

        val imageListNeedUpload = mutableListOf<String>()

        mAdapter.data.forEachIndexed { index, photoMultiTypeBean ->
            if (photoMultiTypeBean.type == PhotoMultiTypeBean.ITEM_TYPE_PHOTO) {
                photoMultiTypeBean.data.image?.let { s ->

                    if (s.isNotEmpty() && !s.startsWith("http")) {
                        HiLog.e(Tag2Common.TAG_12309, "getNotNeedUploadBgImageList 1 xx: $s")
                    } else {
                        val element = splitSimplePath(s)
                        imageListNeedUpload.add(element)
                        HiLog.e(Tag2Common.TAG_12309, "getNotNeedUploadBgImageList 2 xx: $s = $element")
                    }
                }
            }
        }

        return imageListNeedUpload
    }

    private fun checkCanUpdate(): Boolean {
        return mAvatarPathEdit || userInputNameEdit || selectDateEdit || userInputCountryEdit || userInputDesEdit || mPageBgPathEdit || checkUserEditPageBgImage()
    }


    private fun httpEngine() {
        showUserInfo()
    }

    private fun showErrorEngine() {
        binding.multiStateView.viewState = MultiStateAiView.ViewState.ERROR
        binding.multiStateView.getView(MultiStateAiView.ViewState.ERROR)
            ?.findViewById<TextView>(R.id.tvRetry)
            ?.setOnClickListener {
                httpEngine()
            }
    }

    private fun showSuccessEngine() {
        binding.multiStateView.viewState = MultiStateAiView.ViewState.CONTENT
    }
}
