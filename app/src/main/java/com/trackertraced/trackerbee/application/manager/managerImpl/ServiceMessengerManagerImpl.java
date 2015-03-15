package com.trackertraced.trackerbee.application.manager.managerImpl;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.trackertraced.trackerbee.application.manager.ServiceMessengerManager;
import com.trackertraced.trackerbee.application.utils.LogHelper;


/**
 * Created by Mahmudur on 2/25/2015.
 */
public class ServiceMessengerManagerImpl implements ServiceMessengerManager {
    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, ServiceMessengerManagerImpl.class.getSimpleName(), false);
    Messenger serviceMessenger;
    boolean isBound;
    ServiceConnection serviceConnection;

    public ServiceMessengerManagerImpl() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceMessenger = new Messenger(service);
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceMessenger = null;
                isBound = false;
            }
        };
        logHelper.d("serviceConnection: " + serviceConnection);
    }

    @Override
    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    @Override
    public boolean isBound() {
        return isBound;
    }

    @Override
    public void sendMessageToService(Bundle data) {
        Message msg = Message.obtain();
        msg.setData(data);
        logHelper.d("serviceMessenger: " + serviceMessenger);
        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            logHelper.e("sendMessageToService", e);
        }
    }
}
