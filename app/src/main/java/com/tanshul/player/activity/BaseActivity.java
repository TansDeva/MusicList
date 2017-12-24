package com.tanshul.player.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tanshul.player.fragment.BaseFragment;

/**
 * Created by tansdeva on 20/12/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected int mContainerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(mContainerId, fragment, fragment.getClass().getSimpleName());
            ft.commitAllowingStateLoss();
        }
    }

    public void replaceFragment(BaseFragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(mContainerId, fragment, fragment.getClass().getSimpleName());
            ft.commitAllowingStateLoss();
        }
    }

    public Context getContext() {
        //Check if the context is null
        if (mContext == null) {
            mContext = this;
        }
        return mContext;
    }
}
