package com.kiliian.sunshine.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherResponse {

    @SerializedName("list")
    private List<WeatherData> weatherData;

    public List<WeatherData> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(List<WeatherData> weatherData) {
        this.weatherData = weatherData;
    }
}
