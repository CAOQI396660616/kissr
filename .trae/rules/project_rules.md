## rootProject.name = "KissR"

- include ':app'
- include ':engine_home'
- include ':engine_me'
- include ':engine_skin'
- include ':engine_design'
- include ':lib_common'
- include ':lib_wheel'
- include ':lib_base'

## 这是项目的架构

* App

* Engine
    - 键盘
    - 人设
    - 换肤 (暂时隐藏了这个模块)
    - 我的
* Lib
    - 公共库
    - 第三方库
    - 基础库

## 介绍一下每一个模块的作用

- lib_base 是集成了一些每一个项目都会用到的基础第三方库的封装 例如androidx okhttp3 retrofit2 jetPack
  ARouter glide recyclerView封装 等

- lib_common 是集成了此项目单独会用到的一些第三方库 例如 后续我们需要对接的 Google支付登录数据采集等
  还有一些项目的基础功能的封装 比如四大组件的父类抽取 数据Bean的分类 下载业务逻辑封装 播放器业务逻辑封装
  还有一些资源等等

- lib_wheel 是集成了一些项目会用到的一些自定义的控件和一些第三方库的源码用于自己修改 例如 自定义的弹窗
  自定义的弹窗 自定义的弹窗 等 (lib_wheel 不依赖 lib_base lib_common 所以它的资源是独立的)

- ':engine_home'':engine_design'':engine_skin'':engine_me'这四个模块就是我们APP首页对应的 1 2 3 4模块
  分别是 键盘 人设 换肤 我的

  |           | engine_home | engine_design | engine_skin | engine_me |
    |-----------|-------------|---------------|-------------|-----------|
  | 模块        | 键盘          | 人设            | 换肤          | 我的        |
  | 1.0版本是否开发 | 是           | 是             | 否           | 是         |

## 下面就是我们项目的一些规则 请注意 你需要严格遵守这些规则

- 1 lib_base模块 这个模块不允许修改

- 2 关于一下的数据新增都必须放在 lib_common下
    * a 数据bean
    * b assets资源
    * c res资源 例如 抽取的文本 string 抽取的dp数值 等

- 3 在需要完成UI为View切角 颜色渐变 带边框等UI的时候 参考com.ruffian.library.widget.RConstraintLayout
  这个类 也就是使用com.ruffian.library.widget.* 的控件来完成, 除非com.ruffian.library.widget.*
  的控件无法满足我们的UI需求 可以使用其他方式完成
