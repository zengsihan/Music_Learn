package com.wm.remusic.activity;

/**
 * Created by wm on 2016/12/23.
 * 音乐状态监听接口 zsh
 */
public interface MusicStateListener {

    /**
     * 更新歌曲状态信息
     */
     void updateTrackInfo();

    /**
     * 更新时间 zsh
     */
     void updateTime();

    /**
     * 改变主题 zsh
     */
     void changeTheme();

    /**
     * 重载适配器 zsh
     */
     void reloadAdapter();
}
