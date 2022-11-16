package com.example.roompart.historyData

import androidx.room.*

@Dao
interface HistoryDataDao {
    @Query("select * from HistoryData")
    fun queryAll(): List<HistoryData>

    @Insert
    fun insert(historyData: HistoryData)
}