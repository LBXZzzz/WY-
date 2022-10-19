package com.example.topviewap.entries

data class SearchResult(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)

data class Data(
    val alg: String,
    val content: String,
    val iconType: Int,
    val iconUrl: String,
    val score: Int,
    val searchWord: String,
    val source: Int,
    val url: String
)