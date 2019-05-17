package com.deepra.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.data.TWDataProvider;
import com.deepra.twitter.data.TwStatus;
import com.deepra.twitter.data.TweetList;

public class ReaderFragment extends Fragment {

    private OnHorzClickListener mOnHorzClickListener;
    private HorzRecyclerViewAdapter mAdapter;
    private TWDataProvider mTwDataProvider;
    private WebView mWebView;
    private RecyclerView mTwRecyclerView;
    private int mSelectedItem;

    public ReaderFragment() {
    }

    public static ReaderFragment newInstance(String param1, String param2) {
        ReaderFragment fragment = new ReaderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_horz_timeline_list, container, false);
        mTwRecyclerView = (RecyclerView)v.findViewById(R.id.horz_list);
        mAdapter = new HorzRecyclerViewAdapter(mOnHorzClickListener);
        mAdapter.setSelectedItem(mSelectedItem);
        mTwRecyclerView.setAdapter(mAdapter);
        mWebView = v.findViewById(R.id.webview);

        TweetList twStatuses = mTwDataProvider.getTwData().getTweets();
        mAdapter.setTweetList(twStatuses);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof  TWDataProvider)
            mTwDataProvider = (TWDataProvider)context;

        mOnHorzClickListener = createHorzClickListener();
    }

    public void selectItem(int i) {
        mSelectedItem = i;
    }

    private OnHorzClickListener createHorzClickListener() {
        OnHorzClickListener onHorzClickListener = new OnHorzClickListener() {
            @Override
            public void onHorzClick(HorzRecyclerViewAdapter mAdapter, int i) {
                mSelectedItem = i;
                TwStatus status = mAdapter.getList().get(i);
                int indexLinkStart = 0;
                String statusStr = status.getText();
                if(status.getRetweeted_status() != null)
                    statusStr = "RT "+status.getRetweeted_status().getText();
                indexLinkStart = statusStr.indexOf("http://");
                if(indexLinkStart == -1)
                    indexLinkStart = statusStr.indexOf("https://");
                if(indexLinkStart != -1) {
                    String link = statusStr.substring(indexLinkStart);
                    link = link.split("\\s")[0];

                    final ProgressDialog progDailog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.getSettings().setLoadWithOverviewMode(true);
                    mWebView.getSettings().setUseWideViewPort(true);
                    mWebView.setWebViewClient(new WebViewClient(){

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            progDailog.show();
                            view.loadUrl(url);

                            return true;
                        }
                        @Override
                        public void onPageFinished(WebView view, final String url) {
                            progDailog.dismiss();
                        }
                    });

                    mWebView.loadUrl(link);
                }
            }
        };
        return onHorzClickListener;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTwDataProvider = null;
    }


    private class HorzRecyclerViewAdapter extends RecyclerView.Adapter<HorzRecyclerViewAdapter.ViewHolder>{

        private TweetList mTwSortedList;
        private int mSelectedItem;
        private RecyclerView mRecyclerView;

        public TweetList getList() {
            return mTwSortedList;
        }

        public HorzRecyclerViewAdapter(OnHorzClickListener listener) {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_horz_timeline, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            mRecyclerView = recyclerView;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
            holder.mItem = mTwSortedList.get(i);
            holder.mIdView.setText(mTwSortedList.get(i).getUser().getName());
            holder.mContentView.setText(mTwSortedList.get(i).getText());
            holder.mView.setBackgroundColor(Color.parseColor("#00000000"));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnHorzClickListener) {
                        mOnHorzClickListener.onHorzClick(mAdapter, i);
                        v.setBackgroundColor(Color.parseColor("#567845"));

                    }
                }
            });

//            holder.mContentView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    v.setBackgroundColor(Color.parseColor("#567845"));
//                }
//            });

            if(mSelectedItem == i) {
                holder.mView.performClick();
            }

            scrollToSelectedItem(holder, i);
        }

        private void scrollToSelectedItem(@NonNull final ViewHolder holder, final int i) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(mSelectedItem);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTwSortedList.size();
        }

        public void setTweetList(TweetList tweetList) {
            mTwSortedList = tweetList;
        }

        public int getSelectedItem() {
            return mSelectedItem;
        }

        public void setSelectedItem(int selectedItem) {
            this.mSelectedItem = selectedItem;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView mIdView;
            private final TextView mContentView;
            public TwStatus mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.item_number);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    private interface OnHorzClickListener {

        public void onHorzClick(HorzRecyclerViewAdapter mAdapter, int i);

    }
}
