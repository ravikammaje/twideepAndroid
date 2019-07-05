package com.deepra.twitter.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {TweetToSend.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sAppDB;

    public static AppDatabase getInstance(Context context) {

        if(sAppDB == null)
            sAppDB = Room.databaseBuilder(context,
                    AppDatabase.class, "database-name").build();

        return sAppDB;
    }

    public abstract TweetToSendDao tweetToSendDao();
}
