package com.example.smartparkingfinder;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // SharedPreferences

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    // Preference name

    private static final String PREF_NAME =
            "SmartParkingSession";

    // Login key

    private static final String IS_LOGIN =
            "isLogin";

    // Constructor

    public SessionManager(Context context) {

        sharedPreferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        editor =
                sharedPreferences.edit();
    }

    // Save login session

    public void setLogin(boolean isLogin) {

        editor.putBoolean(
                IS_LOGIN,
                isLogin
        );

        editor.apply();
    }

    // Check login session

    public boolean isLoggedIn() {

        return sharedPreferences.getBoolean(
                IS_LOGIN,
                false
        );
    }

    // Logout session

    public void logout() {

        editor.clear();

        editor.apply();
    }
}