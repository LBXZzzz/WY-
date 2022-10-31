package com.example.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.music.MusicService

class MusicActivity : AppCompatActivity() {

    private var TAG = "MusicActivity"
    private lateinit var musicManager: IMusic//可以调用服务里面播放音乐的功能

    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        attemptToBindService()
        init()
    }

    private fun init() {
        mToolbar = findViewById(R.id.too_bar_service)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
    }

    //下面代码将服务与客户端绑定
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "service connected")
            musicManager = IMusic.Stub.asInterface(service)
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