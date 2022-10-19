package com.example.topviewap.utils

import com.example.topviewap.entries.SearchResult
import retrofit2.Call
import retrofit2.http.GET

interface HotSearchDataService {
    @GET("/search/hot/detail")
    suspend fun getHotSearchData(): SearchResult
}