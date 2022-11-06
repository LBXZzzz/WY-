package com.example.topviewap.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import com.example.music.IMusic
import com.example.music.MusicService
import com.example.roompart.SongRoom
import com.example.topviewap.R
import com.example.topviewap.entries.Song
import com.example.topviewap.entries.SongData
import com.example.topviewap.examples.Repository
import com.example.topviewap.utils.Util
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MusicActivity : AppCompatActivity() {
    private var TAG = "MusicActivity"
    private lateinit var musicManager: IMusic//可以调用服务里面播放音乐的功能

    private lateinit var song: Song

    private lateinit var mToolbar: Toolbar
    private lateinit var mIvSongPhoto: CircleImageView//专辑封面的图片
    private lateinit var mIvPlay: ImageView//播放暂停按钮
    private lateinit var mIvPlayMode: ImageView//选择音乐播放模式的按钮
    private lateinit var mSeekBar: SeekBar
    private lateinit var mTvSongName: TextView
    private lateinit var mTvSingerName: TextView
    private lateinit var mTvTotalTime: TextView
    private lateinit var mTvCurrentTime: TextView

    private var rotationAnim: ObjectAnimator? = null//封面旋转的动画属性

    private var isBinder = false//用来判断服务是否已经绑定，绑定则为true
    private var isPlay = false//用来判断歌曲是否在播放
    private var isDrag = false//用来判断进度条是否在拖动
    private var PLAY_MODE = 1//用来判断播放模式，1为顺序播放。2为单曲循环，3为随机播放


    companion object {
        private var number = 0//现在在放第几首歌
        private var songList = ArrayList<Song>()
    }

    private val viewModel by lazy { ViewModelProvider(this).get(MusicViewModal::class.java) }

    val thread = object : Thread() {
        override fun run() {
            while (true) {
                while (isPlay && musicManager.isHavePre) {
                    val message = Message()
                    handler.sendMessage(message)
                    //该判断条件说明MediaPlayer应该准备好了
                    if (MusicService.mMediaPlayer.duration != 0) {
                        mSeekBar.max = MusicService.mMediaPlayer.duration
                    }
                    //如果没在拖动进度条才实时更新歌曲进度
                    if (!isDrag) {
                        mSeekBar.progress = (MusicService.mMediaPlayer.currentPosition)
                    }
                    try {
                        // 每70毫秒更新一次位置
                        sleep(70);

                    } catch (e: InterruptedException) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            mTvTotalTime.text = Util.format(MusicService.mMediaPlayer.duration)
            mTvCurrentTime.text = Util.format(MusicService.mMediaPlayer.currentPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        song = intent.getSerializableExtra("song") as Song
        songList.add(song)
        attemptToBindService()
        init()
        initLayout()
        initFunction()
        initAnim()
        viewModel.searchSongUrl(songList[number].id.toString())
        number++
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
        mIvSongPhoto = findViewById(R.id.iv_music_photo_service)
        mIvPlay = findViewById(R.id.iv_music_play_service)
        mIvPlayMode = findViewById(R.id.iv_play_mode_service)
        mSeekBar = findViewById(R.id.seekbar_service)
        mTvSongName = findViewById(R.id.tv1_toolbar)
        mTvSingerName = findViewById(R.id.tv2_toolbar)
        mTvTotalTime = findViewById(R.id.tv_service_total_time)
        mTvCurrentTime = findViewById(R.id.tv_service_current_time)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
        val songRoom = com.example.roompart.Song()
        songRoom.id = song.id
        val songRoomData = SongRoom(this)
        //把歌曲id存入入数据库
        songRoomData.insert(songRoom)
        Log.d("zwyuu", songRoomData.queryAll()[0].id.toString())
        if (MusicService.isPlayPre) {
            MusicService.isNewSong = true
        }
    }

    private fun initLayout() {
        Picasso.with(this)
            .load(song.al.picUrl)
            .placeholder(R.drawable.loding)
            .resize(150, 150)
            .into(mIvSongPhoto)
        mTvSongName.text = song.name
        var singerName = ""
        for (i in 0 until song.ar.size) {
            singerName = song.ar[i].name + " "
        }
        mTvSingerName.text = singerName
    }

    //用来初始化音乐播放界面的按钮功能
    private fun initFunction() {
        isPlay = true
        mIvPlay.isSelected = isPlay
        mIvPlay.setOnClickListener(View.OnClickListener { v: View? ->
            if (isPlay) {
                mIvPlay.isSelected = false
                rotationAnim?.pause()
                musicManager.stopMusic()
                isPlay = false
            } else {
                mIvPlay.isSelected = true
                rotationAnim?.resume()
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
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressNow = 0

            //拖动条进度改变的时候调用
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressNow = progress
            }

            //拖动条开始拖动的时候调用
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isDrag = true
            }

            //拖动条停止拖动的时候调用
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isDrag = false
                musicManager.seekTo(progressNow)
            }
        })
    }

    //封面旋转动画属性设置
    private fun initAnim() {
        //第一个参数是需要旋转的View，
        // 第二个是动画类型（包括alpha/rotation/scale/translate），
        // 第三个参数是旋转开始时的角度
        //第四个参数是旋转结束时的角
        rotationAnim = ObjectAnimator.ofFloat(mIvSongPhoto, "rotation", 0f, 359f)
        if (rotationAnim != null) {
            rotationAnim?.duration = (20 * 1000)
            rotationAnim?.interpolator = LinearInterpolator()//匀速
            rotationAnim?.repeatCount = -1//设置动画重复次数（-1代表一直转）
            rotationAnim?.repeatMode = ValueAnimator.RESTART//动画重复模式
            /*rotationAnim?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation -> //更新歌曲封面旋转度同步
                mIvPlay.setRotation(animation.animatedValue as Float)
            })*/
            rotationAnim?.start()
            rotationAnim?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                    rotationAnim?.start()
                }
            })
        }
    }


    //下面代码将服务与客户端绑定
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "service connected")
            musicManager = IMusic.Stub.asInterface(service)
            isBinder = true
            thread.start()//服务连接后计时线程开启
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