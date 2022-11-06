package com.example.topviewap.utils

class Util {
    //把毫秒转换为时分秒
    companion object{
        fun format(t: Int): String? {
            return if (t < 60000) {
                val s = (t % 60000 / 1000).toString()
                if (t % 60000 / 1000 > 9) {
                    "00:$s"
                } else {
                    "00:0$s"
                }
            } else if (t >= 60000 && t < 3600000) {
                getString(t % 3600000 / 60000) + ":" + getString(
                    t % 60000 / 1000
                )
            } else {
                getString(t / 3600000) + ":" + getString(
                    t % 3600000 / 60000
                ) + ":" + getString(t % 60000 / 1000)
            }
        }
        private fun getString(t: Int): String? {
            val m: String
            m = if (t > 0) {
                if (t < 10) {
                    "0$t"
                } else {
                    t.toString() + ""
                }
            } else {
                "00"
            }
            return m
        }
    }


}