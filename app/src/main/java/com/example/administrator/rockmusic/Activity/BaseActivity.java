package com.example.administrator.rockmusic.Activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.administrator.rockmusic.R;
import com.example.administrator.rockmusic.Service.MusicPlayService;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *
 * 基类activity，主要负责检查toolbar是否存在，以及将activity保存的作用
 */
public class BaseActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    protected Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        /*设置透明状态栏*/
        setSystemBarTransparent();
        MusicPlayService.addToStack(this);
        /*设置音量键控制音量大小*/
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initView();
    }

    @Override
    protected void onDestroy() {
        MusicPlayService.removeFromStack(this);
        super.onDestroy();
    }
    /*初始化视图*/
    private void initView() {
        /*绑定activity*/
        ButterKnife.bind(this);
        /**
         * 此处用来检查xml文件是否带了toolbar
         */
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        /*设置自定义状态栏*/
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            Log.i("iii","getSupportActionBar is not null");
           /*给左上角图标的左边加上一个返回的图标*/
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setSystemBarTransparent() {
        /*透明状态栏*/
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void showSoftKeyboard(final EditText editText) {
        /*控制键盘获得这个按钮的焦点*/
        editText.setFocusable(true);
        /*触摸获得焦点*/
        editText.setFocusableInTouchMode(true);
        /*设置是否获得焦点若有requestFocus()被调用时，后者优先处理。*/
        editText.requestFocus();
        /*延迟多少毫秒后开始运行*/
        mHandler.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 200L);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
