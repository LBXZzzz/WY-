package com.example.roompart.song

import android.content.Context
import androidx.room.Room

class SongRoom(val context: Context) {


    companion object {
        private var instance: SongDatabase? = null
        fun get(context: Context): SongDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, SongDatabase::class.java, "user_.db")
                    //数据库升级异常之后的回滚
                    .fallbackToDestructiveMigration()
                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }


    //查询数据库里面所有的歌曲集合
    fun queryAll(): List<Song> {
        return get(context).songDao().queryAll()
    }

    //插入数据
    fun insert(song: Song) {
        val songItem = get(context).songDao().queryById(song.id!!.toInt())
        songItem?.let { delete(song) } ?: get(context).songDao().insert(song)
    }

    fun delete(song: Song) {
        get(context).songDao().delete(song)
    }
}