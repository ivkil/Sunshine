package com.kiliian.sunshine.di;


import com.kiliian.sunshine.di.modules.AppModule;
import com.kiliian.sunshine.di.modules.NetworkModule;
import com.kiliian.sunshine.mvp.presenters.ForecastPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(ForecastPresenter forecastPresenter);

}
