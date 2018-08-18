package com.sa.baseproject.appview.music.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class AudioModel(
        var name:String,
        var path:String,
        var isPlaying:Boolean = false
):Serializable
