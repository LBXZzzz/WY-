package com.example.topviewap.utils

import com.example.topviewap.entries.Lyric

class Util {
    //把毫秒转换为时分秒
    companion object {
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

        /**
         *[00:00.000] 作词 : 薛之谦
         *[00:00.481] 作曲 : 薛之谦
         */
        fun lycRow(lycContent: String): ArrayList<Lyric> {
            var lycList = ArrayList<Lyric>()
            val lastIndexOfRightBracket: Int = lycContent.lastIndexOf("]")
            val content: String =
                lycContent.substring(lastIndexOfRightBracket + 1, lycContent.length)
            val times: String =
                lycContent.substring(0, lastIndexOfRightBracket + 1).replace("[", "-")
                    .replace("]", "-")
            return lycList
        }
    }


}