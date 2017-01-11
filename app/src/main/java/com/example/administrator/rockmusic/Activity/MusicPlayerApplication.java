package com.example.administrator.rockmusic.Activity;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.util.LongSparseArray;
import android.util.DisplayMetrics;

import com.example.administrator.rockmusic.utils.AppUncaughtExceptionHander;
import com.example.administrator.rockmusic.utils.ScreenUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

/**
 *
 * 自定义Application类，负责初始化一些工具类
 */
public class MusicPlayerApplication extends Application {
    private static MusicPlayerApplication sInstance;
    private LongSparseArray<String> mDownloadList = new LongSparseArray<>();
    private static Resources sRes;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sRes = getResources();
        /*设置自己的异常处理器*/
        Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHander());
        ScreenUtils.init(this);
        initOkHttpUtils();
        initGlide();
    }

    public static MusicPlayerApplication getInstance() {
        return sInstance;
    }

    public LongSparseArray<String> getDownloadList() {
        return mDownloadList;
    }

    private void initOkHttpUtils() {
        /*连接超时时间*/
        OkHttpUtils.getInstance().setConnectTimeout(30, TimeUnit.SECONDS);
        /*读取超时时间*/
        OkHttpUtils.getInstance().setReadTimeout(30, TimeUnit.SECONDS);
        /*写入超时时间*/
        OkHttpUtils.getInstance().setWriteTimeout(30, TimeUnit.SECONDS);
    }

    private void initGlide() {
    }
      /*切换夜间模式*/
    public static void updateNightMode(boolean on) {
        /*获取屏幕参数*/
        DisplayMetrics dm = sRes.getDisplayMetrics();
        Configuration config = sRes.getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        sRes.updateConfiguration(config, dm);
    }
}
