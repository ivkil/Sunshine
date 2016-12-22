package com.kiliian.sunshine.sync;

import com.kiliian.sunshine.WeatherModel;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.models.WeatherData;
import com.kiliian.sunshine.network.WeatherService;
import com.squareup.sqlbrite.BriteDatabase;

import org.threeten.bp.LocalDate;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;


@Singleton
public class SunshineSyncTask {

    private WeatherService weatherService;
    private BriteDatabase database;

    @Inject
    public SunshineSyncTask(WeatherService weatherService, BriteDatabase database) {
        this.weatherService = weatherService;
        this.database = database;
    }

    synchronized void syncWeather() {
        weatherService.getWeatherForecast(50.4501, 30.5234)
                .subscribeOn(Schedulers.io())
                .flatMap(weatherResponse -> Observable.just(weatherResponse.getWeatherData()))
                .filter(weatherData -> !weatherData.isEmpty())
                .flatMap(Observable::from)
                .doOnNext(this::insertForecast)
                .toList()
                .toBlocking()
                .subscribe(__ -> deleteOldForecast(), Throwable::printStackTrace);

    }

    private void insertForecast(WeatherData weatherData) {
        Weather.Insert_row insertForecast = new WeatherModel.Insert_row(
                database.getWritableDatabase(),
                Weather.FACTORY
        );
        insertForecast.bind(
                weatherData.getDate(),
                weatherData.getWeatherId(),
                weatherData.getTemperature().getMin(),
                weatherData.getTemperature().getMax(),
                weatherData.getPressure(),
                weatherData.getHumidity(),
                weatherData.getWindSpeed(),
                weatherData.getWindDeg()
        );
        database.executeInsert(Weather.TABLE_NAME, insertForecast.program);
    }

    private void deleteOldForecast() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        WeatherModel.Delete_old deleteOld = new WeatherModel.Delete_old(database.getWritableDatabase());
        deleteOld.bind(yesterday.toString());
        database.executeUpdateDelete(Weather.TABLE_NAME, deleteOld.program);
    }
}
