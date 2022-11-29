package com.example.roompart.historyData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roompart.song.Song
import com.example.roompart.song.SongDao

@Database(entities = [HistoryData::class], version = 6, exportSchema = false)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyData(): HistoryDataDao
}