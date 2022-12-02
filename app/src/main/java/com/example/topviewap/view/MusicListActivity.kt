package com.example.topviewap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.topviewap.R

class MusicListActivity : AppCompatActivity() {
    private lateinit var lly: LinearLayout
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        init()

    }

    private fun init() {
        lly = findViewById(R.id.lly_music_list)
        mToolbar = findViewById(R.id.music_list_too_bar)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
    }
}