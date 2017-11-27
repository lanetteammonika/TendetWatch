package com.tenderWatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.tenderWatch.SharedPreference.SharedPreference;

public class Welcome extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = Login.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private Button btnLogin;
    Context context;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnLogin = (Button) findViewById(R.id.btn_logout);
        btnLogin.setOnClickListener(this);
        context=getApplicationContext();


    }
    private void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //  updateUI(false);
                        Log.d(TAG, "handleSignInResult:" + status);

                    }
                });
        intent = new Intent(Welcome.this, MainActivity.class);
        startActivity(intent);
    }
    private void signOutFB() {
        LoginManager.getInstance().logOut();
        intent = new Intent(Welcome.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_logout:
               // signIn();
                SharedPreference sp=new SharedPreference();
               // sp.setPreferences(getApplicationContext(),"Login","YES");
                String login=sp.getPreferences(getApplicationContext(),"Login");

                if(login.equals("FBYES")){
                    signOutFB();
                }
                if(login.equals("GOOGLEYES")){
                    signOutGoogle();
                }

                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }
}
