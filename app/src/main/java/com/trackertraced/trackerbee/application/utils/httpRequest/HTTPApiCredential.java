package com.trackertraced.trackerbee.application.utils.httpRequest;

import android.util.Base64;

/**
 * Created by Mahmudur on 1/21/2015.
 */
public class HTTPApiCredential {

    /**
     * Include API KEY with HTTP Request header
     */
    public static final String API_KEY_HEADER = "X2PHONE-API-KEY";
    public static final String API_KEY_VALUE = "c19a088b648b32e0baac258d0415526a";

    /**
     * For Apply HTTP Basic Authentication
     */
    public static final String USER_NAME = "user";
    public static final String PASSWORD = "x2phone!";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_VALUE = "Basic " + Base64.encodeToString(
            (HTTPApiCredential.USER_NAME + ":" + HTTPApiCredential.PASSWORD).getBytes(),
            Base64.NO_WRAP);

}
