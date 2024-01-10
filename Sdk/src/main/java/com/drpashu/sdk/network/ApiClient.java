package com.drpashu.sdk.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    private static final String BASE_URL = "https://mlbrains.com/api/";
//    private static final String BASE_URL = "https://api.drpashu.com/api/";
//    public static final String BASE_URL_MEDIA = "https://api.drpashu.com";

//    public static final String BASE_URL_MEDIA = "https://mlbrains.com";
    private static final String BASE_URL = "https://api.lmschamp.com/api/";
    public static final String BASE_URL_MEDIA = "https://api.lmschamp.com";
    public static final String BASE_URL_CALL = "https://call-web.api.lmschamp.com/?";
//    public static final String BASE_URL_CALL = "https://call-web.api.drpashu.com/?";
    private static Retrofit retrofit, sdkRetrofit;

    public static Retrofit getRetrofitInstance(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getSdkRetrofitInstance(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        if (sdkRetrofit == null) {
            sdkRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_MEDIA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return sdkRetrofit;
    }
}