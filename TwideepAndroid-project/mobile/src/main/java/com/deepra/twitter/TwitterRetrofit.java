package com.deepra.twitter;

import android.util.Log;

import com.deepra.twitter.data.TwStatus;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import twitter4j.HttpRequest;
import twitter4j.RequestMethod;
import twitter4j.Twitter;

public class TwitterRetrofit {

    private static TwitterRetrofit sTwitterRetrofit;

    public TwitterService getTwitterApi() {
        return mTwitterApi;
    }

    private TwitterService mTwitterApi;

    public static TwitterRetrofit getInstance() {
        if(sTwitterRetrofit == null)
            sTwitterRetrofit = new TwitterRetrofit();
        return sTwitterRetrofit;
    }

    private TwitterRetrofit() {

    }

    public String getOAuthHeader(Twitter twitter, String url) {

        RequestMethod method = RequestMethod.GET;
        HttpRequest req = new HttpRequest(method, url, null, null, null);
        return twitter.getAuthorization().getAuthorizationHeader(req);
    }

    public void createTwitterApi(final Twitter twitter) {

        if(mTwitterApi == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();

                    String oauthHeader = getOAuthHeader(twitter, chain.request().url().toString());
                    Request.Builder builder = originalRequest.newBuilder().header("Authorization", oauthHeader);

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            }).build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TwitterService.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            mTwitterApi = retrofit.create(TwitterService.class);
        }
    }



    public void twGetUserDetails(String name) {
        mTwitterApi.getUserDetails(name).enqueue(mUserDetailsCallback);
    }




    //GetUserDetails
    private final Callback<UserDetails> mUserDetailsCallback = new Callback<UserDetails>() {
        @Override
        public void onResponse(Call<UserDetails> call, retrofit2.Response<UserDetails> response) {
            if (response.isSuccessful()) {
                UserDetails userDetails = response.body();
            } else {
                Log.e("UserDetailsCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<UserDetails> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
