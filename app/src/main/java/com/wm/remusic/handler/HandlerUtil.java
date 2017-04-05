package com.wm.remusic.handler;

import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by wm on 2016/3/26.
 */
public class HandlerUtil extends Handler {

    private static HandlerUtil instance = null;
    WeakReference<Context> mActivityReference;//弱引用，一担被GC扫到，就回收。 zsh

    public static HandlerUtil getInstance(Context context) {// 懒汉单例，线程安全，效率低 zsh
        if (instance == null) {
            instance = new HandlerUtil(context.getApplicationContext());
        }
        return instance;
    }

    HandlerUtil(Context context) {//构造方法应该加上private  zsh
        mActivityReference = new WeakReference<>(context);
    }
}
/*
弱引用：
如：
    A a = new A();
    WeakReference wr = new WeakReference(a);

    当a=null时，这个时候A只被弱引用依赖，那么GC会立刻回收A这个对象，
    这就是弱引用的好处！他可以在你对对象结构和拓扑不是很清晰的情况下，帮助你合理的释放对象，造成不必要的内存泄漏！zsh

*/