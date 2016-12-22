package com.kiliian.sunshine.mvp.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.kiliian.sunshine.WeatherModel;
import com.kiliian.sunshine.app.SunshineApp;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.models.WeatherData;
import com.kiliian.sunshine.mvp.models.WeatherResponse;
import com.kiliian.sunshine.mvp.views.ForecastView;
import com.kiliian.sunshine.network.WeatherService;
import com.squareup.sqlbrite.BriteDatabase;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    @Inject
    WeatherService weatherService;

    @Inject
    BriteDatabase database;

    public ForecastPresenter() {
        SunshineApp.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getWeather();
        loadWeather();
    }

    private void getWeather() {
        database.delete(Weather.TABLE_NAME, null);
        Subscription subscription = weatherService.getWeatherForecast(50.4501, 30.5234)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<WeatherResponse, Observable<WeatherData>>() {
                    @Override
                    public Observable<WeatherData> call(WeatherResponse weatherResponse) {
                        return Observable.from(weatherResponse.getWeatherData());
                    }
                }).subscribe(weatherData -> {
                    Weather.Insert_row insertForecast =
                            new WeatherModel.Insert_row(
                                    database.getWritableDatabase(),
                                    Weather.FACTORY
                            );
                    insertForecast.bind(
                            Instant.ofEpochSecond(weatherData.getDate()).atZone(ZoneId.of("UTC")).toLocalDate(),
                            weatherData.getWeatherId().get(0).getId(),
                            weatherData.getTemperature().getMin(),
                            weatherData.getTemperature().getMax(),
                            weatherData.getPressure(),
                            weatherData.getHumidity(),
                            weatherData.getWindSpeed(),
                            weatherData.getWindDeg()
                    );
                    database.executeInsert(Weather.TABLE_NAME, insertForecast.program);
                }, Throwable::printStackTrace);
        unsubscribeOnDestroy(subscription);
    }

    private void loadWeather() {
        Subscription subscription = database.createQuery(Weather.TABLE_NAME, Weather.SELECT_ALL)
                .mapToList(Weather.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherList -> {
                    getViewState().showForecast(weatherList);
                }, Throwable::printStackTrace);
        unsubscribeOnDestroy(subscription);
    }
}
