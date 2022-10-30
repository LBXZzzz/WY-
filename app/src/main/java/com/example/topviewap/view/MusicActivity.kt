package com.example.topviewap.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.music.IMusic
import com.example.music.MusicService
import com.example.topviewap.R
import com.example.topviewap.entries.Song
import com.squareup.picasso.Picasso

class MusicActivity : AppCompatActivity() {
    private var TAG = "MusicActivity"
    private lateinit var musicManager: IMusic//可以调用服务里面播放音乐的功能

    private lateinit var songList:Song

    private lateinit var mToolbar: Toolbar
    private lateinit var mIvSong:ImageView

    private var isBinder=false//用来判断服务是否已经绑定，绑定则为true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        songList=intent.getSerializableExtra("pet") as Song
        attemptToBindService()
        init()
        initLayout()
        if(isBinder){
            musicManager.startMusic()
        }

    }

    private fun init() {
        mToolbar = findViewById(R.id.too_bar_service)
        mIvSong=findViewById(R.id.iv_music_photo_service)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
    }

    private fun initLayout(){
        Picasso.with(this)
            .load(songList.al.picUrl)
            .resize(150,150)
            .into(mIvSong)
    }

    //下面代码将服务与客户端绑定
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "service connected")
            musicManager = IMusic.Stub.asInterface(service)
            isBinder=true
           // musicManager.startMusic()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "service disconnected")
        }

    }

    private fun attemptToBindService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        //取消与服务的注册
        super.onStop()
    }
}