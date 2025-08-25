package com.dubu.common.player.bean

class VideoDetailEntity(
    //视频详细信息
    val detail: VideoDetailInfo = VideoDetailInfo(),
    //视频剧集播放列表
    val list: MutableList<SectionDetailInfo> = mutableListOf(),
    //视频选集区间信息
    val range_list: MutableList<Range> = mutableListOf(),
    val play_info: PlayInfo = PlayInfo()
)

data class Range(
    val range: String="",
    val range_id: Int=0,
    var isSelect:Boolean = false
)

data class VideoDetailInfo(
    //剧集ID
    val series_id: Int = 0,
    //剧集名称
    val series_name: String = "",
    //视频类型：1=短剧，2=电视剧，3=电影
    val video_type: Int = 0,
    //封面
    val cover: String = "",
    //0=连载中，1=已完结
    val serial_status: Int = 0,
    val description: String = "",
    val watch_num: String = "",
    var collect_num: Int = 0,
    var collect_num_str: String = "0",
    //最近一次播放剧集集数
    var recently_series_no: Int = 0,
    //最新更新的集数
    var last_series_no: Int = 0,
    //是否已追剧：1=是，0=否
    var is_collect: Int = 0,
    //是否有解锁记录 1:解锁过 0：未解锁过
    var has_unlock_history: Int = 0,
    //最大可播放集数
    var max_can_play_series_no: Int = 0,
    //影视分级
    var series_level:String = "",
    //视频时长（针对电影）
    var video_duration:String = "",


    //用户是否能看广告解锁，0-否，1-是
    var user_can_ad_unlock: Int = 0,
    //当天可以看广告解锁的总次数
    var max_ad_unlock_num: Int = 0,
    //当天已经使用广告解锁的次数
    var has_ad_unlock_num: Int = 0,

)

data class SectionDetailInfo(
    //单集ID
    val id: Int = 0,
    //集数
    val series_no: Int = 0,
    //云视频ID
    val video_id: String = "",
    //是否免费，0-是，1-否
    val is_charge: Int = 0,
    //单价（金币）
    var unit_price: Int = 0,
    //点赞数量
    var like_num: Int = 0,
    //点赞数量，字符串类型
    var like_num_str: String = "0",
    //播放量
    var watch_num: String = "",
    //是否已经付费，0-否，1-是
    var has_pay: Int = 0,
    //当前用户是否点赞：1=是，0=否
    var is_like: Int = 0,
    //是否vip免费：1=是，0=否
    var is_vip_free: Int = 0,
    //剧集ID(从detail中复制过来，用于追剧是匹配改变追剧状态)
    var series_id: Int = 0,
    //封面(从detail中复制过来，用于加载视频时使用)
    var cover: String = "",
    //是否选中(用于在选集面板中，定位当前剧集位置)
    var isSelect: Boolean = false,
    //属于第几页的数据(用于集数区间快速定位)
    var whichPage: Int = 0,
    //当前集可以看广告解锁 0-否，1-是
    var can_ad_unlock: Int = 0,
    //广告解锁剧集时候需要传递给后台校验的参数
    var ext: AdExt = AdExt(),
)

data class PlayInfo(
    //视频播放地址
    val video_url: String = "",
    //清晰度列表
    val progressive: MutableList<Progressive> = mutableListOf(),
    //字幕列表
    val subtitle: MutableList<Subtitle> = mutableListOf(),
    //是否是试播：0=否，1=是
    val is_trial:Int = 0
)

data class Progressive(
    //标题，如：480P，720P
    val title: String = "",
    //播放地址
    val video_url: String = "",
    //是否选中
    var isSelect: Boolean = false
)

data class Subtitle(
    //语言,如：en
    val lang: String = "",
    //语言展示标题
    val title:String = "",
    //字幕文件地址
    val subtitle_url: String = "",
    //是否选中
    var isSelect: Boolean = false
)

/*
{
    "custom_data": "eyJzY2VuZSI6InVubG9jayIsInNlcmllc19pZCI6NTMsInNlcmllc19ubyI6MywiZW52IjoiTE9DQUwifQ=="
}
* */
//后台所需Ad传递解锁参数
data class AdExt(
    val custom_data: String = "",
)

