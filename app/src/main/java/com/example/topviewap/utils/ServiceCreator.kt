package com.example.topviewap.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    //网易云的基本网址
    private const val BASE_URL="http://106.15.2.32:3000/"

    //使用Retrofit.Builder构建一个Retrofit对象,于外部不可见
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //外部可见的create()方法
    //调用了Retrofit对象的create()方法，从而创建出相应Service接口的动态代理对象
    fun <T> create (serviceClass :Class<T>):T= retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}