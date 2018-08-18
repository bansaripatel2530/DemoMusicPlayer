package com.sa.baseproject.appview.music.view

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.sa.baseproject.R
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ExoPlayerFactory
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import android.net.Uri
import android.support.annotation.Nullable
import android.util.Log
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.sa.baseproject.appview.music.model.AudioModel
import java.io.File


class MusicPlayerService : Service() {

    private var player: SimpleExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    val PLAYBACK_CHANNEL_ID = "playback_channel"
    val PLAYBACK_NOTIFICATION_ID = 1
    val content:Context = this
    private var data:ArrayList<AudioModel>?=null
    private var item:AudioModel?=null

    override fun onCreate() {
        super.onCreate()
        val context = this

        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        val dataSourceFactory = DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "Music Player")

        )
//        val mediaSource = ExtractorMediaSource.Factory(
//                dataSourceFactory).createMediaSource(Uri.fromFile(File(item!!.path)))
//        player!!.prepare(mediaSource)
        player!!.playWhenReady = true

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_ID,
                R.string.channel_name,
                PLAYBACK_NOTIFICATION_ID,
                object : MediaDescriptionAdapter {
                    override fun getCurrentContentTitle(player: Player): String {
                        return "SONG"
                    }

                    @Nullable
                    override fun createCurrentContentIntent(player: Player): PendingIntent? {
                        return null
                    }

                    @Nullable
                    override fun getCurrentContentText(player: Player): String? {
                        return "description"
                    }

                    @Nullable
                    override fun getCurrentLargeIcon(player: Player, callback: BitmapCallback): Bitmap? {
                        return BitmapFactory.decodeResource(resources,R.drawable.ic_play)
                    }
                }
        )
        playerNotificationManager!!.setNotificationListener(object : PlayerNotificationManager.NotificationListener {
           override
            fun onNotificationStarted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification)
            }

            override
            fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }
        })
        playerNotificationManager!!.setPlayer(player)

//        mediaSession = MediaSessionCompat(context, MEDIA_SESSION_TAG)
//        mediaSession!!.isActive = true
//        playerNotificationManager!!.setMediaSessionToken(mediaSession!!.sessionToken)
//
//        mediaSessionConnector = MediaSessionConnector(mediaSession)
//        mediaSessionConnector!!.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
//            fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
//                return Samples.getMediaDescription(context, SAMPLES[windowIndex])
//            }
//        })
//        mediaSessionConnector!!.setPlayer(player, null)
    }

    override fun onDestroy() {
        playerNotificationManager!!.setPlayer(null)
        player!!.release()
        player = null

        super.onDestroy()
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
//        item = bundle.getSerializable("List")
//        Log.e("ITEM",item!!.name)
        return Service.START_STICKY
    }

}