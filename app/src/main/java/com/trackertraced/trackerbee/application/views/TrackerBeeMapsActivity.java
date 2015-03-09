package com.trackertraced.trackerbee.application.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trackertraced.trackerbee.R;
import com.trackertraced.trackerbee.application.broadcastreceiver.ServiceBroadcastConstants;
import com.trackertraced.trackerbee.application.manager.ServiceMessengerManager;
import com.trackertraced.trackerbee.application.manager.managerImpl.ServiceMessengerManagerImpl;
import com.trackertraced.trackerbee.application.service.TrackerBeeService;
import com.trackertraced.trackerbee.application.utils.ApplicationConstants;
import com.trackertraced.trackerbee.application.utils.ApplicationSharePreferences;
import com.trackertraced.trackerbee.application.utils.LogHelper;

public class TrackerBeeMapsActivity extends FragmentActivity {

    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR,TrackerBeeMapsActivity.class.getSimpleName(),true);

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private UiSettings uiSettingsMap;

    private ServiceMessengerManager serviceMessengerManager = new ServiceMessengerManagerImpl();

    EditText editTextDeviceId;
    Button buttonStart;

    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_bee_maps);

        ApplicationConstants.setContext(getBaseContext());
        ApplicationSharePreferences applicationSharePreferences =  new ApplicationSharePreferences(getBaseContext());

        editTextDeviceId = (EditText) findViewById(R.id.device_id);
        if(ApplicationSharePreferences.getDeviceId()!=null){
            editTextDeviceId.setText(ApplicationSharePreferences.getDeviceId());
        }
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrackerBeeService = new Intent(ApplicationConstants.getContext(), TrackerBeeService.class);
                String deviceId = editTextDeviceId.getText().toString();
                if(deviceId != null || deviceId != "" || deviceId.length() != 0){
                    ApplicationSharePreferences.setDeviceId(deviceId);
                    startService(intentTrackerBeeService);
                    bindService(intentTrackerBeeService,serviceMessengerManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
                    registerReceiver(latestLocationBroadCastReceiver,new IntentFilter(ServiceBroadcastConstants.BROADCAST_LATEST_LOCATION));
                    buttonStart.setText("Stop");
                }
            }
        });
//        logHelper.d("onCreate()");

        setUpMapIfNeeded();
    }

    private final BroadcastReceiver latestLocationBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(ServiceBroadcastConstants.TAG_LATEST_LOCATION);
//            logHelper.d("BroadcastReceiver() location: " + location.toString());
            setUpMap(new LatLng(location.getLatitude(),location.getLongitude()));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(serviceMessengerManager.getServiceConnection());
        unregisterReceiver(latestLocationBroadCastReceiver);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap(LatLng latLng)} once when {@link #mMap} is not null.
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
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Location location = ApplicationSharePreferences.getLastKnownLocation();
                setUpMap(new LatLng(location.getLatitude(),location.getLongitude()));
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
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
