package com.tvip.surveylance;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsTrustManager implements TrustManager, X509TrustManager {

    private boolean isVerified;
    @Override
    public final void checkClientTrusted(final X509Certificate[] xcs, final String string)
            throws CertificateException {
    }

    @Override
    public final void checkServerTrusted(final X509Certificate[] xcs, final String string)
            throws CertificateException {
    }

    @Override
    public final X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    /**
     * Gets an {@link SSLContext} which trusts all certificates.
     * @return {@link SSLContext}
     */
    public static SSLContext getSSLContext() {
        final TrustManager[] trustAllCerts =
                new TrustManager[] {new HttpsTrustManager()};

        try {
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());
            return context;

        } catch (Exception exc) {
            Log.e("CertHostTruster", "Unable to initialize the Trust Manager to trust all the "
                    + "SSL certificates and HTTPS hosts.", exc);
            return null;
        }
    }

    /**
     * Creates an hostname verifier which accepts all hosts.
     * @return {@link HostnameVerifier}
     */
    public static HostnameVerifier getAllHostnamesVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    @SuppressLint("TrulyRandom")
    public static void allowAllSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(final String host, final SSLSession session) {
                    if (host.equalsIgnoreCase("hrd.tvip.co.id") ||
                            (host.equalsIgnoreCase("assessment.tvip.co.id") ||
                             host.contains("google") || host.contains("gstatic"))){
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } catch (Exception ignored) {

        }

    }
}