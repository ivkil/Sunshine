package com.kiliian.sunshine.mvp.views;


import com.arellomobile.mvp.MvpView;
import com.kiliian.sunshine.data.Weather;

import java.util.List;

public interface ForecastView extends MvpView {

    void showForecast(List<Weather> weatherList);
}
