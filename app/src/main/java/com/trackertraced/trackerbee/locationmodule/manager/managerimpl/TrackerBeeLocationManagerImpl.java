package com.trackertraced.trackerbee.locationmodule.manager.managerimpl;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.trackertraced.trackerbee.application.utils.LogHelper;
import com.trackertraced.trackerbee.locationmodule.manager.OnLocationUpdateListener;
import com.trackertraced.trackerbee.locationmodule.manager.TrackerBeeLocationManager;

/**
 * Created by Mahmudur on 06-Mar-15.
 */
public class TrackerBeeLocationManagerImpl implements TrackerBeeLocationManager {
    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR,TrackerBeeLocationManagerImpl.class.getSimpleName(),true);
    private static final long INTERVAL_TIME = 1000 * 60 * 10;// 2 Minutes
    private static final long UPDATE_INTERVAL_MILLISECONDS = 1000;
    private static final float UPDATE_INTERVAL_MINIMUM_DISTANCE = 20f;
    private LocationManager locationManager;
    private TrackerBeeLocationListener trackerBeeLocationListener;
    private Location previousBestLocation = null;
    private OnLocationUpdateListener onLocationUpdateListener = null;
    private boolean isLocationManagerInitialized = false;
    private float minimumDistance = 20.0f;

    public TrackerBeeLocationManagerImpl(){
        isLocationManagerInitialized = false;
    }

    public boolean isLocationManagerInitialized() {
        return isLocationManagerInitialized;
    }

    private void setLocationManagerInitialized(boolean isLocationManagerInitialized) {
        this.isLocationManagerInitialized = isLocationManagerInitialized;
    }

    public void setOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListener) {
        this.onLocationUpdateListener = onLocationUpdateListener;
    }

    public void stopLocationUpdate(){
        this.locationManager.removeUpdates(trackerBeeLocationListener);
        setLocationManagerInitialized(false);
    }

    public void initLocationManager(Context context){
        try{
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            this.trackerBeeLocationListener = new TrackerBeeLocationListener();

            Criteria criteria = new Criteria();
            String provider = this.locationManager.getBestProvider(criteria, false);
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
            Location location = this.locationManager.getLastKnownLocation(provider);
            if(onLocationUpdateListener != null){
                onLocationUpdateListener.OnLocationUpdate(location);
            }
            this.locationManager.requestLocationUpdates(provider,
                    (UPDATE_INTERVAL_MILLISECONDS * 600),
                    UPDATE_INTERVAL_MINIMUM_DISTANCE, this.trackerBeeLocationListener);
            setLocationManagerInitialized(true);
        }catch(Exception e){
            logHelper.e("initLocationManager",e);
            setLocationManagerInitialized(false);
        }
    }

    /**
     * Get the latest location trying multiple providers
     * <p/>
     * Calling this method requires that your application's manifest contains
     * the {@link android.Manifest.permission#ACCESS_FINE_LOCATION} permission
     *
     * @param context
     * @return latest location set or null if none
     */
    public boolean getLatestLocation(Context context) {
        LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = manager.getBestProvider(criteria, true);
        Location bestLocation;
        if (provider != null) {
            // Log.d(TAG, "Provider [" + provider + "] has been selected.");
            bestLocation = manager.getLastKnownLocation(provider);
        } else {
            bestLocation = null;
        }
        Location latestLocation = getLatest(bestLocation,
                manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        latestLocation = getLatest(latestLocation,
                manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        latestLocation = getLatest(latestLocation,
                manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));

        if (previousBestLocation == null) {
            previousBestLocation = latestLocation;
            if(onLocationUpdateListener != null){
                onLocationUpdateListener.OnLocationUpdate(latestLocation);
            }
        }else{
            /*
             * http://stackoverflow.com/questions/2741403/get-the-distance
             * -between-two-geo-points
             */
            if (latestLocation.distanceTo(previousBestLocation) >= this.minimumDistance) {
                if(onLocationUpdateListener != null){
                    previousBestLocation = latestLocation;
                    onLocationUpdateListener.OnLocationUpdate(latestLocation);
                }
            }
        }
        return true;
    }

    /**
     * Get the location with the later date
     *
     * @param location1
     * @param location2
     * @return location
     */
    private static Location getLatest(final Location location1,
                                      final Location location2) {
        if (location1 == null)
            return location2;

        if (location2 == null)
            return location1;

        if (location2.getTime() > location1.getTime())
            return location2;
        else
            return location1;
    }

    private boolean isBetterLocation(Location location,
                                        Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > INTERVAL_TIME;
        boolean isSignificantlyOlder = timeDelta < -INTERVAL_TIME;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private class TrackerBeeLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                if(onLocationUpdateListener != null){
                    onLocationUpdateListener.OnLocationUpdate(location);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
