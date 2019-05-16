package com.deepra.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.TwitterRetrofit;
import com.deepra.twitter.data.TWDataProvider;
import com.deepra.twitter.data.TwStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TimelineFragment extends Fragment {

    private TWDataProvider mTwDataProvider;
    private OnVertTwClickListener mListener;
    private TweetRecyclerViewAdapter mAdapter;
    private View mFragmentView;


    public TimelineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        twGetHomeTimeline();

        if (mFragmentView != null)
            return mFragmentView;


        mFragmentView = inflater.inflate(R.layout.fragment_timeline_list, container, false);
        if (mFragmentView instanceof RecyclerView) {
            mAdapter = new TweetRecyclerViewAdapter(mListener, TweetRecyclerViewAdapter.FORMAT_TWEET_VERTICAL);
            ((RecyclerView) mFragmentView).setAdapter(mAdapter);
        }
        return mFragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  OnVertTwClickListener)
            mListener = (OnVertTwClickListener)context;
        if(context instanceof  TWDataProvider)
            mTwDataProvider = (TWDataProvider)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mTwDataProvider = null;
    }

    public void twGetHomeTimeline() {
        TwitterRetrofit.getInstance().getTwitterApi().getUserHomeTimeline(200, "extended").enqueue(mUserHomeTimelineCallback);
    }

    //Retrofit callbacks
    //GetUserHomeTimeLine
    private final Callback<List<TwStatus>> mUserHomeTimelineCallback = new Callback<List<TwStatus>>() {
        @Override
        public void onResponse(Call<List<TwStatus>> call, retrofit2.Response<List<TwStatus>> response) {
            if (response.isSuccessful()) {
                List<TwStatus> twStatuses = response.body();
                mTwDataProvider.getTwData().addTweets(twStatuses);
                mAdapter.setTweetList(mTwDataProvider.getTwData().getTweets());
            } else {
                Log.d("UserDetailsCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<List<TwStatus>> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
