package com.kiliian.sunshine.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.format.DateFormat;

import com.kiliian.sunshine.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class SunshineDateUtils {

    private static final String DATE_FORMAT_FULL = "EEEE, MMMM dd";
    private static final String DATE_FORMAT_SHORT = "EEE, MMM dd";

    public static String getFriendlyDateString(Context context, LocalDate date, boolean showFullDate) {
        LocalDate today = LocalDate.now();
        if (date.isEqual(today) || showFullDate) {
            String dayName = getDayName(context, date);
            String readableDate = getReadableDateString(context, date, DATE_FORMAT_FULL);
            if (!date.isAfter(today.plusDays(1))) {
                String pattern = DateFormat.getBestDateTimePattern(getLocale(context), "EEEE");
                String localizedDayName = date.format(DateTimeFormatter.ofPattern(pattern, getLocale(context)));
                return readableDate.replace(localizedDayName, dayName);
            } else {
                return readableDate;
            }
        } else if (!date.isAfter(today.plusWeeks(1))) {
            return getDayName(context, date);
        } else {
            return getReadableDateString(context, date, DATE_FORMAT_SHORT);
        }
    }

    public static String getFriendlyDateStringWithLocality(Context context, LocalDate date, String locality) {
        if (locality.isEmpty()) {
            locality = context.getString(R.string.default_locality);
        }
        return locality + " / " + getFriendlyDateString(context, date, false);
    }

    private static String getReadableDateString(Context context, LocalDate date, String format) {
        Locale locale = getLocale(context);
        String pattern = DateFormat.getBestDateTimePattern(locale, format);
        return date.format(DateTimeFormatter.ofPattern(pattern, locale));
    }

    private static String getDayName(Context context, LocalDate date) {
        LocalDate today = LocalDate.now();
        if (date.isEqual(today)) {
            return context.getString(R.string.today);
        }
        if (date.isEqual(today.plusDays(1))) {
            return context.getString(R.string.tomorrow);
        }
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, getLocale(context));
    }

    public static Locale getLocale(Context context) {
        final Set<String> AVAILABLE_TRANSLATIONS = new HashSet<>(Arrays.asList(
                new String[]{"uk"}
        ));
        Resources resources = context.getResources();
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = resources.getConfiguration().getLocales().get(0);
        } else {
            locale = resources.getConfiguration().locale;
        }
        if (!AVAILABLE_TRANSLATIONS.contains(locale.getLanguage())) {
            locale = Locale.ENGLISH;
        }
        return locale;
    }
}
