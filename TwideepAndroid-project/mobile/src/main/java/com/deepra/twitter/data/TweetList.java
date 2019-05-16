package com.deepra.twitter.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class TweetList {

    private TreeSet<TwStatus> mTweetList;

    public TweetList() {
        mTweetList = new TreeSet<TwStatus>(getTweetComparator());
    }

    public TwStatus get(int position){
        int i=0;
        Iterator<TwStatus> iterator = mTweetList.iterator();
        while(iterator.hasNext()) {
            TwStatus tweet = iterator.next();
            if(i==position)
                return tweet;
            i++;
        }
        return null;
    }

    private Comparator<TwStatus> getTweetComparator() {
        Comparator c = new Comparator<TwStatus>() {
            @Override
            public int compare(TwStatus t1, TwStatus t2) {
                return Long.compare(t1.getId(), t2.getId());
            }
        };
        return c;
    }


    public int size() {
        return mTweetList.size();
    }

    public TreeSet<TwStatus> getTweetList() {
        return mTweetList;
    }
}
