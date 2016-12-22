package com.kiliian.sunshine.sync;


import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.kiliian.sunshine.app.SunshineApp;

import javax.inject.Inject;


public class SunshineFirebaseJobService extends JobService {

    @Inject
    SunshineSyncTask syncTask;

    @Override
    public void onCreate() {
        super.onCreate();
        SunshineApp.getAppComponent().inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        syncTask.syncWeather();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
