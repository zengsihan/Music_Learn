/**
 * Copyright (lrc_arrow) www.longdw.com
 */

package com.wm.remusic.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wm.remusic.R;


/**
 * 引导页图片，停留若干秒，然后自动消失。
 */
public class SplashScreen {

    public final static int SLIDE_LEFT = 1;
    public final static int SLIDE_UP = 2;
    public final static int FADE_OUT = 3;

    private Dialog splashDialog;

    private Activity activity;

    public SplashScreen(Activity activity) {
        this.activity = activity;
    }

    /**
     * 显示。
     *
     * @param imageResource 图片资源
     * @param millis        停留时间，以毫秒为单位。
     * @param animation     消失时的动画效果，取值可以是：SplashScreen.SLIDE_LEFT, SplashScreen.SLIDE_UP, SplashScreen.FADE
     */
    public void show(final int imageResource, final int animation) {
        Runnable runnable = new Runnable() {
            public void run() {
                // Get reference to display
                DisplayMetrics metrics = new DisplayMetrics();
//                Display display = activity.getWindowManager().getDefaultDisplay();

                // Create the layout for the dialog
                LinearLayout root = new LinearLayout(activity);//实例化线性布局 zsh
                root.setMinimumHeight(metrics.heightPixels);//设置最小的高度 zsh
                root.setMinimumWidth(metrics.widthPixels);//设置最小的宽度 zsh
                root.setOrientation(LinearLayout.VERTICAL);//设置线性布局的方向，垂直的 zsh
                root.setBackgroundColor(Color.BLACK);//这是背景颜色 zsh
                root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.0F)); //
                root.setBackgroundResource(imageResource);//设置背景的图片资源 zsh

                // Create and show the dialog
                splashDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);//实例化一个对话窗口 zsh
                // check to see if the splash screen should be full screen     检查这个启动画面是否应该设置全屏 zsh
                if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        == WindowManager.LayoutParams.FLAG_FULLSCREEN) {//转成二进制，按位与得出结果后，再“==”来判断 zsh
                    splashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏 zsh
                }
                Window window = splashDialog.getWindow();//得到window对象 zsh
                switch (animation) {//选择动画效果 zsh
                    case SLIDE_LEFT:
                        window.setWindowAnimations(R.style.dialog_anim_slide_left);
                        break;
                    case SLIDE_UP:
                        window.setWindowAnimations(R.style.dialog_anim_slide_up);
                        break;
                    case FADE_OUT:
                        window.setWindowAnimations(R.style.dialog_anim_fade_out);
                        break;
                }

                splashDialog.setContentView(root);//把定义好的布局加入到对话框 zsh
                splashDialog.setCancelable(false);//dialog弹出后会点击屏幕或物理返回键，dialog不消失 zsh
                splashDialog.show();//显示对话框 zsh

                // Set Runnable to remove splash screen just in case
                /*final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        removeSplashScreen();
                    }
                }, millis);*/
            }
        };
        activity.runOnUiThread(runnable);//把这个动作加入到主线程启动。 zsh
    }

    public void removeSplashScreen() {//移除对话框 zsh
        if (splashDialog != null && splashDialog.isShowing()) {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }

}
