package com.example.topviewap.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.*
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.*
import com.example.music.IMusic
import com.example.music.MusicService
import com.example.roompart.song.SongRoom
import com.example.topviewap.R
import com.example.topviewap.entries.LycData
import com.example.topviewap.entries.Song
import com.example.topviewap.entries.SongData
import com.example.topviewap.examples.Repository
import com.example.topviewap.utils.BlurTransformation
import com.example.topviewap.utils.Util
import com.example.topviewap.widget.LycView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MusicActivity : AppCompatActivity(), View.OnClickListener {
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
    private lateinit var mBtNextSong: ImageView
    private lateinit var mBtPreSong: ImageView
    private lateinit var lycView: LycView//歌词的view
    private lateinit var cvPhoto: CardView

    private lateinit var llyMusic: LinearLayout

    private var rotationAnim: ObjectAnimator? = null//封面旋转的动画属性

    private var isBinder = false//用来判断服务是否已经绑定，绑定则为true
    private var isPlay = false//用来判断歌曲是否在播放
    private var isDrag = false//用来判断进度条是否在拖动
    private var isRoom = false//用来判断是否从数据库内加载音乐界面


    private var PLAY_MODE = 1//用来判断播放模式，1为顺序播放,2为单曲循环，3为随机播放


    companion object {
        lateinit var songRoomList: List<com.example.roompart.song.Song>
        var isRoom = false//用来判断是否从数据库内加载音乐界面
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
                        sleep(70)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
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
        //通知栏沉浸式
        window.statusBarColor = Color.TRANSPARENT
        val lly = findViewById<LinearLayout>(R.id.lly_music)
        lly.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        song = intent.getSerializableExtra("song") as Song
        attemptToBindService()
        viewModel.searchSongUrl(song.id.toString())
        viewModel.searchLyc(song.id.toString())
        init()
        initLayout()
        initFunction()
        initAnim()
        val intentFilter = IntentFilter()
        intentFilter.addAction("UPDATE")
        val musicBroadReceiver = MusicBroadReceiver()
        registerReceiver(musicBroadReceiver, intentFilter)
        MusicService.songNumber++
        viewModel.searchSongLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                viewModel.hotDataList.clear()
                viewModel.hotDataList.add(data)
                if (isBinder) {
                    musicManager.startMusic(viewModel.hotDataList[0].url)
                }

            }
        })
        viewModel.lycSongLiveData.observe(this, Observer {
            val data = it.getOrNull()
            if (data != null) {
                viewModel.lycList.clear()
                viewModel.lycList.add(data)
                lycView.setLycList(Util.lycRow(viewModel.lycList[0].lrc.lyric))
                lycView.draw()
            }
        })
    }

    private val target: com.squareup.picasso.Target = object : com.squareup.picasso.Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, loadedFrom: Picasso.LoadedFrom?) {
            //替换背景
            llyMusic.setBackgroundDrawable(BitmapDrawable(resources, bitmap))
        }

        override fun onBitmapFailed(drawable: Drawable?) {}
        override fun onPrepareLoad(drawable: Drawable?) {}
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
        mBtNextSong = findViewById(R.id.iv_next_song_service)
        mBtPreSong = findViewById(R.id.iv_pre_song_service)
        lycView = findViewById(R.id.lyc)
        cvPhoto = findViewById(R.id.music_card_view)
        llyMusic = findViewById(R.id.lly_music)
        cvPhoto.setOnClickListener(this)
        lycView.setOnClickListener(this)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
        val songRoom = com.example.roompart.song.Song()
        songRoom.id = song.id
        songRoom.songName = song.name
        songRoom.singerName = song.ar[0].name
        songRoom.picUrl = song.al.picUrl
        val songRoomData = SongRoom(this)
        //把歌曲id存入入数据库
        songRoomData.insert(songRoom)
        if (MusicService.isPlayPre) {
            MusicService.isNewSong = true
        }
        songRoomList = songRoomData.queryAll()
        for (i in 0 until songRoomList.size) {
            Log.d("zwyuu", songRoomList[i].id.toString())
            Log.d("zwyuu", songRoomList[i].picUrl.toString())
            Log.d("zwyuu", songRoomList[i].songName.toString())
            Log.d("zwyuu", songRoomList[i].singerName.toString())
        }
        val editor = getSharedPreferences("songNumberData", Context.MODE_PRIVATE).edit()
        editor.putInt("songNumber", songRoomList.size - 1)
        editor.apply()
    }

    private fun initLayout() {
        val picUrl: String
        val songName: String
        var singerName = ""
        if (isRoom) {
            picUrl = songRoomList[MusicService.songNumber].picUrl.toString()
            songName = songRoomList[MusicService.songNumber].songName.toString()
            singerName = songRoomList[MusicService.songNumber].singerName.toString()
            viewModel.searchLyc(songRoomList[MusicService.songNumber].id.toString())
        } else {
            picUrl = song.al.picUrl
            songName = song.name
            viewModel.searchLyc(song.id.toString())
            for (i in 0 until song.ar.size) {
                singerName = song.ar[i].name + " "
            }
        }

        //设置view的背景，高斯模糊
        Picasso.with(this)
            .load(picUrl)
            .placeholder(R.drawable.loding)
            .transform(BlurTransformation(this))//高斯模糊
            .resize(150, 150)
            .into(target)

        Picasso.with(this)
            .load(picUrl)
            .placeholder(R.drawable.loding)
            //.transform(BlurTransformation(this))高斯模糊
            .resize(150, 150)
            .into(mIvSongPhoto)
        mTvSongName.text = songName
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
                    MusicService.PLAY_MODE = 2
                    PLAY_MODE = 2
                    mIvPlayMode.setImageResource(R.drawable.ic_loop_playback)
                    Toast.makeText(applicationContext, "单曲循环", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    MusicService.PLAY_MODE = 3
                    PLAY_MODE = 3
                    mIvPlayMode.setImageResource(R.drawable.ic_random_play)
                    Toast.makeText(applicationContext, "随机播放", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    PLAY_MODE = 1
                    MusicService.PLAY_MODE = 1
                    mIvPlayMode.setImageResource(R.drawable.ic_list_play)
                    Toast.makeText(applicationContext, "列表播放", Toast.LENGTH_SHORT).show()
                }
            }

        })
        mBtNextSong.setOnClickListener { v: View? ->
            musicManager.nextSong()
            isRoom = true
            initLayout()
            initFunction()
            isRoom = false
        }
        mBtPreSong.setOnClickListener {
            musicManager.preSong()
            isRoom = true
            initLayout()
            initFunction()
            isRoom = false
        }
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


    inner class MusicBroadReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e(TAG, "当前歌曲播放完毕")
            isRoom = true
            initLayout()
            initFunction()
            isRoom = false
        }

    }

    class MusicViewModal : ViewModel() {
        private val songData = MutableLiveData<String>()
        val hotDataList = mutableListOf<SongData>()
        val searchSongLiveData = Transformations.switchMap(songData) { id ->
            Repository.songUrlData(id)
        }

        fun searchSongUrl(id: String) {
            songData.value = id
        }

        private val lyc = MutableLiveData<String>()
        val lycList = mutableListOf<LycData>()
        val lycSongLiveData = Transformations.switchMap(lyc) { id ->
            Repository.songLycData(id)
        }

        fun searchLyc(id: String) {
            lyc.value = id
        }
    }

    override fun onClick(v: View) {
        showLycViewAndPhoto(v.id)
    }

    private fun showLycViewAndPhoto(id: Int) {
        when (id) {
            R.id.lyc -> {
                lycView.visibility = View.GONE
                cvPhoto.visibility = View.VISIBLE
            }
            R.id.music_card_view -> {
                lycView.visibility = View.VISIBLE
                cvPhoto.visibility = View.GONE
            }
        }
    }
}