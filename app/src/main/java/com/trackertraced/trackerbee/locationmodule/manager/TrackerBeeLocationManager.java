package com.trackertraced.trackerbee.locationmodule.manager;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Mahmudur on 06-Mar-15.
 */
public interface TrackerBeeLocationManager {

    public boolean getLatestLocation(Context context);

    public boolean isLocationManagerInitialized();

    public void initLocationManager(Context context);

    public void setOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListener);

    public void stopLocationUpdate();
}
