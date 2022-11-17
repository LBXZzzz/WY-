package com.example.music.songIdNetwork



object HotSearchData {

    //ServiceCreator创建了一个接口的动态代理对象
    private val hotSearchDataService = ServiceCreator.create<HotSearchDataService>()

    //调用此方法可以通过歌曲id获取歌曲Url
    suspend fun songUrl(id: String) = hotSearchDataService.getSongUrl(id)
}