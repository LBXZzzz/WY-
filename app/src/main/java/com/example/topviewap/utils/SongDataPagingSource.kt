package com.example.topviewap.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.topviewap.entries.SearchData
import com.example.topviewap.entries.Song
import com.example.topviewap.network.HotSearchData

/**
 * 继承PagingSource时需要声明两个泛型类型，第一个类型表示页数的数据类型
 * 第二个类型表示每一项数据（注意不是每一页）所对应的对象类型
 */

class SongDataPagingSource(private val key: String) : PagingSource<Int, Song>() {
    override fun getRefreshKey(state: PagingState<Int, Song>): Int? = null

    companion object{
        val songList= mutableListOf<Song>()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            val page = params.key ?: 0
            val pageSize = page * 30
            val searchData = HotSearchData.searchData(key, pageSize)
            val item = searchData.result.songs
            songList.addAll(item)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (item.isNotEmpty()) page + 1 else null
            LoadResult.Page(item, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}