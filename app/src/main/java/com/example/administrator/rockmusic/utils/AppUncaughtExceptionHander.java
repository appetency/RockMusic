package com.example.administrator.rockmusic.utils;

import android.util.Log;

/**
 * Created by Xiamin on 2016/10/27.
 * App异常收集器
 */

public class AppUncaughtExceptionHander implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("iii",e.toString());
        System.exit(1);
    }
}
