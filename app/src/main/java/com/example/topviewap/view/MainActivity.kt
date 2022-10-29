package com.example.topviewap.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.music.MusicActivity
import com.example.topviewap.R
import com.example.topviewap.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var mViewPaper: ViewPager2
    private lateinit var mToolbar: Toolbar


    lateinit var bt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setSupportActionBar(mToolbar)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    fun initPaper() {
        //mViewPaper=findViewById(R.id.view_paper_main)
    }

    fun init() {
        mToolbar = findViewById(R.id.main_too_bar)
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