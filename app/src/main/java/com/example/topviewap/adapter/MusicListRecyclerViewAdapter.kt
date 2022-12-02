package com.example.topviewap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roompart.song.Song
import com.example.topviewap.R
import com.squareup.picasso.Picasso


class MusicListRecyclerViewAdapter(private val context: Context, songItemArrayList: List<Song>) :
    RecyclerView.Adapter<MusicListRecyclerViewAdapter.ViewHolder?>() {
    private val songItemArrayList: List<Song>
    private val mContext: Context

    init {
        this.songItemArrayList = songItemArrayList
        mContext = context
    }

    //recyclerview的点击事件的接口
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    var mOnItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songName: TextView = itemView.findViewById(R.id.tv_song_name)
        val singerName: TextView = itemView.findViewById(R.id.tv_singer_name)
        val picView: ImageView = itemView.findViewById(R.id.iv_music_photo)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == songItemArrayList.size) {
            //最后一个 是底部item
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_song_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songItemArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songItemArrayList[position]
        if (song != null) {
            holder.songName.text = song.songName
            holder.singerName.text = song.singerName
            Picasso.with(context)
                .load(song.picUrl)
                .resize(100, 100)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.picView);
        }
        holder.itemView.setOnClickListener { v: View? ->
            val position1 = holder.layoutPosition
            mOnItemClickListener!!.onItemClick(holder.itemView, position1)
        }
    }

}