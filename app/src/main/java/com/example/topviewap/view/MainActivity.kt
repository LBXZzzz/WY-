package com.example.topviewap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.topviewap.R
import com.example.topviewap.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel:MainViewModel
    private lateinit var mViewPaper:ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)
    }

    fun initPaper(){
        mViewPaper=findViewById(R.id.view_paper_main)
        //
        val fragmentList= mutableListOf<Fragment>()
        fragmentList.add(MusicFragment.newInstance("1","1"))
        fragmentList.add(MusicFragment.newInstance("1","1"))
    }
}