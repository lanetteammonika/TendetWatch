package com.tenderWatch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.ClientDrawer.TenderList;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.User;
import com.tenderWatch.SharedPreference.SharedPreference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aboutapp;
    Context context;
    private Button btnContractor, btnClient;
    Intent intent;
    SharedPreference sp = new SharedPreference();
    private static final String TAG = MainActivity.class.getSimpleName();
    Object user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        InitView();
        InitListener();
        user=sp.getPreferencesObject(MainActivity.this);
        if(user!= null){
            String role=sp.getPreferences(MainActivity.this,"role");
            if(role.equals("client")) {
                intent = new Intent(MainActivity.this, ClientDrawer.class);
            }else{
                intent = new Intent(MainActivity.this, MainDrawer.class);
            }
            sp.setPreferences(MainActivity.this, "role",((User) user).getRole().toString());
           // intent.putExtra("Role", "client");
            Log.i(TAG, "testing");
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }

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
                intent = new Intent(MainActivity.this, Login.class);
                sp.setPreferences(getApplicationContext(), "role", "contractor");
                intent.putExtra("Role", "contractor");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.btn_client:
                intent = new Intent(MainActivity.this, Login.class);
                sp.setPreferences(MainActivity.this, "role", "client");
                intent.putExtra("Role", "client");
                Log.i(TAG, "testing");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
        }
    }

    boolean doubleBackToExitPressedOnce = true;

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
        //finish();
    }
}
