package com.kiliian.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.presenters.ForecastPresenter;
import com.kiliian.sunshine.mvp.views.ForecastView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MvpAppCompatActivity implements ForecastView, ForecastAdapter.ForecastAdapterOnClickHandler {

    @InjectPresenter
    ForecastPresenter forecastPresenter;

    @BindView(R.id.recyclerview_forecast)
    RecyclerView recyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar loadingIndicator;

    private ForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0f);
        }
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ForecastAdapter(this, this);
        recyclerView.setAdapter(adapter);
        showLoading();
    }

    private void showWeatherDataView() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void showForecast(List<Weather> weatherList) {
        adapter.setData(weatherList);
        showWeatherDataView();
    }

    @Override
    public void onClick(String dateStr) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ARG_DATE, dateStr);
        startActivity(intent);
    }
}
