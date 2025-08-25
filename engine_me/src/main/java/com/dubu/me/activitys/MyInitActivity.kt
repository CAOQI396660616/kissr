package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.beans.config.LanguageInfo
import com.dubu.common.beans.me.PhotoDataBean
import com.dubu.common.beans.me.PhotoMultiTypeBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.CancelSureDialog
import com.dubu.common.dialog.PickDateDialog
import com.dubu.common.dialog.PickTextDialog
import com.dubu.common.manager.SettingsManager
import com.dubu.common.manager.LoginManager
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.*
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.common.utils.picker.helper.GlideEngine
import com.dubu.me.R
import com.dubu.me.adapters.LanguageListAdapter
import com.dubu.me.adapters.PhotoMultiItemAdapter
import com.dubu.me.databinding.ActivityMyInitBinding
import com.dubu.me.vm.MineViewModel
import com.hikennyc.view.MultiStateAiView
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
import java.lang.Math.abs


/**
 * 欢迎成为主播设置页面
 *
 * @author cq
 * @date 2025/06/05
 * @constructor 创建[MyInitActivity]
 */
@Route(path = RouteConst.ACTIVITY_MINE_INIT)
class MyInitActivity : BaseBindingActivity<ActivityMyInitBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyInitActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val model: MineViewModel by viewModels()
    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_my_init
    }


    private fun shouldInterceptBack(): Boolean {
        // 添加你的拦截条件
        return true
    }

    override fun onBack() {
        backAction()
    }

    override fun onCreated() {
        httpEngine()
        initView()
        initClick()
    }

    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)
        showUi(1)
    }


    private var countryList: MutableList<String>? = null
    private var languageList: MutableList<LanguageInfo>? = null
    private fun getData() {



        if (HiRealCache.countryList.isNullOrEmpty() || HiRealCache.languageList.isNullOrEmpty()){

            model.getJsonList(success = { cList ->
                countryList = mutableListOf()
                cList.country_list?.forEachIndexed { _, countryBean ->
                    countryBean.country_code?.let { countryList?.add(it) }
                }
                languageList =  HiRealCache.languageList
                showSuccessEngine()
                HiLog.e(Tag2Common.TAG_12300, "getJsonList getJsonList getJsonList = ${HiRealCache.countryList}")


            }, failed = { code, msg ->
                showErrorEngine()
            })

        }else{
            countryList = mutableListOf()
            HiRealCache.countryList?.forEachIndexed { _, countryBean ->
                countryBean.country_code?.let { countryList?.add(it) }
            }
            languageList =  HiRealCache.languageList

            HiLog.e(Tag2Common.TAG_12300, "no getJsonList getJsonList getJsonList = ${HiRealCache.countryList}")
            showSuccessEngine()
        }

    }

    private fun httpEngine() {
        getData()

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
        initRoot()
    }

    private fun backAction() {
        CancelSureDialog()
            .withTitle(R.string.kind_tips)
            .withContent(R.string.kind_tip_un_init)
            .withEndListener {
                finish()
            }
            .show(supportFragmentManager, "CancelSureDialog")
    }


    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            backAction()
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (!isTouchInsideViewLine(ev, binding.etDes)) {
                KeyboardUtils.hideSoftInput(binding.etDes)
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
        val viewH = abs(view.top - view.bottom)
        val startY = location[1]
        val endY = location[1] + viewH
        return y in startY..endY
    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var stepUserPos = 1 //用户处于第几步骤
    private var userInputName: String = "" //用户输入的文本 : 昵称

    private var selectDate: String = ""
    private var selectYear: String = ""
    private var selectMonth: String = ""
    private var selectDay: String = ""


    private var userInputCountry: String = "" //用户输入的文本 : 国家
    private var userInputLanguage: String = "" //用户输入的文本 : 语言


    private var userInputDes: String = "" //用户输入的文本 : 简介

    private val mAdapter: PhotoMultiItemAdapter by lazy {
        PhotoMultiItemAdapter()
    }

    private fun showUi(pos: Int) {
        stepUserPos = pos
        when (pos) {
            1 -> {
                binding.clRoot1.visibility = View.VISIBLE
                binding.clRoot2.visibility = View.GONE
                binding.clRoot3.visibility = View.GONE
                binding.clRoot4.visibility = View.GONE

                binding.view1.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view2.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clD8D8D8)
                }
                binding.view3.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clD8D8D8)
                }
                binding.view4.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clD8D8D8)
                }

            }
            2 -> {
                binding.clRoot1.visibility = View.GONE
                binding.clRoot2.visibility = View.VISIBLE
                binding.clRoot3.visibility = View.GONE
                binding.clRoot4.visibility = View.GONE

                binding.view1.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view2.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view3.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clD8D8D8)
                }
                binding.view4.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clD8D8D8)
                }
            }
            3 -> {
                binding.clRoot1.visibility = View.GONE
                binding.clRoot2.visibility = View.GONE
                binding.clRoot3.visibility = View.VISIBLE
                binding.clRoot4.visibility = View.GONE

                binding.view1.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view2.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view3.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view4.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clD8D8D8)
                }

            }
            4 -> {
                binding.clRoot1.visibility = View.GONE
                binding.clRoot2.visibility = View.GONE
                binding.clRoot3.visibility = View.GONE
                binding.clRoot4.visibility = View.VISIBLE

                binding.view1.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view2.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view3.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }
                binding.view4.helper.apply {
                    backgroundColorNormal = DisplayUiTool.getColor(R.color.clF54B6B)
                }

                binding.tvNext.text = getString(R.string.done)


                bindingApply {
                    with(recyclerView) {
                        layoutManager =
                            GridLayoutManager(this@MyInitActivity, 4, RecyclerView.VERTICAL, false)
                        adapter = mAdapter
                    }
                }
                val mutableListOf = mutableListOf<PhotoMultiTypeBean>()

                val photoMultiTypeBean =
                    PhotoMultiTypeBean(PhotoMultiTypeBean.ITEM_TYPE_ADD, PhotoDataBean(""))
                mutableListOf.add(photoMultiTypeBean)


                mAdapter.setList(mutableListOf)


                mAdapter.setOnItemClickListener { _, _, position ->
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
                                    mAdapter.removeAt(position)
                                }
                                .show(supportFragmentManager, "CancelSureDialog")

                        }


                    }

                }

            }
        }
    }


    private var completeCount = 0

    private val mAdapterLang: LanguageListAdapter by lazy {
        LanguageListAdapter()
    }

    private fun initRoot() {

        ClickUtils.applySingleDebouncing(binding.tvNext) {
            when (stepUserPos) {

                1 -> {
                    if (checkCanGotoNextByPos(1))
                        showUi(2)
                }
                2 -> {
                    if (checkCanGotoNextByPos(2))
                        showUi(3)
                }
                3 -> {
                    if (checkCanGotoNextByPos(3))
                        showUi(4)
                }
                4 -> {
                    if (checkCanGotoNextByPos(4))
                        updateUserInfo()
                }
            }
        }


        /* 第一步 */

        //编辑文本 发送消息
        bindingApply {
            etMsg.addTextChangedListener { eb ->
                val trim = eb.toString().trim()
                val en = ((trim.length) > 0)
                userInputName = if (en) {
                    eb.toString().trim()
                } else {
                    ""
                }

                changeNameSum(userInputName.length)
            }
        }



        ClickUtils.applySingleDebouncing(binding.llData) {
            PickDateDialog()
                .withDateChooseListener { date ->
                    selectDate = date

                    val list = date?.split("-")
                    if (list?.size == 3) {
                        selectYear = list[0]
                        selectMonth = list[1]
                        selectDay = list[2]

                        binding.tvDD.text = selectDay
                        binding.tvMM.text = selectMonth
                        binding.tvYY.text = selectYear
                    }

                }
                .show(supportFragmentManager, "PickDateDialog")
        }


        /* 第二步 */
        ClickUtils.applySingleDebouncing(binding.llCountry) {
            /*val countryList = CountryJsonManager.getInstance().getCountryList(this@MyInitActivity)
            val mutableListOf = mutableListOf<String>()
            countryList.forEachIndexed { index, countryInfo ->
                mutableListOf.add(countryInfo.countryName)
            }*/
            PickTextDialog()
                .withTitle(R.string.select_country)
                .withInitDate(countryList!!)
                .withDateChooseListener { date ->
                    userInputCountry = date
                    binding.tvCountry.text = userInputCountry
                }
                .show(supportFragmentManager, "PickTextDialog")


        }

        bindingApply {
            with(recyclerViewLang) {
                layoutManager =
                    GridLayoutManager(this@MyInitActivity,2, RecyclerView.VERTICAL, false)
                adapter = mAdapterLang
            }
        }

        mAdapterLang.setList(languageList)

        mAdapterLang.setOnItemClickListener { adapter, view, position ->

            mAdapterLang.changeUi(position)
        }


//        ClickUtils.applySingleDebouncing(binding.llLanguage) {
//            /*val countryList = LanguageJsonManager.getInstance().getLanguageList(this@MyInitActivity)
//            val mutableListOf = mutableListOf<String>()
//            countryList.forEachIndexed { index, countryInfo ->
//                mutableListOf.add(countryInfo.languageName)
//            }*/
//            PickTextDialog()
//                .withTitle(R.string.app_9_002)
//                .withInitDate(languageList!!)
//                .withDateChooseListener { date ->
//                    userInputLanguage = date
//                    binding.tvLang.text = userInputLanguage
//                }
//                .show(supportFragmentManager, "PickTextDialog")
//
//
//        }


        /* 第三步*/


        //编辑文本 发送消息
        bindingApply {
            etDes.addTextChangedListener { eb ->
                val trim = eb.toString().trim()
                val en = ((trim.length) > 0)
                userInputDes = if (en) {
                    eb.toString().trim()
                } else {
                    ""
                }

                changeDesSum(userInputDes.length)
            }
        }


    }

    /**
     *
     * 开始完善用户资料
     */
    private fun updateUserInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            completeCount = 0
            runOnUiThread {
                showLoadingDialog()
            }

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
                                model.initUserInfo(
                                    kol_name = userInputName,
                                    birthday = selectDate,
                                    country = userInputCountry,
                                    language = userInputLanguage,
                                    des = userInputDes,
                                    images = imageList,
                                    success = { dataUser ->
                                        HiLog.e(
                                            Tag2Common.TAG_12303,
                                            "更新用户信息 成功 }"
                                        )

                                        HiRealCache.user?.isNeedInit = 0
                                        HiRealCache.user?.nickname = userInputName
                                        HiRealCache.user?.birthday = selectDate
                                        HiRealCache.user?.countryCode = userInputCountry
                                        HiRealCache.user?.languageCodes = userInputLanguage
                                        HiRealCache.user?.info = userInputDes
                                        HiRealCache.user?.images = dataUser.images
                                        LoginManager.initLoginSuccess(HiRealCache.user)


                                        Router.toMainActivity()
                                        //todo allen:server (对接后台数据)
                                        //allen 目前这里暂时做 注册过后的语言保存
                                        SettingsManager.saveLanguageToMMKV("lang")

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


    /**
     * 检查用户点击下一步时候 该步骤的资料都填齐全了
     *
     * @param [pos] 用户处于第几步骤
     * @return [Boolean] 可以下一步吗?
     */
    private fun checkCanGotoNextByPos(pos: Int): Boolean {

        var isCanNext = false

        when (pos) {

            1 -> {
                isCanNext = (userInputName.isNotEmpty()) && (selectDate.isNotEmpty())
            }
            2 -> {
                userInputLanguage = mAdapterLang.checkUserChooseListToStr()
                isCanNext = (mAdapterLang.checkUserChooseList().isNotEmpty()) && (userInputCountry.isNotEmpty())
            }
            3 -> {
                isCanNext = (userInputDes.isNotEmpty())
            }
            4 -> {
                isCanNext = mAdapter.data.size > 1
            }
        }

        if (!isCanNext) {
            ToastTool.toastError(getString(R.string.toast_err_must_complete_cur_step))
        }


        return isCanNext
    }


    private fun changeNameSum(len: Int) {
        binding.tvNameSum.text = "$len/15"
    }

    private fun changeDesSum(len: Int) {
        binding.tvDesSum.text = "$len/500"
    }

}
