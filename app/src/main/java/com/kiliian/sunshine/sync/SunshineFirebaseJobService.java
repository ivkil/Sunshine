package com.kiliian.sunshine.sync;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


public class SunshineFirebaseJobService extends JobService {

    public static final String JOB_FINISHED_ACTION = "com.kiliian.sunshine.SYNC_JOB_FINISHED";

    private JobParameters job;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                jobFinished(job, false);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(JOB_FINISHED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        this.job = job;
        Intent intent = new Intent(this, SunshineSyncService.class);
        startService(intent);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }
}
