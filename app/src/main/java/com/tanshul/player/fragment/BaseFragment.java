package com.tanshul.player.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tanshul.player.activity.HomeActivity;

import butterknife.Unbinder;

/**
 * Created by tansdeva on 20/12/17.
 */

public class BaseFragment extends Fragment {
    protected View mView;
    protected Context mContext;
    protected Unbinder unbinder;
    protected static HomeActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mActivity = (HomeActivity) mContext;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
