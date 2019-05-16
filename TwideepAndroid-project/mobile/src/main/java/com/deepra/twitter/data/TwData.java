package com.deepra.twitter.data;

import java.util.List;

public class TwData {


    private TweetList mTweets;

    public TweetList getSentTweets() {
        return mSentTweets;
    }

    private TweetList mSentTweets;

    public TwData() {
        mTweets = new TweetList();
        mSentTweets = new TweetList();
    }

    public TweetList getTweets() {
        return mTweets;
    }

    public void setTweets(TweetList tweetList) {
        this.mTweets = tweetList;
    }

    public void addTweets(List<TwStatus> twStatuses) {
        mTweets.getTweetList().addAll(twStatuses);
    }

    public void addSentTweets(List<TwStatus> twStatuses) {
        mSentTweets.getTweetList().addAll(twStatuses);
    }
}
