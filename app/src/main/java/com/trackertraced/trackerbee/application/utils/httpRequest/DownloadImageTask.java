package com.trackertraced.trackerbee.application.utils.httpRequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.InputStream;

/**
 * Created by Mahmudur on 1/5/2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadImageTask";
    LinearLayout mLinearLayout;
    private Context mContext;

    public DownloadImageTask(LinearLayout linearLayout, Context context) {
        this.mLinearLayout = linearLayout;
        this.mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Log.d(TAG, "doInBackground(): " + url);
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground()", e);
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Log.d(TAG, "onPostExecute()");
        Drawable dw = new BitmapDrawable(mContext.getResources(), bitmap);
        mLinearLayout.setBackgroundDrawable(dw);
    }
}
