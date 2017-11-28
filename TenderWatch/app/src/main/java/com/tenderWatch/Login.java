package com.tenderWatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;
import com.tenderWatch.Models.LoginPost;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.Validation.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    //LoginButton loginButton;
    Context context;
    Intent intent;
    private CallbackManager callbackManager;
    private static final String TAG = Login.class.getSimpleName();
    private EditText txtEmail,txtPassword;
    private LoginButton loginButton;
    private Button fb;
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess, btngoogle, btnlogin;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private Api mAPIService;
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        System.out.print("called");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("Role");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("Role");
        }
        Init();
        InitListener();

    }

    private void Init() {
        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnlogin =(Button) findViewById(R.id.btn_login);
        mAPIService = ApiUtils.getAPIService();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btngoogle = (Button) findViewById(R.id.google);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);

    }

    private void InitListener() {

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    Validation.isEmailAddress(txtEmail, true);
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Validation.isPassword(txtPassword,true);
                Log.i(TAG, "post submitted to API." +  Validation.isValidPassword(txtPassword.getText().toString()));

            }
        });
        btnSignIn.setOnClickListener(this);
        btngoogle.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestServerAuthCode(getString(R.string.server_client_id), false)
            .requestEmail()
            .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

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
                String deviceId =Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                savePostFB(accessToken, "contractor", deviceId);
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

    private boolean checkValidation() {
        boolean ret = true;

       // if (!Validation.hasText(etNormalText)) ret = false;
        if (!Validation.isEmailAddress(txtEmail, true)) ret = false;
        if (!Validation.isPassword(txtPassword, true)) ret = false;

        return ret;
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

    public void savePostFB(String idToken, String role,String deviceId) {
        mAPIService.savePostFB(idToken,role,deviceId).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {

                if(response.isSuccessful()) {
                    // showResponse(response.body().toString());
                    intent = new Intent(Login.this, Welcome.class);
                    startActivity(intent);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginPost> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void sendPostGoogle(String idToken, String role,String deviceId) {
        mAPIService.savePostGoogle(idToken,role,deviceId).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {

                if(response.isSuccessful()) {
                   // showResponse(response.body().toString());
                    intent = new Intent(Login.this, Welcome.class);
                    startActivity(intent);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginPost> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void sendPost(String email,String password, String role,String deviceId) {
        mAPIService.savePost(email,password,role,deviceId).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {

                if(response.isSuccessful()) {
                    // showResponse(response.body().toString());
                    intent = new Intent(Login.this, Welcome.class);
                    startActivity(intent);
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
            sendPostGoogle(idToken, "contractor", deviceId);
            // Show signed-in UI.
            Log.d(TAG, "idToken:" + idToken);

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
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = Auth.GoogleSignInApi.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }

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
            case R.id.btn_login:
                if ( checkValidation () ){
                    login();
                    Toast.makeText(Login.this, "Form contains not error", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Login.this, "Form contains error", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void login() {
        String email=txtEmail.getText().toString();
        String password =txtPassword.getText().toString();
        String role=newString;
        SharedPreference sp=new SharedPreference();
        String deviceId =  sp.getPreferences(getApplicationContext(),"deviceId");
        sendPost(email,password,role,deviceId);

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
///
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
