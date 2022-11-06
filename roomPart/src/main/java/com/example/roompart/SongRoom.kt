package com.example.roompart

import android.content.Context
import androidx.room.Room

class SongRoom(val context: Context) {



    companion object{
        private var instance: SongDatabase? = null
        fun get(context: Context):SongDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, SongDatabase::class.java, "user_.db")
                    .fallbackToDestructiveMigration()
                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }


    //查询数据库里面所有的歌曲集合
    fun queryAll():List<Song>{
        return get(context).songDao().queryAll()
    }

    //插入数据
    fun insert(song: Song){
        get(context).songDao().insert(song)
    }
    /*//删除数据
    fun delete()*/
}