package com.application.presensitk.config;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private final SharedPreferences mSharedPreferences;
    public final static String PREFIX = "app";

    public SPHelper(Context mContext){
        mSharedPreferences = mContext.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
    }

    public void saveValue(String key, boolean value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public boolean readValue(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveValue(String key, int value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public int readValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void saveValue(String key, String value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String readValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public Boolean isAdmin(){
        return mSharedPreferences.getBoolean("admin", false);
    }
}
