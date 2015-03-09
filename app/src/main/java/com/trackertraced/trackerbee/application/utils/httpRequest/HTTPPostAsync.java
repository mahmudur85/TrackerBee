package com.trackertraced.trackerbee.application.utils.httpRequest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by Mahmudur on 12/31/2014.
 */
public class HTTPPostAsync extends AsyncTask<String, Void, String> {
    private static final String TAG = "HTTPPostAsync";

    private String mULR;
    private HTTPParams mParams = null;
    private boolean mSecured;

    HTTPResponseCode.ResponseCode responseCode;

    public HTTPResponseAsync delegate = null;

    public HTTPPostAsync(String url,HTTPParams params, boolean secured){
        this.mULR = url;
        this.mParams = params;
        this.mSecured = secured;
    }

    private String HTTPPost(String url, HTTPParams params, boolean secured){
        Log.d(TAG,"HTTPPost url: " + url);
        String responseBody = null;
        byte[] responseArray = null;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = HTTPClient.httpClient(secured);
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader(HTTPApiCredential.API_KEY_HEADER,HTTPApiCredential.API_KEY_VALUE);
            httppost.setHeader(HTTPApiCredential.AUTHORIZATION_HEADER, HTTPApiCredential.AUTHORIZATION_VALUE);
            // Add your data
            if (params != null) {
                if(params.nameValuePairs != null) {
                    httppost.setEntity(new UrlEncodedFormEntity(params.nameValuePairs,
                            HTTP.UTF_8));
                    Log.d(TAG, "HTTPPost params: " + params.nameValuePairs.toString());
                }
                if(params.string != null){
                    StringEntity entity = new StringEntity(params.string, HTTP.UTF_8);
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    httppost.setEntity(entity);
                    Log.d(TAG, "HTTPPost params: " + params.string);
                }
            }
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            responseCode = HTTPResponseCode.getResponseCode(response.getStatusLine().getStatusCode());
            Log.d(TAG,"HTTPPost statusCode: " + responseCode);
            responseArray = EntityUtils.toByteArray(response.getEntity());
            Log.d(TAG, "HTTPPost response Array: " + responseArray);
            ByteBuffer byteBuffer = ByteBuffer.wrap(responseArray);
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder charsetDecoder = charset.newDecoder();
            CharBuffer charBuffer = charsetDecoder.decode(byteBuffer);
            responseBody = charBuffer.toString();
            Log.d(TAG, "HTTPPost response: " + responseBody);
        } catch (ClientProtocolException e) {
            Log.e(TAG,"HTTPPost(): ClientProtocolException",e);
        } catch (IOException e) {
            Log.e(TAG, "HTTPPost(): IOException", e);
        }
        return responseBody;
    }

    @Override
    protected String doInBackground(String... params) {
        if(mULR!=null){
            return HTTPPost(mULR,mParams,mSecured);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute(): " + result);
        if(delegate!=null){
            delegate.HTTPResponse(result,responseCode);
        }
    }
}
