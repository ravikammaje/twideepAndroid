package com.deepra.twitter.data;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;

import java.util.Comparator;
import java.util.TreeSet;

public class TwSortedList {

    private RecyclerView.Adapter mAdapter = null;
    private SortedList<TwStatus> mTwStatuses;
    private Comparator<TwStatus> mTweetComparator;

    public TwSortedList(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mTwStatuses = new SortedList<>(TwStatus.class, new SortedListAdapterCallback<TwStatus>(mAdapter) {
            @Override
            public int compare(TwStatus twStatus, TwStatus t21) {
                return Long.compare(t21.getId(), twStatus.getId());
            }

            @Override
            public boolean areContentsTheSame(TwStatus twStatus, TwStatus t21) {
                return t21.getId() == twStatus.getId();
            }

            @Override
            public boolean areItemsTheSame(TwStatus twStatus, TwStatus t21) {
                return t21.getId() == twStatus.getId();
            }
        });
    }


    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public SortedList<TwStatus> getTwStatuses() {
        return mTwStatuses;
    }

    public Comparator<TwStatus> getTweetComparator() {
        return mTweetComparator;
    }
}
