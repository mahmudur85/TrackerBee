package com.trackertraced.trackerbee.application.utils;

import android.content.Context;

import com.trackertraced.trackerbee.application.manager.ServiceMessengerManager;
import com.trackertraced.trackerbee.application.manager.managerImpl.ServiceMessengerManagerImpl;
import com.trackertraced.trackerbee.locationmodule.manager.TrackerBeeLocationManager;
import com.trackertraced.trackerbee.locationmodule.manager.managerimpl.TrackerBeeLocationManagerImpl;

/**
 * Created by Mahmudur on 20-Feb-15.
 */
public class ApplicationConstants {
    private static TrackerBee trackerBee;
    private static Context context;
    private static ServiceMessengerManager serviceMessengerManager = new ServiceMessengerManagerImpl();
    private static TrackerBeeLocationManager trackerBeeLocationManager = new TrackerBeeLocationManagerImpl();

    public static TrackerBee getTrackerBee() {
        return trackerBee;
    }

    public static void setTrackerBee(TrackerBee trackerBee) {
        ApplicationConstants.trackerBee = trackerBee;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ApplicationConstants.context = context;
    }

    public static ServiceMessengerManager getServiceMessengerManager() {
        return serviceMessengerManager;
    }

    public static void setServiceMessengerManager(ServiceMessengerManager serviceMessengerManager) {
        ApplicationConstants.serviceMessengerManager = serviceMessengerManager;
    }

    public static TrackerBeeLocationManager getTrackerBeeLocationManager() {
        return trackerBeeLocationManager;
    }

    public static void setTrackerBeeLocationManager(TrackerBeeLocationManager trackerBeeLocationManager) {
        ApplicationConstants.trackerBeeLocationManager = trackerBeeLocationManager;
    }
}
