package com.deepra.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.TwitterRetrofit;
import com.deepra.twitter.data.Entities;
import com.deepra.twitter.data.Media;
import com.deepra.twitter.data.TWDataProvider;
import com.deepra.twitter.data.TwStatus;
import com.deepra.twitter.data.TweetList;
import com.deepra.twitter.data.db.AppDatabase;
import com.deepra.twitter.data.db.TweetToSend;
import com.deepra.twitter.data.db.TweetToSendDao;
import com.deepra.utils.RunInAsyncTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import twitter4j.Twitter;

public class ComposeFragment extends Fragment {

//    private OnHorzClickListener mOnHorzClickListener;
    private TweetRecyclerViewAdapter mAdapter;
    private TWDataProvider mTwDataProvider;
    private WebView mWebView;
    private OnVertTwClickListener mLeftVertTweetListClickListener;
    private int mLeftSelectedItem;
    private TwStatus mSelectedStatus;
    private Button mSendbutton;
    private Button mCopyLeft;
    private OnVertTwClickListener mRightVertTweetListClickListener;
    private TweetRecyclerViewAdapter mAdapterToSend;

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
        mCopyLeft = v.findViewById(R.id.copy_right);
        mSendbutton = v.findViewById(R.id.send_now);

        setListeners();

        RecyclerView twRecyclerView = (RecyclerView)v.findViewById(R.id.sent_list);
        mAdapter = new TweetRecyclerViewAdapter(mLeftVertTweetListClickListener, TweetRecyclerViewAdapter.FORMAT_TWEET_VERTICAL);
        twRecyclerView.setAdapter(mAdapter);
        TweetList twStatuses = mTwDataProvider.getTwData().getSentTweets();
        mAdapter.setTweetList(twStatuses);

        RecyclerView twQueueRecyclerView = (RecyclerView)v.findViewById(R.id.to_send_list);
        mAdapterToSend = new TweetRecyclerViewAdapter(mRightVertTweetListClickListener, TweetRecyclerViewAdapter.FORMAT_TWEET_VERTICAL);
        twQueueRecyclerView.setAdapter(mAdapterToSend);

        insertToDBInAsyncTask();
        return v;
    }

    private void insertToDBInAsyncTask() {
        final AppDatabase db = AppDatabase.getInstance(getContext());
        Runnable insert = new Runnable() {

            @Override
            public void run() {
                TweetToSendDao tweetToSendDao = db.tweetToSendDao();
                getFromDBAndUpdateSendList(tweetToSendDao);
            }
        };
        new RunInAsyncTask(insert, getActivity()).execute();
    }

    private void setListeners() {

        mSendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedStatus != null) {
                    twPostTweet(mSelectedStatus);
                }
                else
                    Toast.makeText(getContext(), "Please select a tweet and try again", Toast.LENGTH_SHORT).show();
            }
        });

        mCopyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTweetToSendQueue(mSelectedStatus);
            }
        });

    }

    private void addTweetToSendQueue(TwStatus mSelectedStatus) {
        mAdapter.unselectAll();

        final AppDatabase db = AppDatabase.getInstance(getContext());
        final TweetToSend tweet = new TweetToSend();
        tweet.tweet = getTweetAfterRemovingExtras(mSelectedStatus, mSelectedStatus.getEntities());
        tweet.mediaIds = getMediaIdsString(mSelectedStatus.getExtended_entities());
        tweet.createdAt = mSelectedStatus.getCreated_at();
        Runnable insert = new Runnable() {

            @Override
            public void run() {
                TweetToSendDao tweetToSendDao = db.tweetToSendDao();
                tweetToSendDao.insert(tweet);
                getFromDBAndUpdateSendList(tweetToSendDao);
            }
        };
        new RunInAsyncTask(insert, getActivity()).execute();

        Log.d("Twideep", "tweet inserted");
    }



    private void getFromDBAndUpdateSendList(TweetToSendDao tweetToSendDao) {
        final TweetList tweetList = new TweetList();
        List<TweetToSend> tweetsToSend = tweetToSendDao.getAll();
        for(int i =0; i < tweetsToSend.size(); i++) {

            TwStatus toSendStatus = new TwStatus();
            toSendStatus.setText(tweetsToSend.get(i).tweet);
            toSendStatus.setMediaIdString(tweetsToSend.get(i).mediaIds);
            toSendStatus.setCreated_at(tweetsToSend.get(i).createdAt);
            toSendStatus.setId(-tweetsToSend.get(i).dbId);

            tweetList.getTweetList().add(toSendStatus);
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mAdapterToSend.setTweetList(tweetList);
            }
        });
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
                mLeftSelectedItem = i;
                mSelectedStatus = status;
            }
        };
        return onHorzClickListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTwDataProvider = null;
    }


    //------------------------------------
    public void twGetSentTimeline() {
        TwitterRetrofit.getInstance().getTwitterApi().getUserSentTimeline("extended", 200).enqueue(mUserSentTimelineCallback);
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
    //------------------------------------

    public void twPostTweet(TwStatus status) {

        String media_ids_string = "";
        Entities extended_entities = status.getExtended_entities();
        Entities entities = status.getEntities();

        String tweet = getTweetAfterRemovingExtras(status, entities);

        media_ids_string = getMediaIdsString(extended_entities);


        TwitterRetrofit.getInstance().getTwitterApi().postTweet(tweet, media_ids_string).enqueue(mPostTweetCallback);
    }

    private String getMediaIdsString(Entities extended_entities) {
        String media_ids_string = "";
        if(null!=extended_entities)
            if(null!=extended_entities.getMedia()) {
                Media[] media = extended_entities.getMedia();
                int mediaCount = media.length;
                for(int i=0; i < mediaCount; i++) {
                    media_ids_string += media[i].getId_str();
                    if(i+1 < mediaCount)
                        media_ids_string += ",";
                }
            }
        return media_ids_string;
    }

    private String getTweetAfterRemovingExtras(TwStatus status, Entities entities) {
        String tweet = status.getText();

        if(null!=entities)
            if(null!=entities.getMedia()) {
                Media[] media = entities.getMedia();
                int mediaCount = media.length;
                for(int i=0; i < mediaCount; i++) {
                    String url = media[i].getUrl();
                    tweet = tweet.replace(url, "");
                }
            }
        return tweet;
    }

    private final Callback<TwStatus> mPostTweetCallback = new Callback<TwStatus>() {
        @Override
        public void onResponse(Call<TwStatus> call, retrofit2.Response<TwStatus> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), "Tweet Posted", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("UserDetailsCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<TwStatus> call, Throwable t) {
            t.printStackTrace();
        }
    };


}
