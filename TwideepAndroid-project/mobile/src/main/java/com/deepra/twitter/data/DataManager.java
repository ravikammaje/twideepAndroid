package com.deepra.twitter.data;

public class DataManager {

//    private final TwSortedList mTwSortedList;
    private DataManager mDataManager;

    public DataManager getInstance() {
        if(mDataManager == null)
            mDataManager = new DataManager();
        return mDataManager;
    }

    private DataManager() {
        //mTwSortedList = new TwSortedList();
    }




}
