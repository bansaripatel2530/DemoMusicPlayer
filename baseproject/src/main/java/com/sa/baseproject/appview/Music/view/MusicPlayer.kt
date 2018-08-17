package com.sa.baseproject.appview.Music.view

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import java.util.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.sa.baseproject.appview.Music.model.AudioModel
import com.sa.baseproject.appview.news.view.NewsActivity
import com.sa.baseproject.base.AppFragmentState
import kotlin.collections.ArrayList
import java.io.File


class MusicPlayer(var activity: Activity) : LifecycleObserver {

    private var exoPlayer: ExoPlayer? = null


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initializePlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initializePlayer()


    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {


    }

    fun releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.release();
            exoPlayer = null;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onPause() {
        releasePlayer()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        initializePlayer()

    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                    activity, DefaultTrackSelector())

        }
    }


    fun prepareExoPlayerFromFileUri(data: AudioModel) {
//            val cM = ConcatenatingMediaSource()
//            for (i in 0 until data.size) {
//                val path = "file:/"+data[i].path
//                val mediaSource = ExtractorMediaSource.Factory(
//                        dataSourceFactory).createMediaSource(Uri.fromFile(File(data[i].path)))
//                cM.addMediaSource(mediaSource)
//
//            }
//
//            exoPlayer!!.prepare(cM)
//            exoPlayer!!.playWhenReady = true
        val dataSourceFactory = DefaultDataSourceFactory(
                activity, Util.getUserAgent(activity, "Music Player")

        )
        val mediaSource = ExtractorMediaSource.Factory(
                dataSourceFactory).createMediaSource(Uri.fromFile(File(data.path)))
        exoPlayer!!.prepare(mediaSource)
        val fragment = (activity as NewsActivity).appFragmentManager!!.getFragment() as MusicListFragment
        fragment?.initMediaControls(exoPlayer!!)

    }




}