package com.example.topviewap.utils

import com.example.topviewap.entries.HotData
import com.example.topviewap.entries.LycData
import com.example.topviewap.entries.SearchData
import com.example.topviewap.entries.SongUrlData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HotSearchDataService {

    @GET("/search/hot/detail")
    suspend fun getHotSearchData(): HotData

    @GET("/cloudsearch")
    suspend fun getSearchData(
        @Query("keywords") keywords: String,
        @Query("offset") offset: Int
    ): SearchData

    @GET("/song/url")
    suspend fun getSongUrl(
        @Query("id") keywords: String,
    ): SongUrlData

    @GET("/lyric")
    suspend fun getLyc(
        @Query("id") keywords: String,
    ): LycData
}