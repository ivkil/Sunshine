package com.kiliian.sunshine.utilities;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RxUtils {

    public static <T> Observable.Transformer<T, T> async2ui() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, T> scheduleIfConnectionProblems(FirebaseJobDispatcher dispatcher, Class<? extends JobService> serviceClass, String tag) {
        return observable -> observable
                .doOnSubscribe(() -> dispatcher.cancel(tag))
                .doOnError(throwable -> {
                    if (throwable instanceof IOException) {
                        Job syncSunshineJob = dispatcher.newJobBuilder()
                                .setService(serviceClass)
                                .setTag(tag)
                                .setReplaceCurrent(true)
                                .build();
                        dispatcher.mustSchedule(syncSunshineJob);
                    }
                });
    }
}
