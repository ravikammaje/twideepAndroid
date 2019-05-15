package com.deepra.twideepandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepra.oauth.twideepandroid.OAuthInfo;
import com.deepra.twitter.TwitterRetrofit;
import com.deepra.twitter.data.TwData;
import com.deepra.utils.AlertDialogManager;
import com.deepra.utils.ConnectionDetector;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TWDataProvider {

    static String TWITTER_CONSUMER_KEY = "nao1AudMEZKg6fnuDcG3aZRxS";
    static String TWITTER_CONSUMER_SECRET = "IRPAjITeqcCBrqeOCjaxivINuYpWfkSzrPnKBlMcgYQxNuyTO7";
    static final String TWITTER_CALLBACK_URL = "oauth://";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";

    @BindView(R.id.btnLoginTwitter) Button mBtnLoginTwitter;
    @BindView(R.id.lblUpdate) TextView mLblUpdate;
    @BindView(R.id.txtUpdateStatus) EditText mTxtUpdate;
    @BindView(R.id.btnUpdateStatus) Button mBtnUpdateStatus;
    @BindView(R.id.btnLogoutTwitter) Button mBtnLogoutTwitter;
    @BindView(R.id.lblUserName) TextView mLblUserName;
    @BindView(R.id.btnTimeLine) Button mBtnTimeLine;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    public static Configuration sConfiguration;
    private TwData mTwData;

    public MainActivityFragment() {
        mTwData = new TwData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        sConfiguration = builder.build();
        TwitterFactory factory = new TwitterFactory(sConfiguration);
        twitter = factory.getInstance();

        AlertDialogManager alert = new AlertDialogManager();
        // Shared Preferences
        mSharedPreferences = getActivity().getSharedPreferences("MyPref", 0);
        OAuthInfo.sTwitter_OAUTH_SECRET = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
        OAuthInfo.sTwitter_OAUTH_TOKEN = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");

        ConnectionDetector cd = new ConnectionDetector(getActivity());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            return;
        }

        // Check if twitter keys are set
        if (TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0) {
            alert.showAlertDialog(getActivity(), "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
            return;
        }

        /**
         * Twitter login button click event will call loginToTwitter() function
         * */
        mBtnLoginTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Call login twitter function
                loginToTwitter();
            }
        });


        if (!isTwitterLoggedInAlready()) {
            Uri uri = getActivity().getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {
                    // Get the access token
                    AccessToken accessToken1 = twitter.getOAuthAccessToken(
                            requestToken, verifier);
                    String accessToken = accessToken1.getToken();
                    String accessSecret = accessToken1.getTokenSecret();

                    // Shared Preferences
                    Editor e = mSharedPreferences.edit();

                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken);
                    e.putString(PREF_KEY_OAUTH_SECRET, accessSecret);
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.commit(); // save changes

                    Log.e("Twitter OAuth Token", "> " + accessToken);

                    // Hide login button
                    mBtnLoginTwitter.setVisibility(View.GONE);

                    OAuthInfo.sTwitter_OAUTH_TOKEN = accessToken;
                    OAuthInfo.sTwitter_OAUTH_SECRET = accessSecret;

                    navigateToTimelineFragment();

                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
        else {
            mBtnTimeLine.setVisibility(View.VISIBLE);
            mBtnTimeLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToTimelineFragment();
                }
            });
        }
    }

    private void navigateToTimelineFragment() {
        TimelineFragment timelineFragment = new TimelineFragment();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, timelineFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        AccessToken accessToken1 = new AccessToken(OAuthInfo.sTwitter_OAUTH_TOKEN, OAuthInfo.sTwitter_OAUTH_SECRET);
        twitter.setOAuthAccessToken(accessToken1);

        TwitterRetrofit.getInstance().createTwitterApi(twitter);
//        TwitterRetrofit.getInstance().twGetHomeTimeline();
        TwitterRetrofit.getInstance().twGetUserDetails("deepraWearables");
    }

    /**
     * Function to login twitter
     */
    private void loginToTwitter() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        // Check if already logged in
//        if (!isTwitterLoggedInAlready()) {
//


            try {
                requestToken = twitter
                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(requestToken.getAuthenticationURL())));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
//        } else {
//            // user already logged into twitter
//            Toast.makeText(getActivity(),
//                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
//        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    @Override
    public TwData getTwData() {
        return mTwData;
    }
}
