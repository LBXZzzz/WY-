package com.example.topviewap.view


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.topviewap.R
import com.example.topviewap.adapter.MainFragmentAdapter
import com.example.topviewap.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mainViewModel: MainViewModel
    private lateinit var mViewPaper2: ViewPager2
    private lateinit var mToolbar: Toolbar
    private lateinit var mivMusic: ImageView
    private var ivCurrent: ImageView? = null
    private lateinit var mivUser: ImageView
    private lateinit var llUser: LinearLayout
    private lateinit var llMusic: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setSupportActionBar(mToolbar)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initPaper()
    }

    fun init() {
        mToolbar = findViewById(R.id.main_too_bar)
        mViewPaper2 = findViewById(R.id.view_paper_main)
        mivMusic = findViewById(R.id.music_photo)
        mivUser = findViewById(R.id.user_photo)
        llUser = findViewById(R.id.user)
        llMusic = findViewById(R.id.music)
        llUser.setOnClickListener(this)
        llMusic.setOnClickListener(this)
        ivCurrent = mivMusic
    }

    fun initPaper() {
        //mViewPaper=findViewById(R.id.view_paper_main)
        mViewPaper2 = findViewById<ViewPager2>(R.id.view_paper_main)
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(MusicFragment.newInstance("1", "2"))
        fragmentList.add(MusicFragment.newInstance("1", "2"))
        val mainFragmentAdapter = MainFragmentAdapter(
            supportFragmentManager,
            lifecycle, fragmentList
        )
        mViewPaper2.adapter = mainFragmentAdapter
        mViewPaper2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { //滚动的动画
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) { //页面选择了之后，实现响应事件
                super.onPageSelected(position)
                changeTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    private fun changeTab(position: Int) {
        ivCurrent!!.isSelected = false
        when (position) {
            R.id.music -> {
                mViewPaper2.currentItem = 0
                mivMusic.isSelected = true
                ivCurrent = mivMusic
            }
            0 -> {
                mivMusic.isSelected = true
                ivCurrent = mivMusic
            }
            R.id.user -> {
                mViewPaper2.currentItem = 1
                ivCurrent = mivUser
                mivUser.isSelected = true
            }
            1 -> {
                ivCurrent = mivUser
                mivUser.isSelected = true
            }
        }
    }

    override fun onClick(view: View) {
        Log.e("zwyuu", "ppp")
        changeTab(view.id)
    }

    //对Toolbar菜单设置功能
    //onCreateOptionsMenu()加载了toolbar.xml这个菜单文件
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    //onOptionsItemSelected()方法中处理各个按钮的点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backup -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }


}