package com.example.lcom48.tenderwatch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aboutapp;
    Context context;
    private Button btnContractor,btnClient;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        InitView();
        InitListener();


    }

    private void InitListener() {
        btnContractor.setOnClickListener(this);
        btnClient.setOnClickListener(this);
        aboutapp.setOnClickListener(this);
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
                final Dialog dialog=new Dialog(context,R.style.full_screen_dialog);
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
                startActivity(intent);
                break;
            case R.id.btn_client:
                intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                break;
        }
    }
}
