package com.example.roompart.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Song")
class Song {
    @PrimaryKey()
    var id: Int? = 0

    @ColumnInfo(name = "song_name")
    var songName: String? = null

    @ColumnInfo(name = "singer_name")
    var singerName: String? = null

    @ColumnInfo(name = "pic_url")
    var picUrl: String? = null
}