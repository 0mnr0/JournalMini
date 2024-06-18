package com.svl.journalmini;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {


    public static void set(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();  // or editor.commit(); but apply() is asynchronous and recommended
    }

    public static String get(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
}

