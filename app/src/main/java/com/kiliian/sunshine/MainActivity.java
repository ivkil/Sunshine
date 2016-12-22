package com.kiliian.sunshine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.presenters.ForecastPresenter;
import com.kiliian.sunshine.mvp.views.ForecastView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MvpAppCompatActivity implements ForecastView {

    @InjectPresenter
    ForecastPresenter forecastPresenter;

    @BindView(R.id.recycler_view_forecast)
    RecyclerView recyclerView;

    private ForecastSimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ForecastSimpleAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showForecast(List<Weather> weatherList) {
        adapter.setData(weatherList);
    }
}
