package com.kiliian.sunshine.network;

import com.google.gson.Gson;
import com.kiliian.sunshine.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RestClient {

    private Retrofit retrofit;

    @Inject
    public RestClient(OkHttpClient client, Gson gson) {
        OkHttpClient.Builder okHttpClient = client.newBuilder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(interceptor);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .client(okHttpClient.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public <T> T create(Class<T> apiInterfaceClass) {
        return retrofit.create(apiInterfaceClass);
    }
}