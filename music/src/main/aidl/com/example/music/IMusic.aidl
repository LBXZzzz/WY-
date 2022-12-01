// IMusic.aidl
package com.example.music;

// Declare any non-default types here with import statements

interface IMusic {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
        //开始播放音乐
        void startMusic(String url);

        //暂停音乐
        void stopMusic();

        //下一首播放
        void nextSong();

        //上一首播放
        void preSong();

        //拖动进度条
        void seekTo(int time);

        //判断音乐播放器是否准备好了
        boolean isHavePre();

        //获取mediaplayer当前时间
        int getCurrentDuration();

        //获取mediaplayer总时间
        int getTotalDuration();
}