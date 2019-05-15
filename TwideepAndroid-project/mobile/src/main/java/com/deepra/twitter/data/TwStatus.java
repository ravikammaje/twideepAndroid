package com.deepra.twitter.data;

import com.deepra.twitter.UserDetails;

import java.util.Date;

public class TwStatus {


    boolean truncated;
    String created_at;
    boolean favorited;
    String id_str;
    String in_reply_to_user_id_str;
    String text;
    String contributors;
    long id;
    int retweet_count;
    String in_reply_to_status_id_str;
    boolean retweeted;
    String in_reply_to_user_id;
    //String place;
    String source;
    UserDetails user;
    String in_reply_to_screen_name;
    String in_reply_to_status_id;
    String full_text;
    TwStatus retweeted_status;

    public TwStatus getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(TwStatus retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getTweet() {
        if(retweeted_status != null) {
            return "RT " + retweeted_status.getTweet();
        }
        if(text == null)
            return full_text;
        if(full_text == null)
            return text;
        return (full_text.length() > text.length()) ? full_text : text;
    }

    public String getFull_text() {
        return getTweet();
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public TwStatus setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public TwStatus setTruncated(boolean truncated) {
        this.truncated = truncated;
        return this;
    }



    public boolean isFavorited() {
        return favorited;
    }

    public TwStatus setFavorited(boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    public String getId_str() {
        return id_str;
    }

    public TwStatus setId_str(String id_str) {
        this.id_str = id_str;
        return this;
    }

    public String getIn_reply_to_user_id_str() {
        return in_reply_to_user_id_str;
    }

    public TwStatus setIn_reply_to_user_id_str(String in_reply_to_user_id_str) {
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
        return this;
    }

    public String getText() {
        return getTweet();
    }

    public TwStatus setText(String text) {
        this.text = text;
        return this;
    }

    public String getContributors() {
        return contributors;
    }

    public TwStatus setContributors(String contributors) {
        this.contributors = contributors;
        return this;
    }

    public long getId() {
        return id;
    }

    public TwStatus setId(long id) {
        this.id = id;
        return this;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public TwStatus setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
        return this;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public TwStatus setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
        return this;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public TwStatus setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
        return this;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public TwStatus setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
        return this;
    }

//    public String getPlace() {
//        return place;
//    }
//
//    public TwStatus setPlace(String place) {
//        this.place = place;
//        return this;
//    }

    public String getSource() {
        return source;
    }

    public TwStatus setSource(String source) {
        this.source = source;
        return this;
    }

    public UserDetails getUser() {
        return user;
    }

    public TwStatus setUser(UserDetails user) {
        this.user = user;
        return this;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public TwStatus setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        return this;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public TwStatus setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
        return this;
    }
}
