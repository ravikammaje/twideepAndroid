package com.deepra.twitter.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TweetToSendDao {

    @Query("SELECT * FROM tweettosend")
    List<TweetToSend> getAll();

    @Insert
    void insert(TweetToSend tweet);

    @Insert
    void insertAll(TweetToSend... tweets);

    @Delete
    void delete(TweetToSend tweet);
}
