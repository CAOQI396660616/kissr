package com.dubu.rtc.tools

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.opensource.svgaplayer.*
import com.opensource.svgaplayer.SVGAImageView
import java.net.URL

/**
 * @description SVAG 播放工具类
 * @author Allen
 * @time 2021/12/11 15:32
 *
 */
object SvgaAnimationUtil {
    /**
     * 根据后台图片地址去显示动画 Svga  EventManager.postSticky(EventKey.CALL_INVITE_INFO, it)
     */
    fun showSVGA(http: String, iv: SVGAImageView, cont: AppCompatActivity) {
        val parser =  SVGAParser(cont)
        parser.decodeFromURL(URL(http), object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                val svgD = SVGADrawable(videoItem)
                iv.visibility = View.VISIBLE
                iv.loops = 1
                iv.setImageDrawable(svgD)
                iv.startAnimation()

                iv.callback = object : SVGACallback {
                    override fun onFinished() {
                        iv.visibility = View.GONE
                        EventManager.postSticky(EventKey.VOICE_ROOM_SVGA_PLAY, UserComeInAnimControl.SVGA_IS_NEXT)
                    }

                    override fun onPause() {

                    }

                    override fun onRepeat() {

                    }

                    override fun onStep(frame: Int, percentage: Double) {

                    }

                }
            }

            override fun onError() {
                iv.visibility = View.GONE
            }

        })
    }



}