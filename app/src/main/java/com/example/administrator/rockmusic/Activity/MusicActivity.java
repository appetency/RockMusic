package com.example.administrator.rockmusic.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.rockmusic.Activity.Fragment.LocalMusicFragment;
import com.example.administrator.rockmusic.Activity.Fragment.PlayFragment;
import com.example.administrator.rockmusic.Activity.Fragment.SongListFragment;
import com.example.administrator.rockmusic.Bean.MusicInfoBean;
import com.example.administrator.rockmusic.MVP.IPlayBar;
import com.example.administrator.rockmusic.MyView.PlayerBar;
import com.example.administrator.rockmusic.R;
import com.example.administrator.rockmusic.Service.MusicPlayService;
import com.example.administrator.rockmusic.adapter.FragmentAdapter;
import com.example.administrator.rockmusic.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Xiamin on 2016/9/15.
 */
public class MusicActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener
        , IPlayBar, PlayerBar.ShowPlayingFragmentListener
        , NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.iv_menu)
    /*菜单按钮*/
    ImageView mvMenu;
    @Bind(R.id.iv_search)
    /*搜索按钮*/
    ImageView mIvSearch;
    @Bind(R.id.tv_local_music)
    /*我的音乐*/
    TextView mTvLocalMusic;
    @Bind(R.id.tv_online_music)
    /*在线音乐*/
    TextView mTvOnlineMusic;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    PlayerBar mPlayBar;

    private View vNavigationHeader;
    private LocalMusicFragment mLocalMusicFragment;
    private SongListFragment mSongListFragment;
    private PlayFragment mPlayFragment;
    private static final String TAG = "MusicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        /*初始界面*/
        initView();
        /*绑定服务*/
        bindService();
    }

    /**
     * 初始化view
     * 主界面的初始化，侧边栏的设置
     */
    private void initView() {
        /*绑定底部音乐布局*/
        mPlayBar = (PlayerBar) findViewById(R.id.fl_play_bar);

        mPlayBar.setShowPlayingFragmentListener(this);

        mViewPager.addOnPageChangeListener(this);
        mDrawerLayout.setOnClickListener(this);
        /*搜索设置点击监听*/
        mIvSearch.setOnClickListener(this);
        /*本地音乐设置点击监听*/
        mTvLocalMusic.setOnClickListener(this);
        /*在线音乐设置点击监听*/
        mTvOnlineMusic.setOnClickListener(this);
        /*菜单设置点击监听*/
        mvMenu.setOnClickListener(this);
        /*导航设置选择监听*/
        mNavigationView.setNavigationItemSelectedListener(this);
        ImageView imageView = new ImageView(this);
        /*imageView设置布局参数*/
        imageView.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT
                , ScreenUtils.dp2px(200f)));
        /*设置图片源*/
        imageView.setImageResource(R.drawable.jay);
        /*给导航视图头部添加图片*/
        mNavigationView.addHeaderView(imageView);
        /*本地音乐fragment*/
        mLocalMusicFragment = new LocalMusicFragment();
        /*在线音乐大纲界面*/
        mSongListFragment = new SongListFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(mLocalMusicFragment);
        adapter.addFragment(mSongListFragment);
        mViewPager.setAdapter(adapter);
    }

    private MusicPlayService servicebinder;
    private ServiceConnection connet = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("TAG", "onServiceConnected");
            servicebinder = ((MusicPlayService.Mybinder) iBinder).getservice();
            //    servicebinder.initPlayer();
        }

        //当启动源和service连接意外丢失时会调用
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("TAG", "onServiceDisconnected");
        }
    };

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        bindService(intent, connet, Context.BIND_AUTO_CREATE);
    }

    public MusicPlayService getMusicService() {
        return servicebinder;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu: {
                Log.i("TAG", "点击侧滑按钮");
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            }
            case R.id.iv_search: {
                Log.i("TAG", "点击搜索按钮");
                break;
            }
            case R.id.tv_local_music: {
                Log.i("TAG", "点击本地音乐");
                mViewPager.setCurrentItem(0);
                break;
            }
            case R.id.tv_online_music: {
                Log.i("TAG", "点击在线音乐");
                mViewPager.setCurrentItem(1);
                break;
            }
        }
    }

    @Override
    /*设置滑动页面选择*/
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mTvLocalMusic.setSelected(true);
            mTvOnlineMusic.setSelected(false);
        } else if (position == 1) {
            mTvLocalMusic.setSelected(false);
            mTvOnlineMusic.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connet);
    }

    @Override
    public void setPlayBar(MusicInfoBean musicInfoBean) {
        mPlayBar.setInfo(musicInfoBean);
    }

    @Override
    public void onBackPressed() {
        if (mIsPlayingFragment == true && mPlayFragment != null) {
            hidePlayingFragment();
            return;
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        moveTaskToBack(false);
        //    super.onBackPressed();
    }


    private boolean mIsPlayingFragment;

    @Override
    public void ShowPlayingFragment(MusicInfoBean mMusicInfoBean) {
        //每次点击都得刷新fragment，而hide和show不走生命周期
        mPlayFragment = new PlayFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_up, 0)
                .replace(android.R.id.content, mPlayFragment)
                .show(mPlayFragment)
                .commit();
        mIsPlayingFragment = true;
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commit();
        mIsPlayingFragment = false;
    }

    /**
     * 侧边栏被点击的选项
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        mDrawerLayout.closeDrawers();
        /*对于NavigationItem 当被按下后会呈现暗色，我们需要手动将其置为可按的状态*/
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                item.setChecked(false);
            }
        }, 500);
        switch (item.getItemId()) {
            case R.id.action_setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_night:
                Toast.makeText(this, "action_night", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_timer:
                Toast.makeText(this, "action_timer", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                getMusicService().stopPlayer();
                for (Activity k : MusicPlayService.getActivityStack()) {
                    k.finish();
                }
                return true;
            case R.id.action_about:
                return true;
        }
        return false;
    }

    /**
     * fragment触摸事件分发
     * 由于fragment没有触摸事件，而我的播放页需要监听手势，因此需要监听触摸
     */
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if(listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener) ;
    }
    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }
}
