package com.kiliian.sunshine.sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;

import com.kiliian.sunshine.DetailActivity;
import com.kiliian.sunshine.R;
import com.kiliian.sunshine.WeatherModel;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.data.prefs.DefaultPrefs;
import com.kiliian.sunshine.mvp.models.WeatherData;
import com.kiliian.sunshine.network.WeatherService;
import com.kiliian.sunshine.utilities.SunshineWeatherUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqldelight.SqlDelightStatement;

import org.threeten.bp.LocalDate;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;


@Singleton
public class SunshineSyncTask {

    private static final int WEATHER_NOTIFICATION_ID = 3004;

    private Context context;
    private WeatherService weatherService;
    private BriteDatabase database;
    private DefaultPrefs defaultPrefs;

    @Inject
    public SunshineSyncTask(Context context, WeatherService weatherService,
                            BriteDatabase database, DefaultPrefs defaultPrefs) {
        this.context = context;
        this.weatherService = weatherService;
        this.database = database;
        this.defaultPrefs = defaultPrefs;
    }

    synchronized void syncWeather() {
        weatherService.getWeatherForecast(50.4501, 30.5234)
                .subscribeOn(Schedulers.io())
                .flatMap(weatherResponse -> Observable.just(weatherResponse.getWeatherData()))
                .filter(weatherData -> !weatherData.isEmpty())
                .flatMap(Observable::from)
                .doOnNext(this::insertForecast)
                .toList()
                .toBlocking()
                .subscribe(__ -> deleteOldForecast(), Throwable::printStackTrace);

        long timeSinceLastNotification = System.currentTimeMillis() - defaultPrefs.lastNotificationTime();
        if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
            notifyUserOfNewWeather();
        }
    }

    private void insertForecast(WeatherData weatherData) {
        Weather.Insert_row insertForecast = new WeatherModel.Insert_row(
                database.getWritableDatabase(),
                Weather.FACTORY
        );
        insertForecast.bind(
                weatherData.getDate(),
                weatherData.getWeatherId(),
                weatherData.getTemperature().getMin(),
                weatherData.getTemperature().getMax(),
                weatherData.getPressure(),
                weatherData.getHumidity(),
                weatherData.getWindSpeed(),
                weatherData.getWindDeg()
        );
        database.executeInsert(Weather.TABLE_NAME, insertForecast.program);
    }

    private void deleteOldForecast() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        WeatherModel.Delete_old deleteOld = new WeatherModel.Delete_old(database.getWritableDatabase());
        deleteOld.bind(yesterday.toString());
        database.executeUpdateDelete(Weather.TABLE_NAME, deleteOld.program);
    }

    private void notifyUserOfNewWeather() {
        SqlDelightStatement query = Weather.FACTORY.select_weather(LocalDate.now());
        database.createQuery(Weather.TABLE_NAME, query.statement, query.args)
                .mapToOne(Weather.MAPPER)
                .subscribeOn(Schedulers.io())
                .take(1)
                .subscribe(weather -> {
                    showNotification(weather);
                    defaultPrefs.lastNotificationTime(System.currentTimeMillis());
                }, Throwable::printStackTrace);
    }

    private void showNotification(Weather weather) {
        int weatherId = (int) weather.weather_id();

        Resources resources = context.getResources();
        int largeArtResourceId = SunshineWeatherUtils
                .getLargeArtResourceIdForWeatherCondition(weatherId);

        Bitmap largeIcon = BitmapFactory.decodeResource(
                resources,
                largeArtResourceId);

        String notificationTitle = context.getString(R.string.app_name);

        String notificationText = getNotificationText(weatherId, weather.max(), weather.min());

        int smallArtResourceId = SunshineWeatherUtils
                .getSmallArtResourceIdForWeatherCondition(weatherId);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(smallArtResourceId)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setAutoCancel(true);

        Intent detailIntentForToday = new Intent(context, DetailActivity.class);
        detailIntentForToday.putExtra(DetailActivity.ARG_DATE, weather.date().toString());

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
        PendingIntent resultPendingIntent = taskStackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());
    }

    private String getNotificationText(int weatherId, double high, double low) {
        String shortDescription = SunshineWeatherUtils
                .getStringForWeatherCondition(context, weatherId);
        String notificationFormat = context.getString(R.string.format_notification);
        return String.format(notificationFormat,
                shortDescription,
                SunshineWeatherUtils.formatTemperature(context, high),
                SunshineWeatherUtils.formatTemperature(context, low));
    }
}
