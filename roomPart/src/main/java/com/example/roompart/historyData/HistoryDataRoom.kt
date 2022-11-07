package com.example.roompart.historyData

import android.content.Context
import androidx.room.Room

class HistoryDataRoom(val context: Context) {
    companion object {
        private var instance: HistoryDataBase? = null
        fun get(context: Context): HistoryDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, HistoryDataBase::class.java, "user_.db")
                    .fallbackToDestructiveMigration()
                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }


    //查询数据库里面所有的歌曲集合
    fun queryAll(): List<HistoryData> {
        return get(context).historyData().queryAll()
    }

    //插入数据
    fun insert(historyData: HistoryData) {
        get(context).historyData().insert(historyData)
    }
}