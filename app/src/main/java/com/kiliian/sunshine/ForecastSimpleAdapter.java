package com.kiliian.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiliian.sunshine.data.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ForecastSimpleAdapter extends RecyclerView.Adapter<ForecastSimpleAdapter.ForecastSimpleAdapterViewHolder> {

    private List<Weather> weatherList;

    public ForecastSimpleAdapter() {
        weatherList = new ArrayList<>();
    }

    @Override
    public ForecastSimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastSimpleAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastSimpleAdapterViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        String weatherDataSrt = String.format(Locale.getDefault(), "%1$s - %2$s - %3$s",
                weather.date().toString(), weather.max(), weather.min());
        holder.textView.setText(weatherDataSrt);
    }

    public void setData(List<Weather> newWeatherList) {
        weatherList.clear();
        if (newWeatherList != null) {
            weatherList.addAll(newWeatherList);
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return null == weatherList ? 0 : weatherList.size();
    }

    class ForecastSimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_weather_data)
        TextView textView;

        ForecastSimpleAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
