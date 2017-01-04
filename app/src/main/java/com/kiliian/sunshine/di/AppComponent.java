package com.kiliian.sunshine.di;


import com.kiliian.sunshine.ForecastAdapter;
import com.kiliian.sunshine.di.modules.AppModule;
import com.kiliian.sunshine.di.modules.NetworkModule;
import com.kiliian.sunshine.mvp.presenters.DetailPresenter;
import com.kiliian.sunshine.mvp.presenters.ForecastPresenter;
import com.kiliian.sunshine.sync.SunshineSyncService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(ForecastPresenter forecastPresenter);

    void inject(DetailPresenter detailPresenter);

    void inject(SunshineSyncService sunshineSyncService);

    void inject(ForecastAdapter forecastAdapter);
}
