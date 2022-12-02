package com.example.topviewap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.MusicService
import com.example.roompart.song.SongRoom
import com.example.topviewap.R
import com.example.topviewap.adapter.MusicListRecyclerViewAdapter

class MusicListActivity : AppCompatActivity() {
    private lateinit var lly: LinearLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var rv: RecyclerView
    lateinit var songRoomList: List<com.example.roompart.song.Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        init()
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        val songRoomData = SongRoom(this)
        songRoomList = songRoomData.queryAll()
        val adapter = MusicListRecyclerViewAdapter(this, songRoomList)
        rv.adapter = adapter
    }

    private fun init() {
        lly = findViewById(R.id.lly_music_list)
        mToolbar = findViewById(R.id.music_list_too_bar)
        rv = findViewById(R.id.rv_music_list)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
    }
}