package com.kiliian.sunshine.network;

import com.kiliian.sunshine.mvp.models.WeatherResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {

    @GET("/data/2.5/forecast/daily?cnt=14")
    Observable<WeatherResponse> getWeatherForecast(@Query("lat") double lat, @Query("lon") double lng);
}