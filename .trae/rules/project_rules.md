rootProject.name = "KissR"
include ':app'
include ':engine_home'
include ':engine_me'
include ':engine_skin'
include ':engine_design'
include ':lib_common'
include ':lib_wheel'
include ':lib_base'


这是项目的架构

App
    - 键盘
    - 人设
    - 换肤 (暂时隐藏了这个模块)
    - 我的
Engine
    - 键盘
    - 人设
    - 换肤 (暂时隐藏了这个模块)
    - 我的
Lib
    - 公共库
    - 第三方库
    - 基础库




lib_base 是集成了一些每一个项目都会用到的基础第三方库的封装 例如androidx okhttp3 retrofit2 jetPack ARouter glide recyclerView封装 等
lib_common 是集成了此项目单独会用到的一些第三方库 例如 后续我们需要对接的  Google支付登录数据采集等 还有一些项目的基础功能的封装 比如四大组件的父类抽取 数据Bean的分类 下载业务逻辑封装 播放器业务逻辑封装 还有一些资源等等
lib_wheel 是集成了一些项目会用到的一些自定义的控件和一些第三方库的源码用于自己修改 例如 自定义的弹窗 自定义的弹窗 自定义的弹窗 等 (lib_wheel 不依赖 lib_base lib_common 所以它的资源是独立的)