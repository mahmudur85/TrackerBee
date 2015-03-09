package com.trackertraced.trackerbee.application.utils.httpRequest;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;

/**
 * Created by Mahmudur on 12/31/2014.
 */
public class HTTPClient {
    private static final long CONN_MGR_TIMEOUT = 10000;
    private static final int CONN_TIMEOUT = 50000;
    private static final int SO_TIMEOUT = 50000;

    public static HttpClient httpClient(boolean secured) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, true);
            ConnManagerParams.setTimeout(params, CONN_MGR_TIMEOUT);
            HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);

            SchemeRegistry registry = new SchemeRegistry();

            if(secured) {
                registry.register(new Scheme("https", sf, 443));
            }else{
                registry.register(new Scheme("http", PlainSocketFactory
                        .getSocketFactory(), 80));
            }

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
