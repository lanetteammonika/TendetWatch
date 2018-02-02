package com.tenderWatch;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.ClientDrawer.TenderList;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.User;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.app.Config;
import com.tenderWatch.utils.ConnectivityReceiver;
import com.tenderWatch.utils.NotificationUtils;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView aboutapp;
    Context context;
    private Button btnContractor, btnClient;
    Intent intent;
    SharedPreference sp = new SharedPreference();
    private static final String TAG = MainActivity.class.getSimpleName();
    Object user;
    boolean doubleBackToExitPressedOnce = true;
    ConnectivityReceiver cr = new ConnectivityReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        InitView();
        InitListener();
        user = sp.getPreferencesObject(MainActivity.this);
         FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        Button crashButton = new Button(this);
//        crashButton.setText("..");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });
//        addContentView(crashButton,
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics())
//                .debuggable(true)           // Enables Crashlytics debugger
//                .build();
//        Fabric.with(fabric);
        if (cr.isConnected(MainActivity.this)) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        final String message = intent.getStringExtra("message");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                            }
                        });


                        //txtMessage.setText(message);
                    }
                }
            };


            displayFirebaseRegId();
        } else {
            sp.ShowDialog(MainActivity.this, "Please check your internet connection");
        }
        if (user != null) {
            String role = sp.getPreferences(MainActivity.this, "role");
            if (role.equals("client")) {
                intent = new Intent(MainActivity.this, ClientDrawer.class);
            } else {
                intent = new Intent(MainActivity.this, MainDrawer.class);
            }
            sp.setPreferences(MainActivity.this, "role", role);
            // intent.putExtra("Role", "client");
            Log.i(TAG, "testing");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }

    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.e(TAG, "Firebase Reg Id: " + regId);
        else
            Log.e(TAG, "Firebase Reg Id is not received yet!");
    }

    private void InitListener() {
        btnContractor.setOnClickListener(this);
        btnClient.setOnClickListener(this);
        aboutapp.setOnClickListener(this);
        String deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sp.setPreferences(getApplicationContext(), "deviceId", deviceId);
    }

    private void InitView() {
        btnContractor = (Button) findViewById(R.id.btn_contractor);
        btnClient = (Button) findViewById(R.id.btn_client);
        aboutapp = (TextView) findViewById(R.id.aboutapp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutapp:
                final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
                dialog.setContentView(R.layout.aboutapp);

                Button dismissButton = (Button) dialog.findViewById(R.id.button_close);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                WebView mWebView = (WebView) dialog.findViewById(R.id.about_webview);
                WebSettings webSettings = mWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                mWebView.loadUrl("file:///android_res/raw/about.htm");
                dialog.show();
                break;
            case R.id.btn_contractor:
                Intent i = new Intent(MainActivity.this, Login.class);
                sp.setPreferences(getApplicationContext(), "role", "contractor");
                i.putExtra("Role", "contractor");
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.btn_client:
                Intent i2 = new Intent(MainActivity.this, Login.class);
                sp.setPreferences(MainActivity.this, "role", "client");
                i2.putExtra("Role", "client");
                Log.i(TAG, "testing");
                startActivity(i2);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        // Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
