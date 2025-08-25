package com.dubu.common.player.listener

import com.aytech.flextv.ui.player.aliyunlistplayer.entity.VideoOrientation
import com.dubu.common.player.bean.ErrorInfo

interface PlayerCtrlListener {
    fun onError(errorInfo: ErrorInfo)

    fun onBackClick()

    fun onRenderingStart(seriesId: Int,sectionId: Int)

    fun onUpdateInfo(sectionId: Int,oldProgressPosition:Long,newProgressPosition:Long)

    fun onSeriesPageSelected(seriesNo: Int, price: Int)

    fun onLikeClick(sectionId: Int, isLike: Boolean)  //点赞

    fun onFollowClick(seriesId: Int, isFollow: Boolean) //追剧

    fun onBottomDialogClick(curOrientation: VideoOrientation)// 底部选集按钮

    fun onGetPlayInfo(sectionId: Int, seriesNo: Int)

    fun onFocusMode(isFocusMode: Boolean)

    fun onPlay(isPlay: Boolean)

    fun onLastVideoComplete( id: Int)

    fun onVideoComplete( id: Int, seriesNo: Int)

    fun onDiscountActivityClick(promotionType: Int)

    fun onPushTurnOnTrackEvent(eventId: String, eventKey: String)

    fun onShare(seriesId: Int,curOrientation:VideoOrientation) //分享

    fun onChangeScreenOrientation(curOrientation:VideoOrientation,isFocusMode:Boolean)

    fun onSelectVideoDefinition(curDefinition: String)

    fun onSelectVideoLanguage(curLanguage: String,curOrientation:VideoOrientation)

    fun onPreViewVideoUnlock(seriesNo: Int,sectionId : Int,price:Int)

    fun onHandleFocusMode(isFocusMode:Boolean)

    fun onHandleEventTrack(eventId:String,eventKey:String,itemId:String = "0")
}