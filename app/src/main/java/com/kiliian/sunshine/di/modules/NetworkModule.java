package com.kiliian.sunshine.di.modules;

import com.google.gson.Gson;
import com.kiliian.sunshine.network.QueryParamsInterceptor;
import com.kiliian.sunshine.network.RestClient;
import com.kiliian.sunshine.network.WeatherService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;


@Module
public class NetworkModule {

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(QueryParamsInterceptor queryParams) {
        return new OkHttpClient.Builder().addInterceptor(queryParams).build();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    static WeatherService provideWeatherService(RestClient restClient) {
        return restClient.create(WeatherService.class);
    }

}
