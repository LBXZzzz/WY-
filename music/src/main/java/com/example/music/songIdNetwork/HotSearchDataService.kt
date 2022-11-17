package com.example.music.songIdNetwork


import retrofit2.http.GET
import retrofit2.http.Query

interface HotSearchDataService {

    @GET("/song/url")
    suspend fun getSongUrl(
        @Query("id") keywords: String,
    ): SongUrlData
}