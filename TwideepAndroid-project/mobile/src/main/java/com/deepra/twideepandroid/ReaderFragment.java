package com.deepra.twideepandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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

import com.deepra.twitter.data.TwSortedList;
import com.deepra.twitter.data.TwStatus;

import java.util.List;

public class ReaderFragment extends Fragment {

    private OnHorzClickListener mOnHorzClickListener;
    private HorzRecyclerViewAdapter mAdapter;
    private TwSortedList mTwSortedList;
    private TWDataProvider mTwDataProvider;
    private WebView mWebView;

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
        RecyclerView twRecyclerView = (RecyclerView)v.findViewById(R.id.horz_list);
        mAdapter = new HorzRecyclerViewAdapter(mOnHorzClickListener);
        twRecyclerView.setAdapter(mAdapter);
        mTwSortedList = new TwSortedList(mAdapter);
        mWebView = v.findViewById(R.id.webview);

        List<TwStatus> twStatuses = mTwDataProvider.getTwData().getTweets();
        mAdapter.getTwSortedList().getTwStatuses().addAll(twStatuses);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof  TWDataProvider)
            mTwDataProvider = (TWDataProvider)context;

        mOnHorzClickListener = createHorzClickListener();
    }

    private OnHorzClickListener createHorzClickListener() {
        OnHorzClickListener onHorzClickListener = new OnHorzClickListener() {
            @Override
            public void onHorzClick(HorzRecyclerViewAdapter mAdapter, int i) {
                TwStatus status = mAdapter.getTwSortedList().getTwStatuses().get(i);
                int indexLinkStart = 0;
                indexLinkStart = status.getText().indexOf("http://");
                if(indexLinkStart == -1)
                    indexLinkStart = status.getText().indexOf("https://");
                if(indexLinkStart != -1) {
                    String link = status.getText().substring(indexLinkStart);
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

        private final TwSortedList mTwSortedList;

        public TwSortedList getTwSortedList() {
            return mTwSortedList;
        }

        public HorzRecyclerViewAdapter(OnHorzClickListener listener) {
            mTwSortedList = new TwSortedList(this);

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_horz_timeline, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
            holder.mItem = mTwSortedList.getTwStatuses().get(i);
            holder.mIdView.setText(mTwSortedList.getTwStatuses().get(i).getUser().getName());
            holder.mContentView.setText(mTwSortedList.getTwStatuses().get(i).getText());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnHorzClickListener) {
                        mOnHorzClickListener.onHorzClick(mAdapter, i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTwSortedList.getTwStatuses().size();
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
