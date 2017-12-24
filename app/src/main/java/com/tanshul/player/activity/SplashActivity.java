package com.tanshul.player.activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanshul.player.R;
import com.tanshul.player.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Splash activity (initial screen)
 */

public class SplashActivity extends BaseActivity {
    private int mDelay, mDuration;
    private ObjectAnimator mRotateAnimation;

    @BindView(R.id.tv_player_text)
    TextView tvOlaPlayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        startAnimation();
    }

    private void startAnimation() {
        tvOlaPlayText.setVisibility(View.VISIBLE);
        mDelay = Utility.getInt(R.integer.anim_fast);
        mDuration = Utility.getInt(R.integer.anim_slow);
        mRotateAnimation = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.view_flip);
        mRotateAnimation.setTarget(tvOlaPlayText);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRotateAnimation.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, HomeActivity.class));
                    }
                }, mDuration);
            }
        }, mDelay);
    }
}
