package com.example.roompart

import androidx.room.*

@Dao
interface  SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: Song)

    @Delete
    fun delete(song: Song)

    @Update
    fun update(song: Song)

    @Query("select * from Song where id =:id")
    fun queryById(id:Int) : Song

    @Query("select * from Song")
    fun queryAll():List<Song>

}
