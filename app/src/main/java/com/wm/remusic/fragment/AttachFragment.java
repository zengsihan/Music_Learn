package com.wm.remusic.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by wm on 2016/3/17.
 * //把绑定的方法单独提了出来 zsh
 */
public class AttachFragment extends Fragment {

    public Activity mContext;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.mContext = activity;
    }


}
