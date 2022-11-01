package com.example.topviewap.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import com.example.music.IMusic
import com.example.music.MusicService
import com.example.topviewap.R
import com.example.topviewap.entries.Song
import com.example.topviewap.entries.SongData
import com.example.topviewap.examples.Repository
import com.squareup.picasso.Picasso

class MusicActivity : AppCompatActivity() {
    private var TAG = "MusicActivity"
    private lateinit var musicManager: IMusic//可以调用服务里面播放音乐的功能

    private lateinit var song: Song
    private var songList= ArrayList<Song>()
    private var number=0

    private lateinit var mToolbar: Toolbar
    private lateinit var mIvSong: ImageView//专辑封面的图片
    private lateinit var mIvPlay: ImageView//播放暂停按钮
    private lateinit var mIvPlayMode: ImageView//选择音乐播放模式的按钮

    private var isBinder = false//用来判断服务是否已经绑定，绑定则为true
    private var isPlay = false//用来判断歌曲是否在播放
    private var PLAY_MODE = 1//用来判断播放模式，1为顺序播放。2为单曲循环，3为随机播放

    private val viewModel by lazy { ViewModelProvider(this).get(MusicViewModal::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        song = intent.getSerializableExtra("song") as Song
        songList.add(song)
        number++
        attemptToBindService()
        init()
        initLayout()
        initFunction()
        viewModel.searchSongUrl(songList[number].id.toString())
        viewModel.hotSearchLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                viewModel.hotDataList.clear()
                viewModel.hotDataList.add(data)
                if (isBinder) {
                    musicManager.startMusic(viewModel.hotDataList[0].url)
                }
            }
        })
    }

    private fun init() {
        mToolbar = findViewById(R.id.too_bar_service)
        mIvSong = findViewById(R.id.iv_music_photo_service)
        mIvPlay = findViewById(R.id.iv_music_play_service)
        mIvPlayMode = findViewById(R.id.iv_play_mode_service)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
    }

    private fun initLayout() {
        Picasso.with(this)
            .load(song.al.picUrl)
            .resize(150, 150)
            .into(mIvSong)
    }

    //用来初始化音乐播放界面的按钮功能
    private fun initFunction() {
        isPlay = true
        mIvPlay.isSelected = isPlay
        mIvPlay.setOnClickListener(View.OnClickListener { v: View? ->
            if (isPlay) {
                mIvPlay.isSelected = false
                musicManager.stopMusic()
                isPlay = false
            } else {
                mIvPlay.isSelected = true
                musicManager.startMusic("")
                isPlay = true
            }
        })
        mIvPlayMode.setOnClickListener(View.OnClickListener { v: View? ->
            when (PLAY_MODE) {
                1 -> {
                    PLAY_MODE = 2
                    mIvPlayMode.setImageResource(R.drawable.ic_list_play)
                    Toast.makeText(applicationContext, "列表播放", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    PLAY_MODE = 3
                    mIvPlayMode.setImageResource(R.drawable.ic_loop_playback)
                    Toast.makeText(applicationContext, "单曲循环", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    PLAY_MODE = 1
                    mIvPlayMode.setImageResource(R.drawable.ic_random_play)
                    Toast.makeText(applicationContext, "随机播放", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    //下面代码将服务与客户端绑定
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "service connected")
            musicManager = IMusic.Stub.asInterface(service)
            isBinder = true
            //val songId=viewModel.hotDataList[0].url
            //musicManager.startMusic(songId)
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

    class MusicViewModal : ViewModel() {
        private val songData = MutableLiveData<String>()
        val hotDataList = mutableListOf<SongData>()
        val hotSearchLiveData = Transformations.switchMap(songData) { id ->
            Repository.songUrlData(id)
        }

        fun searchSongUrl(id: String) {
            songData.value = id
        }
    }
}