package com.deepra.twitter.data.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TweetToSend {

    @PrimaryKey(autoGenerate = true)
    public long dbId;

    @ColumnInfo(name = "tweetid")
    public long tweetId;

    @ColumnInfo(name = "tweet")
    public String tweet;

    @ColumnInfo(name = "mediaIds")
    public String mediaIds;
    public String createdAt;
}
