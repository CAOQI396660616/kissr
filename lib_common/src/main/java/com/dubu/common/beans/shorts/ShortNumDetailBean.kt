package com.dubu.common.beans.shorts

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ShortNumDetailBean(
    val videoNum: Int? = 0,//视频第几集
    val videoLockState: Boolean? = false,//视频是否上锁
    val videoPlayState: Boolean? = false,//视频是否正在播放
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(videoNum)
        parcel.writeValue(videoLockState)
        parcel.writeValue(videoPlayState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShortNumDetailBean> {
        override fun createFromParcel(parcel: Parcel): ShortNumDetailBean {
            return ShortNumDetailBean(parcel)
        }

        override fun newArray(size: Int): Array<ShortNumDetailBean?> {
            return arrayOfNulls(size)
        }
    }
}

