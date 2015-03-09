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
    }

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public void sendMessageToService(Bundle data) {
        LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, ServiceMessengerManagerImpl.class.getSimpleName(), true);
        Message msg = Message.obtain();
        msg.setData(data);
        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            logHelper.e("sendMessageToService", e);
        }
    }
}
