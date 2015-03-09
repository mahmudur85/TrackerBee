package com.trackertraced.trackerbee.application.utils.httpRequest;

/**
 * Created by Mahmudur on 12/31/2014.
 */
public class HTTPURLBuilder {
    private static final String HTTP_API_LOCAL_PATH = "http://192.168.30.248/x2phone_rest";
    public static final String HTTP_BASE_PATH = "http://104.238.118.37:8000/";
    public static final String API = "api";
    public static final String USER = "user";
    public static final String REGISTER = "register";
    public static final String ACTIVATE_SESSION = "activate_session";
    public static final String LOG_INSTANCE = "logInstance";
    public static final String GET_INSTANCE = "getInstance";

    public static String getHTTPUlr(String[] paths) {
        String url = HTTP_BASE_PATH;
        for (String path : paths) {
            url += "/" + path;
        }
        return url;
    }
}
