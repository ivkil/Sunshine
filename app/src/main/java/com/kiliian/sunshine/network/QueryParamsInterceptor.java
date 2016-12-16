package com.kiliian.sunshine.network;

import com.kiliian.sunshine.BuildConfig;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class QueryParamsInterceptor implements Interceptor {

    @Inject
    public QueryParamsInterceptor() {}

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl url = original.url().newBuilder()
                .addQueryParameter("APPID", BuildConfig.WEATHER_APP_ID)
                .build();
        Request request = original.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}