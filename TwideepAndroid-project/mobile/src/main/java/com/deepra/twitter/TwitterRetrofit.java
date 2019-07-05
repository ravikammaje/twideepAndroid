package com.deepra.twitter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.deepra.oauth.twideepandroid.OAuthInfo;
import com.deepra.ui.MainActivity;
import com.deepra.utils.Oauth1SigningInterceptor;
import com.deepra.utils.UnSafeOKHttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import twitter4j.HttpParameter;
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

    public String getOAuthHeader(RequestMethod method, Twitter twitter, String url) {

        //RequestMethod method = RequestMethod.GET;
        HttpRequest req = new HttpRequest(method, url, null, null, null);
        return twitter.getAuthorization().getAuthorizationHeader(req);
    }

    public String getOAuthHeader(HttpRequest req, Twitter twitter) {

        return twitter.getAuthorization().getAuthorizationHeader(req);
    }

    public String getOAuthHeader(Request req, Twitter twitter) {

        try {

            Oauth1SigningInterceptor interceptor = new Oauth1SigningInterceptor(OAuthInfo.TWITTER_CONSUMER_KEY, OAuthInfo.TWITTER_CONSUMER_SECRET, OAuthInfo.sTwitter_OAUTH_TOKEN, OAuthInfo.sTwitter_OAUTH_SECRET, new Random());
            return interceptor.returnOauthSignature(req);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createTwitterApi(final Twitter twitter) {

        try {

            if (mTwitterApi == null) {

                OkHttpClient.Builder builder = MainActivity.DEBUG ? UnSafeOKHttp.getUnsafeOkHttpBuilder() : new OkHttpClient.Builder();
                OkHttpClient okHttpClient = builder.addInterceptor(getInterceptor(twitter)).build();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TwitterService.BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build();

                mTwitterApi = retrofit.create(TwitterService.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private Interceptor getInterceptor(final Twitter twitter) throws IOException {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpRequest httpRequest = getHttpRequestFromOKHttpRequest(originalRequest);
                //String oauthHeader = getOAuthHeader(httpRequest, twitter);//getOAuthHeader(getRequestMethod(originalRequest.method()), twitter, chain.request().url().toString());
                String oauthHeader = getOAuthHeader(originalRequest, twitter);//getOAuthHeader(getRequestMethod(originalRequest.method()), twitter, chain.request().url().toString());
                Request.Builder builder = originalRequest.newBuilder().header("Authorization", oauthHeader);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        };



//String consumerKey, String consumerSecret, String accessToken,
//                                     String accessSecret, Random random)

//        Oauth1SigningInterceptor interceptor = new Oauth1SigningInterceptor(OAuthInfo.TWITTER_CONSUMER_KEY, OAuthInfo.TWITTER_CONSUMER_SECRET, OAuthInfo.sTwitter_OAUTH_TOKEN, OAuthInfo.sTwitter_OAUTH_SECRET, new Random());
//        return  interceptor;
    }

    private HttpRequest getHttpRequestFromOKHttpRequest(Request originalRequest) {
        RequestMethod method = getRequestMethod(originalRequest.method());
        String url = originalRequest.url().toString();
        HttpParameter[] parameters = null;
        HashMap<String, String> header = null;
        if(originalRequest.body() instanceof FormBody) {
            FormBody formBody = (FormBody) originalRequest.body();
            parameters = new HttpParameter[formBody.size()];
            for(int i=0; i < formBody.size(); i++) {
                parameters[i] = new HttpParameter(formBody.encodedName(i), formBody.encodedValue(i));
            }
            header = new HashMap<>();
            header.put("Content-Type", "application/x-www-form-urlencoded");
        }
        HttpRequest req = new HttpRequest(method, url, parameters, null, header);
        return req;
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

    public RequestMethod getRequestMethod(String requestType) {
        if("GET".equals(requestType))
            return RequestMethod.GET;
        if("POST".equals(requestType))
            return RequestMethod.POST;
        if("DELETE".equals(requestType))
            return RequestMethod.DELETE;
        if("HEAD".equals(requestType))
            return RequestMethod.HEAD;
        if("PUT".equals(requestType))
            return RequestMethod.PUT;
        return RequestMethod.GET;
    }
}
