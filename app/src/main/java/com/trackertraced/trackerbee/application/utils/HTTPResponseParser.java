package com.trackertraced.trackerbee.application.utils;

import android.location.Location;

import com.trackertraced.trackerbee.locationmodule.model.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HTTPResponseParser {
    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, HTTPResponseParser.class.getSimpleName(), false);

    private static final String KEY_RESULT = "result";
    private static final String VALUE_RESULT_ERROR = "Error";

    private String jsonString;
    private JSONArray jsonArray;

    public static enum RESPONSE_TYPE {
        NULL("null", 0),
        ERROR("error", 1),
        NOT_JSON("not_json", 2),
        SUCCESS("success", 3);

        private String string;
        private Integer integer;

        private RESPONSE_TYPE(String string, Integer integer) {
            this.string = string;
            this.integer = integer;
        }

        public String getString() {
            return string;
        }

        public Integer getInteger() {
            return integer;
        }
    }

    public HTTPResponseParser(String jsonString) {
        this.jsonString = jsonString;
    }

    public RESPONSE_TYPE getResponseType() {
        if (this.jsonString.isEmpty() || this.jsonString.length() == 0 || null == this.jsonString) {
            return RESPONSE_TYPE.NULL;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(this.jsonString);
                if (jsonObject.getString(KEY_RESULT).contains(VALUE_RESULT_ERROR)) {
                    return RESPONSE_TYPE.ERROR;
                } else {
                    this.jsonArray = jsonObject.getJSONArray(KEY_RESULT);
                    return RESPONSE_TYPE.SUCCESS;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return RESPONSE_TYPE.NOT_JSON;
            }
        }
    }

    /*
    {
        "result": [
            {
                "l": "23.757842",
                "n": "90.370057",
                "e": "-85.763104",
                "t": "2015-03-15 11:53:02"
            }
        ]
    }
    */
    public ArrayList<LocationModel> getLocationModels() {
        if (null != this.jsonArray) {
            ArrayList<LocationModel> locationModelsl = new ArrayList<>();
            for (int i = 0; i < this.jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = this.jsonArray.getJSONObject(i);
                    logHelper.d("jsonObject" + jsonObject);
                    Location location = new Location("");
                    location.setLatitude(
                            Double.parseDouble(
                                    jsonObject.getString("l")
                            )
                    );
                    location.setLongitude(
                            Double.parseDouble(
                                    jsonObject.getString("n")
                            )
                    );
                    location.setAltitude(
                            Double.parseDouble(
                                    jsonObject.getString("e")
                            )
                    );
                    String localTime = ApplicationHelper.convertServerTimeToLocalTime(
                            jsonObject.getString("t")
                    );
                    location.setTime(ApplicationHelper.getDateTimeToUnixTimestamp(localTime));
                    LocationModel locationModel = new LocationModel(
                            location,
                            ApplicationHelper.getDateTimeToUnixTimestamp(
                                    localTime
                            )
                    );
                    locationModelsl.add(locationModel);
                    logHelper.d("locationModel" + locationModel);
                } catch (JSONException e) {
                    logHelper.e("JSONException", e);
                } catch (Exception e) {
                    logHelper.e("Exception", e);
                }
            }
            return locationModelsl;
        } else {
            return null;
        }
    }
}
