package com.deepra.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.TwitterRetrofit;
import com.deepra.twitter.data.TWDataProvider;
import com.deepra.twitter.data.TwStatus;
import com.deepra.twitter.data.TweetList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ComposeFragment extends Fragment {

//    private OnHorzClickListener mOnHorzClickListener;
    private TweetRecyclerViewAdapter mAdapter;
    private TWDataProvider mTwDataProvider;
    private WebView mWebView;
    private OnVertTwClickListener mLeftVertTweetListClickListener;

    public ComposeFragment() {
    }

    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        twGetSentTimeline();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_message, container, false);
        RecyclerView twRecyclerView = (RecyclerView)v.findViewById(R.id.sent_list);
        mAdapter = new TweetRecyclerViewAdapter(mLeftVertTweetListClickListener, TweetRecyclerViewAdapter.FORMAT_TWEET_VERTICAL);
        twRecyclerView.setAdapter(mAdapter);
        TweetList twStatuses = mTwDataProvider.getTwData().getSentTweets();
        mAdapter.setTweetList(twStatuses);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof  TWDataProvider)
            mTwDataProvider = (TWDataProvider)context;

        mLeftVertTweetListClickListener = createLeftVertTweetListClickListener();
    }

    private OnVertTwClickListener createLeftVertTweetListClickListener() {
        OnVertTwClickListener onHorzClickListener = new OnVertTwClickListener() {
            @Override
            public void onVertTwClick(TweetRecyclerViewAdapter mAdapter, int i) {
                TwStatus status = mAdapter.getTwSortedList().get(i);
//                int indexLinkStart = 0;
//                String statusStr = status.getText();
//                if(status.getRetweeted_status() != null)
//                    statusStr = "RT "+status.getRetweeted_status().getText();
//                indexLinkStart = statusStr.indexOf("http://");
//                if(indexLinkStart == -1)
//                    indexLinkStart = statusStr.indexOf("https://");
//                if(indexLinkStart != -1) {
//                    String link = statusStr.substring(indexLinkStart);
//                    link = link.split("\\s")[0];
//
//                    final ProgressDialog progDailog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
//                    mWebView.getSettings().setJavaScriptEnabled(true);
//                    mWebView.getSettings().setLoadWithOverviewMode(true);
//                    mWebView.getSettings().setUseWideViewPort(true);
//                    mWebView.setWebViewClient(new WebViewClient(){
//
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            progDailog.show();
//                            view.loadUrl(url);
//
//                            return true;
//                        }
//                        @Override
//                        public void onPageFinished(WebView view, final String url) {
//                            progDailog.dismiss();
//                        }
//                    });
//
//                    mWebView.loadUrl(link);
//                }
            }
        };
        return onHorzClickListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTwDataProvider = null;
    }


    public void twGetSentTimeline() {
        TwitterRetrofit.getInstance().getTwitterApi().getUserSentTimeline("extended").enqueue(mUserSentTimelineCallback);
    }

    //Retrofit callbacks
    //GetUserHomeTimeLine
    private final Callback<List<TwStatus>> mUserSentTimelineCallback = new Callback<List<TwStatus>>() {
        @Override
        public void onResponse(Call<List<TwStatus>> call, retrofit2.Response<List<TwStatus>> response) {
            if (response.isSuccessful()) {
                List<TwStatus> twStatuses = response.body();
                mTwDataProvider.getTwData().addSentTweets(twStatuses);
                mAdapter.setTweetList(mTwDataProvider.getTwData().getSentTweets());
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
