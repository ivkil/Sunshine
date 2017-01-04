package com.kiliian.sunshine.mvp.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.kiliian.sunshine.app.SunshineApp;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.views.ForecastView;
import com.kiliian.sunshine.sync.SunshineSyncUtils;
import com.kiliian.sunshine.utilities.RxUtils;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import rx.Subscription;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    @Inject
    SunshineSyncUtils syncUtils;

    @Inject
    BriteDatabase database;

    public ForecastPresenter() {
        SunshineApp.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadWeather();
    }

    private void loadWeather() {
        Subscription subscription = database.createQuery(Weather.TABLE_NAME, Weather.SELECT_ALL)
                .mapToList(Weather.MAPPER)
                .compose(RxUtils.async2ui())
                .filter(weatherList -> !weatherList.isEmpty())
                .subscribe(weatherList -> getViewState().showForecast(weatherList),
                        Throwable::printStackTrace);
        unsubscribeOnDestroy(subscription);
    }

    public void allowSyncWeather() {
        syncUtils.syncImmediately();
    }
}
