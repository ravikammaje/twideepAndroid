package com.deepra.twideepandroid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepra.twideepandroid.TimelineFragment.OnListFragmentInteractionListener;
import com.deepra.twitter.data.TwSortedList;
import com.deepra.twitter.data.TwStatus;

public class TweetRecyclerViewAdapter extends RecyclerView.Adapter<TweetRecyclerViewAdapter.ViewHolder> {

    private final TwSortedList mTwSortedList;
    private final OnVertTwClickListener mListener;

    public TweetRecyclerViewAdapter(OnVertTwClickListener listener) {
        mTwSortedList = new TwSortedList(this);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mTwSortedList.getTwStatuses().get(position);
        holder.mIdView.setText(mTwSortedList.getTwStatuses().get(position).getUser().getName());
        holder.mContentView.setText(mTwSortedList.getTwStatuses().get(position).getText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onVertTwClick(TweetRecyclerViewAdapter.this, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTwSortedList.getTwStatuses().size();
    }

    public TwSortedList getTwSortedList() {
        return mTwSortedList;
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
