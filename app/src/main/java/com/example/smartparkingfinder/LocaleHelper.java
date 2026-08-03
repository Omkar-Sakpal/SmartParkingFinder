package com.example.smartparkingfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {

    private static final String PREF_NAME =
            "language_pref";

    private static final String KEY_LANGUAGE =
            "language";

    // Set locale

    public static void setLocale(
            Context context,
            String languageCode) {

        saveLanguage(
                context,
                languageCode
        );

        Locale locale =
                new Locale(languageCode);

        Locale.setDefault(locale);

        Configuration configuration =
                new Configuration();

        configuration.setLocale(locale);

        context.getResources()

                .updateConfiguration(
                        configuration,
                        context.getResources()
                                .getDisplayMetrics()
                );
    }

    // Save language

    private static void saveLanguage(
            Context context,
            String language) {

        SharedPreferences preferences =

                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        preferences.edit()

                .putString(
                        KEY_LANGUAGE,
                        language
                )

                .apply();
    }

    // Get saved language

    public static String getSavedLanguage(
            Context context) {

        SharedPreferences preferences =

                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        return preferences.getString(
                KEY_LANGUAGE,
                "en"
        );
    }
}