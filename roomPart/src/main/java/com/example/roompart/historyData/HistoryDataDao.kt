package com.example.roompart.historyData

import androidx.room.*
import com.example.roompart.song.Song

@Dao
interface HistoryDataDao {
    @Query("select * from HistoryData")
    fun queryAll(): List<HistoryData>

    @Insert
    fun insert(historyData: HistoryData)

    @Query("select * from HistoryData where song_name =:songName")
    fun findBySongName(songName: String): HistoryData?

    @Delete
    fun delete(historyData: HistoryData)
}