package com.kiliian.sunshine.mvp.views;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.kiliian.sunshine.data.Weather;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ForecastView extends MvpView {

    void showForecast(List<Weather> weatherList);
}
