/*
 * Copyright (C) 2016 Bilibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wm.remusic.uitl;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author xyczero
 * @time 16/5/2
 */
public class ThemeHelper {
    private static final String CURRENT_THEME = "theme_current";

    public static final int CARD_SAKURA = 0x1;
    public static final int CARD_HOPE = 0x2;
    public static final int CARD_STORM = 0x3;
    public static final int CARD_WOOD = 0x4;
    public static final int CARD_LIGHT = 0x5;
    public static final int CARD_THUNDER = 0x6;
    public static final int CARD_SAND = 0x7;
    public static final int CARD_FIREY = 0x8;

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE);//获取一个共享参数的实例，定义好名字为multiple_theme（后缀好像是xml），模式为私有，然后返回供其使用。zsh
    }

    public static void setTheme(Context context, int themeId) {//设置主题
        getSharePreference(context).edit()      //编辑主题
                .putInt(CURRENT_THEME, themeId) //设置
                .commit();                    //提交
    }

    public static int getTheme(Context context) {
        return getSharePreference(context).getInt(CURRENT_THEME, CARD_SAKURA);//得到主题，名字为theme_current，如果没有设置过，则默认使用0x1的主题 zsh
    }

    public static boolean isDefaultTheme(Context context) {//判断是否为默认的主题 zsh
        return getTheme(context) == CARD_SAKURA;
    }

    public static String getName(int currentTheme) {//得到主题的名字 zsh
        switch (currentTheme) {
            case CARD_SAKURA:
                return "THE SAKURA";
            case CARD_STORM:
                return "THE STORM";
            case CARD_WOOD:
                return "THE WOOD";
            case CARD_LIGHT:
                return "THE LIGHT";
            case CARD_HOPE:
                return "THE HOPE";
            case CARD_THUNDER:
                return "THE THUNDER";
            case CARD_SAND:
                return "THE SAND";
            case CARD_FIREY:
                return "THE FIREY";
        }
        return "THE RETURN";
    }
}
