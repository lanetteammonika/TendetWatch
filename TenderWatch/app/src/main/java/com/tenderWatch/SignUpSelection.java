package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class SignUpSelection extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Context context;
    Intent intent;
    private CallbackManager callbackManager;
    private static final String TAG = SignUpSelection.class.getSimpleName();
    private EditText txtEmail, txtPassword;
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
    String newString, profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup);
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
        InitView();
        InitListener();
    }

    private void InitListener() {
        btnSignIn.setOnClickListener(this);
        btngoogle.setOnClickListener(this);
        //btnlogin.setOnClickListener(this);
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

                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                try {
                                    Log.i(TAG, object.getString("email"));
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                        profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        String src = profilePicUrl.toString();
                                        URL url = new URL(profilePicUrl);
                                        getBitmapFromURL(url);
                                        //InputStream is = new FileInputStream(profilePicUrl);
                                        // InputStream in = url.openConnection().getInputStream();
                                        //  Bitmap profilePic= BitmapFactory.decodeStream(url.openConnection().getInputStream());


                                        Log.i(TAG, profilePicUrl);
                                        // Uri uri =  Uri.parse(profilePicUrl);
                                        //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                        //   Log.i(TAG, String.valueOf(profilePic));
                                        // mImageView.setBitmap(profilePic);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,cover,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
                String deviceId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                //savePostFB(accessToken, "contractor", deviceId);
                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.v("User is login", "YES");

                }
                sp.setPreferences(getApplicationContext(), "Login", "FBYES");
                intent = new Intent(SignUpSelection.this, SignUp.class);
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

    @SuppressLint("StaticFieldLeak")
    public static void getBitmapFromURL(URL url) throws IOException {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            //connection.setRequestMethod("GET");
//            connection.setDoOutput(true);
//            connection.setChunkedStreamingMode(0);
//
//            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
//          //  writeStream(out);
//
//            InputStream in = new BufferedInputStream(connection.getInputStream());
//            //readStream(in)
//
//// get the response code, returns 200 if it's OK
//           // int responseCode = connection.getResponseCode();
//
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            //connection.connect();
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
        String response;

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        int responceCode = connection.getResponseCode();

        if (responceCode == HttpURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                response = "";// String variable declared global
                response += line;
                Log.i("response_line", response);
            }
        } else {
            response = "";
        }

    }
    private void InitView() {
        fb = (Button) findViewById(R.id.fbsignup);
        loginButton = (LoginButton) findViewById(R.id.signupfb_button);
        // btnlogin =(Button) findViewById(R.id.btn_login);
        //mAPIService = ApiUtils.getAPIService();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        btnSignIn = (SignInButton) findViewById(R.id.signupgoogle_button);
        btngoogle = (Button) findViewById(R.id.googlesignup);
        // txtEmail = (EditText) findViewById(R.id.txt_email);
        // txtPassword = (EditText) findViewById(R.id.txt_password);
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
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        //  GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//        String idToken = result.getIdToken();
//
        if (result.isSuccess()) {
            SharedPreference sp = new SharedPreference();
            sp.setPreferences(getApplicationContext(), "Login", "GOOGLEYES");
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            String idToken = acct.getIdToken();
            String deviceId =Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            //sendPostGoogle(idToken, "contractor", deviceId);
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



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.googlesignup:
                signIn();
                break;
            case R.id.fbsignup:
                loginButton.performClick();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
