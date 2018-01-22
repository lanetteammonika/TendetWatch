package com.tenderWatch.SharedPreference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tenderWatch.Login;
import com.tenderWatch.Models.User;

import java.io.File;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by lcom48 on 24/11/17.
 */

public class SharedPreference {
    ProgressDialog mProgressDialog;
    public static void setPreferences(Context context, String key, String value) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }
    public static void removePreferences(Context context, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.remove(key);
        prefsEditor.commit();
    }
    public static void setPreferencesObject(Context context, Object MyObject) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MyObject);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }
    public static Object getPreferencesObject(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }
    //
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

    public void ShowDialog(Context context, String Msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Tender Watch");
        builder.setMessage(Msg);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                      //  Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });

        builder.show();
    }
    public void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading....");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
   // public  void ShowAlert(Context con)
}
