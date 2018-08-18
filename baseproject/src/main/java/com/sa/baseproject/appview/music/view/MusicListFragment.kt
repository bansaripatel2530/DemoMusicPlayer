package com.sa.baseproject.appview.music.view

import android.arch.lifecycle.*
import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sa.baseproject.R
import com.sa.baseproject.appview.music.adapter.MusicListAdapter
import com.sa.baseproject.appview.music.model.AudioModel
import com.sa.baseproject.appview.music.viewmodel.MusicListViewModel
import com.sa.baseproject.base.AppFragment
import kotlinx.android.synthetic.main.fragment_music_list.*
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.widget.SeekBar
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.util.Util.startForegroundService
import com.sa.baseproject.appview.news.view.NewsActivity
import com.sa.baseproject.utils.ToastUtils
import java.util.*
import kotlin.collections.ArrayList




class MusicListFragment : AppFragment(), MusicListAdapter.OnClickListener {


    private var adapter: MusicListAdapter? = null
    private var viewProvider: MusicListViewModel? = null
    private var musicPlayer: MusicPlayer? = null
    private var exoplayer: ExoPlayer? = null
    private var handler: Handler? = null
    private var item: AudioModel? = null
    private var audioList: List<AudioModel>? = null
    private var position: Int = 0

    override fun initializeComponent(view: View?) {

    }

    override fun pageVisible() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        musicPlayer = MusicPlayer(activity!!)
//        lifecycle.addObserver(musicPlayer!!)
        return inflater.inflate(R.layout.fragment_music_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setListData()
        viewProvider = ViewModelProviders.of(this).get(MusicListViewModel::class.java)
        viewProvider!!.getActivity(activity!!)
        viewProvider!!.tempAudioList.observe(this, Observer<ArrayList<AudioModel>> { list ->
            adapter!!.setData(list)
        })

    }

    private fun setListData() {
        adapter = MusicListAdapter(activity!!, this)
        val layoutManager = LinearLayoutManager(context)
        rvMusic.layoutManager = layoutManager
        rvMusic.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(activity!!,
                layoutManager.orientation)
        rvMusic.addItemDecoration(dividerItemDecoration)
        rvMusic.adapter = adapter


    }

