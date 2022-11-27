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
            val lycList = ArrayList<Lyric>()
            val sb: String =
                //substring()检索
                lycContent.substring(0, lycContent.length).replace("[", "/n[")
            val str: List<String> = sb.split("/n")
            //特别注意根据此方法获得的字符串数组第一个为空，所以要从1开始
            for (i in 1 until str.size) {
                val lastIndexOfRightBracket: Int = str[i].lastIndexOf("]")
                val content: String =
                    str[i].substring(lastIndexOfRightBracket + 1, str[i].length)
                val time: String = str[i].substring(0, lastIndexOfRightBracket + 1)
                val lyricSingle = Lyric(content, timeStr(time))
                lycList.add(lyricSingle)
            }
            return lycList
        }

        fun timeStr(timeStr: String): Int {
            var timeStr1 = timeStr.replace(":", ".")
            timeStr1 = timeStr1.replace("[", "")
            timeStr1 = timeStr1.replace("]", "")
            timeStr1 = timeStr1.replace(".", "@")
            val strTime: List<String> = timeStr1.split("@")
            //分离出分、秒并转换为整型
            val minute = Integer.parseInt(strTime[0]);
            val second = Integer.parseInt(strTime[1]);
            val millisecond = Integer.parseInt(strTime[2]);
            //计算上一行与下一行的时间转换为毫秒数
            val currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
            return currentTime;
        }
    }

}