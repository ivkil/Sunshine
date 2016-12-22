package com.kiliian.sunshine.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.kiliian.sunshine.WeatherModel;
import com.squareup.sqldelight.ColumnAdapter;

import org.threeten.bp.LocalDate;

import rx.functions.Func1;

@AutoValue
public abstract class Weather implements WeatherModel {

    private static final ColumnAdapter<LocalDate, String> DATE_STRING_COLUMN_ADAPTER = new ColumnAdapter<LocalDate, String>() {
        @NonNull
        @Override
        public LocalDate decode(String databaseValue) {
            return LocalDate.parse(databaseValue);
        }

        @Override
        public String encode(@NonNull LocalDate value) {
            return value.toString();
        }
    };

    public static final Factory<Weather> FACTORY = new Factory<>(AutoValue_Weather::new, DATE_STRING_COLUMN_ADAPTER);

    public static final Func1<Cursor, Weather> MAPPER = cursor -> new Mapper<>(FACTORY).map(cursor);

}