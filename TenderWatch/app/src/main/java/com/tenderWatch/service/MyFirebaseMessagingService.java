package com.tenderWatch.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.CountryList;
import com.tenderWatch.MainActivity;
import com.tenderWatch.R;
import com.tenderWatch.app.Config;
import com.tenderWatch.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

/**
 * Created by lcom47 on 28/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            String data = json.getString("type");
            String title=json.getString("title");
            String msg=json.getString("msg");

           // String type = json.getString("type");

            Log.e(TAG, "type: " + data);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Log.e(TAG, "handleDataMessage: " + " In Foreground");
                Intent i = new Intent("android.content.BroadcastReceiver");
                sendBroadcast(i);
            } else {
                Log.e(TAG, "handleDataMessage: " + " In BackGround");
                if (data != null) {
                    Log.e(TAG, "background type call" + data + msg);
                }
                Intent resultIntent;
                if(data.equals("interested")){
                     resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("nav_not","ggge");
                }else if(data.equals("expirationService")){
                     resultIntent = new Intent(getApplicationContext(), CountryList.class);
                    resultIntent.putExtra("sub","ggge");
                }else{
                     resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                }

               // resultIntent.putExtra("message", msg);
               // if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(MyFirebaseMessagingService.this, title, msg,  resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(MyFirebaseMessagingService.this, title, message, timestamp, resultIntent, imageUrl);
//                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }



    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(MyFirebaseMessagingService context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message,  intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(MyFirebaseMessagingService context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
}
