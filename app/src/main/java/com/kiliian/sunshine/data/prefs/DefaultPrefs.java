package com.kiliian.sunshine.data.prefs;

import de.devland.esperandro.annotations.Default;
import de.devland.esperandro.annotations.SharedPreferences;


@SharedPreferences
public interface DefaultPrefs {

    @Default(ofLong = 0L)
    long lastNotificationTime();

    void lastNotificationTime(long timeMillis);

}
