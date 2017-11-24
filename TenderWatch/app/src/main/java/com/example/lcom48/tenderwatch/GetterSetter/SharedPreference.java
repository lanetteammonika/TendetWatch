package com.example.lcom48.tenderwatch.GetterSetter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * Created by lcom48 on 24/11/17.
 */

public class SharedPreference {
    public static void setPreferences(Context context, String key, String value) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    /**
     * This method is used to get shared object
     * @param context Application context
     * @param key shared object key
     * @return return value, for default "" asign.
     */
    public static String getPreferences(Context context, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String json = appSharedPrefs.getString(key, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return json;
    }
}
