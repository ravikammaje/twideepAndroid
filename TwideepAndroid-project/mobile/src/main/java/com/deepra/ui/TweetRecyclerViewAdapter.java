package com.deepra.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.data.TwStatus;
import com.deepra.twitter.data.TweetList;

public class TweetRecyclerViewAdapter extends RecyclerView.Adapter<TweetRecyclerViewAdapter.ViewHolder> {

    public static final int FORMAT_TWEET_HORIZONTAL = 1;
    public static final int FORMAT_TWEET_VERTICAL = 2;

    private final int mFormatStyle;
    private TweetList mTwSortedList;

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
        holder.mItem = mTwSortedList.get(position);
        holder.mIdView.setText(mTwSortedList.get(position).getUser().getName());
        holder.mContentView.setText(mTwSortedList.get(position).getText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onVertTwClick(TweetRecyclerViewAdapter.this, position);
                }
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public TwStatus mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
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

