package com.kiliian.sunshine.mvp.models;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.util.List;

public class WeatherData {

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

    public void setDate(long date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return Instant.ofEpochSecond(date).atZone(ZoneId.of("UTC")).toLocalDate();
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public int getWeatherId() {
        return weatherId == null || weatherId.isEmpty() ? -1 : weatherId.get(0).getId();
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
