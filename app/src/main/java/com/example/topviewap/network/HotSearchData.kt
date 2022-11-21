package com.example.topviewap.network

import com.example.topviewap.utils.HotSearchDataService
import com.example.topviewap.utils.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object HotSearchData {

    //ServiceCreator创建了一个接口的动态代理对象
    private val hotSearchDataService = ServiceCreator.create<HotSearchDataService>()

    //调用此方法获取搜索热词
    suspend fun hotSearchData() = hotSearchDataService.getHotSearchData()

    //调用此方法进行搜索
    suspend fun searchData(key: String, offset: Int) =
        hotSearchDataService.getSearchData(key, offset)

    //调用此方法可以通过歌曲id获取歌曲Url
    suspend fun songUrl(id: String) = hotSearchDataService.getSongUrl(id)

    //调用此方法可以获取歌词
    suspend fun lycData(id: String) = hotSearchDataService.getLyc(id)
}