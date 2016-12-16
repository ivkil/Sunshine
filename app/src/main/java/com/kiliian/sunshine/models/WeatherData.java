package com.kiliian.sunshine.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


class WeatherData {

    @SerializedName("dt")
    private long date;

    @SerializedName("temp")
    private Temperature temperature;

    @SerializedName("weather")
    private List<WeatherId> weatherId;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("humidity")
    private double humidity;

    @SerializedName("speed")
    private double windSpeed;

    @SerializedName("deg")
    private double windDeg;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public List<WeatherId> getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(List<WeatherId> weatherId) {
        this.weatherId = weatherId;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(double windDeg) {
        this.windDeg = windDeg;
    }
}
