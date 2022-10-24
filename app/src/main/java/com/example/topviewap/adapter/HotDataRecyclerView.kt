package com.example.topviewap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topviewap.R
import com.example.topviewap.entries.Song
import com.squareup.picasso.Picasso


class HotDataRecyclerView(private val dataList: List<Song>, private val context: Context) :
    PagingDataAdapter<Song, HotDataRecyclerView.ViewHolder>(COMPARATOR) {
    //搜索界面热搜歌曲的RecyclerView适配器

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songName: TextView = view.findViewById(R.id.tv_song_name)
        val singerName: TextView = view.findViewById(R.id.tv_singer_name)
        val picView: ImageView = view.findViewById(R.id.iv_music_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_song_item,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = getItem(position)
        if (song != null) {
            holder.songName.text = song.name
            holder.singerName.text = song.ar[0].name
            Picasso.with(context)
                .load(song.al.picUrl)
                .resize(100, 100)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.picView);
        }

    }

}