package com.trackertraced.trackerbee.application.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.trackertraced.trackerbee.application.service.TrackerBeeService;

public class TrackerBeeBroadcastReceiver extends BroadcastReceiver {
    public TrackerBeeBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Intent serviceIntent =  new Intent(context, TrackerBeeService.class);
        context.startService(serviceIntent);
    }
}
