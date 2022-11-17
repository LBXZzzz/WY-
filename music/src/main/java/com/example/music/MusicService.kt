package com.example.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.IBinder
import android.util.Log
import com.example.music.songIdNetwork.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MusicService : Service() ,MediaPlayer.OnCompletionListener,
MediaPlayer.OnErrorListener{

    private val TAG = "MusicService"

    companion object {
        var isPlayPre = false//判断歌曲是否准备过
        var isNewSong = false//判断一下是否要播放新歌
        val mMediaPlayer = MediaPlayer()
    }

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer.setOnCompletionListener(this)
        mMediaPlayer.setOnErrorListener(this)
    }
    private val musicBinder = object : IMusic.Stub() {
        override fun startMusic(url: String?) {
            if (isNewSong) {
                mMediaPlayer.reset()
                isPlayPre = false
            }
            if (!isPlayPre) {
                mMediaPlayer.setDataSource(url)
                mMediaPlayer.prepareAsync()
                mMediaPlayer.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
                    mMediaPlayer.start()
                    isPlayPre = true
                })
            } else {
                mMediaPlayer.start()
            }

        }


        override fun stopMusic() {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
            }
        }

        override fun nextSong() {
            TODO("Not yet implemented")
        }


        override fun preSong() {
            TODO("Not yet implemented")
        }

        override fun seekTo(time: Int) {
            mMediaPlayer.seekTo(time)
        }

        override fun isHavePre(): Boolean {
            return mMediaPlayer.isPlaying
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return musicBinder
    }

    override fun onCompletion(mp: MediaPlayer?) {
        /*when (PLAY_MODE) {
            1 -> {
                ++
                if (number >= songRoomList.size) {
                    number = 0
                }
            }
            2 -> {

            }
        }*/
         GlobalScope.launch {
             val sb=Repository.songUrlData("516657051")
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("Not yet implemented")
    }

}