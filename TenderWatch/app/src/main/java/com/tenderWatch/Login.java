package com.tenderWatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.Validation.Validation;
import com.tenderWatch.utils.ConnectivityReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Context context;
    Intent intent;
    private CallbackManager callbackManager;
    private static final String TAG = Login.class.getSimpleName();
    private EditText txtEmail, txtPassword;
    private LoginButton loginButton;
    private TextView lblCreateAccount, lblForgotPassword;
    private Button fb;
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess, btngoogle, btnlogin;
    private LinearLayout back;
    private ImageView showPassword,hidePassword;
    private boolean showPass=false;

    private Api mAPIService;
    String newString;
    SharedPreference sp = new SharedPreference();
    ConnectivityReceiver cr=new ConnectivityReceiver();

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        System.out.print("called");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("Role");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("Role");
        }
        Init();
        InitListener();

    }

    private void Init() {
        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnlogin = (Button) findViewById(R.id.btn_login);
        mAPIService = ApiUtils.getAPIService();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btngoogle = (Button) findViewById(R.id.google);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        lblCreateAccount = (TextView) findViewById(R.id.create_account);
        back = (LinearLayout) findViewById(R.id.login_toolbar);
        lblForgotPassword = (TextView) findViewById(R.id.lbl_forgotPassword);
        showPassword=(ImageView) findViewById(R.id.show_password);
        hidePassword=(ImageView) findViewById(R.id.hide_password);
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
                Log.i(TAG, "post submitted to API." + Validation.isValidPassword(txtPassword.getText().toString()));

            }
        });
        btnSignIn.setOnClickListener(this);
        btngoogle.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        lblCreateAccount.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        hidePassword.setOnClickListener(this);
        back.setOnClickListener(this);
        lblForgotPassword.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id), false)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
        fb.setOnClickListener(this);
        if(cr.isConnected(Login.this)){
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, String.valueOf(loginResult));
                sp.hideProgressDialog();
                String accessToken = loginResult.getAccessToken().getToken();
                String deviceId = FirebaseInstanceId.getInstance().getToken();
                String role = sp.getPreferences(Login.this, "role");
                savePostFB(accessToken, role, deviceId);
                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.v("User is login", "YES");
                }
                sp.setPreferences(getApplicationContext(), "Login", "FBYES");
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
        }else{
            sp.ShowDialog(Login.this,"Please check your internet connection");
        }
    }

    private boolean checkValidation() {
        boolean ret = true;
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
            ///showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
        }
    }

    public void savePostFB(String idToken, String role, String deviceId) {
        sp.showProgressDialog(Login.this);
        if(cr.isConnected(Login.this)){
        mAPIService.savePostFB(idToken, role, deviceId).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                sp.hideProgressDialog();
                if (response.isSuccessful()) {
                    sp.setPreferencesObject(Login.this,response.body().getUser());
                    Object user=sp.getPreferencesObject(Login.this);
                    String role = sp.getPreferences(Login.this, "role");
                    if(role.equals("client")){
                        intent = new Intent(Login.this, ClientDrawer.class);

                    }else {
                        intent = new Intent(Login.this, MainDrawer.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    Log.i(TAG, "post submitted to API." + response.body());
                } else {
                    sp.ShowDialog(Login.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                sp.ShowDialog(Login.this, "Server is down. Come back later!!");
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
        }else{
            sp.ShowDialog(Login.this,"Please check your internet connection");
        }
    }

    public void sendPostGoogle(String idToken, String role, String deviceId) {
        sp.showProgressDialog(Login.this);
if(cr.isConnected(Login.this)){
        mAPIService.savePostGoogle(idToken, role, deviceId).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                sp.hideProgressDialog();
                if (response.isSuccessful()) {
                    sp.setPreferencesObject(Login.this,response.body().getUser());
                    Object user=sp.getPreferencesObject(Login.this);
                    String role = sp.getPreferences(Login.this, "role");
                    if(role.equals("client")){
                        intent = new Intent(Login.this, ClientDrawer.class);

                    }else {
                        intent = new Intent(Login.this, MainDrawer.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    sp.ShowDialog(Login.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                sp.ShowDialog(Login.this, "Server is down. Come back later!!");
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
}else{
    sp.ShowDialog(Login.this,"Please check your internet connection");
}
    }

    public void sendPost(String email, String password, String role, final String deviceId) {
        sp.showProgressDialog(Login.this);
if(cr.isConnected(Login.this)){
        mAPIService.savePost(email, password, role, deviceId).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                sp.hideProgressDialog();
                if (response.isSuccessful()) {
                    sp.setPreferences(Login.this,"androidId",deviceId);
                    sp.setPreferencesObject(Login.this,response.body().getUser());
                    Object user=sp.getPreferencesObject(Login.this);
                    String role = sp.getPreferences(Login.this, "role");
                    String profile=response.body().getUser().getProfilePhoto();
                    String email=response.body().getUser().getEmail();
                    String id=response.body().getUser().getId();
                    String token=response.body().getToken();
                    sp.setPreferences(Login.this,"profile",profile);
                    sp.setPreferences(Login.this,"email",email);
                    sp.setPreferences(Login.this,"id",id);
                    sp.setPreferences(Login.this,"token",token);

                    if(role.equals("client")){
                        intent = new Intent(Login.this, ClientDrawer.class);

                    }else {
                        intent = new Intent(Login.this, MainDrawer.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    sp.ShowDialog(Login.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                sp.ShowDialog(Login.this, "Server is down. Come back later!!");
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
}else{
    sp.ShowDialog(Login.this,"Please check your internet connection");
}
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        //  if(sp.getPreferences(Login.this,"Login") == null) {
        if(cr.isConnected(Login.this)){
        if (result.isSuccess()) {
            SharedPreference sp = new SharedPreference();
            sp.setPreferences(getApplicationContext(), "Login", "GOOGLEYES");
            GoogleSignInAccount acct = result.getSignInAccount();
            String idToken = acct.getIdToken();
            String deviceId = FirebaseInstanceId.getInstance().getToken();
            String role = sp.getPreferences(Login.this, "role");
            sendPostGoogle(idToken, role, deviceId);
            // Show signed-in UI.
            Log.d(TAG, "idToken:" + idToken);
        } else {
            Log.e(TAG, "display name: ");
        }
        }else{
            sp.ShowDialog(Login.this,"Please check your internet connection");
        }
        //  }
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
                if (checkValidation()) {
                    login();
                    break;
                }
                break;

            case R.id.create_account:
                intent = new Intent(Login.this, SignUpSelection.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.lbl_forgotPassword:
                intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.login_toolbar:
                intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.show_password:
                showPassword.setVisibility(View.GONE);
                hidePassword.setVisibility(View.VISIBLE);
                if (!showPass) {
                    txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPass = true;
                } else {
                    txtPassword.setInputType(129);
                    showPass = false;
                }
                break;
            case R.id.hide_password:
                hidePassword.setVisibility(View.GONE);
                showPassword.setVisibility(View.VISIBLE);
                if (!showPass) {
                    txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPass = true;
                } else {
                    txtPassword.setInputType(129);
                    showPass = false;
                }
                break;
        }
    }

    private void login() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        SharedPreference sp = new SharedPreference();
        String role = sp.getPreferences(Login.this, "role");
        //user =sp.getPreferencesObject(Login.this);
        String deviceId = FirebaseInstanceId.getInstance().getToken();
        sendPost(email, password, role, deviceId);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
