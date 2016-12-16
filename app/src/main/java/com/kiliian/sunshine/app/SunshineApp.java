package com.kiliian.sunshine.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.kiliian.sunshine.di.AppComponent;
import com.kiliian.sunshine.di.DaggerAppComponent;

public class SunshineApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .build();

    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }


    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        SunshineApp.appComponent = appComponent;
    }
}
