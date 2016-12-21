package com.kiliian.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiliian.sunshine.mvp.models.WeatherData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ForecastSimpleAdapter extends RecyclerView.Adapter<ForecastSimpleAdapter.ForecastSimpleAdapterViewHolder> {

    private List<WeatherData> weatherDataList;

    public ForecastSimpleAdapter() {
        weatherDataList = new ArrayList<>();
    }

    @Override
    public ForecastSimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastSimpleAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastSimpleAdapterViewHolder holder, int position) {
        WeatherData weatherData = weatherDataList.get(position);
        String dateStr = new Date(weatherData.getDate() * 1000L).toString();
        String weatherDataSrt = String.format(Locale.getDefault(), "%1$s - %2$s - %3$s",
                dateStr, weatherData.getTemperature().getMax(), weatherData.getTemperature().getMin());
        holder.textView.setText(weatherDataSrt);
    }

    public void setData(List<WeatherData> newWeatherDataList) {
        weatherDataList.clear();
        if (newWeatherDataList != null) {
            weatherDataList.addAll(newWeatherDataList);
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return null == weatherDataList ? 0 : weatherDataList.size();
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
