package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.CancelSureDialog
import com.dubu.common.manager.SettingsManager
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.hi.HiStatusBarTool
import com.dubu.me.R
import com.dubu.me.adapters.ServiceListAdapter
import com.dubu.me.databinding.ActivityMeSetServiceBinding
import com.dubu.me.dialogs.BottomEditServiceDialog
import com.dubu.me.vm.MineViewModel


/**
 * 重要设置 : 服务列表 页面
 * @author cq
 * @date 2025/06/09
 * @constructor 创建[MySetServiceActivity]
 */
class MySetServiceActivity : BaseBindingActivity<ActivityMeSetServiceBinding>() {

    private val model: MineViewModel by viewModels()

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MySetServiceActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val mAdapter: ServiceListAdapter by lazy {
        ServiceListAdapter()
    }

    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_set_service
    }

    override fun onCreated() {

        initView()
        initClick()
    }


    private fun initView() {
        HiStatusBarTool.setStatusBarOptColor(this, R.color.clF3F4F5, true)

        if (HiRealCache.user?.serviceStatus == null || HiRealCache.user?.serviceStatus!! == 0) {
            HiLog.e(Tag2Common.TAG_123XX, "key11 = ${HiRealCache.user?.serviceStatus}")
            binding.switchNotice.isChecked = false
        } else {
            HiLog.e(Tag2Common.TAG_123XX, "key22 = ${HiRealCache.user?.serviceStatus}")
            binding.switchNotice.isChecked = true
        }

        bindingApply {
            with(recyclerView) {
                layoutManager =
                    LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                adapter = mAdapter
            }
        }


        getMyServiceList()

    }


    private fun getMyServiceList() {

        showLoadingDialog()
        model.getMeServiceList(success = {
            dismissLoadingDialog()
            HiLog.e(Tag2Common.TAG_12302, "getMeServiceList 成功: ${GsonUtils.toJson(it)}")

            if (it.isEmpty()) {
                mAdapter.setEmptyView(R.layout.layout_default_empty)
            } else {
                mAdapter.setList(it)
            }


        }, failed = { code, msg ->
            dismissLoadingDialog()
            HiLog.e(Tag2Common.TAG_12302, "getMeServiceList 失败: $code = $msg")
        })


    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }

        ClickUtils.applySingleDebouncing(binding.clAdd) {
            val bottomDialog = BottomEditServiceDialog()
            bottomDialog.show(supportFragmentManager, "BottomEditServiceDialog")
            bottomDialog.setOperateListener { serviceName, servicePrice ->
                showLoadingDialog()
                model.createService(serviceName, servicePrice, success = {
                    HiLog.e(Tag2Common.TAG_12302, "createService 成功: ${GsonUtils.toJson(it)}")
                    getMyServiceList()
                    SettingsManager.saveServiceToMMKV("1")
                }, failed = { code, msg ->
                    dismissLoadingDialog()
                    HiLog.e(Tag2Common.TAG_12302, "createService 失败: $code = $msg")
                })
            }
        }


        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.ivEdit -> {
                    val kolServiceBean = mAdapter.data[position]
                    val bottomDialog = BottomEditServiceDialog()
                    bottomDialog.setData(kolServiceBean)
                    bottomDialog.show(supportFragmentManager, "BottomEditServiceDialog")
                    bottomDialog.setOperateListener { serviceName, servicePrice ->
                        showLoadingDialog()
                        model.editService(
                            (kolServiceBean.id ?: 0).toString(),
                            serviceName,
                            servicePrice,
                            success = {
                                HiLog.e(
                                    Tag2Common.TAG_12302,
                                    "editService 成功: ${GsonUtils.toJson(it)}"
                                )
                                getMyServiceList()
                            },
                            failed = { code, msg ->
                                dismissLoadingDialog()
                                HiLog.e(Tag2Common.TAG_12302, "editService 失败: $code = $msg")
                            })
                    }

                }
                R.id.ivDelete -> {

                    CancelSureDialog()
                        .withTitle(R.string.kind_tips)
                        .withContent(R.string.kind_tip_del_service)
                        .withEndListener {
                            val kolServiceBean = mAdapter.data[position]
                            showLoadingDialog()
                            model.deleteService((kolServiceBean.id ?: 0).toString(), success = {
                                HiLog.e(
                                    Tag2Common.TAG_12302,
                                    "deleteService 成功: ${GsonUtils.toJson(it)}"
                                )
                                mAdapter.removeAt(position)
                                getMyServiceList()
                            }, failed = { code, msg ->
                                dismissLoadingDialog()
                                HiLog.e(Tag2Common.TAG_12302, "deleteService 失败: $code = $msg")
                            })
                        }
                        .show(supportFragmentManager, "CancelSureDialog")

                }
            }
        }


        binding.switchNotice.setOnCheckedChangeListener { buttonView: android.widget.CompoundButton, isChecked ->

            showLoadingDialog()
            val toggle = if (isChecked) {
                1
            } else {
                0
            }

            model.updateUserOpenService(toggle, success = {
                dismissLoadingDialog()
            }, failed = { _, _ ->
                dismissLoadingDialog()
            })

        }

    }


    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */


}
