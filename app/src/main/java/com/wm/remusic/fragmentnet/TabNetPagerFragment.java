package com.wm.remusic.fragmentnet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.wm.remusic.R;
import com.wm.remusic.fragment.AttachFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 2016/4/11.
 */
public class TabNetPagerFragment extends AttachFragment implements ChangeView {
    //PreferencesUtility mPreferences;
    private ViewPager viewPager;
    private int page = 0;
    private ActionBar ab;
    private String[] title;
    private boolean isFirstLoad = true;

    public static final TabNetPagerFragment newInstance(int page, String[] title) {
        TabNetPagerFragment f = new TabNetPagerFragment();
        Bundle bdl = new Bundle(1);//实例一个bundle,用于activity之间传数据的。zsh
        bdl.putInt("page_number", page);//存信息，key-value ,zsh
        f.setArguments(bdl);//传数据的方法，系统推荐的，，最好不要在构造方法里直接传参数，在切横屏等情况下会出错。 zsh
        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //在执行了 onCreate方法后，立即执行 zsh
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setCurrentItem(page);//设置选中的页面。 zsh
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_net_tab, container, false);//绑定布局 zsh

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);//预加载页面个数，左右两边是一样的。  zsh
        }

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setTabTextColors(R.color.text_color, ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor());
        tabLayout.setSelectedTabIndicatorColor(ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor());
        tabLayout.setupWithViewPager(viewPager);


        return rootView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(recommendFragment == null){
            return;
        }
        if(isVisibleToUser && isFirstLoad){
            recommendFragment.requestData();
            isFirstLoad = false;
        }
    }
    RecommendFragment recommendFragment;

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        recommendFragment = new RecommendFragment();
        recommendFragment.setChanger(this);
        adapter.addFragment(recommendFragment, "新曲");
        adapter.addFragment(new AllPlaylistFragment(), "歌单");
        //  adapter.addFragment(new NetFragment(), "主播电台");
        adapter.addFragment(new RankingFragment(), "排行榜");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void changeTo(int page) {
        if (viewPager != null)
            viewPager.setCurrentItem(page);
    }

    //自定义的适配器 zsh
    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

