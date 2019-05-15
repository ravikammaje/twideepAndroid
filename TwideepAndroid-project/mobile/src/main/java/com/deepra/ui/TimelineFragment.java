package com.deepra.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepra.twideepandroid.R;
import com.deepra.twitter.data.TWDataProvider;
import com.deepra.twitter.OAuthToken;
import com.deepra.twitter.TwitterRetrofit;
import com.deepra.twitter.TwitterService;
import com.deepra.twitter.data.TwStatus;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TimelineFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private TWDataProvider mTwDataProvider;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnVertTwClickListener mListener;
    private TweetRecyclerViewAdapter mAdapter;


    public TimelineFragment() {

    }

    public void setArguments(OAuthToken token, Credentials credentials, TwitterService twitterService) {
//        mToken = token;
//        mCredentials = credentials;
//        mTwitterService = twitterService;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TimelineFragment newInstance(int columnCount) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        twGetHomeTimeline();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            createItemListener();
            mAdapter = new TweetRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

//    private void createItemListener() {
//        mListener = new onItemClickListener() {
//            @Override
//            public void onClick(TweetRecyclerViewAdapter adapter, int position) {
//                Toast.makeText(getContext(), "Please wait while we navigate to reader view...", Toast.LENGTH_LONG).show();
//                TwStatus status = adapter.getTwSortedList().getTwStatuses().get(position);
//                if(status.getText().contains("http://") || status.getText().contains("https://")) {
//                    Toast.makeText(getContext(), "Navigating to..." + status.getText(), Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  OnVertTwClickListener)
            mListener = (OnVertTwClickListener)context;
        if(context instanceof  TWDataProvider)
            mTwDataProvider = (TWDataProvider)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mTwDataProvider = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TwStatus item);
    }

    public void twGetHomeTimeline() {
        TwitterRetrofit.getInstance().getTwitterApi().getUserHomeTimeline(200, "extended").enqueue(mUserHomeTimelineCallback);
    }
    //Retrofit callbacks
    //GetUserHomeTimeLine
    private final Callback<List<TwStatus>> mUserHomeTimelineCallback = new Callback<List<TwStatus>>() {
        @Override
        public void onResponse(Call<List<TwStatus>> call, retrofit2.Response<List<TwStatus>> response) {
            if (response.isSuccessful()) {
                List<TwStatus> twStatuses = response.body();
                mTwDataProvider.getTwData().setTweets(twStatuses);

                mAdapter.getTwSortedList().getTwStatuses().addAll(twStatuses);
                for(int i=0; i < mAdapter.getTwSortedList().getTwStatuses().size(); i++) {
                    TwStatus status = mAdapter.getTwSortedList().getTwStatuses().get(i);
                    Log.d(this.getClass().toString(), status.getId()+":"+status.getText());
                }
            } else {
                Log.d("UserDetailsCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<List<TwStatus>> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
