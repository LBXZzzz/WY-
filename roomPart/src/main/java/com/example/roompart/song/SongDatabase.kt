package com.example.roompart.song

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 4, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

}
