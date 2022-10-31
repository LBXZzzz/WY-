package com.example.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.IBinder

class MusicService : Service() {

    private val mMediaPlayer = MediaPlayer()

    companion object {
        var isPlayer = false//歌曲是否在播放
    }

    private val musicBinder = object : IMusic.Stub() {
        override fun startMusic(url: String?) {
            mMediaPlayer.setDataSource(url)
            mMediaPlayer.prepareAsync()
            mMediaPlayer.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
                isPlayer = true
                mMediaPlayer.start()
            })
        }


        override fun stopMusic() {
            if (isPlayer) {
                mMediaPlayer.pause()
            }
        }

        override fun nextSong() {
            TODO("Not yet implemented")
        }

        override fun preSong() {
            TODO("Not yet implemented")
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return musicBinder
    }
}