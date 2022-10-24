package com.example.topviewap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.topviewap.R
import com.example.topviewap.entries.Song
import com.squareup.picasso.Picasso


class HotDataRecyclerView(private val dataList: List<Song>,private val context:Context) :
    RecyclerView.Adapter<HotDataRecyclerView.ViewHolder>() {
    //搜索界面热搜歌曲的RecyclerView适配器

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songName: TextView = view.findViewById(R.id.tv_song_name)
        val singerName:TextView = view.findViewById(R.id.tv_singer_name)
        val picView:ImageView=view.findViewById(R.id.iv_music_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_song_item,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songName = dataList[position].name
        holder.songName.text = songName
        holder.singerName.text=dataList[position].ar[0].name
        Picasso.with(context)
            .load(dataList[position].al.picUrl)
            .resize(100,100)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.picView);
    }

    override fun getItemCount(): Int = dataList.size
}