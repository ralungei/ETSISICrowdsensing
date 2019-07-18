package com.etsisi.dev.etsisicrowdsensing.web.api.network;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUPMInstance {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private Retrofit retrofit;
    // URL For emulator https://10.0.2.2:5001/api/
    private static final String BASE_URL = "https://etsisicrowdsensingwebapi.azurewebsites.net/api/";

    //private static final String BASE_URL = "https://etsisicrowdsensingwebapi.azurewebsites.net/api/";
    // ETSISI WEB API
    //private static final String BASE_URL = "http://138.100.58.3:5552/appfeed/v1/";

    private IncidencesService incidencesService;
    private FeedbacksService feedbacksService;


    public RetrofitUPMInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        incidencesService = retrofit.create(IncidencesService.class);
        feedbacksService = retrofit.create(FeedbacksService.class);
    }

    public IncidencesService getIncidencesService() {
        return incidencesService;
    }

    public FeedbacksService getFeedbacksService() {
        return feedbacksService;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}