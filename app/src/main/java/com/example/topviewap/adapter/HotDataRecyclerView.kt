package com.example.topviewap.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.topviewap.R
import com.example.topviewap.entries.Data


class HotDataRecyclerView(private val hotDataList: List<Data>) :
    RecyclerView.Adapter<HotDataRecyclerView.ViewHolder>() {
    //搜索界面热搜歌曲的RecyclerView适配器

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songName: TextView = view.findViewById(R.id.tv_song_name)
        val rankNumber:TextView = view.findViewById(R.id.tv_rank_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_hot_data_item,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songName = hotDataList[position].searchWord
        val number =(position+1).toString()+". "
        holder.songName.text = songName
        holder.rankNumber.text=number
    }

    override fun getItemCount(): Int = hotDataList.size
}