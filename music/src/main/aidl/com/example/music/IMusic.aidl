// IMusic.aidl
package com.example.music;

// Declare any non-default types here with import statements

interface IMusic {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
        //开始播放音乐
        void startMusic();

        //暂停音乐
        void stopMusic();

        //下一首播放
        void nextSong();

        //上一首播放
        void preSong();
}