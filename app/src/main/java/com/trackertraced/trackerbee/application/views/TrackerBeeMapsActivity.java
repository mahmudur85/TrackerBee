package com.trackertraced.trackerbee.application.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.trackertraced.trackerbee.R;
import com.trackertraced.trackerbee.application.broadcastreceiver.ServiceBroadcastConstants;
import com.trackertraced.trackerbee.application.manager.ServiceMessengerManager;
import com.trackertraced.trackerbee.application.manager.managerImpl.ServiceMessengerManagerImpl;
import com.trackertraced.trackerbee.application.service.TrackerBeeService;
import com.trackertraced.trackerbee.application.utils.ApplicationConstants;
import com.trackertraced.trackerbee.application.utils.ApplicationHelper;
import com.trackertraced.trackerbee.application.utils.ApplicationSharePreferences;
import com.trackertraced.trackerbee.application.utils.ConstantsKeyValues;
import com.trackertraced.trackerbee.application.utils.DeviceUuidFactory;
import com.trackertraced.trackerbee.application.utils.LogHelper;
import com.trackertraced.trackerbee.locationmodule.model.LocationModel;

import java.util.ArrayList;

public class TrackerBeeMapsActivity
        extends FragmentActivity
        implements OnMapClickListener,
        OnMapLongClickListener,
        GoogleMap.OnMapLoadedCallback {
    // http://internet.com/mobile/developing-with-google-maps-v2-for-android/
    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, TrackerBeeMapsActivity.class.getSimpleName(), true);
    TextView textViewDeviceId;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private UiSettings uiSettingsMap;
//    private ServiceMessengerManager serviceMessengerManager = new ServiceMessengerManagerImpl();

    //Intent intentTrackerBeeService;

//    EditText editTextDeviceId;

    boolean firstLoad;

    Marker marker;
    private ArrayList<LatLng> arrayPoints = null;
    Polyline polyline;
    PolylineOptions polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_bee_maps);

        ApplicationConstants.setContext(getBaseContext());

        ApplicationSharePreferences applicationSharePreferences = new ApplicationSharePreferences(getBaseContext());

        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(getBaseContext());
        ApplicationSharePreferences.setDeviceId(deviceUuidFactory.getDeviceUuid().toString());
        textViewDeviceId = (TextView) findViewById(R.id.tv_device_id);
        textViewDeviceId.setText(deviceUuidFactory.getDeviceUuid().toString());
        setUpMapIfNeeded();

//        editTextDeviceId = (EditText) findViewById(R.id.device_id);
//        buttonStart = (Button) findViewById(R.id.button_start);
//        buttonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String deviceId = editTextDeviceId.getText().toString();
//                if(buttonStart.getText().equals("Stop")){
//                    buttonStart.setText("Start");
//                    Intent intentTrackerBeeService = new Intent(ApplicationConstants.getContext(), TrackerBeeService.class);
//                    stopService(intentTrackerBeeService);
//                }else{
//                    if(deviceId != null || deviceId != "" || deviceId.length() != 0){
//                        ApplicationSharePreferences.setDeviceId(deviceId);
//                        Intent intentTrackerBeeService = new Intent(ApplicationConstants.getContext(), TrackerBeeService.class);
//                        startService(intentTrackerBeeService);
//                        bindService(intentTrackerBeeService,serviceMessengerManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
//                        registerReceiver(latestLocationBroadCastReceiver,new IntentFilter(ServiceBroadcastConstants.BROADCAST_LATEST_LOCATION));
//                        buttonStart.setText("Stop");
//                    }
//                }
//            }
//        });
//        logHelper.d("onCreate()");
    }

    private final BroadcastReceiver latestLocationBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(ServiceBroadcastConstants.TAG_LATEST_LOCATION);
