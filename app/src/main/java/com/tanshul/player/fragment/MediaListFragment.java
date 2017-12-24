package com.tanshul.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tanshul.player.R;
import com.tanshul.player.adapter.HomeListAdapter;
import com.tanshul.player.listener.WebRequestListener;
import com.tanshul.player.model.SearchResponse;
import com.tanshul.player.utils.Constants;
import com.tanshul.player.utils.Utility;
import com.tanshul.player.view.EmptyRecyclerView;
import com.tanshul.player.web.WebService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tansdeva on 20/12/17.
 * Home screen card-view fragment
 */

public class MediaListFragment extends BaseFragment {
    private int mCurrentPage = 1;
    private HomeListAdapter mAdapter;
    private LinearLayoutManager llManager;
    public ArrayList<SearchResponse> mList, mSongs;
    private boolean isFirst = true, isLoading = true, isPaging = true;

    //Declare all views here
    @BindView(R.id.srl_refresh_list)
    SwipeRefreshLayout srlRefreshList;
    @BindView(R.id.rv_item_list)
    EmptyRecyclerView rvItemList;
    @BindView(R.id.rl_empty_view)
    RelativeLayout rlEmptyView;

    public ArrayList<SearchResponse> getSongs() {
        return mList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_list, container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        mSongs = new ArrayList<>();
        llManager = new LinearLayoutManager(mContext);
        mAdapter = new HomeListAdapter(mContext, mView, mList);
        rvItemList.setLayoutManager(llManager);
        rvItemList.setEmptyView(rlEmptyView);
        rvItemList.setAdapter(mAdapter);
        rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItems = recyclerView.getChildCount();
                int totalItems = llManager.getItemCount();
                if (dy > 0) {
                    if (!isLoading && isPaging) {
                        int firstItem = llManager.findFirstVisibleItemPosition();
                        if (totalItems - visibleItems <= firstItem + Constants.PAGEING_THRESHOLD) {
                            mCurrentPage++;
                            getSongsList();
                        }
                    }
                }
            }
        });
        getSongsList();
    }

    private void getSongsList() {
        isLoading = true;
        if (mCurrentPage == 1) {
            if (isFirst) {
                rvItemList.showLoadView();
            } else {
                rvItemList.showBlankView();
            }
        } else {
            rvItemList.showLoadMore(true);
        }
        new WebService(mContext).getSearchList(mCurrentPage, new WebRequestListener() {

            @Override
            public void onSuccess(String response) {
                hideLoading();
                SearchResponse[] list = new Gson().fromJson(response, SearchResponse[].class);
                for (SearchResponse item : list) {
                    mList.add(item);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                hideLoading();
                Utility.showMessage(error, mView);
            }
        });
    }

    private void hideLoading() {
        try {
            isLoading = false;
            if (isFirst) {
                isFirst = false;
                rvItemList.hideLoadView();
            } else {
                rvItemList.showLoadMore(false);
                srlRefreshList.setRefreshing(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSearch(String keyword) {
        mSongs.clear();
        mSongs.addAll(mList);
        ArrayList<SearchResponse> items = new ArrayList<>();
        for (SearchResponse item : mList) {
            if (item.getSong().toLowerCase().contains(keyword)) {
                items.add(item);
            }
        }
        mList.clear();
        mList.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    public void clearSearch() {
        if (mSongs.size() != 0) {
            mList.clear();
            mList.addAll(mSongs);
            mAdapter.notifyDataSetChanged();
            mSongs.clear();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.pauseSong();
    }
}
