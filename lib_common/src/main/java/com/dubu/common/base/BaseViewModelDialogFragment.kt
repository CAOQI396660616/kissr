package com.dubu.common.base

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dubu.common.R
import com.dubu.common.utils.StatusBarTool
import java.lang.reflect.ParameterizedType

/**
 *  @author  Even
 *  @date   2021/10/20
 */
abstract class BaseViewModelDialogFragment<VM : BaseViewModel, T : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val variableId: Int
) :
    DialogFragment() {

    lateinit var viewModel: VM
    lateinit var dataBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setStyle(STYLE_NO_TITLE, R.style.transparentDialog)*/
        viewModel = createViewModel()

    }

    override fun onStart() {
        super.onStart()
        //透明背景
        /*val window = dialog!!.window
        val params = window!!.attributes
        params.dimAmount = 0.0f
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window!!.attributes = params*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        //去掉dialog 默认的padding
        window?.decorView?.setPadding(
            0,
            window.decorView.paddingTop,
            0,
            window.decorView.paddingBottom
        )
        val layoutParams = window?.attributes
        layoutParams?.width = getWidthRate()
        layoutParams?.height = getHeightRate()
        layoutParams?.gravity = getGravity()
        if (getWindowAnimation() != -1) {
            layoutParams?.windowAnimations = getWindowAnimation()
        }
        window?.attributes = layoutParams
//        window.setBackgroundDrawable(ColorDrawable())
        window?.setBackgroundDrawableResource(getLayoutBgDrawable())
        this.isCancelable = isCancelDialogOnOutSize()

        if (window != null) {
           // StatusBarTool.setTransparent(window, isShowStatusBar = true, isShowNavigationBar = false)
        }

        dataBinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.setVariable(variableId, viewModel)

        val view = if (userDefaultTitleBar()) {
            //使用默认标题
            val linearLayout = LinearLayout(requireContext())
            linearLayout.addView(
                getTitleBarView(), LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.addView(
                dataBinding.root, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
            linearLayout
        } else {
            //不使用默认标题
            dataBinding.root
        }
        initView()
        return view
    }

    open fun createViewModel(): VM {
        val genericSuperclass = javaClass.genericSuperclass
        val parameterizedType = genericSuperclass as ParameterizedType
        val actualTypeArguments = parameterizedType.actualTypeArguments
        val viewModelClass: Class<VM> = actualTypeArguments[0] as Class<VM>
        return ViewModelProvider(this)[viewModelClass]
    }


    /**
     * 是否使用默认标题栏，如果不使用，则重写此方法，返回false即可
     */
    open fun userDefaultTitleBar(): Boolean {
        return true
    }

    //set title bar view
    abstract fun getTitleBarView(): View

    abstract fun getLayoutBgDrawable(): Int

    abstract fun initView()

    /**
     * 点击外部区域能否取消
     */
    open fun isCancelDialogOnOutSize() = true

    /**
     * 弹窗默认动画
     * 当为-1的时候不显示动画
     */
    open fun getWindowAnimation(): Int = R.style.dialog_fragment_anim

    /**
     * 对其方式
     */
    open fun getGravity() = Gravity.BOTTOM

    /**
     * 设置弹窗高度比例，默认是自适应，如果要设置为屏幕比例则修改这个比例
     */
    open fun getHeightRate(): Int = WindowManager.LayoutParams.WRAP_CONTENT

    /**
     * 设置弹窗宽度比例，默认是全屏的0.75
     */
    open fun getWidthRate(): Int = WindowManager.LayoutParams.MATCH_PARENT
}