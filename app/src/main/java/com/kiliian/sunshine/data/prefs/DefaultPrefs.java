package com.kiliian.sunshine.data.prefs;

import de.devland.esperandro.annotations.Default;
import de.devland.esperandro.annotations.SharedPreferences;


@SharedPreferences
public interface DefaultPrefs {

    @Default(ofLong = 0L)
    long lastNotificationTime();

    void lastNotificationTime(long timeMillis);

    @Default(ofLong = 4630463494191357593L)
    long latitudeRowBits();

    void latitudeRowBits(long latitudeRowBits);

    @Default(ofLong = -4585080716383482966L)
    long longitudeRowBits();

    void longitudeRowBits(long longitudeRowBits);

    String locality();

    void locality(String locality);

}
