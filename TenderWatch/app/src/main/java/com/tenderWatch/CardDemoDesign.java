package com.tenderWatch;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import com.tenderWatch.Models.RequestCharges;
import com.tenderWatch.Models.RequestPayment;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDemoDesign extends AppCompatActivity {
    Button btnClick;
    String number,cvc;
    int expMonth,expYear;
    CreateUser user = new CreateUser();
    private static final String TAG = CardDemoDesign.class.getSimpleName();
    MultipartBody.Part payment,subscribe,select,deviceId2, selections1, email1, password1, country1, deviceType1, subscribe1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1;
    SharedPreference sp = new SharedPreference();
    Intent intent;
    private Api mAPIService;
    String selCon;
    RequestPayment rp=new RequestPayment();
    RequestCharges rc=new RequestCharges();
    ConnectivityReceiver cr = new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;


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
        myBroadcastReceiver=new MyBroadcastReceiver();

    }
    private void Call(){
        Card card = new Card(number,expMonth,expYear,cvc);
        final Stripe stripe = new Stripe(CardDemoDesign.this, "pk_test_mjxYxMlj4K2WZfR6TwlHdIXW");
       if(cr.isConnected(CardDemoDesign.this)){
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onError(Exception error) {
                        Log.e("Stripe Error", error.getMessage());
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        Log.e("Bank Token", token.getId());
                        AddPaymentFromCard(token.getId());

                        token.getBankAccount();
                    }
                }
        );
       }else{
           sp.ShowDialog(CardDemoDesign.this,"Please check your internet connection");
       }

    }

    private void CallUpdateServices() {
        String token="Bearer " +sp.getPreferences(CardDemoDesign.this,"token");

        int payment2= Integer.parseInt(sp.getPreferences(CardDemoDesign.this,"payment"));
        rp.setPayment(payment2);
        String subscribe2;
        if(payment2==15){
            subscribe2="2";
        }else {
            subscribe2="0";
        }
        rp.setSubscribe(subscribe2);
        rp.setSelections(user.getSubscribe());
        if(cr.isConnected(CardDemoDesign.this)) {
            mAPIService.updateService(token, rp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, "response register-->");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "response register-->");
                }
            });
        }else{
            sp.ShowDialog(CardDemoDesign.this,"Please check your internet connection.");
        }
    }

    private void AddPaymentFromCard(String source) {
        String token = "Bearer " + sp.getPreferences(CardDemoDesign.this, "token");

        int payment = Integer.parseInt(sp.getPreferences(CardDemoDesign.this, "payment")) * 100;
        rc.setSource(source);
        rc.setAmount(payment);
        if(cr.isConnected(CardDemoDesign.this)){
        mAPIService.payChargesCard(token, rc).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "response register-->");
                CallUpdateServices();
                ShowMsg(CardDemoDesign.this, "Payment Successfull ");

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "response register-->");
            }
        });
        }else{
            sp.ShowDialog(CardDemoDesign.this,"Please check your internet connection.");
        }
    }
    public void ShowMsg(Context context, String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Tender Watch");
        builder.setMessage(Msg);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(CardDemoDesign.this, MainDrawer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(CardDemoDesign.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(CardDemoDesign.this).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(CardDemoDesign.this).unregisterReceiver(myBroadcastReceiver);
    }
}

