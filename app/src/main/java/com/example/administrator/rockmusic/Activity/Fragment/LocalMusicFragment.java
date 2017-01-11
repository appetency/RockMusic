package com.example.administrator.rockmusic.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.rockmusic.Activity.MusicActivity;
import com.example.administrator.rockmusic.R;
import com.example.administrator.rockmusic.Service.MusicPlayService;
import com.example.administrator.rockmusic.adapter.LocalMusicAdapter;

import butterknife.Bind;

/**
 * Created by Xiamin on 2016/9/15.
 * 本地音乐fragment，位于主界面的viewpager中
 */
public class LocalMusicFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    @Bind(R.id.lv_local_music)
    /*绑定列表视图*/
    ListView mListLocalMusic;
    @Bind(R.id.tv_empty)
    /*显示暂无本地音乐*/
    TextView mTvEmpty;

    private LocalMusicAdapter adapter;

    @Override
    public void initView() {
        adapter = new LocalMusicAdapter();
        /*设置适配器*/
        mListLocalMusic.setAdapter(adapter);
        /*设置列表监听*/
        mListLocalMusic.setOnItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*填充视图*/
        return inflater.inflate(R.layout.fragment_local_music,container,false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /**
         * 让service播放该音乐
         * 改变activity上的playbar信息
         */
        getPlayService().play(i);
        ((MusicActivity)getActivity()).setPlayBar(MusicPlayService.getMusicList().get(i));
    }
}
