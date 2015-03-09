package com.trackertraced.trackerbee.application.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.trackertraced.trackerbee.application.broadcastreceiver.ServiceBroadcastConstants;

/**
 * Created by Mahmudur on 20-Feb-15.
 */
public class ApplicationSharePreferences {
    private static final String TAG = "ApplicationSharedPreferences";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public final static class SharedPreferencesTags{
        public static final String APPLICATION_SHARED_PREFS = "com.trackertraced.trackerbee";
        public static final String TAG_LAST_KNOWN_LOCATION_LATITUDE = "last.known.location.latitude";
        public static final String TAG_LAST_KNOWN_LOCATION_LONGITUDE = "last.known.location.longitude";
        public static final String TAG_LAST_KNOWN_LOCATION_ALTITUDE = "last.known.location.altitude";
        public static final String TAG_DEVICE_ID = "device.id";
        public static final String TAG_SERVICE_START = "service.start";
    }

    public ApplicationSharePreferences(Context context){
        sharedPreferences = context.getSharedPreferences(SharedPreferencesTags.APPLICATION_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();
    }

    public static void setLastKnownLocation(Location location){
        editor.putLong(SharedPreferencesTags.TAG_LAST_KNOWN_LOCATION_LATITUDE,Double.doubleToLongBits(location.getLatitude()));
        editor.putLong(SharedPreferencesTags.TAG_LAST_KNOWN_LOCATION_LONGITUDE, Double.doubleToLongBits(location.getLongitude()));
        editor.putLong(SharedPreferencesTags.TAG_LAST_KNOWN_LOCATION_ALTITUDE,Double.doubleToLongBits(location.getAltitude()));
        editor.commit();
    }

    public static Location getLastKnownLocation(){
        Location location = new Location("");
        try{
            double lat = Double.longBitsToDouble(sharedPreferences.getLong(SharedPreferencesTags.TAG_LAST_KNOWN_LOCATION_LATITUDE,0));
            double lon = Double.longBitsToDouble(sharedPreferences.getLong(SharedPreferencesTags.TAG_LAST_KNOWN_LOCATION_LONGITUDE,0));
            double alt = Double.longBitsToDouble(sharedPreferences.getLong(SharedPreferencesTags.TAG_LAST_KNOWN_LOCATION_ALTITUDE,0));

            location.setLatitude(lat);
            location.setLongitude(lon);
            location.setAltitude(alt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public static void setDeviceId(String deviceId){
        editor.putString(SharedPreferencesTags.TAG_DEVICE_ID,deviceId);
        editor.commit();
    }

    public static String getDeviceId(){
        return sharedPreferences.getString(SharedPreferencesTags.TAG_DEVICE_ID,null);
    }
}
