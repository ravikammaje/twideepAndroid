package com.deepra.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.data.TwStatus;
import com.deepra.twitter.data.TweetList;

import java.text.SimpleDateFormat;

public class TweetRecyclerViewAdapter extends RecyclerView.Adapter<TweetRecyclerViewAdapter.ViewHolder> {

    public static final int FORMAT_TWEET_HORIZONTAL = 1;
    public static final int FORMAT_TWEET_VERTICAL = 2;

    private final int mFormatStyle;
    private TweetList mTwSortedList;

    int mSelectedItem=-1;

    public TweetList getTwSortedList() {
        return mTwSortedList;
    }

    private final OnVertTwClickListener mListener;

    public TweetRecyclerViewAdapter(OnVertTwClickListener listener, int formatStyle) {
        mFormatStyle = formatStyle;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(mFormatStyle == FORMAT_TWEET_HORIZONTAL)
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_timeline, parent, false);
        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_timeline_vert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TwStatus twStatus = mTwSortedList.get(position);
        holder.mItem = twStatus;
        holder.mIdView.setText(twStatus.getUser().getName());
        holder.mContentView.setText(twStatus.getText());
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        holder.mTimestamp.setText(format.format(twStatus.getCreatedAtDate()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onVertTwClick(TweetRecyclerViewAdapter.this, position);
                    mSelectedItem = position;
                    notifyDataSetChanged();
                }
            }
        });

        if(mSelectedItem == position) {
            holder.mView.setBackgroundColor(Color.parseColor("#123456"));
        }
        else
            holder.mView.setBackgroundColor(Color.parseColor("#ffffff"));

    }

    @Override
    public int getItemCount() {
        if(mTwSortedList == null)
            return 0;
        return mTwSortedList.size();
    }

    public void setTweetList(TweetList tweetList) {
        mTwSortedList = tweetList;
        notifyDataSetChanged();
    }

    public void unselectAll() {
        mSelectedItem = -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mTimestamp;
        public TwStatus mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mTimestamp = view.findViewById(R.id.timestamp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

interface OnVertTwClickListener {

    public void onVertTwClick(TweetRecyclerViewAdapter adapter, int position);
}

