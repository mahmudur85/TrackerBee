package com.trackertraced.trackerbee.application.utils.httpRequest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
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
 * Created by Mahmudur on 1/13/2015.
 */
public class HTTPPutAsync extends AsyncTask<String, Void, String> {
    private static final String TAG = "HTTPPutAsync";

    private String mULR;
    private HTTPParams mParams = null;
    private boolean mSecured;

    HTTPResponseCode.ResponseCode responseCode;

    public HTTPResponseAsync delegate = null;

    public HTTPPutAsync(String mULR, HTTPParams mParams, boolean mSecured) {
        this.mULR = mULR;
        this.mParams = mParams;
        this.mSecured = mSecured;
    }

    private String HTTPPut(String url, HTTPParams params, boolean secured){
        Log.d(TAG,"HTTPPut url: " + url);
        String responseBody = null;
        byte[] responseArray = null;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = HTTPClient.httpClient(secured);
        HttpPut httpPut = new HttpPut(url);
        try{
            httpPut.addHeader(HTTPApiCredential.API_KEY_HEADER, HTTPApiCredential.API_KEY_VALUE);
            httpPut.setHeader(HTTPApiCredential.AUTHORIZATION_HEADER, HTTPApiCredential.AUTHORIZATION_VALUE);
            if (params != null) {
                if (params.nameValuePairs != null) {
                    httpPut.setEntity(new UrlEncodedFormEntity(params.nameValuePairs,
                            HTTP.UTF_8));
//                    Log.d(TAG, "HTTPPut params: " + params.nameValuePairs.toString());
                }
                if (params.string != null) {
                    StringEntity entity = new StringEntity(params.string, HTTP.UTF_8);
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    httpPut.setEntity(entity);
//                    Log.d(TAG, "HTTPPut params: " + params.string);
                }
//                Log.d(TAG, "HTTPPut entity: " + httpPut.getEntity().toString());
            }
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpPut);
            responseCode = HTTPResponseCode.getResponseCode(response.getStatusLine().getStatusCode());
//            Log.d(TAG,"HTTPPut responseCode: " + responseCode);
            responseArray = EntityUtils.toByteArray(response.getEntity());
//            Log.d(TAG, "HTTPPut response Array: " + responseArray);
            ByteBuffer byteBuffer = ByteBuffer.wrap(responseArray);
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder charsetDecoder = charset.newDecoder();
            CharBuffer charBuffer = charsetDecoder.decode(byteBuffer);
            responseBody = charBuffer.toString();
//            Log.d(TAG, "HTTPPut response: " + responseBody);
        } catch (ClientProtocolException e) {
            Log.e(TAG,"HTTPPut(): ClientProtocolException",e);
        } catch (IOException e) {
            Log.e(TAG, "HTTPPut(): IOException", e);
        }
        return responseBody;
    }

    @Override
    protected String doInBackground(String... params) {
        if(mULR!=null){
            return HTTPPut(mULR,mParams,mSecured);
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
