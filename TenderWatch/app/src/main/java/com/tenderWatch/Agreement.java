package com.tenderWatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Agreement extends AppCompatActivity implements View.OnClickListener{
    ImageView box,boxChecked;
    Button signUp;
    CreateUser user=new CreateUser();
    private static final String TAG = Agreement.class.getSimpleName();
    private Api mAPIService;
    MultipartBody.Part email1,password1,country1,selections1,subscribe1,contactNo1,occupation1,aboutMe1,role1,deviceId1,image1;
    SharedPreference sp =new SharedPreference();
    Intent intent;
LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        InitView();
        InitListener();
        Log.i(TAG, String.valueOf(user.getProfilePhoto()));
    }

    private void InitView() {
        mAPIService = ApiUtils.getAPIService();

        WebView mWebView = (WebView) findViewById(R.id.agreement_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_res/raw/agreement.html");
        box=(ImageView) findViewById(R.id.box);
        boxChecked=(ImageView) findViewById(R.id.box_checked);
        back=(LinearLayout) findViewById(R.id.agreement_back);

        signUp=(Button) findViewById(R.id.post_signup);
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
                if(sp.getPreferences(Agreement.this,"role").equals("client")) {
                    intent = new Intent(Agreement.this, SignUp.class);
                }else{
                    intent = new Intent(Agreement.this, Category.class);
                }
                startActivityForResult(intent,1);
                break;
        }
    }
    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Agreement.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        //Create Upload Server Client
        // ApiService service = RetroClient.getApiService();
       // mAPIService = ApiUtils.getAPIService();

        //File creating from selected URL
        String imagePath="";
        //File file = new File(imagePath);

        // create RequestBody instance from file

        String email=user.getEmail().toString();
        String password=user.getPassword().toString();
        String country=user.getCountry().toString();
        String contact=user.getContactNo().toString();
        String occupation=user.getOccupation().toString();
        String aboutMe=user.getAboutMe().toString();
        String role=user.getRole().toString();
        String deviceId=user.getDeviceId().toString();
        File file1=user.getProfilePhoto();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);

        email1 = MultipartBody.Part.createFormData("email", email);
        password1 =MultipartBody.Part.createFormData("password", password);
        country1 =MultipartBody.Part.createFormData("country",country);
        contactNo1 =MultipartBody.Part.createFormData("contactNo", contact);
        occupation1 =MultipartBody.Part.createFormData("occupation", occupation);
        aboutMe1 =MultipartBody.Part.createFormData("aboutMe",aboutMe);
        role1 =MultipartBody.Part.createFormData("role", role);
        deviceId1 =MultipartBody.Part.createFormData("deviceId",deviceId );
        image1 =MultipartBody.Part.createFormData("image", file1.getName(),requestFile);

        Call<Register> resultCall = mAPIService.uploadImage(email1,password1,country1,contactNo1,occupation1,aboutMe1,role1,deviceId1,image1);
        resultCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Log.i(TAG,"response register-->");
                if(response.isSuccessful()) {
                    // showResponse(response.body().toString());
//                    intent = new Intent(Login.this, Welcome.class);
//                    startActivity(intent);
                    sp.ShowDialog(Agreement.this,"Successful Registration");

                    //  Log.i(TAG, "post submitted to API." + response.body().toString());
                }else{
                    sp.ShowDialog(Agreement.this,response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.i(TAG,"error register-->");
                sp.ShowDialog(Agreement.this,"Server is down. Come back later!!");

            }
        });

    }
    private void SignUpPost() {
        if(signUp.getAlpha()==1){
            if(sp.getPreferences(Agreement.this,"role").equals("contractor")){
                uploadContractor();
            }else {
                uploadImage();

            }
        }
    }

    private void uploadContractor() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Agreement.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        //Create Upload Server Client
        // ApiService service = RetroClient.getApiService();
        // mAPIService = ApiUtils.getAPIService();

        //File creating from selected URL
        String imagePath="";
        //File file = new File(imagePath);

        // create RequestBody instance from file

        String email=user.getEmail().toString();
        String password=user.getPassword().toString();
        String country=user.getCountry().toString();
        String contact=user.getContactNo().toString();
        String occupation=user.getOccupation().toString();
        String aboutMe=user.getAboutMe().toString();
        String role=user.getRole().toString();
        String deviceId=user.getDeviceId().toString();
        String selections= String.valueOf(user.getSelections());
        HashMap<String, ArrayList<String>> subscribe=user.getSubscribe();
        File file1=user.getProfilePhoto();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);

        email1 = MultipartBody.Part.createFormData("email", email);
        password1 =MultipartBody.Part.createFormData("password", password);
        country1 =MultipartBody.Part.createFormData("country",country);
        contactNo1 =MultipartBody.Part.createFormData("contactNo", contact);
        occupation1 =MultipartBody.Part.createFormData("occupation", occupation);
        aboutMe1 =MultipartBody.Part.createFormData("aboutMe",aboutMe);
        role1 =MultipartBody.Part.createFormData("role", role);
        deviceId1 =MultipartBody.Part.createFormData("deviceId",deviceId );
        selections1 =MultipartBody.Part.createFormData("selections", selections);
        subscribe1 =MultipartBody.Part.createFormData("subscribe", String.valueOf(subscribe));
        image1 =MultipartBody.Part.createFormData("image", file1.getName(),requestFile);

        Call<Register> resultCall = mAPIService.uploadContractor(email1,password1,country1,contactNo1,occupation1,aboutMe1,role1,deviceId1,image1,selections1,subscribe1);
        resultCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Log.i(TAG,"response register-->");
                if(response.isSuccessful()) {
                    // showResponse(response.body().toString());
//                    intent = new Intent(Login.this, Welcome.class);
//                    startActivity(intent);
                    sp.ShowDialog(Agreement.this,"Successful Registration");

                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }else{
                    sp.ShowDialog(Agreement.this,response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.i(TAG,"error register-->");
                sp.ShowDialog(Agreement.this,"Server is down. Come back later!!");

            }
        });

    }
}
