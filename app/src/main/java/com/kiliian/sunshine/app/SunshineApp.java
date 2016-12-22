package com.kiliian.sunshine.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.kiliian.sunshine.di.AppComponent;
import com.kiliian.sunshine.di.DaggerAppComponent;
import com.kiliian.sunshine.di.modules.AppModule;

public class SunshineApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        AndroidThreeTen.init(this);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }


    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        SunshineApp.appComponent = appComponent;
    }
}
