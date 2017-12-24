package com.tanshul.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanshul.player.R;
import com.tanshul.player.adapter.HomeCardAdapter;
import com.tanshul.player.model.SearchResponse;
import com.tanshul.player.utils.Constants;
import com.tanshul.player.utils.PixelUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Home screen card-view fragment
 */

public class MediaCardFragment extends BaseFragment {
    private HomeCardAdapter mAdapter;
    private LinearLayoutManager llManager;
    private ArrayList<SearchResponse> mSongs = new ArrayList<>();
    private int mIndex = 0, mInitOffset, mStartPosition, mStopPosition;

    //Declare all views here
    @BindView(R.id.rv_item_list)
    RecyclerView rvItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_card, container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llManager = new LinearLayoutManager(mContext);
        mAdapter = new HomeCardAdapter(mContext, mSongs);
        rvItemList.setLayoutManager(llManager);
        rvItemList.setAdapter(mAdapter);
        int screen = PixelUtil.getDisplayInfo().y;
        mStartPosition = (int) (Constants.VIEW_START * screen);
        mStopPosition = (int) (Constants.VIEW_FINAL * screen);
        mInitOffset = (int) (PixelUtil.getDisplayInfo().x * Constants.ASPECT_RATIO);
        rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    startPlayback();
                }
            }
        });
        mAdapter.startMediaPlayer(mIndex);
    }

    private void startPlayback() {
        int item = 0;
        int item1 = llManager.findFirstVisibleItemPosition();
        int item2 = llManager.findLastVisibleItemPosition();
        if (item2 - item1 > 1) {
            item = item1 + 1;
        } else {
            View view1 = llManager.getChildAt(item1);
            View view2 = llManager.getChildAt(item2);
            if (view2 == null) {
                item = item1;
            } else if (view1 == null) {
                item = item2;
            } else {
                int offset1 = PixelUtil.getItemYPosition(view1) + mInitOffset;
                int offset2 = PixelUtil.getItemYPosition(view2);
                if (offset1 > mStartPosition && offset1 < mStopPosition) {
                    item = item1;
                } else if (offset2 > mStartPosition && offset2 < mStopPosition) {
                    item = item2;
                }
            }
        }
        if (item == mIndex) {
            //Do nothing
        } else {
            mIndex = item;
            mAdapter.startMediaPlayer(mIndex);
        }
    }

    public void setSongs(ArrayList<SearchResponse> list) {
        this.mSongs = list;
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.pauseSong();
    }
}
