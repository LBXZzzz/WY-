package com.example.roompart.historyData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HistoryData")
class HistoryData {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0

    @ColumnInfo(name = "song_name")
    var data: String? = null
}