package com.tenderWatch;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.stripe.android.RequestOptions;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDemoDesign extends AppCompatActivity {
    Button btnClick;
    String number,cvc;
    int expMonth,expYear;
    CreateUser user = new CreateUser();
    private static final String TAG = CardDemoDesign.class.getSimpleName();
    MultipartBody.Part deviceId2, selections1, email1, password1, country1, deviceType1, subscribe1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1;
    SharedPreference sp = new SharedPreference();
    Intent intent;
    private Api mAPIService;
    String selCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_demo_design);
        btnClick=(Button) findViewById(R.id.btnClick);
        final CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        mAPIService= ApiUtils.getAPIService();
        selCon=getIntent().getStringExtra("selCon");

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(CardDemoDesign.this);
        cardForm.isCardScanningAvailable();
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=cardForm.getCardNumber();
                expMonth= Integer.parseInt(cardForm.getExpirationMonth());
                expYear= Integer.parseInt(cardForm.getExpirationYear());
                cvc=cardForm.getCvv();
                Call();
            }
        });

    }
    private void Call(){
        Card card = new Card(number,expMonth,expYear,cvc);
        final Stripe stripe = new Stripe(CardDemoDesign.this, "pk_test_mjxYxMlj4K2WZfR6TwlHdIXW");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onError(Exception error) {
                        Log.e("Stripe Error", error.getMessage());
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        Log.e("Bank Token", token.getId());
                        if(selCon != null){
                            uploadContractor();
                        }else{
                            intent=new Intent(CardDemoDesign.this,MainDrawer.class);
                            startActivity(intent);
                        }

                        token.getBankAccount();

                    }
                }
        );

    }

    private void uploadContractor() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(CardDemoDesign.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        String email = user.getEmail().toString();
        String password = user.getPassword().toString();
        String country = user.getCountry().toString();
        String contact = user.getContactNo().toString();
        String occupation = user.getOccupation().toString();
        String aboutMe = user.getAboutMe().toString();
        String role = user.getRole().toString();
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
        sp.showProgressDialog(CardDemoDesign.this);

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
                    sp.setPreferencesObject(CardDemoDesign.this, u1);
                    sp.setPreferences(CardDemoDesign.this, "token", response.body().getToken());
                    User u2 = (User) sp.getPreferencesObject(CardDemoDesign.this);
                    String t = sp.getPreferences(CardDemoDesign.this, "token");

                    intent = new Intent(CardDemoDesign.this, MainDrawer.class);
                    intent.putExtra("data", jsonString);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    sp.ShowDialog(CardDemoDesign.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.i(TAG, "error register-->");
                sp.ShowDialog(CardDemoDesign.this, "Server is down. Come back later!!");
            }
        });

    }

}

