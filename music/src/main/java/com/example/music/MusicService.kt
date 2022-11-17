package com.example.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.IBinder
import android.util.Log
import com.example.music.songIdNetwork.Repository
import com.example.roompart.song.SongRoom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MusicService : Service(), MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener {

    private val TAG = "MusicService"

    companion object {
        var isPlayPre = false//判断歌曲是否准备过
        var isNewSong = false//判断一下是否要播放新歌
        var songNumber = 0//当前在播放列表里的第几首歌
        var PLAY_MODE = 1//当前的播放模式
        val mMediaPlayer = MediaPlayer()
        lateinit var songRoomList: List<com.example.roompart.song.Song>
    }

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer.setOnCompletionListener(this)
        mMediaPlayer.setOnErrorListener(this)
    }

    private val musicBinder = object : IMusic.Stub() {
        override fun startMusic(url: String?) {
            Log.d("zwytt", url.toString())
            if (isNewSong) {
                mMediaPlayer.reset()
                isPlayPre = false
                isNewSong = false
            }
            if (!isPlayPre) {
                mMediaPlayer.setDataSource(url)
                mMediaPlayer.prepareAsync()
                mMediaPlayer.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
                    mMediaPlayer.start()
                    isPlayPre = true
                    Log.e("zwyii", "llllllllssss")
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
        val songRoomData = SongRoom(this)
        songRoomList = songRoomData.queryAll()
        when (PLAY_MODE) {
            1 -> {
                songNumber++
                if (songNumber >= songRoomList.size) {
                    songNumber = 0
                }
                GlobalScope.launch {
                    val sb = Repository.songUrlData(songRoomList[songNumber].id.toString())
                    musicBinder.startMusic(sb)
                }
            }
            2 -> {

            }
        }
        val intent = Intent()
        intent.action = "UPDATE"
        sendBroadcast(intent)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

}