package com.kiliian.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.utilities.SunshineDateUtils;
import com.kiliian.sunshine.utilities.SunshineWeatherUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private boolean useTodayLayout;
    private Context context;

    private List<Weather> weatherList;

    public ForecastAdapter(Context context) {
        this.context = context;
        weatherList = new ArrayList<>();
        useTodayLayout = context.getResources().getBoolean(R.bool.use_today_layout);
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.forecast_list_item;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        view.setClickable(true);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        int weatherId = (int) weather.weather_id();

        // Weather icon
        int weatherImageId;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                weatherImageId = SunshineWeatherUtils
                        .getLargeArtResourceIdForWeatherCondition(weatherId);
                break;
            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = SunshineWeatherUtils
                        .getSmallArtResourceIdForWeatherCondition(weatherId);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        holder.iconView.setImageResource(weatherImageId);

        //Date
        String dateStr = SunshineDateUtils.getFriendlyDateString(context, weather.date(), false);
        holder.dateView.setText(dateStr);

        //Weather description
        String description = SunshineWeatherUtils.getStringForWeatherCondition(context, weatherId);
        String descriptionA11y = context.getString(R.string.a11y_forecast, description);
        holder.descriptionView.setText(description);
        holder.descriptionView.setContentDescription(descriptionA11y);

        //Temperature
        String highString = SunshineWeatherUtils.formatTemperature(context, weather.max());
        String highA11y = context.getString(R.string.a11y_high_temp, highString);
        holder.highTempView.setText(highString);
        holder.highTempView.setContentDescription(highA11y);

        String lowString = SunshineWeatherUtils.formatTemperature(context, weather.min());
        String lowA11y = context.getString(R.string.a11y_low_temp, lowString);
        holder.lowTempView.setText(lowString);
        holder.lowTempView.setContentDescription(lowA11y);
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

    @Override
    public int getItemViewType(int position) {
        if (useTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.weather_icon)
        ImageView iconView;

        @BindView(R.id.date)
        TextView dateView;

        @BindView(R.id.weather_description)
        TextView descriptionView;

        @BindView(R.id.high_temperature)
        TextView highTempView;

        @BindView(R.id.low_temperature)
        TextView lowTempView;

        ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
