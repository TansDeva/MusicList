package com.tanshul.player.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tanshul.player.R;
import com.tanshul.player.model.SearchResponse;
import com.tanshul.player.utils.Constants;
import com.tanshul.player.utils.PixelUtil;
import com.tanshul.player.utils.Utility;
import com.tanshul.player.view.HomeImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Home card-view adapter to auto-play highlighted element
 */

public class HomeCardAdapter extends BaseAdapter {
    private int mWidth, mHeight;

    public HomeCardAdapter(Context context, ArrayList<SearchResponse> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
        mWidth = PixelUtil.getDisplayInfo().x;
        mHeight = (int) (mWidth * Constants.ASPECT_RATIO);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void pauseSong() {
        if (mIndex == -1) {
            return;
        }
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mItemList.get(mIndex).setPlaying(Constants.PAUSED);
        notifyDataSetChanged();
    }

    @Override
    public HomeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_search_card, parent, false);
        return new HomeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final HomeCardViewHolder viewHolder = (HomeCardViewHolder) holder;
        final SearchResponse item = mItemList.get(position);
        String imageUrl = Utility.getImageUrl(mWidth, mHeight, item.getCoverImage());
        Picasso.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(viewHolder.ivSongImage);
        viewHolder.tvSongName.setText(item.getSong());
        viewHolder.tvSongArtist.setText(item.getArtists());
        setMediaIcon(item.getPlaying(), viewHolder);
        viewHolder.ivSongPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mMediaPlayer.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                mItemList.get(position).setPlaying(Constants.PLAYING);
                notifyDataSetChanged();
            }
        });
        viewHolder.ivSongPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mTimer.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    mMediaPlayer.pause();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                mItemList.get(position).setPlaying(Constants.PAUSED);
                notifyDataSetChanged();
            }
        });
    }

    private void setMediaIcon(int value, HomeCardViewHolder holder) {
        ImageView imageView = holder.ivSongPlay;
        holder.sbSongTime.setVisibility(View.GONE);
        holder.rlSongOptions.setVisibility(View.GONE);
        holder.ivSongPlay.setVisibility(View.GONE);
        holder.ivSongPause.setVisibility(View.GONE);
        holder.pbLoadSong.setVisibility(View.GONE);
        switch (value) {
            case Constants.BUFFERING:
                holder.pbLoadSong.setVisibility(View.VISIBLE);
                holder.rlSongOptions.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_song_buffer);
                break;
            case Constants.NOT_PLAYING:
                imageView.setImageResource(R.drawable.ic_media_play);
                break;
            case Constants.PLAYING:
                holder.ivSongPause.setVisibility(View.VISIBLE);
                holder.sbSongTime.setVisibility(View.VISIBLE);
                holder.rlSongOptions.setVisibility(View.VISIBLE);
                holder.sbSongTime.setProgress(mItemList.get(mIndex).getProgress());
                imageView.setImageResource(R.drawable.ic_media_pause);
                break;
            case Constants.PAUSED:
                holder.ivSongPlay.setVisibility(View.VISIBLE);
                holder.sbSongTime.setVisibility(View.VISIBLE);
                holder.rlSongOptions.setVisibility(View.VISIBLE);
                holder.sbSongTime.setProgress(mItemList.get(mIndex).getProgress());
                imageView.setImageResource(R.drawable.ic_media_play);
                break;
            case Constants.FINISHED:
                imageView.setImageResource(R.drawable.ic_media_play);
                break;
        }
    }

    public void startMediaPlayer(final int position) {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mIndex = position;
        for (SearchResponse item : mItemList) {
            item.setPlaying(Constants.NOT_PLAYING);
        }
        String url = mItemList.get(position).getUrl();
        mItemList.get(position).setPlaying(Constants.BUFFERING);
        notifyDataSetChanged();
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPlaying = true;
                    mItemList.get(position).setPlaying(Constants.PLAYING);
                    notifyDataSetChanged();
                    startTimer();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    isPlaying = false;
                    try {
                        mTimer.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mItemList.get(position).setPlaying(Constants.FINISHED);
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        int total = mMediaPlayer.getDuration();
        int left = total - mMediaPlayer.getCurrentPosition();
        mTimer = new CountDownTimer(left, total / 100) {
            @SuppressLint("NewApi")
            @Override
            public void onTick(long l) {
                int current = mMediaPlayer.getCurrentPosition();
                int progress = current * 100 / mMediaPlayer.getDuration();
                mItemList.get(mIndex).setProgress(progress);
                notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                //Do nothing
            }
        };
        mTimer.start();
    }

    public class HomeCardViewHolder extends BaseHolder {
        @BindView(R.id.iv_song_image)
        HomeImageView ivSongImage;
        @BindView(R.id.tv_song_name)
        TextView tvSongName;
        @BindView(R.id.tv_song_artist)
        TextView tvSongArtist;
        @BindView(R.id.pb_load_song)
        ProgressBar pbLoadSong;
        @BindView(R.id.iv_song_play)
        ImageView ivSongPlay;
        @BindView(R.id.iv_song_pause)
        ImageView ivSongPause;
        @BindView(R.id.rl_song_actions)
        RelativeLayout rlSongActions;
        @BindView(R.id.sb_song_time)
        SeekBar sbSongTime;
        @BindView(R.id.rl_song_options)
        RelativeLayout rlSongOptions;

        public HomeCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
