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

    //调用此方法进行搜索
    suspend fun hotSearchData() = hotSearchDataService.getHotSearchData()



}