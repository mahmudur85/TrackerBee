package com.trackertraced.trackerbee.application.utils.httpRequest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mahmudur on 12/31/2014.
 */
public class HTTPGetAsync extends AsyncTask<String, Void, String> {
    private static final String TAG = "HTTPGetAsync";

    private String mULR;
    private HTTPParams mParams = null;
    private boolean mSecured;

    HTTPResponseCode.ResponseCode responseCode;

    public HTTPResponseAsync delegate = null;

    public HTTPGetAsync(String url, HTTPParams params, boolean secured){
        this.mULR = url;
        Log.d(TAG,"mULR: " + this.mULR);
        this.mParams = params;
        this.mSecured = secured;
    }

    private String HTTPGet(String url, HTTPParams params, boolean secured){
        String responseBody = null;
        HttpClient client = HTTPClient.httpClient(secured);
        try{
            if(params!=null){
                if(params.nameValuePairs != null) {
                    String paramString = URLEncodedUtils.format(params.nameValuePairs, HTTP.UTF_8);
                    url = url + "?" + paramString;
                }else if(params.string != null){
                    url = url + "?" + params.string;
                }
            }
            HttpGet httpGet = new HttpGet(url);
            Log.d(TAG,"HTTPGet(): " + url);
            httpGet.addHeader(HTTPApiCredential.API_KEY_HEADER,HTTPApiCredential.API_KEY_VALUE);
            httpGet.setHeader(HTTPApiCredential.AUTHORIZATION_HEADER, HTTPApiCredential.AUTHORIZATION_VALUE);
            HttpResponse response = client.execute(httpGet);
            responseCode = HTTPResponseCode.getResponseCode(response.getStatusLine().getStatusCode());
            Log.d(TAG,"HTTPGet() statusCode: " + responseCode);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            responseBody = builder.toString();
            Log.d(TAG, "HTTPGet() response: " + responseBody);
        }catch(ClientProtocolException e){
            Log.e(TAG, "HTTPGet(): ClientProtocolException", e);
        } catch (IOException e){
            Log.e(TAG, "HTTPGet(): IOException", e);
        }
        return responseBody;
    }

    @Override
    protected String doInBackground(String... params) {
        if(mULR!=null){
            return HTTPGet(mULR,mParams,mSecured);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute(): " + result);
        if(delegate != null){
            delegate.HTTPResponse(result,responseCode);
        }
    }
}
