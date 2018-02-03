package com.tenderWatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Agreement extends AppCompatActivity implements View.OnClickListener {
    ImageView box, boxChecked;
    Button signUp;
    CreateUser user = new CreateUser();
    private static final String TAG = Agreement.class.getSimpleName();
    private Api mAPIService;
    MultipartBody.Part deviceId2, selections1, email1, password1, country1, deviceType1, subscribe1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1;
    SharedPreference sp = new SharedPreference();
    Intent intent;
    LinearLayout back, webLayout;
    ConnectivityReceiver cr=new ConnectivityReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        InitView();
        InitListener();
        Log.i(TAG, String.valueOf(user.getProfilePhoto()));

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
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

    private void InitView() {
        mAPIService = ApiUtils.getAPIService();

        WebView mWebView = (WebView) findViewById(R.id.agreement_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_res/raw/agreement.html");
        box = (ImageView) findViewById(R.id.box);
        boxChecked = (ImageView) findViewById(R.id.box_checked);
        back = (LinearLayout) findViewById(R.id.agreement_back);
        webLayout = (LinearLayout) findViewById(R.id.weblayout);
// Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = webLayout.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = Agreement.this.getResources().getDimensionPixelSize(R.dimen.value_340);
        webLayout.setLayoutParams(params);
        signUp = (Button) findViewById(R.id.post_signup);
    }

    private void InitListener() {
        box.setOnClickListener(this);
        boxChecked.setOnClickListener(this);
        signUp.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.box:
                box.setVisibility(View.GONE);
                boxChecked.setVisibility(View.VISIBLE);
                signUp.setAlpha((float) 1);
                break;
            case R.id.box_checked:
                boxChecked.setVisibility(View.GONE);
                box.setVisibility(View.VISIBLE);
                signUp.setAlpha((float) 0.7);
                break;
            case R.id.post_signup:
                SignUpPost();
                break;
            case R.id.agreement_back:
                if (sp.getPreferences(Agreement.this, "role").equals("client")) {
                    intent = new Intent(Agreement.this, SignUp.class);
                } else {
                    intent = new Intent(Agreement.this, Category.class);
                }
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Agreement.this);
        //progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        String email = user.getEmail().toString();
        String password = user.getPassword().toString();
        String country = user.getCountry().toString();
        String contact = user.getContactNo().toString();
        String occupation = user.getOccupation().toString();
        String aboutMe = user.getAboutMe().toString();
        String role = user.getRole().toString();
        String deviceId = user.getDeviceId().toString();
        File file1 = user.getProfilePhoto();
        String regId = FirebaseInstanceId.getInstance().getToken();

        RequestBody requestFile;
        if (user.getProfilePhoto() != null) {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            image1 = MultipartBody.Part.createFormData("image", file1.getName(), requestFile);

        } else {
            image1 = MultipartBody.Part.createFormData("image", "");

        }
        email1 = MultipartBody.Part.createFormData("email", email);
        password1 = MultipartBody.Part.createFormData("password", password);
        country1 = MultipartBody.Part.createFormData("country", country);
        contactNo1 = MultipartBody.Part.createFormData("contactNo", contact);
        occupation1 = MultipartBody.Part.createFormData("occupation", occupation);
        aboutMe1 = MultipartBody.Part.createFormData("aboutMe", aboutMe);
        role1 = MultipartBody.Part.createFormData("role", role);
        deviceId1 = MultipartBody.Part.createFormData("androidDeviceId", regId);
        if (image1 == null) {
            image1 = MultipartBody.Part.createFormData("image", "");
        }
        Call<Register> resultCall = mAPIService.uploadImage(email1, password1, country1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1);
        sp.showProgressDialog(Agreement.this);
if(cr.isConnected(Agreement.this)) {
    resultCall.enqueue(new Callback<Register>() {
        @Override
        public void onResponse(Call<Register> call, Response<Register> response) {
            sp.hideProgressDialog();
            Log.i(TAG, "response register-->");
            if (response.isSuccessful()) {
                User u1 = response.body().getUser();
                sp.setPreferencesObject(Agreement.this, u1);
                sp.setPreferences(Agreement.this, "token", response.body().getToken());
                User u2 = (User) sp.getPreferencesObject(Agreement.this);
                String t = sp.getPreferences(Agreement.this, "token");
                intent = new Intent(Agreement.this, ClientDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                sp.ShowDialog(Agreement.this, "Successful Registration");
            } else {
                sp.ShowDialog(Agreement.this, response.errorBody().source().toString().split("\"")[3]);
            }
        }

        @Override
        public void onFailure(Call<Register> call, Throwable t) {
            Log.i(TAG, "error register-->");
            sp.ShowDialog(Agreement.this, "Server is down. Come back later!!");

        }
    });
}else{
    sp.ShowDialog(Agreement.this,"Please check your internet connection.");
}
    }

    private void SignUpPost() {
        if (signUp.getAlpha() == 1) {
            if (sp.getPreferences(Agreement.this, "role").equals("contractor")) {
              if(sp.getPreferences(Agreement.this,"sel_con") !=null){
                  if(sp.getPreferences(Agreement.this,"sel_con").equals("$15 / month")){
                      uploadContractor();
                          intent = new Intent(Agreement.this, PaymentSelection.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(intent);

                  }else{
                      uploadContractor();

                      intent = new Intent(Agreement.this, PaymentSelection.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                  }
              }else{

                  uploadContractor();
              }

            } else {
                uploadImage();

            }
        }
    }

    private void uploadContractor() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Agreement.this);
      //  progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        String email = user.getEmail();
        String password = user.getPassword();
        String country = user.getCountry();
        String contact = user.getContactNo();
        String occupation = user.getOccupation();
        String aboutMe = user.getAboutMe();
        String role = user.getRole();
        String deviceId = FirebaseInstanceId.getInstance().getToken();
        String selections = String.valueOf(user.getSelections());
        HashMap<String, ArrayList<String>> subscribe = user.getSubscribe();
        String[] device = new String[1];

        File file1 = user.getProfilePhoto();
        RequestBody requestFile;
        if (user.getProfilePhoto() != null) {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            image1 = MultipartBody.Part.createFormData("image", file1.getName(), requestFile);

        } else {
            image1 = MultipartBody.Part.createFormData("image", "");

        }
        RequestBody email2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody password2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody country2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody contactNo2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody occupation2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody aboutMe2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody role2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody androidDeviceId2 = RequestBody.create(MediaType.parse("text/plain"),email);
//        RequestBody image2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody selections2 = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody subscribe2 = RequestBody.create(MediaType.parse("text/plain"),email);


        email1 = MultipartBody.Part.createFormData("email", email);
        password1 = MultipartBody.Part.createFormData("password", password);
        country1 = MultipartBody.Part.createFormData("country", country);
        contactNo1 = MultipartBody.Part.createFormData("contactNo", contact);
        occupation1 = MultipartBody.Part.createFormData("occupation", occupation);
        aboutMe1 = MultipartBody.Part.createFormData("aboutMe", aboutMe);
        role1 = MultipartBody.Part.createFormData("role", role);
        deviceId1 = MultipartBody.Part.createFormData("androidDeviceId", deviceId);
        deviceId2 = MultipartBody.Part.createFormData("deviceId", "");
        subscribe1 = MultipartBody.Part.createFormData("subscribe", selections);
        selections1 = MultipartBody.Part.createFormData("selections", new Gson().toJson(subscribe));

        Call<Register> resultCall = mAPIService.uploadContractor(email1, password1, country1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1, subscribe1, selections1);
        sp.showProgressDialog(Agreement.this);
if(cr.isConnected(Agreement.this)) {
    resultCall.enqueue(new Callback<Register>() {
        @Override
        public void onResponse(Call<Register> call, Response<Register> response) {
            Log.i(TAG, "response register-->");
            sp.hideProgressDialog();
            if (response.isSuccessful()) {
                ///String role = sp.getPreferences(Agreement.this, "role");
                Gson gson = new Gson();
                String jsonString = gson.toJson(user);
                User u1 = response.body().getUser();
                sp.setPreferencesObject(Agreement.this, u1);
                sp.setPreferences(Agreement.this, "token", response.body().getToken());
                User u2 = (User) sp.getPreferencesObject(Agreement.this);
                String t = sp.getPreferences(Agreement.this, "token");
                if (sp.getPreferences(Agreement.this, "sel_con") != null) {
                    intent = new Intent(Agreement.this, PaymentSelection.class);
                } else {
                    intent = new Intent(Agreement.this, MainDrawer.class);
                }
                intent.putExtra("data", jsonString);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                Log.i(TAG, "post submitted to API." + response.body().toString());
            } else {
                sp.ShowDialog(Agreement.this, response.errorBody().source().toString().split("\"")[3]);
            }
        }

        //-resloved bug regarding signup and login in android given by client-changes some design of listview in
        @Override
        public void onFailure(Call<Register> call, Throwable t) {
            Log.i(TAG, "error register-->");
            sp.ShowDialog(Agreement.this, "Server is down. Come back later!!");
        }
    });
}else{
    sp.ShowDialog(Agreement.this,"Please check your internet connection.");
}
    }
}
