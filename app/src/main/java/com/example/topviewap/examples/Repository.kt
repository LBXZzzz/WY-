package com.example.topviewap.examples

import android.util.Log
import androidx.lifecycle.liveData
import com.example.topviewap.network.HotSearchData
import kotlinx.coroutines.Dispatchers

object Repository {

    fun hotSearch() = liveData(Dispatchers.IO) {
        //liveData()函数自动构建并返回一个LiveData对象
        val result = try {
            //开始搜索数据，返回码为200则成功
            val hotSearchData = HotSearchData.hotSearchData()
            if (hotSearchData.code == 200){
                val hotData = hotSearchData.data
                //使用Kotlin内置的Result.success()方法来包装获取的数据列表
                Result.success(hotData)
            }else{
                Result.failure(RuntimeException(""))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
        //emit()方法其实类似于调用LiveData的setValue()方法来通知数据变化
        emit(result)
    }

    fun searchData(key:String,offset:Int) = liveData(Dispatchers.IO) {
        //liveData()函数自动构建并返回一个LiveData对象
        val result = try {
            //开始搜索数据，返回码为200则成功
            val searchData = HotSearchData.searchData(key,offset)
            if (HotSearchData.searchData(key,offset).code == 200){
                val searchDataList = searchData.result.songs
                Log.d("zwyuu",searchDataList.toString())
                //使用Kotlin内置的Result.success()方法来包装获取的数据列表
                Result.success(searchDataList)
            }else{
                Result.failure(RuntimeException(""))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
        //emit()方法其实类似于调用LiveData的setValue()方法来通知数据变化
        emit(result)
    }
}