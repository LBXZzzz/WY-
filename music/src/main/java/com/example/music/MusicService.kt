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
        override fun startMusic() {
            val songUrl = "https://music.163.com/song/media/outer/url?id=1982706733.mp3"
            mMediaPlayer.setDataSource(songUrl)
            mMediaPlayer.prepareAsync()
            mMediaPlayer.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
                isPlayer = true
                mMediaPlayer.start()
            })
        }

        override fun stopMusic() {
            TODO("Not yet implemented")
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