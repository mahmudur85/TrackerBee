package com.trackertraced.trackerbee.application.utils.httpRequest;

/**
 * Created by Mahmudur on 1/12/2015.
 */
public class HTTPResponseCode {
    private static final int NONE = 0;
    private static final int SUCCESS_OK = 200;
    private static final int SUCCESS_CREATED = 201;

    private static final int ERROR_BAD_REQUEST = 400;
    private static final int ERROR_UNAUTHORIZED = 401;
    private static final int ERROR_FORBIDDEN = 403;
    private static final int ERROR_NOT_FOUND = 404;
    private static final int ERROR_NOT_ACCEPTABLE = 406;
    private static final int ERROR_UNPROCESSABLE_ENTITY = 422;

    private static final int ERROR_INTERNAL_SERVER = 500;
    private static final int ERROR_SERVICE_UNAVAILABLE = 503;

    public static enum ResponseCode{
        NONE(0),
        SUCCESS_OK(200),
        SUCCESS_CREATED(201),
        ERROR_BAD_REQUEST(400),
        ERROR_UNAUTHORIZED(401),
        ERROR_FORBIDDEN(403),
        ERROR_NOT_FOUND(404),
        ERROR_NOT_ACCEPTABLE(406),
        ERROR_UNPROCESSABLE_ENTITY(422),
        ERROR_INTERNAL_SERVER(500),
        ERROR_SERVICE_UNAVAILABLE(503);

        private int code;

        ResponseCode(int code){
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    public static ResponseCode getResponseCode(int code){
        ResponseCode responseCode = ResponseCode.NONE;
        switch(code){
            case HTTPResponseCode.SUCCESS_OK:
                responseCode = ResponseCode.SUCCESS_OK;
                break;
            case HTTPResponseCode.SUCCESS_CREATED:
                responseCode = ResponseCode.SUCCESS_CREATED;
                break;
            case HTTPResponseCode.ERROR_BAD_REQUEST:
                responseCode = ResponseCode.ERROR_BAD_REQUEST;
                break;
            case HTTPResponseCode.ERROR_FORBIDDEN:
                responseCode = ResponseCode.ERROR_FORBIDDEN;
                break;
            case HTTPResponseCode.ERROR_INTERNAL_SERVER:
                responseCode = ResponseCode.ERROR_INTERNAL_SERVER;
                break;
            case HTTPResponseCode.ERROR_NOT_ACCEPTABLE:
                responseCode = ResponseCode.ERROR_NOT_ACCEPTABLE;
                break;
            case HTTPResponseCode.ERROR_NOT_FOUND:
                responseCode = ResponseCode.ERROR_NOT_FOUND;
                break;
            case HTTPResponseCode.ERROR_SERVICE_UNAVAILABLE:
                responseCode = ResponseCode.ERROR_SERVICE_UNAVAILABLE;
                break;
            case HTTPResponseCode.ERROR_UNAUTHORIZED:
                responseCode = ResponseCode.ERROR_UNAUTHORIZED;
                break;
            case HTTPResponseCode.ERROR_UNPROCESSABLE_ENTITY:
                responseCode = ResponseCode.ERROR_UNPROCESSABLE_ENTITY;
                break;
        }

        return responseCode;
    }
}
