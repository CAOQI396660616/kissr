rootProject.name = "KissR"
include ':app'
include ':engine_home'
include ':engine_me'
include ':engine_chat'
include ':engine_message'
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
