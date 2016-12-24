package com.kiliian.sunshine.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.kiliian.sunshine.data.Weather;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface DetailView extends MvpView {

    void showWeather(Weather weather);

}
