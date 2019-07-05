package com.deepra.twitter;

import com.deepra.twitter.data.TwStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitterService {

        String BASE_URL = "https://api.twitter.com/";

        @GET("/1.1/users/show.json")
        Call<UserDetails> getUserDetails(@Query("screen_name") String name);

        @GET("1.1/statuses/home_timeline.json")
        Call<List<TwStatus>> getUserHomeTimeline(@Query("count") int count,
                                                 @Query("since_id") long since_id,
                                                 @Query("max_id") long max_id,
                                                 @Query("trim_user") boolean trim_user,
                                                 @Query("exclude_replies") boolean exclude_replies,
                                                 @Query("include_entities") boolean include_entities);

        @GET("1.1/statuses/home_timeline.json")
        Call<List<TwStatus>> getUserHomeTimeline(@Query("count") int count, @Query("tweet_mode") String extended);

        @GET("1.1/statuses/home_timeline.json")
        Call<List<TwStatus>> getUserHomeTimeline();

        @GET("1.1/statuses/user_timeline.json")
        Call<List<TwStatus>> getUserSentTimeline(@Query("screen_name") String screen_name, @Query("tweet_mode") String extended);


        @GET("1.1/statuses/user_timeline.json")
        Call<List<TwStatus>> getUserSentTimeline(@Query("tweet_mode") String extended, @Query("count") int count);

        @POST("1.1/statuses/update.json")
        @FormUrlEncoded
        Call<TwStatus> postTweet(@Field("status") String status, @Field("media_ids") String media_ids);

        @POST("1.1/statuses/update.json")
        @FormUrlEncoded
        Call<TwStatus> postTweet(@Field("status") String status);

}
