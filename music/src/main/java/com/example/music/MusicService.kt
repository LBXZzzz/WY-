package com.example.music

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
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
            if (isNewSong) {
                mMediaPlayer.reset()
                isPlayPre = false
                isNewSong = false
            }
            if (!isPlayPre) {
                Log.e(TAG, "当前播放歌曲的url:${url}")
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
            songRoomList = getRoomList()
            songNumber = getSongNumber()
            songNumber++
            if (songNumber >= songRoomList.size) {
                songNumber = 0
            }
            GlobalScope.launch {
                isNewSong = true
                val sb = Repository.songUrlData(songRoomList[songNumber].id.toString())
                startMusic(sb)
            }
            saveSongNumber(songNumber)
            //发送广播MusicActivity，让其更新布局
            val intent = Intent()
            intent.action = "UPDATE"
            sendBroadcast(intent)
        }


        override fun preSong() {
            songRoomList = getRoomList()
            songNumber = getSongNumber()
            songNumber--
            if (songNumber < 0) {
                songNumber = songRoomList.size - 1
            }
            GlobalScope.launch {
                isNewSong = true
                val sb = Repository.songUrlData(songRoomList[songNumber].id.toString())
                startMusic(sb)
            }
            saveSongNumber(songNumber)
            //发送广播MusicActivity，让其更新布局
            val intent = Intent()
            intent.action = "UPDATE"
            sendBroadcast(intent)
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

    @SuppressLint("CommitPrefEdits")
    override fun onCompletion(mp: MediaPlayer?) {
        songNumber = getSongNumber()
        songRoomList = getRoomList()
        when (PLAY_MODE) {
            1 -> {
                songNumber++
                if (songNumber >= songRoomList.size) {
                    songNumber = 0
                }
                GlobalScope.launch {
                    isNewSong = true
                    val sb = Repository.songUrlData(songRoomList[songNumber].id.toString())
                    musicBinder.startMusic(sb)
                }
            }
            2 -> {
                GlobalScope.launch {
                    isNewSong = true
                    val sb = Repository.songUrlData(songRoomList[songNumber].id.toString())
                    musicBinder.startMusic(sb)
                }
            }
        }
        //把当前播放第几首歌存起来
        saveSongNumber(songNumber)
        //发送广播MusicActivity，让其更新布局
        val intent = Intent()
        intent.action = "UPDATE"
        sendBroadcast(intent)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    private fun getRoomList(): List<com.example.roompart.song.Song> {
        val songRoomData = SongRoom(this)
        return songRoomData.queryAll()
    }

    private fun getSongNumber(): Int {
        val prefs = getSharedPreferences("songNumberData", Context.MODE_PRIVATE)
        return prefs.getInt("songNumber", 0)
    }

    private fun saveSongNumber(number: Int) {
        val editor = getSharedPreferences("songNumberData", Context.MODE_PRIVATE).edit()
        editor.putInt("songNumber", number)
        editor.apply()//注意不要遗漏这一个，不然没保存
    }
}