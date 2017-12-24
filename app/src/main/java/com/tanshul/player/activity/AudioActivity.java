package com.tanshul.player.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tanshul.player.R;
import com.tanshul.player.model.SearchResponse;
import com.tanshul.player.utils.Constants;
import com.tanshul.player.utils.PixelUtil;
import com.tanshul.player.utils.Utility;
import com.tanshul.player.view.HomeImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Audio-player screen with music playback
 */

public class AudioActivity extends BaseActivity {
    private SearchResponse mItem;
    private boolean isSeekManual = true;
    private int mCurrentTime = 0, mSeekMinimum;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private CountDownTimer timerAudio, timerBackward, timerForward;

    @BindView(R.id.tb_title_audio)
    Toolbar mToolbar;
    @BindView(R.id.iv_song_image)
    HomeImageView ivSongImage;
    @BindView(R.id.tv_time_current)
    TextView tvTimeCurrent;
    @BindView(R.id.sb_media_position)
    SeekBar sbMediaPosition;
    @BindView(R.id.tv_time_finish)
    TextView tvTimeFinish;
    @BindView(R.id.iv_backward)
    ImageView ivBackward;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.iv_forward)
    ImageView ivForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);
        setupToolbar();
        setupElements();
        setupTimers();
        setControls();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_title_back);
        mToolbar.setTitleTextColor(Utility.getColor(R.color.white));
    }

    private void setupElements() {
        int width = PixelUtil.getDisplayInfo().x;
        int height = (int) (width * Constants.ASPECT_RATIO);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mItem = getIntent().getParcelableExtra(Constants.SONG_DATA);
        getSupportActionBar().setTitle(mItem.getSong());
        String imageUrl = Utility.getImageUrl(width, height, mItem.getCoverImage());
        Picasso.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(ivSongImage);
        try {
            mMediaPlayer.setDataSource(mItem.getUrl());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mSeekMinimum = mMediaPlayer.getDuration() / 250;
                    int total = mMediaPlayer.getDuration() / 1000;
                    int minutes = total / 60;
                    int seconds = total % 60;
                    tvTimeFinish.setText(String.format("%d:%02d", minutes, seconds));
                    startAudio();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        sbMediaPosition.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void setupTimers() {
        timerBackward = new CountDownTimer(110, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentTime = mMediaPlayer.getCurrentPosition();
                if (mCurrentTime > mSeekMinimum) {
                    mCurrentTime -= mSeekMinimum;
                } else {
                    mCurrentTime = 0;
                }
                isSeekManual = false;
                int progress = mCurrentTime * 100 / mMediaPlayer.getDuration();
                sbMediaPosition.setProgress(progress);
                mMediaPlayer.seekTo(mCurrentTime);
                startAudio();
            }

            @Override
            public void onFinish() {
                timerBackward.start();
            }
        };
        timerForward = new CountDownTimer(110, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentTime = mMediaPlayer.getCurrentPosition();
                if (mCurrentTime < (mMediaPlayer.getDuration() - (1000 + mSeekMinimum))) {
                    mCurrentTime += mSeekMinimum;
                }
                isSeekManual = false;
                int progress = mCurrentTime * 100 / mMediaPlayer.getDuration();
                sbMediaPosition.setProgress(progress);
                mMediaPlayer.seekTo(mCurrentTime);
                startAudio();
            }

            @Override
            public void onFinish() {
                timerForward.start();
            }
        };
    }

    private void setControls() {
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio();
            }
        });
        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAudio();
            }
        });
        sbMediaPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSeekManual) {
                    pauseAudio();
                    mCurrentTime = mMediaPlayer.getDuration() * progress / 100;
                    startAudio();
                } else {
                    isSeekManual = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ivBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timerBackward.start();
                    pauseAudio();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    timerBackward.cancel();
                    startAudio();
                    return true;
                }
                return false;
            }
        });
        ivForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timerForward.start();
                    pauseAudio();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    timerForward.cancel();
                    startAudio();
                    return true;
                }
                return false;
            }
        });
    }

    private void startAudio() {
        ivPlay.setVisibility(View.GONE);
        ivPause.setVisibility(View.VISIBLE);
        mMediaPlayer.start();
        startTimer();
    }

    private void pauseAudio() {
        ivPause.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        mMediaPlayer.pause();
    }

    private void startTimer() {
        int total = mMediaPlayer.getDuration();
        int left = total - mMediaPlayer.getCurrentPosition();
        timerAudio = new CountDownTimer(left, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentTime = mMediaPlayer.getCurrentPosition();
                if (mCurrentTime > mSeekMinimum) {
                    mCurrentTime -= mSeekMinimum;
                } else {
                    mCurrentTime = 0;
                }
                isSeekManual = false;
                int progress = mCurrentTime * 100 / mMediaPlayer.getDuration();
                sbMediaPosition.setProgress(progress);
                int total = mCurrentTime / 1000;
                int minutes = total / 60;
                int seconds = total % 60;
                tvTimeCurrent.setText(String.format("%d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                try {
                    timerAudio.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pauseAudio();
                mMediaPlayer.seekTo(0);
                sbMediaPosition.setProgress(0);
                tvTimeCurrent.setText(R.string.audio_time_start);
            }
        };
        timerAudio.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            timerAudio.cancel();
            mMediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
