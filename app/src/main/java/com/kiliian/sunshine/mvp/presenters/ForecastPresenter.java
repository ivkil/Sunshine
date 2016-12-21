package com.kiliian.sunshine.mvp.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.kiliian.sunshine.app.SunshineApp;
import com.kiliian.sunshine.mvp.views.ForecastView;
import com.kiliian.sunshine.network.WeatherService;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    @Inject
    WeatherService weatherService;

    public ForecastPresenter() {
        SunshineApp.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadWeather();
    }

    private void loadWeather() {
        Subscription subscription = weatherService.getWeatherForecast(50.4501, 30.5234)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherResponse -> {
                    getViewState().showForecast(weatherResponse.getWeatherData());
                }, Throwable::printStackTrace);
        unsubscribeOnDestroy(subscription);
    }
}