    override fun onClick(item: AudioModel, data: ArrayList<AudioModel>, position: Int) {
        llPlay.visibility = View.VISIBLE
        this.item = item
        this.audioList = data
        this.position = position
        val intent = Intent(activity, MusicPlayerService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("B",item)
        intent.putExtra("Bundle",bundle)
        Util.startForegroundService(activity as NewsActivity, intent)
//        viewProvider!!.initializePlayer(this.item!!, tvSongName, musicPlayer)

    }




    fun initMediaControls(ex: ExoPlayer) {
//        val bundle = Bundle()
//        bundle.putParcelableArrayList("LIST",audioList as java.util.ArrayList<AudioModel>)
//        intent.putExtra("Bundle",bundle)

        this.exoplayer = ex
        initPlayButton()
        initSeekBar()
        setPlayPause(!audioList!![position].isPlaying)


    }


    private fun initPlayButton() {
        ivplay.requestFocus()
        ivplay.setOnClickListener {
            if (exoplayer != null) {
                if (ivplay.drawable.constantState == ContextCompat.getDrawable(activity!!, android.R.drawable.ic_media_play)!!.constantState) {
                    audioList!![position].isPlaying = true
                    setPlayPause(audioList!![position].isPlaying)
                } else if (ivplay.drawable.constantState == ContextCompat.getDrawable(activity!!, android.R.drawable.ic_media_pause)!!.constantState) {
                    audioList!![position].isPlaying = false
                    setPlayPause(audioList!![position].isPlaying)
                }
            } else {
                viewProvider!!.initializePlayer(audioList!![position], tvSongName, musicPlayer)
            }

        }

        ivplayNext.setOnClickListener {
            audioList!![position].isPlaying = false
            if (exoplayer != null) {
                setPlayPause(audioList!![position].isPlaying)
            }
            position = position + 1
            this.exoplayer = null
            if (position >= audioList!!.size) {
                position = 0
            }

            viewProvider!!.initializePlayer(audioList!![position], tvSongName, musicPlayer)

        }

        ivplayPrevious.setOnClickListener {
            audioList!![position].isPlaying = false
            setPlayPause(audioList!![position].isPlaying)
            position = position - 1
            this.exoplayer = null
            if (position < 0) {
                position = audioList!!.size - 1
            }
            viewProvider!!.initializePlayer(audioList!![position], tvSongName, musicPlayer)

        }

        ivShuffle.setOnClickListener {
            val seed = System.nanoTime()
            viewProvider!!.tempAudioList.postValue(audioList!!.shuffled(Random(seed)) as ArrayList<AudioModel>)
            rvMusic.scrollToPosition(0)
            ToastUtils.longToast(0, "List Change")
        }

    }

    private fun setPlayPause(play: Boolean) {
        audioList!![position].isPlaying = play
        this.exoplayer!!.playWhenReady = play
        if (!audioList!![position].isPlaying) {
            ivplay.setImageResource(android.R.drawable.ic_media_play)
        } else {
            setProgress()
            ivplay.setImageResource(android.R.drawable.ic_media_pause)
        }
    }


    private fun stringForTime(timeMs: Int): String {
        val mFormatBuilder = StringBuilder()
        val mFormatter: Formatter
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000

        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private fun setProgress() {
        seekBar.progress = 0
        seekBar.max = this.exoplayer!!.duration.toInt() / 1000
        time_current.text = stringForTime(exoplayer!!.currentPosition.toInt())
        player_end_time.text = stringForTime(exoplayer!!.duration.toInt())
        if (handler == null) handler = Handler()
        handler!!.post(object : Runnable {
            override fun run() {
                if (exoplayer != null && audioList!![position].isPlaying) {
                    if (isAdded) {
                        seekBar.max = exoplayer!!.duration.toInt() / 1000
                        val mCurrentPosition = exoplayer!!.currentPosition.toInt() / 1000
                        seekBar.progress = mCurrentPosition
                        time_current.text = stringForTime(exoplayer!!.currentPosition.toInt())
                        player_end_time.text = stringForTime(exoplayer!!.duration.toInt())
                        Log.e("CURRENT--->0", "" + exoplayer!!.currentPosition)
                        Log.e("END--->0", "" + exoplayer!!.duration)

                        if (exoplayer!!.contentPosition != 0L) {
                            if (exoplayer!!.currentPosition >= exoplayer!!.duration) {
                                Log.e("CURRENT--->1", "" + exoplayer!!.currentPosition)
                                Log.e("END--->1", "" + exoplayer!!.duration)
                                audioList!![position].isPlaying = false
                                setPlayPause(audioList!![position].isPlaying)
                                seekBar.progress = 0
                                exoplayer = null
                                position = position + 1
                                viewProvider!!.initializePlayer(audioList!![position], tvSongName, musicPlayer)
                                time_current.text = stringForTime(0)
                                handler!!.removeCallbacks(this)

                            }
                        }

                        handler!!.postDelayed(this, 1000)
                    }

                } else {
                    handler!!.removeCallbacks(this)
                }
            }
        })

    }

    private fun initSeekBar() {
        seekBar.requestFocus()
        if (exoplayer != null) {
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    exoplayer!!.seekTo((progress * 1000).toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
            seekBar.max = 0
            seekBar.max = this.exoplayer!!.duration.toInt() / 1000

        }

    }


    fun showLoadingProgress() {
        exo_progress.visibility = View.VISIBLE
        ivplay.visibility = View.GONE
    }

    fun hideLoadingProgress() {
        exo_progress.visibility = View.GONE
        ivplay.visibility = View.VISIBLE
    }




}