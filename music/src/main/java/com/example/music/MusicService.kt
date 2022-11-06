package com.example.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.IBinder
import android.util.Log

class MusicService : Service() {


    companion object {
        var isPlayPre = false//判断歌曲是否准备过
        val mMediaPlayer = MediaPlayer()
    }

    private val musicBinder = object : IMusic.Stub() {
        override fun startMusic(url: String?) {
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
            return isPlayPre
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return musicBinder
    }
}