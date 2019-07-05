package com.deepra.twideepandroid;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.deepra.twitter.TwitterRetrofit;
import com.deepra.twitter.data.TwStatus;
import com.deepra.twitter.data.db.AppDatabase;
import com.deepra.twitter.data.db.TweetToSend;
import com.deepra.twitter.data.db.TweetToSendDao;
import com.deepra.utils.RunInAsyncTask;

import java.util.List;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class TweetSenderService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.deepra.twideepandroid.action.FOO";
    public static final String ACTION_BAZ = "com.deepra.twideepandroid.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.deepra.twideepandroid.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.deepra.twideepandroid.extra.PARAM2";
    private static final long DELAY = 60*60*1000; // one hour


    public TweetSenderService() {
        super("TweetSenderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            sendTweet();

        }
    }

    private void sendTweet() {

        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sendTweetNow();
                sendTweet();
            }
        }, DELAY);
    }

    private void sendTweetNow() {
        final Runnable reinsertTweet = new Runnable() {
            @Override
            public void run() {
                final AppDatabase db = AppDatabase.getInstance(TweetSenderService.this);
                final TweetToSendDao tweetToSendDao = db.tweetToSendDao();
                List<TweetToSend> tweetsToSend = tweetToSendDao.getAll();
                if (0 < tweetsToSend.size()) {
                    final TweetToSend tweetToSend = tweetsToSend.get(0);
                    TwitterRetrofit.getInstance().getTwitterApi().postTweet(tweetToSend.tweet, tweetToSend.mediaIds).enqueue(mPostTweetCallback);
                    tweetToSendDao.delete(tweetToSend);
                    tweetToSend.dbId = 0;
                    tweetToSendDao.insert(tweetToSend);
                }
            }
        };
        RunInAsyncTask runInAsyncTask = (new RunInAsyncTask(reinsertTweet, this));
        runInAsyncTask.execute();
    }

    private final Callback<TwStatus> mPostTweetCallback = new Callback<TwStatus>() {
        @Override
        public void onResponse(Call<TwStatus> call, retrofit2.Response<TwStatus> response) {
            if (response.isSuccessful()) {
                Log.d("TweetSenderService","Success!! Code: " + response.code() + "Message: " + response.message());
            }
            else
                Log.d("TweetSenderService","Fail!! Code: " + response.code() + "Message: " + response.message());

        }

        @Override
        public void onFailure(Call<TwStatus> call, Throwable t) {
            t.printStackTrace();
        }
    };

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
