package com.example.topviewap.utils

import com.example.topviewap.entries.HotData
import com.example.topviewap.entries.SearchData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HotSearchDataService {
    @GET("/search/hot/detail")
    suspend fun getHotSearchData(): HotData


    @GET("/cloudsearch")
    suspend fun getSearchData(@Query("keywords") keywords: String, @Query("offset") offset: Int): SearchData
}