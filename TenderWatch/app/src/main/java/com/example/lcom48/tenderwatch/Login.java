package com.example.lcom48.tenderwatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lcom48.tenderwatch.GetterSetter.SharedPreference;
import com.example.lcom48.tenderwatch.Models.LoginPost;
import com.example.lcom48.tenderwatch.Retrofit.ApiUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import android.provider.Settings.Secure;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    //LoginButton loginButton;
    Context context;
    Intent intent;
    private CallbackManager callbackManager;
    private static final String TAG = Login.class.getSimpleName();
    private TextView info;
    private LoginButton loginButton;
    private Button fb;
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess, btngoogle;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private com.example.lcom48.tenderwatch.Retrofit.Api mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        Init();
        InitListener();
    }

    private void Init() {
        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        mAPIService = ApiUtils.getAPIService();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btngoogle = (Button) findViewById(R.id.google);
    }

    private void InitListener() {
        btnSignIn.setOnClickListener(this);
        btngoogle.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestServerAuthCode(getString(R.string.server_client_id), false)
            .requestEmail()
            .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
        fb.setOnClickListener(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, String.valueOf(loginResult));
                SharedPreference sp = new SharedPreference();
                String accessToken= loginResult.getAccessToken().getToken();
                if(AccessToken.getCurrentAccessToken()!=null)
                {
                    Log.v("User is login","YES");

                }
                sp.setPreferences(getApplicationContext(), "Login", "FBYES");
                intent = new Intent(Login.this, Welcome.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onConnectionFailed:");
                // info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "onConnectionFailed:");
                //  info.setText("Login attempt failed.");
            }
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    public void sendPost(String idToken, String role,String deviceId) {
        mAPIService.savePost(idToken,role,deviceId).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {

                if(response.isSuccessful()) {
                   // showResponse(response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginPost> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
      //  GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//        String idToken = result.getIdToken();
//
        if (result.isSuccess()) {
            SharedPreference sp = new SharedPreference();
            sp.setPreferences(getApplicationContext(), "Login", "GOOGLEYES");
            GoogleSignInAccount acct = result.getSignInAccount();
            String idToken = acct.getIdToken();
            String deviceId =Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            sendPost(idToken, "contractor", deviceId);
            // Show signed-in UI.
            Log.d(TAG, "idToken:" + idToken);
            intent = new Intent(Login.this, Welcome.class);
            startActivity(intent);
            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//
//            String personName = acct.getDisplayName();
//         //   String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email);

            //txtName.setText(personName);
            // txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);
            //   updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
            Log.e(TAG, "display name: ");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }






//...


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.google:
                signIn();
                break;
            case R.id.fb:
                loginButton.performClick();
                break;
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            //  mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
