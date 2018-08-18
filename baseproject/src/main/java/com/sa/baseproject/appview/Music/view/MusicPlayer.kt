package com.sa.baseproject.appview.music.view

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.sa.baseproject.appview.music.model.AudioModel
import com.sa.baseproject.appview.news.view.NewsActivity
import java.io.File


class MusicPlayer(var activity: Activity) : LifecycleObserver,Player.EventListener {

    private var exoPlayer: ExoPlayer? = null
    private var fragment:MusicListFragment?=null


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initializePlayer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initializePlayer()
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.release()
            exoPlayer = null
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
         fragment = (activity as NewsActivity).appFragmentManager!!.getFragment() as MusicListFragment

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
        exoPlayer!!.addListener(this)

        val fragment = (activity as NewsActivity).appFragmentManager!!.getFragment() as MusicListFragment
        fragment.initMediaControls(exoPlayer!!)

    }

    fun prepareExoPlayerForM3u8(url:String) {
        val userAgent = Util.getUserAgent(activity, "User Agent")
        val dataSourceFactory =  DefaultHttpDataSourceFactory(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                1800000,
                true)
        val mediaSource =  HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))

        exoPlayer!!.playWhenReady = true
        exoPlayer!!.prepare(mediaSource)
        exoPlayer!!.addListener(this)
        val fragment = (activity as NewsActivity).appFragmentManager!!.getFragment() as MusicListFragment
        fragment.initMediaControls(exoPlayer!!)
    }


    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        Log.e("TAG","onPlaybackParametersChanged")
    }

    override fun onSeekProcessed() {
        Log.e("TAG","onSeekProcessed")

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        Log.e("TAG","onTracksChanged")

    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Log.e("TAG","onPlayerError")

    }

    override fun onLoadingChanged(isLoading: Boolean) {
        Log.e("TAG","onLoadingChanged")
        if(isLoading){
            fragment!!.showLoadingProgress()
        }else{
            fragment!!.hideLoadingProgress()
        }
    }

    override fun onPositionDiscontinuity(reason: Int) {
        Log.e("TAG","onPositionDiscontinuity")

    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        Log.e("TAG","onRepeatModeChanged")

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        Log.e("TAG","onShuffleModeEnabledChanged")

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
        Log.e("TAG","onTimelineChanged")

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.e("TAG","onPlayerStateChanged")
        if (playbackState == Player.STATE_BUFFERING){
            fragment!!.showLoadingProgress()
        } else {
            fragment!!.hideLoadingProgress()
        }

    }

}