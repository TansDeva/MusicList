package com.tanshul.player.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tanshul.player.R;
import com.tanshul.player.utils.Utility;

/**
 * Created by tansdeva on 20/12/17.
 * Custom recycler-view to handle loaders & no-data
 */
public class EmptyRecyclerView extends RecyclerView {

    // Declare all views here
    LinearLayout llNoNet;
    LinearLayout llNoData;
    ProgressBar pbLoadData;
    ProgressBar pbLoadMore;
    RelativeLayout rlEmptyList;

    public EmptyRecyclerView(Context context) {
        super(context);
        initViews();
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        // Do nothing
    }

    public void setEmptyView(View emptyView) {
        pbLoadData = (ProgressBar) emptyView.findViewById(R.id.pb_load_data);
        pbLoadMore = (ProgressBar) emptyView.findViewById(R.id.pb_load_more);
        rlEmptyList = (RelativeLayout) emptyView.findViewById(R.id.rl_list_empty);
        llNoData = (LinearLayout) emptyView.findViewById(R.id.ll_no_data);
        llNoNet = (LinearLayout) emptyView.findViewById(R.id.ll_no_net);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    private void checkIfEmpty() {
        if (getAdapter() != null) {
            if (getAdapter().getItemCount() == 0) {
                showEmptyView(false);
            } else {
                if (rlEmptyList != null) {
                    rlEmptyList.setVisibility(GONE);
                }
                setVisibility(VISIBLE);
            }
        }
    }

    public void showBlankView() {
        setVisibility(VISIBLE);
        rlEmptyList.setVisibility(GONE);
        pbLoadMore.setVisibility(GONE);
        pbLoadData.setVisibility(GONE);
    }

    public void showLoadView() {
        setVisibility(GONE);
        rlEmptyList.setVisibility(GONE);
        pbLoadMore.setVisibility(GONE);
        pbLoadData.setVisibility(VISIBLE);
    }

    public void showLoadMore(boolean show) {
        int visibility = show ? VISIBLE : GONE;
        pbLoadMore.setVisibility(visibility);
    }

    public void hideLoadView() {
        pbLoadData.setVisibility(GONE);
        pbLoadMore.setVisibility(GONE);
        checkIfEmpty();
    }

    public void showEmptyView(boolean retry) {
        setVisibility(GONE);
        try {
            pbLoadData.setVisibility(GONE);
            pbLoadMore.setVisibility(GONE);
            rlEmptyList.setVisibility(VISIBLE);
            if (retry) {
                showRetryView();
            } else {
                if (!Utility.isConnected()) {
                    showRetryView();
                } else {
                    llNoNet.setVisibility(GONE);
                    llNoData.setVisibility(VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRetryView() {
        llNoData.setVisibility(GONE);
        llNoNet.setVisibility(VISIBLE);
    }
}
