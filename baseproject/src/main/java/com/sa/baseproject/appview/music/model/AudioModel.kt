package com.sa.baseproject.appview.music.model

import android.os.Parcel
import android.os.Parcelable


data class AudioModel(
        var name:String,
        var path:String,
        var isPlaying:Boolean = false
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeString(name)
        p0.writeString(path)
        p0.writeByte(if (isPlaying) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioModel> {
        override fun createFromParcel(parcel: Parcel): AudioModel {
            return AudioModel(parcel)
        }

        override fun newArray(size: Int): Array<AudioModel?> {
            return arrayOfNulls(size)
        }
    }
}