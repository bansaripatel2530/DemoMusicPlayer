package com.sa.baseproject.appview.Music.viewmodel

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.provider.MediaStore
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.View
import android.widget.TextView
import com.sa.baseproject.appview.Music.model.AudioModel
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlayer

import com.sa.baseproject.appview.Music.view.MusicPlayer


class MusicListViewModel(var app: Application) : AndroidViewModel(app) {
    val tempAudioList = MutableLiveData<ArrayList<AudioModel>>()
    var activity:Activity?=null
    private var mExoPlayer:ExoPlayer?=null
    init {
        getAllMediaMp3Files()

    }


    private fun getAllMediaMp3Files() {
        val list = arrayListOf<AudioModel>()
        val contentResolver = app.getContentResolver()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(
                uri, null, null, null, null
        )
        if (cursor == null) {
            Toast.makeText(app, "Something Went Wrong.", Toast.LENGTH_LONG).show()
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(app, "No Music Found on SD Card.", Toast.LENGTH_LONG).show()
        } else {
            val title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            do {
                val songTitle = cursor.getString(title)
                val audioModel = AudioModel(songTitle, cursor.getString(path))
                list.add(audioModel)
                tempAudioList.postValue(list)

            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    fun initializePlayer(data: AudioModel, tvSongName: TextView, musicPlayer: MusicPlayer?) {
        tvSongName.text = data.path
        musicPlayer!!.prepareExoPlayerFromFileUri(data)
    }

    fun getActivity(act:Activity){
        this.activity = act
    }


    fun openPlayerLayout(layout_bottomsheet: View) {
        val behavior = BottomSheetBehavior.from<View>(layout_bottomsheet)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        behavior.peekHeight = 100
    }
}