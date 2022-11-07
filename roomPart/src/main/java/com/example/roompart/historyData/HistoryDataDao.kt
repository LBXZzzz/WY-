package com.example.roompart.historyData

import androidx.room.*

@Dao
interface HistoryDataDao {
    @Query("select * from HistoryData")
    fun queryAll(): List<HistoryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyData: HistoryData)
}