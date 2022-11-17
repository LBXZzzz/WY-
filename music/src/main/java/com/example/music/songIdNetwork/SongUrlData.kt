package com.example.music.songIdNetwork

data class SongUrlData(
    val code: Int,
    val `data`: List<SongData>
)

data class SongData(
    val br: Int,
    val canExtend: Boolean,
    val code: Int,
    val effectTypes: Any,
    val encodeType: String,
    val expi: Int,
    val fee: Int,
    val flag: Int,
    val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
    val freeTrialInfo: FreeTrialInfo,
    val freeTrialPrivilege: TrialPrivilege,
    val gain: Double,
    val id: Int,
    val level: String,
    val md5: String,
    val payed: Int,
    val podcastCtrp: Any,
    val rightSource: Int,
    val size: Int,
    val time: Int,
    val type: String,
    val uf: Any,
    val url: String,
    val urlSource: Int
)

data class FreeTimeTrialPrivilege(
    val remainTime: Int,
    val resConsumable: Boolean,
    val type: Int,
    val userConsumable: Boolean
)

data class FreeTrialInfo(
    val end: Int,
    val start: Int
)

data class TrialPrivilege(
    val listenType: Any,
    val resConsumable: Boolean,
    val userConsumable: Boolean
)