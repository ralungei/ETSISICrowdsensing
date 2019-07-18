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

public class RetrofitInstance {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private Retrofit retrofit;
    // URL For emulator https://10.0.2.2:5001/api/
    private static final String BASE_URL = "https://10.0.2.2:5001/api/";
    //private static final String BASE_URL = "https://etsisicrowdsensingwebapi.azurewebsites.net/api/";
    private IncidencesService incidencesService;
    private SubjectsService subjectsService;
    private PlanesService planesService;
    private FeedbacksService feedbacksService;


    public RetrofitInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        incidencesService = retrofit.create(IncidencesService.class);
        subjectsService = retrofit.create(SubjectsService.class);
        feedbacksService = retrofit.create(FeedbacksService.class);
        planesService = retrofit.create(PlanesService.class);
    }

    public IncidencesService getIncidencesService() {
        return incidencesService;
    }

    public SubjectsService getSubjectsService() {
        return subjectsService;
    }

    public PlanesService getPlanesService() {
        return planesService;
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