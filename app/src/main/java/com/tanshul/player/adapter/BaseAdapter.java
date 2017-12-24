package com.tanshul.player.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tanshul.player.model.SearchResponse;
import com.tanshul.player.utils.Constants;

import java.util.ArrayList;

/**
 * Created by tansdeva on 20/12/17.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseHolder> {
    protected int mIndex = -1;
    protected Context mContext;
    protected CountDownTimer mTimer;
    protected boolean isPlaying = false;
    protected ArrayList<SearchResponse> mItemList;
    protected MediaPlayer mMediaPlayer = new MediaPlayer();

    public void resetPlayer() {
        try {
            mMediaPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (SearchResponse item : mItemList) {
            item.setPlaying(Constants.NOT_PLAYING);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }
    }
}
