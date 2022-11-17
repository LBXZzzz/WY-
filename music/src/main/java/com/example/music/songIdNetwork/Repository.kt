package com.example.music.songIdNetwork


import android.util.Log

import kotlinx.coroutines.Dispatchers

object Repository {


    suspend fun songUrlData(id: String):String  {
        //liveData()函数自动构建并返回一个LiveData对象

        val searchData = HotSearchData.songUrl(id)
        if (HotSearchData.songUrl(id).code == 200) {
            return searchData.data[0].url
            //使用Kotlin内置的Result.success()方法来包装获取的数据列表
        }
        return ""
    }

}