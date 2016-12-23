package com.kiliian.sunshine.utilities;

import android.content.Context;
import android.text.format.DateFormat;

import com.kiliian.sunshine.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.util.Locale;


public class SunshineDateUtils {

    private static final String DATE_FORMAT_FULL = "EEEE, MMMM dd";
    private static final String DATE_FORMAT_SHORT = "EEE, MMM dd";

    public static String getFriendlyDateString(Context context, LocalDate date, boolean showFullDate) {
        LocalDate today = LocalDate.now();
        if (date.isEqual(today) || showFullDate) {
            String dayName = getDayName(context, date);
            String readableDate = getReadableDateString(date, DATE_FORMAT_FULL);
            if (!date.isAfter(today.plusDays(1))) {
                String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE");
                String localizedDayName = date.format(DateTimeFormatter.ofPattern(pattern));
                return readableDate.replace(localizedDayName, dayName);
            } else {
                return readableDate;
            }
        } else if (!date.isAfter(today.plusWeeks(1))) {
            return getDayName(context, date);
        } else {
            return getReadableDateString(date, DATE_FORMAT_SHORT);
        }
    }

    private static String getReadableDateString(LocalDate date, String format) {
        String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), format);
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    private static String getDayName(Context context, LocalDate date) {
        LocalDate today = LocalDate.now();
        if (date.isEqual(today)) {
            return context.getString(R.string.today);
        }
        if (date.isEqual(today.plusDays(1))) {
            return context.getString(R.string.tomorrow);
        }
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
    }
}
