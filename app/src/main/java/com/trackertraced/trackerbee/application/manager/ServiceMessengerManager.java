package com.trackertraced.trackerbee.application.manager;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Messenger;

/**
 * Created by Mahmudur on 2/25/2015.
 */
public interface ServiceMessengerManager {
    public ServiceConnection getServiceConnection();

    public boolean isBound();

    public void sendMessageToService(Bundle data);
}
