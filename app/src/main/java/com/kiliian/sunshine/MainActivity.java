package com.kiliian.sunshine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

    private static final int REQUEST_LOCATION = 100;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else if (savedInstanceState == null) {
            forecastPresenter.allowSyncWeather();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION && grantResults.length == 1) {
            forecastPresenter.allowSyncWeather();
        }
    }
}
