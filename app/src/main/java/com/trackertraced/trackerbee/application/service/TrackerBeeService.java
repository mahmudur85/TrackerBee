package com.trackertraced.trackerbee.application.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.trackertraced.trackerbee.application.broadcastreceiver.ServiceBroadcastConstants;
import com.trackertraced.trackerbee.application.utils.ApplicationConstants;
import com.trackertraced.trackerbee.application.utils.ApplicationHelper;
import com.trackertraced.trackerbee.application.utils.ApplicationSharePreferences;
import com.trackertraced.trackerbee.application.utils.ConstantsKeyValues;
import com.trackertraced.trackerbee.application.utils.DeviceUuidFactory;
import com.trackertraced.trackerbee.application.utils.LogHelper;
import com.trackertraced.trackerbee.application.utils.httpRequest.HTTPParams;
import com.trackertraced.trackerbee.application.utils.httpRequest.HTTPPostAsync;
import com.trackertraced.trackerbee.application.utils.httpRequest.HTTPResponseAsync;
import com.trackertraced.trackerbee.application.utils.httpRequest.HTTPResponseCode;
import com.trackertraced.trackerbee.application.utils.httpRequest.HTTPURLBuilder;
import com.trackertraced.trackerbee.locationmodule.manager.OnLocationUpdateListener;
import com.trackertraced.trackerbee.locationmodule.manager.TrackerBeeLocationManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrackerBeeService extends Service implements HTTPResponseAsync, OnLocationUpdateListener {

    // private String USER_ID = "mashru";
    private static String USER_ID;
    private static final String server = "27.147.151.61";
    //	private static final String server = "192.168.2.6";
    private static final int port = 10500;

    public String getUSER_ID() {
        return USER_ID;
    }

    public static void setUSER_ID(String uSER_ID) {
        USER_ID = uSER_ID;
    }

    private static final String TAG = "TrackerBeeService";

    private TrackerBeeLocationManager trackerBeeLocationManager;

    private ScheduledExecutorService locationScheduledTaskExecutor;

    private HTTPPostAsync mHTTPPostAsync;

    final Messenger serviceMessenger = new Messenger(new IncomingHandler());

    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, TrackerBeeService.class.getSimpleName(), true);

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (trackerBeeLocationManager == null) {
            trackerBeeLocationManager = ApplicationConstants.getTrackerBeeLocationManager();
            trackerBeeLocationManager.setOnLocationUpdateListener(this);
        }

        if (!trackerBeeLocationManager.isLocationManagerInitialized()) {
            trackerBeeLocationManager.initLocationManager(ApplicationConstants.getContext());
        }

        if (locationScheduledTaskExecutor == null) {
            locationScheduledTaskExecutor = Executors.newScheduledThreadPool(1);
        }
        try {
            locationScheduledTaskExecutor.scheduleAtFixedRate(
                    locationPingRunnable, 0, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logHelper.e("scheduleAtFixedRate", e);
        }
//        return START_REDELIVER_INTENT;
        return START_STICKY;
    }

    Runnable locationPingRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                trackerBeeLocationManager.getLatestLocation(ApplicationConstants.getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    String getLogInstanceJSON(Location cur_loc) {
        // { insID: $('#insID').val(), latt: $('#latt').val(), lon: $('#lon').val(), elv: $('#elv').val() }
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(getBaseContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("insID", deviceUuidFactory.getDeviceUuid());
            jsonObject.put("latt", String.valueOf(cur_loc.getLatitude()));
            jsonObject.put("lon", String.valueOf(cur_loc.getLongitude()));
            jsonObject.put("elv", String.valueOf(cur_loc.getAltitude()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    ArrayList<NameValuePair> getLogInstanceNameValuePair(Location cur_loc) {
        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("insID", ApplicationSharePreferences.getDeviceId()));
        data.add(new BasicNameValuePair("latt", String.valueOf(cur_loc.getLatitude())));
        data.add(new BasicNameValuePair("lon", String.valueOf(cur_loc.getLongitude())));
        data.add(new BasicNameValuePair("elv", String.valueOf(cur_loc.getAltitude())));
        logHelper.d("getLogInstanceNameValuePair() data: " + data.toString());
        return data;
    }

    private void broadcastLatestLocation(Location cur_loc) {
        Intent intent = new Intent(ServiceBroadcastConstants.BROADCAST_LATEST_LOCATION);
        intent.putExtra(ServiceBroadcastConstants.TAG_LATEST_LOCATION, cur_loc);
        sendBroadcast(intent);
    }

    private void sendLocationToServer(Location cur_loc) {
        String[] path = {HTTPURLBuilder.API, HTTPURLBuilder.LOG_INSTANCE};
        String url = HTTPURLBuilder.getHTTPUlr(path);
        HTTPParams httpParams = new HTTPParams();
        httpParams.nameValuePairs = getLogInstanceNameValuePair(cur_loc);
        mHTTPPostAsync = new HTTPPostAsync(url, httpParams, false);
        mHTTPPostAsync.delegate = this;
        mHTTPPostAsync.execute("");
    }

    ArrayList<NameValuePair> getGetInstanceNameValuePair(int rows, String timeTo, String timeFrom) {
        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("insID", ApplicationSharePreferences.getDeviceId()));
        if (rows > 0) {
            data.add(new BasicNameValuePair("rows", String.valueOf(rows)));
        }
        if (ApplicationHelper.isEmptyOrNull(timeTo)) {
            data.add(
                    new BasicNameValuePair(
                            "timeTo",
                            timeTo
                    )
            );
        }
        if (ApplicationHelper.isEmptyOrNull(timeFrom)) {
            data.add(
                    new BasicNameValuePair(
                            "timeFrom",
                            timeFrom
                    )
            );
        }
        logHelper.d("getGetInstanceNameValuePair() data: " + data.toString());
        return data;
    }

    private void requestGetInstance(int rows, String timeTo, String timeFrom) {
        String[] path = {HTTPURLBuilder.API, HTTPURLBuilder.GET_INSTANCE};
        String url = HTTPURLBuilder.getHTTPUlr(path);
        HTTPParams httpParams = new HTTPParams();
        httpParams.nameValuePairs = getGetInstanceNameValuePair(rows, timeTo, timeFrom);
        mHTTPPostAsync = new HTTPPostAsync(url, httpParams, false);
        mHTTPPostAsync.delegate = this;
        mHTTPPostAsync.execute("");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            locationScheduledTaskExecutor.shutdown();
            trackerBeeLocationManager.stopLocationUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return serviceMessenger.getBinder();
    }

    @Override
    public void HTTPResponse(String response, HTTPResponseCode.ResponseCode responseCode) {
        logHelper.d("response: " + response);
        mHTTPPostAsync.delegate = null;
    }

    @Override
    public void OnLocationUpdate(Location location) {
        //logHelper.d(location);
        if (location != null) {
            ApplicationSharePreferences.setLastKnownLocation(location);
            broadcastLatestLocation(location);
            sendLocationToServer(location);
        }
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            LogHelper logHelperIncomingHandler = new LogHelper(LogHelper.LogTags.KMR, IncomingHandler.class.getSimpleName(), true);
//            msg.
            Bundle data = msg.getData();
            int messageType = data.getInt(
                    ConstantsKeyValues.ServerMessageConsttants.MessageTags.TAG_MESSAGE_TYPE
            );
            String timeTo = "";
            String timeFrom = "";
            int rows = 0;
            switch (messageType) {
                case ConstantsKeyValues.ServerMessageConsttants.MessageTypes.TYPE_MESSAGE_GET_INSTANCE_BY_TIME_RANGE:
                    logHelperIncomingHandler.d("TAG_MESSAGE_TYPE: TYPE_MESSAGE_GET_INSTANCE_BY_TIME_RANGE");
                    timeTo = data.getString(ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_TIME_TO);
                    timeFrom = data.getString(ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_TIME_FROM);
                    break;
                case ConstantsKeyValues.ServerMessageConsttants.MessageTypes.TYPE_MESSAGE_GET_INSTANCE_TIME_FROM:
                    logHelperIncomingHandler.d("TAG_MESSAGE_TYPE: TYPE_MESSAGE_GET_INSTANCE_TIME_FROM");
                    timeFrom = data.getString(ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_TIME_FROM);
                    break;
                case ConstantsKeyValues.ServerMessageConsttants.MessageTypes.TYPE_MESSAGE_GET_INSTANCE_BY_ROWS:
                    logHelperIncomingHandler.d("TAG_MESSAGE_TYPE: TYPE_MESSAGE_GET_INSTANCE_BY_ROWS");
                    rows = data.getInt(ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_ROWS);
                    break;
                default:
                    break;
            }
            logHelperIncomingHandler.d("rows: " + rows + ", timeTo: " + timeTo + ", timeFrom: " + timeFrom);
            requestGetInstance(rows, timeTo, timeFrom);
        }
    }

}
