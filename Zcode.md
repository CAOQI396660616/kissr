

    HiLog.e("Tag2025", "visitorLogin 数据= ${ret.data}")
    Toast.makeText(this@LoginActivity, "游客登录失败:  $code , $msg", Toast.LENGTH_SHORT).show()



       <com.dubu.common.views.cview.DrawableTextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginEnd="@dimen/dp_16"
                    android:drawablePadding="@dimen/dp_10"
                    android:gravity="center_vertical"
                    android:textSize="@dimen/sp_14"
                    app:drawable="@drawable/ic_arrow_right"
                    app:drawableHeight="@dimen/dp_16"
                    app:drawablePosition="3"
                    app:drawableWidth="@dimen/dp_16"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    app:layout_constraintTop_toTopOf="parent" />


    <com.dubu.common.views.cview.DrawableTextView
        android:id="@+id/tv_id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="@dimen/dp_12"
        android:layout_marginTop="@dimen/dp_4"
        android:background="@drawable/bg_solid_r14_white_10"
        android:drawablePadding="@dimen/dp_6"
        android:gravity="center"
        android:maxWidth="@dimen/dp_200"
        android:minHeight="@dimen/dp_24"
        android:paddingHorizontal="@dimen/dp_10"
        android:paddingVertical="@dimen/dp_2"
        android:text="ID:549527007"
        android:textColor="@color/color_white_alpha_80"
        android:textSize="@dimen/sp_12"
        app:drawable="@drawable/ic_app_logo"
        app:drawableHeight="@dimen/dp_12"
        app:drawablePosition="3"
        app:drawableWidth="@dimen/dp_12"
        app:layout_constraintBottom_toBottomOf="@id/iv_avatar"
        app:layout_constraintStart_toEndOf="@id/iv_avatar"
        app:layout_constraintTop_toBottomOf="@id/tv_name" />




    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (!isTouchInsideViewLine(ev, binding.etMsg)) {
                KeyboardUtils.hideSoftInput(binding.etMsg)
                HiLog.e("TAG1010","点击了外部")
            }else{
                HiLog.e("TAG1010","点击了内部")
            }

            HiLog.e("TAG1010","-------------------------")

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










⭐ 首选推荐：MPAndroidChart
是目前 Android 生态中功能最完整、社区最活跃的图表库之一，支持包括柱状图在内的数十种图表类型，具备高度可定制性和流畅交互体验。

🔧 基本使用步骤（以柱状图为例）：
添加依赖（在 build.gradle 中）：

gradle
dependencies {
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
126

XML 布局配置：

xml
<com.github.mikephil.charting.charts.BarChart
android:id="@+id/barChart"
android:layout_width="match_parent"
android:layout_height="300dp" />
28

Java/Kotlin 数据绑定与配置：

kotlin
// 1. 准备数据点（X位置，Y高度）
val entries = listOf(
BarEntry(0f, 10f), // 第一根柱子
BarEntry(1f, 20f), // 第二根柱子
BarEntry(2f, 15f)  // 第三根柱子
)

// 2. 创建数据集并绑定数据
val dataSet = BarDataSet(entries, "销售数据")
dataSet.color = Color.BLUE // 柱状颜色

// 3. 绑定数据到图表
val barData = BarData(dataSet)
barChart.data = barData

// 4. 配置X轴标签（如月份）
val labels = arrayOf("1月", "2月", "3月")
barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

// 5. 启动动画
barChart.animateY(1000) // Y轴方向动画，持续1秒
barChart.invalidate()   // 刷新视图
268

🌈 高级定制能力（MPAndroidChart）：   //////////////https://github.com/PhilJay/MPAndroidChart //// https://juejin.cn/post/7269966434124562469
样式调整：支持渐变色、圆角柱体（需自定义 BarChartRenderer）7

交互功能：支持手势缩放、滑动、点击高亮

组合图表：可混合折线图与柱状图

水平柱状图：使用 HorizontalBarChart 控件即可4

🛠️ 备选方案：XCL-Charts
如果你更倾向于轻量级、基于原生 Canvas 绘制的图表库，或需要支持一些特殊图表（如南丁格尔玫瑰图、雷达图等），XCL-Charts 是值得考虑的替代方案35。

特点对比：
特性	MPAndroidChart	XCL-Charts ////////////////////////////////https://github.com/xcltapestry/XCL-Charts
图表类型	柱状、折线、饼图、散点等	支持更多小众类型（如玫瑰图）
动画效果	丰富流畅	基础动画
社区维护	非常活跃（GitHub 20k+ Stars）	更新较慢
自定义灵活性	高（支持Renderer重写）7	高（Canvas原生绘制）
上手难度	中	中高
💎 总结建议：
推荐优先使用 MPAndroidChart：功能全面、文档丰富，适合快速实现高质量柱状图，覆盖绝大多数业务场景168；

wo 要完成什么?
所以需要优先设计

若需特殊图表或追求极简依赖：可尝试 XCL-Charts 35；

示例项目参考：

MPAndroidChart 基础柱状图2

带圆角柱状图实现方案7

实际选型时，你可根据项目对动画效果、包体积大小、特殊图表需求等因素权衡选择。一般场景下，MPAndroidChart 的灵活性和社区支持会让开发
如果我来设置其实就是啥
吹风 磁暴 刀片 陨石 冰墙 天火
隐身 火人 灵动 急冷

KAER
ALT+Q  ALT+W  ALT+E
F      V    SPACE

NORMAL
Q   W  R
F   V  SPACE




≥, ≤ ,

<  &lt;

&gt;


kol_name=%E6%B5%8B%E8%AF%95%E6%98%B5%E7%A7%B0&birthday=1999-11-11&birthday=&birthday=&birthday=&images%5B%5D=uploads%2F20250618%2F68526a4d0f447.jpeg&images%5B%5D=uploads%2F20250618%2F68526a4dd0e8e.jpeg











//                Glide.with(context)
//                    .load(url)
//                    .addListener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            // 处理尺寸逻辑
//                            return false // 返回 false 以继续显示图片
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            dataSource: DataSource?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//
//                            if (resource != null) {
//                                val width = resource?.intrinsicWidth  ?:0 // 获取固有宽度
//                                val height = resource?.intrinsicHeight ?:0// 获取固有高度
//
//                                val scaleWH = BIMMsgTool.scaleWH(width, height)
//
//                                p.width = scaleWH.first
//                                p.height = scaleWH.second
//                            } else {
//                                p.width = 200
//                                p.height = 200
//                            }
//
//                            imgContent.layoutParams = p
//
//                            // 处理尺寸逻辑
//                            return false // 返回 false 以继续显示图片
//                        }
//
//                    })
//                    .into(imgContent)












