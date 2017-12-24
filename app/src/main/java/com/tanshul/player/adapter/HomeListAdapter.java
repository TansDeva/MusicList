package com.tanshul.player.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tanshul.player.R;
import com.tanshul.player.activity.AudioActivity;
import com.tanshul.player.model.SearchResponse;
import com.tanshul.player.utils.Constants;
import com.tanshul.player.utils.Utility;
import com.tanshul.player.view.TextViewGentona;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Home list-view adapter with media play & save options
 */

public class HomeListAdapter extends BaseAdapter {
    private View mView;
    private int mImageSize;

    public HomeListAdapter(Context context, View view, ArrayList<SearchResponse> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
        this.mView = view;
        mImageSize = Utility.getDimen(R.dimen.home_list_image);
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
    public HomeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_search_list, parent, false);
        return new HomeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        final HomeListViewHolder viewHolder = (HomeListViewHolder) holder;
        final SearchResponse item = mItemList.get(position);
        String imageUrl = Utility.getImageUrl(mImageSize, item.getCoverImage());
        Picasso.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(viewHolder.ivSongImage);
        viewHolder.tvSongName.setText(item.getSong());
        viewHolder.tvSongArtist.setText(item.getArtists());
        setMediaIcon(item.getPlaying(), viewHolder);
        viewHolder.llSongPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != mIndex) {
                    mIndex = position;
                    for (SearchResponse item : mItemList) {
                        item.setPlaying(Constants.NOT_PLAYING);
                    }
                    startMediaPlayer(position);
                } else {
                    if (isPlaying) {
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
                    } else {
                        try {
                            mMediaPlayer.start();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        mItemList.get(position).setPlaying(Constants.PLAYING);
                        notifyDataSetChanged();
                        startTimer();
                    }
                    isPlaying = !isPlaying;
                }
            }
        });
        viewHolder.llSongSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isStoragePermission(mContext)) {
                    Utility.getFileDownload(item.getUrl(), item.getSong(), mView);
                }
            }
        });
        viewHolder.cvMainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AudioActivity.class);
                intent.putExtra(Constants.SONG_DATA, item);
                mContext.startActivity(intent);
            }
        });
    }

    private void setMediaIcon(int value, HomeListViewHolder holder) {
        ImageView imageView = holder.ivSongPlay;
        TextView textView = holder.tvSongPlay;
        holder.sbSongTime.setVisibility(View.GONE);
        switch (value) {
            case Constants.BUFFERING:
                imageView.setImageResource(R.drawable.ic_song_buffer);
                textView.setText(R.string.home_song_buffer);
                break;
            case Constants.NOT_PLAYING:
                imageView.setImageResource(R.drawable.ic_media_play);
                textView.setText(R.string.home_song_play);
                break;
            case Constants.PLAYING:
                holder.sbSongTime.setVisibility(View.VISIBLE);
                holder.sbSongTime.setProgress(mItemList.get(mIndex).getProgress());
                imageView.setImageResource(R.drawable.ic_media_pause);
                textView.setText(R.string.home_song_pause);
                break;
            case Constants.PAUSED:
                holder.sbSongTime.setVisibility(View.VISIBLE);
                holder.sbSongTime.setProgress(mItemList.get(mIndex).getProgress());
                imageView.setImageResource(R.drawable.ic_media_play);
                textView.setText(R.string.home_song_play);
                break;
            case Constants.FINISHED:
                imageView.setImageResource(R.drawable.ic_media_play);
                textView.setText(R.string.home_song_play);
                break;
        }
    }

    private void startMediaPlayer(final int position) {
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
        if (mTimer != null) {
            mTimer.cancel();
        }
        int total = mMediaPlayer.getDuration();
        int left = total - mMediaPlayer.getCurrentPosition();
        mTimer = new CountDownTimer(left, total / 100) {
            @SuppressLint("NewApi")
            @Override
            public void onTick(long l) {
                int current = mMediaPlayer.getCurrentPosition();
                int progress = current * 100 / mMediaPlayer.getDuration();
                mItemList.get(mIndex).setProgress(progress);
                notifyItemChanged(mIndex);
            }

            @Override
            public void onFinish() {
                //Do nothing
            }
        };
        mTimer.start();
    }

    public class HomeListViewHolder extends BaseHolder {
        @BindView(R.id.cv_main_item)
        CardView cvMainItem;
        @BindView(R.id.iv_song_image)
        ImageView ivSongImage;
        @BindView(R.id.sb_song_time)
        SeekBar sbSongTime;
        @BindView(R.id.tv_song_name)
        TextViewGentona tvSongName;
        @BindView(R.id.tv_song_artist)
        TextView tvSongArtist;
        @BindView(R.id.ll_song_play)
        LinearLayout llSongPlay;
        @BindView(R.id.iv_song_play)
        ImageView ivSongPlay;
        @BindView(R.id.tv_song_play)
        TextView tvSongPlay;
        @BindView(R.id.ll_song_save)
        LinearLayout llSongSave;

        public HomeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
