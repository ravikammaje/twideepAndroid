package com.deepra.twitter;

import okhttp3.Credentials;

public class TwitterReadAPI {

    private OAuthToken mToken;
    private Credentials mCredentials;
    private TwitterService mTwitterService;

    public TwitterReadAPI(OAuthToken token, Credentials credentials, TwitterService twitterService) {
        mToken = token;
        mCredentials = credentials;
        mTwitterService = twitterService;
    }

    public void getTimeline() {

    }

    public OAuthToken getToken() {
        return mToken;
    }

    public TwitterReadAPI setToken(OAuthToken token) {
        mToken = token;
        return this;
    }

    public Credentials getCredentials() {
        return mCredentials;
    }

    public TwitterReadAPI setCredentials(Credentials credentials) {
        mCredentials = credentials;
        return this;
    }

    public TwitterService getTwitterService() {
        return mTwitterService;
    }

    public TwitterReadAPI setTwitterService(TwitterService twitterService) {
        mTwitterService = twitterService;
        return this;
    }
}
