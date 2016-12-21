package com.kiliian.sunshine.di;


import com.kiliian.sunshine.di.modules.NetworkModule;
import com.kiliian.sunshine.mvp.presenters.ForecastPresenter;
import com.kiliian.sunshine.network.WeatherService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {

    WeatherService getWeatherService();

    void inject(ForecastPresenter forecastPresenter);
}
