package com.tenderWatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lcom47 on 31/1/18.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyBroadcastReceiver", "onReceive: ");
        Toast.makeText(context, "You get the new notification!!!", Toast.LENGTH_SHORT).show();
    }
}
