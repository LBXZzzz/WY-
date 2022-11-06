package com.example.roompart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Song")
class Song {
    @PrimaryKey()
    var id :Int? = 0
    @ColumnInfo(name = "book_name")
    var bookName:String? = null
}