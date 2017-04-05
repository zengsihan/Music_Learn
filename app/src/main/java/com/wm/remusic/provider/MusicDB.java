/*
* Copyright (C) 2014 The CyanogenMod Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.wm.remusic.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wm.remusic.recent.SongPlayCount;

//音乐数据库辅助类 zsh
public class MusicDB extends SQLiteOpenHelper {

    public static final String DATABASENAME = "musicdb.db";//数据库名字，zsh
    private static final int VERSION = 4;//版本号 zsh
    private static MusicDB sInstance = null;//实例化对象名字 zsh

    private final Context mContext;//上下文对象。 zsh

    public MusicDB(final Context context) {
        super(context, DATABASENAME, null, VERSION);//上下文对象，数据库名字，游标对象，版本号 zsh

        mContext = context;
    }

    public static final synchronized MusicDB getInstance(final Context context) {//单列 zsh
        if (sInstance == null) {
            sInstance = new MusicDB(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MusicPlaybackState.getInstance(mContext).onCreate(db);
        RecentStore.getInstance(mContext).onCreate(db);
        SongPlayCount.getInstance(mContext).onCreate(db);
        SearchHistory.getInstance(mContext).onCreate(db);
        PlaylistInfo.getInstance(mContext).onCreate(db);
        PlaylistsManager.getInstance(mContext).onCreate(db);
        DownFileStore.getInstance(mContext).onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MusicPlaybackState.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        RecentStore.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        SongPlayCount.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        SearchHistory.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        PlaylistInfo.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        PlaylistsManager.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        DownFileStore.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
    }

    @Override  //版本回退 zsh
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MusicPlaybackState.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
        RecentStore.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
        SongPlayCount.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
        SearchHistory.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
        PlaylistInfo.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
        PlaylistsManager.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
        DownFileStore.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
    }
}
