package com.wm.remusic.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.wm.remusic.MainApplication;
import com.wm.remusic.R;
import com.wm.remusic.adapter.MainFragmentAdapter;
import com.wm.remusic.adapter.MainFragmentItem;
import com.wm.remusic.info.Playlist;
import com.wm.remusic.provider.DownFileStore;
import com.wm.remusic.provider.PlaylistInfo;
import com.wm.remusic.recent.TopTracksLoader;
import com.wm.remusic.uitl.CommonUtils;
import com.wm.remusic.uitl.IConstants;
import com.wm.remusic.uitl.MusicUtils;
import com.wm.remusic.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wm
 *         Created by wm on 2016/3/8.
 *         本地界面主界面
 */
public class MainFragment extends BaseFragment {

    private MainFragmentAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<MainFragmentItem> mList = new ArrayList<>();
    private PlaylistInfo playlistInfo; //playlist 管理类     数据库
    private SwipeRefreshLayout swipeRefresh; //下拉刷新layout

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            reloadAdapter();//重载适配器 zsh
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistInfo = PlaylistInfo.getInstance(mContext);//获取实例化对象 zsh
        //下面在判断sdk，权限之类的 zsh
        if (CommonUtils.isLollipop() && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);//绑定布局 zsh

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(mContext);//实例线性布局管理器 zsh
        recyclerView.setLayoutManager(layoutManager);//通过布局管理器来设置item的显示方式，这里是这是线性布局格式。 zsh
        //swipeRefresh.setColorSchemeResources(R.color.theme_color_PrimaryAccent);
        swipeRefresh.setColorSchemeColors(ThemeUtils.getColorById(mContext, R.color.theme_color_primary));//设置刷新时的颜色,可以设置多个 zsh

        //添加下拉刷新监听器 zsh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAdapter();

            }
        });
        //先给adapter设置空数据，异步加载好后更新数据，防止Recyclerview no attach
        mAdapter = new MainFragmentAdapter(mContext);//实例化适配器 zsh
        recyclerView.setAdapter(mAdapter);//设置适配器 zsh
        recyclerView.setHasFixedSize(true);//确保尺寸是通过用户输入从而确保RecyclerView的尺寸是一个常数,不用去计算每个item的size。 zsh  还有点模糊这个
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));//设置item间的间隔 zsh
        //设置没有item动画
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        reloadAdapter();//重载适配器 zsh

        mContext.getWindow().setBackgroundDrawableResource(R.color.background_material_light_1);//设置背景 zsh
        return view;
    }

    //
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            reloadAdapter();
        }
    }


    //为info设置数据，并放入mlistInfo
    private void setInfo(String title, int count, int id, int i) {
        MainFragmentItem information = new MainFragmentItem(); //实例化 zsh
        information.title = title;//标题 zsh
        information.count = count;//数量 zsh
        information.avatar = id;//图片id  zsh
        if (mList.size() < 4) {
            mList.add(new MainFragmentItem());//添加到列表的最后 zsh
        }
        mList.set(i, information); //将新的info对象加入到信息列表中    这是指定位置的替换，不是添加,不会改变原来list的大小。 zsh
    }

    //设置音乐overflow条目
    private void setMusicInfo() {
        //检查版本号，权限等     READ_EXTERNAL_STORAGE：可以读取设备外部存储空间的权限 zsh
        if (CommonUtils.isLollipop() && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            loadCount(false);//版本高于等于5.1并且 可以读取设备外部存储空间的权限 时走if。 zsh
        } else {
            loadCount(true);
        }
    }

    private void loadCount(boolean has) {
        int localMusicCount = 0, recentMusicCount = 0,downLoadCount = 0 ,artistsCount = 0;
        if(has){
            try{
                localMusicCount = MusicUtils.queryMusic(mContext, IConstants.START_FROM_LOCAL).size();//查询本地音乐后得到有多少首歌。 zsh
                recentMusicCount = TopTracksLoader.getCount(MainApplication.context, TopTracksLoader.QueryType.RecentSongs);//得到最近播放的歌曲 zsh
                downLoadCount = DownFileStore.getInstance(mContext).getDownLoadedListAll().size();//下载的歌 zsh
                artistsCount = MusicUtils.queryArtist(mContext).size();//获取歌手信息 zsh
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        setInfo(mContext.getResources().getString(R.string.local_music), localMusicCount, R.drawable.music_icn_local, 0);
        setInfo(mContext.getResources().getString(R.string.recent_play), recentMusicCount, R.drawable.music_icn_recent, 1);
        setInfo(mContext.getResources().getString(R.string.local_manage), downLoadCount, R.drawable.music_icn_dld, 2);
        setInfo(mContext.getResources().getString(R.string.my_artist), artistsCount, R.drawable.music_icn_artist, 3);
    }

    //刷新列表
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {//异步栈处理 zsh
            @Override
            protected Void doInBackground(final Void... unused) {//在后台处理 zsh
                ArrayList results = new ArrayList();
                setMusicInfo();
                ArrayList<Playlist> playlists = playlistInfo.getPlaylist();
                ArrayList<Playlist> netPlaylists = playlistInfo.getNetPlaylist();

                //把本地音乐、歌单这些列表全部加入到results列表里。 zsh
                results.addAll(mList);
                results.add(mContext.getResources().getString(R.string.created_playlists));
                results.addAll(playlists);
                if (netPlaylists != null) {
                    results.add("收藏的歌单");
                    results.addAll(netPlaylists);
                }

                if(mAdapter == null){
                    mAdapter = new MainFragmentAdapter(mContext);
                }
                mAdapter.updateResults(results, playlists, netPlaylists);//更新适配器 zsh
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mContext == null)
                    return;
                mAdapter.notifyDataSetChanged();//通知 任何注册观察者的数据集已经改变了。 zsh
                swipeRefresh.setRefreshing(false);//停止刷新的状态，让他不再转圈圈了。zsh
            }
        }.execute();
    }

    @Override
    public void changeTheme() {
        super.changeTheme();
        swipeRefresh.setColorSchemeColors(ThemeUtils.getColorById(mContext, R.color.theme_color_primary));
    }
}
