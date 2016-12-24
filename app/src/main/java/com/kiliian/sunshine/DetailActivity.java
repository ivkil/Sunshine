package com.kiliian.sunshine;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.presenters.DetailPresenter;
import com.kiliian.sunshine.mvp.views.DetailView;
import com.kiliian.sunshine.utilities.SunshineDateUtils;
import com.kiliian.sunshine.utilities.SunshineWeatherUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kiliian.sunshine.R.id.pressure;
import static com.kiliian.sunshine.R.id.pressure_label;

public class DetailActivity extends MvpAppCompatActivity implements DetailView {

    public static final String ARG_DATE = "weather-date";

    @InjectPresenter
    DetailPresenter detailPresenter;

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

    @BindView(R.id.humidity_label)
    TextView humidityLabelView;

    @BindView(R.id.humidity)
    TextView humidityView;

    @BindView(R.id.wind_label)
    TextView windLabelView;

    @BindView(R.id.wind_measurement)
    TextView windMeasurementView;

    @BindView(pressure_label)
    TextView pressureLabelView;

    @BindView(pressure)
    TextView pressureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        String dateStr = getIntent().getStringExtra(ARG_DATE);
        detailPresenter.setDate(dateStr);
    }

    @Override
    public void showWeather(Weather weather) {
        int weatherId = (int) weather.weather_id();

        //Weather icon
        int weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);
        iconView.setImageResource(weatherImageId);

        //Weather date
        String dateText = SunshineDateUtils.getFriendlyDateString(this, weather.date(), true);
        dateView.setText(dateText);

        //Weather description
        String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        String descriptionA11y = getString(R.string.a11y_forecast, description);
        descriptionView.setText(description);
        descriptionView.setContentDescription(descriptionA11y);
        iconView.setContentDescription(descriptionA11y);

        //Temperature
        double highInCelsius = weather.max();
        String highString = SunshineWeatherUtils.formatTemperature(this, highInCelsius);
        String highA11y = getString(R.string.a11y_high_temp, highString);
        highTempView.setText(highString);
        highTempView.setContentDescription(highA11y);

        double lowInCelsius = weather.min();
        String lowString = SunshineWeatherUtils.formatTemperature(this, lowInCelsius);
        String lowA11y = getString(R.string.a11y_low_temp, lowString);
        lowTempView.setText(lowString);
        lowTempView.setContentDescription(lowA11y);

        //Humidity
        String humidityString = getString(R.string.format_humidity, weather.humidity());
        String humidityA11y = getString(R.string.a11y_humidity, humidityString);
        humidityView.setText(humidityString);
        humidityView.setContentDescription(humidityA11y);
        humidityLabelView.setContentDescription(humidityA11y);

        //Wind
        String windString = SunshineWeatherUtils.getFormattedWind(this, weather.wind(), weather.degrees());
        String windA11y = getString(R.string.a11y_wind, windString);
        windMeasurementView.setText(windString);
        windMeasurementView.setContentDescription(windA11y);
        windLabelView.setContentDescription(windA11y);

        //Pressure
        String pressureString = getString(R.string.format_pressure, weather.pressure());
        String pressureA11y = getString(R.string.a11y_pressure, pressureString);
        pressureView.setText(pressureString);
        pressureView.setContentDescription(pressureA11y);
        pressureLabelView.setContentDescription(pressureA11y);
    }
}
