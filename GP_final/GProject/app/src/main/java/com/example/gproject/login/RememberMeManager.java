package com.example.gproject.login;

import android.content.Context;
import android.content.SharedPreferences;

public class RememberMeManager {
    private static final String PREF_NAME = "RememberMePrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    public static void saveCredentials(Context context, String username, String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static String[] getSavedCredentials(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, "");
        String password = prefs.getString(KEY_PASSWORD, "");
        return new String[]{username, password};
    }

    public static boolean hasSavedCredentials(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_USERNAME) && prefs.contains(KEY_PASSWORD);
    }

    public static void clearSavedCredentials(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }
}
