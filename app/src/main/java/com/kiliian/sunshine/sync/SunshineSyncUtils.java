package com.kiliian.sunshine.sync;


import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SunshineSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    static final String SUNSHINE_SYNC_IMMEDIATELY_TAG = "sunshine-sync-immediately";
    private static final String SUNSHINE_SYNC_TAG = "sunshine-sync";

    private FirebaseJobDispatcher dispatcher;
    private SunshineSyncTask syncTask;

    private boolean initialized;

    @Inject
    public SunshineSyncUtils(FirebaseJobDispatcher dispatcher, SunshineSyncTask syncTask) {
        this.dispatcher = dispatcher;
        this.syncTask = syncTask;
    }

    synchronized public void initialize() {
        if (initialized) return;
        initialized = true;
        syncTask.syncWeather();
        scheduleFirebaseJobDispatcherSync();
    }

    private void scheduleFirebaseJobDispatcherSync() {
        Job syncSunshineJob = dispatcher.newJobBuilder()
                .setService(SunshineFirebaseJobService.class)
                .setTag(SUNSHINE_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.mustSchedule(syncSunshineJob);
    }

}
