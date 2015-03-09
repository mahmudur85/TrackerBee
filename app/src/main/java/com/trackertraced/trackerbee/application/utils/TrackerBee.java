package com.trackertraced.trackerbee.application.utils;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by Mahmudur on 20-Feb-15.
 */
public class TrackerBee extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationConstants.setTrackerBee(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