//            logHelper.d("BroadcastReceiver() location: " + location.toString());
            //addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            addPolyline(latLng);
            moveToLocation(latLng);
        }
    };
    private final BroadcastReceiver locationListBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<LocationModel> locationModels = intent.getParcelableArrayListExtra(ServiceBroadcastConstants.TAG_LOCATION_LIST);
            logHelper.d("location: " + locationModels);
            //addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
            removePolyline();
            clearMap();
            for (LocationModel locationModel : locationModels) {
                LatLng latLng = new LatLng(
                        locationModel.getLocation().getLatitude(),
                        locationModel.getLocation().getLongitude()
                );
                addPolyline(latLng);
                moveToLocation(latLng);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(
                ApplicationConstants
                        .getServiceMessengerManager()
                        .getServiceConnection()
        );
//        unregisterReceiver(latestLocationBroadCastReceiver);
        unregisterReceiver(locationListBroadCastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentTrackerBeeService = new Intent(ApplicationConstants.getContext(), TrackerBeeService.class);
        startService(intentTrackerBeeService);
        bindService(intentTrackerBeeService,
                ApplicationConstants
                        .getServiceMessengerManager()
                        .getServiceConnection(),
                Context.BIND_AUTO_CREATE
        );
//        registerReceiver(latestLocationBroadCastReceiver, new IntentFilter(ServiceBroadcastConstants.BROADCAST_LATEST_LOCATION));
        registerReceiver(locationListBroadCastReceiver, new IntentFilter(ServiceBroadcastConstants.BROADCAST_LOCATION_LIST));
//        if(ApplicationSharePreferences.getDeviceId()!=null){
//            bindService(intentTrackerBeeService,serviceMessengerManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
//            registerReceiver(latestLocationBroadCastReceiver,new IntentFilter(ServiceBroadcastConstants.BROADCAST_LATEST_LOCATION));
//            buttonStart.setText("Stop");
//        }
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firstLoad = false;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #addMarker(LatLng latLng)} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
        arrayPoints = new ArrayList<LatLng>();
        mMap.setMyLocationEnabled(true);
        // mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapLoadedCallback(this);
    }

    private void moveToLocation(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(15)                   // Sets the zoom
                .build();    // Creates a CameraPosition from the builder
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);//
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void addMarker(LatLng latLng) {
        removeMarker();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        moveToLocation(latLng);
    }

    private void removeMarker() {
        if (null != marker) {
            marker.remove();
        }
    }

    private void addPolyline(ArrayList<LatLng> positionList) {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        polylineOptions.addAll(positionList);
        polyline = mMap.addPolyline(polylineOptions);
    }

    private void addPolyline(LatLng position) {
        arrayPoints.add(position);
        addPolyline(arrayPoints);
    }

    private void removePolyline() {
        if (null != polyline) {
            polyline.remove();
        }
    }

    private void clearMap() {
        mMap.clear();
        arrayPoints.clear();
    }

    @Override
    public void onMapClick(LatLng point) {
        //add marker
        addMarker(point);
        // setting polyline in the map
        addPolyline(point);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        clearMap();
    }

    /**
     * Method for sending request message to {@link com.trackertraced.trackerbee.application.service.TrackerBeeService}
     * to GPS Logs from server
     *
     * @param timeTo
     * @param timeFrom
     */
    public void requestInstanceLog(String timeTo, String timeFrom) {
        Bundle params = new Bundle();
        params.putInt(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.TAG_MESSAGE_TYPE,
                ConstantsKeyValues.ServerMessageConsttants.MessageTypes.TYPE_MESSAGE_GET_INSTANCE_BY_TIME_RANGE
        );
        params.putString(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_TIME_TO,
                timeTo
        );
        params.putString(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_TIME_FROM,
                timeFrom
        );
        ApplicationConstants.getServiceMessengerManager().sendMessageToService(params);
    }

    /**
     * Method for sending request message to {@link com.trackertraced.trackerbee.application.service.TrackerBeeService}
     * to GPS Logs from server
     *
     * @param timeFrom
     */
    public void requestInstanceLog(String timeFrom) {
        Bundle params = new Bundle();
        params.putInt(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.TAG_MESSAGE_TYPE,
                ConstantsKeyValues.ServerMessageConsttants.MessageTypes.TYPE_MESSAGE_GET_INSTANCE_TIME_FROM
        );
        params.putString(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_TIME_FROM,
                timeFrom
        );
        ApplicationConstants.getServiceMessengerManager().sendMessageToService(params);
    }

    /**
     * Method for sending request message to {@link com.trackertraced.trackerbee.application.service.TrackerBeeService}
     * to GPS Logs from server
     *
     * @param rows
     */
    public void requestInstanceLog(int rows) {
        Bundle params = new Bundle();
        params.putInt(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.TAG_MESSAGE_TYPE,
                ConstantsKeyValues.ServerMessageConsttants.MessageTypes.TYPE_MESSAGE_GET_INSTANCE_BY_ROWS
        );
        params.putInt(
                ConstantsKeyValues.ServerMessageConsttants.MessageTags.GetInstance.TAG_ROWS,
                rows
        );
        ApplicationConstants.getServiceMessengerManager().sendMessageToService(params);
    }

    @Override
    public void onMapLoaded() {
        if (!firstLoad) {
            firstLoad = true;
            this.requestInstanceLog(
                    ApplicationHelper.getYesterdayDateString()
            );
        }
    }

    //TODO:
    /**
     * speed, accuracy field in android/hardware gps
     * plotting
     * reporting
     * user management
     * notification
     * supporting - turn on device if right pin is given
     * deliver android app to rozer, mashru
     * emergency stop
     * log instance when
     * */
}
