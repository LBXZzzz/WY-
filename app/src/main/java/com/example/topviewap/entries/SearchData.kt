package com.example.topviewap.entries

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

data class SearchData(
    val code: Int,
    val result: Result
): Serializable

data class Result(
    val searchQcReminder: Any,
    val songCount: Int,
    val songs: List<Song>
): Serializable


data class Song(
    val a:@RawValue Any,
    val al:@RawValue Al,
    val alia: List<String>,
    val ar:@RawValue List<Ar>,
    val cd: String,
    val cf: String,
    val copyright: Int,
    val cp: Int,
    val crbt:@RawValue Any,
    val djId: Int,
    val dt: Int,
    val entertainmentTags:@RawValue Any,
    val fee: Int,
    val ftype: Int,
    val h:@RawValue H,
    val hr:@RawValue Hr,
    val id: Int,
    val l:@RawValue L,
    val m:@RawValue M,
    val mark: Int,
    val mst: Int,
    val mv: Int,
    val name: String,
    val no: Int,
    val noCopyrightRcmd:@RawValue Any,
    val originCoverType: Int,
    val originSongSimpleData:@RawValue Any,
    val pop: Int,
    val privilege:@RawValue Privilege,
    val pst: Int,
    val publishTime: Long,
    val resourceState: Boolean,
    val rt: String,
    val rtUrl:@RawValue Any,
    val rtUrls:@RawValue List<Any>,
    val rtype: Int,
    val rurl:@RawValue Any,
    val s_id: Int,
    val single: Int,
    val songJumpInfo:@RawValue Any,
    val sq:@RawValue Sq,
    val st: Int,
    val t: Int,
    val tagPicList:@RawValue Any,
    val v: Int,
    val version: Int
): Serializable

data class Al(
    val id: Int,
    val name: String,
    val pic: Long,
    val picUrl: String,
    val pic_str: String,
    val tns: List<Any>
): Serializable

data class Ar(
    val alia: List<String>,
    val alias: List<String>,
    val id: Int,
    val name: String,
    val tns: List<Any>
): Serializable

data class H(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
): Serializable

data class Hr(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
): Serializable

data class L(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
): Serializable

data class M(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
): Serializable

data class Privilege(
    val chargeInfoList: List<ChargeInfo>,
    val cp: Int,
    val cs: Boolean,
    val dl: Int,
    val dlLevel: String,
    val downloadMaxBrLevel: String,
    val downloadMaxbr: Int,
    val fee: Int,
    val fl: Int,
    val flLevel: String,
    val flag: Int,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val id: Int,
    val maxBrLevel: String,
    val maxbr: Int,
    val payed: Int,
    val pl: Int,
    val plLevel: String,
    val playMaxBrLevel: String,
    val playMaxbr: Int,
    val preSell: Boolean,
    val rscl: Any,
    val sp: Int,
    val st: Int,
    val subp: Int,
    val toast: Boolean
): Serializable

data class Sq(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
): Serializable

data class ChargeInfo(
    val chargeMessage: Any,
    val chargeType: Int,
    val chargeUrl: Any,
    val rate: Int
): Serializable

data class FreeTrialPrivilege(
    val listenType: Any,
    val resConsumable: Boolean,
    val userConsumable: Boolean
): Serializable