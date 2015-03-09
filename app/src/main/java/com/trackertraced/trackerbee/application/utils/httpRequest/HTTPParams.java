package com.trackertraced.trackerbee.application.utils.httpRequest;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mahmudur on 1/12/2015.
 */
public class HTTPParams implements Serializable{
    public ArrayList<NameValuePair> nameValuePairs;
    public String string;

    public HTTPParams() {
        this.nameValuePairs = null;
        this.string = null;
    }

    public HTTPParams(ArrayList<NameValuePair> params) {
        this.nameValuePairs = params;
        this.string = null;
    }


    public HTTPParams(String params) {
        this.nameValuePairs = null;
        this.string = params;
    }

//    public HTTPParams(ArrayList<NameValuePair> nameValuePairs, String string) {
//        this.nameValuePairs = nameValuePairs;
//        this.string = string;
//    }
}
