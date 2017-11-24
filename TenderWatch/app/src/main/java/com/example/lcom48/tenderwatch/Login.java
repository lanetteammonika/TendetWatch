package com.example.lcom48.tenderwatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity implements View.OnClickListener {
    //LoginButton loginButton;
    Context context;
    Intent intent;
    private CallbackManager callbackManager;
    private TextView info;
    private LoginButton loginButton;
    private Button fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //info = (TextView)findViewById(R.id.info);
         fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
       // loginButton = (LoginButton)findViewById(R.id.login_button);
       // loginButton.setBackgroundResource(R.drawable.round_corner);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        context=this;
        // If using in a fragment
        // loginButton.setFragment(context);
        // Other app specific specialization


        // Callback registration
        ;
        fb.setOnClickListener(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("tag", String.valueOf(loginResult));
//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
//                );
                intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
               // info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
              //  info.setText("Login attempt failed.");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view == fb) {
            loginButton.performClick();
        }
    }
}
