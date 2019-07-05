package com.deepra.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class RunInAsyncTask extends AsyncTask {

    private final Runnable mRunnable;
    private Context mActivity;

    public RunInAsyncTask(Runnable runnable, Context context) {
        mActivity = context;
        mRunnable = runnable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        mRunnable.run();
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Toast.makeText(mActivity, "Done", 500);
    }
}
