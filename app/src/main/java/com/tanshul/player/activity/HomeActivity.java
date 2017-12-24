package com.tanshul.player.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanshul.player.R;
import com.tanshul.player.fragment.MediaCardFragment;
import com.tanshul.player.fragment.MediaListFragment;
import com.tanshul.player.utils.PixelUtil;
import com.tanshul.player.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Splash activity (initial screen)
 */

public class HomeActivity extends BaseActivity {
    private MediaListFragment mListFragment;
    private MediaCardFragment mCardFragment;
    private boolean isSearchView = false, isCardView = false;
    private static final long ANIMATION_DURATION = 500;

    //Declare all views here
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.iv_menu_toggle)
    ImageView ivMenuToggle;
    @BindView(R.id.iv_menu_search)
    ImageView ivMenuSearch;
    @BindView(R.id.rl_search_view)
    RelativeLayout rlSearchView;
    @BindView(R.id.et_search_text)
    EditText etSearchText;
    @BindView(R.id.iv_menu_cancel)
    ImageView ivMenuCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupElements();
        setListeners();
    }

    private void setupElements() {
        mContainerId = R.id.fragment_container;
        mListFragment = new MediaListFragment();
        mCardFragment = new MediaCardFragment();
        addFragment(mListFragment);
    }

    private void setListeners() {
        ivMenuSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    int valX = PixelUtil.getDisplayInfo().x / 2;
                    setupSearchView(true, valX, 0);
                    return true;
                }
                return false;
            }
        });
        ivMenuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCardView = !isCardView;
                if (isCardView) {
                    replaceFragment(mCardFragment);
                    mCardFragment.setSongs(mListFragment.getSongs());
                    ivMenuToggle.setImageResource(R.drawable.ic_list_normal);
                } else {
                    replaceFragment(mListFragment);
                    ivMenuToggle.setImageResource(R.drawable.ic_list_card);
                }
                ivMenuSearch.setVisibility(isCardView ? View.GONE : View.VISIBLE);
            }
        });
        ivMenuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListFragment.clearSearch();
                Utility.hideKeyboard(etSearchText, true);
            }
        });
        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Utility.hideKeyboard(etSearchText, false);
                    String text = etSearchText.getText().toString().trim().toLowerCase();
                    if (!Utility.validateString(text)) {
                        Utility.showMessage(R.string.msg_search_empty, rootView);
                    } else {
                        mListFragment.setSearch(text);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void showSearchView(boolean show) {
        int visible = show ? View.VISIBLE : View.GONE;
        rlSearchView.setVisibility(visible);
    }

    private void setKeyboard(boolean show) {
        if (show) {
            Utility.showKeyboard(etSearchText);
        } else {
            Utility.hideKeyboard(etSearchText, true);
        }
    }

    @SuppressLint("NewApi")
    private void setupSearchView(final boolean show, int xPoint, int yPoint) {
        isSearchView = show;
        int startPoint, endPoint;
        int radius = PixelUtil.getDisplayInfo().x;
        if (show) {
            startPoint = 0;
            endPoint = radius;
            showSearchView(true);
        } else {
            xPoint = PixelUtil.getDisplayInfo().x;
            startPoint = radius;
            endPoint = 0;
            if (Utility.isLollipop()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showSearchView(false);
                    }
                }, ANIMATION_DURATION);
            } else {
                showSearchView(true);
            }
        }
        if (Utility.isLollipop()) {
            Animator animator = ViewAnimationUtils.createCircularReveal(rlSearchView,
                    xPoint, yPoint, startPoint, endPoint);
            animator.setDuration(ANIMATION_DURATION);
            animator.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setKeyboard(show);
                }
            }, ANIMATION_DURATION);
        } else {
            setKeyboard(show);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchView) {
            mListFragment.clearSearch();
            setupSearchView(false, 0, 0);
        } else if (isCardView) {
            ivMenuToggle.performClick();
        } else {
            super.onBackPressed();
        }
    }
}
