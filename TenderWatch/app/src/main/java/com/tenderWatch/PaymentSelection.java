package com.tenderWatch;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.AccountParams;
import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Card;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.RequestCharges;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.PayPalConfig;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import org.json.JSONException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION.SDK_INT;
import static java.security.AccessController.getContext;

public class PaymentSelection extends AppCompatActivity implements View.OnClickListener {
    Button btnPaypal, btnCreditCard, btnGooglePay, btnBank;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    private PaymentsClient paymentsClient;
    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 1;
    private List Data, Data2;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> countryName = new ArrayList<String>();
    Api mAPIService;
    CustomList countryAdapter;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2, down_arrow3, up_arrow3, tenderImage;
    LinearLayout country_home, llbankType;
    ListView spinner, spinnerbanktype;
    private ArrayAdapter<String> listAdapter;
    CreateUser user = new CreateUser();
    private static final String TAG = PaymentSelection.class.getSimpleName();
    MultipartBody.Part deviceId2, selections1, email1, password1, country1, deviceType1, subscribe1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1;
    String selCon;
    Intent intent;
    SharedPreference sp=new SharedPreference();
RequestCharges rc=new RequestCharges();
ConnectivityReceiver cr=new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        btnPaypal = (Button) findViewById(R.id.payment_paypal);
        btnCreditCard = (Button) findViewById(R.id.payment_creditcard);
        btnGooglePay = (Button) findViewById(R.id.payment_googlepay);
        btnBank = (Button) findViewById(R.id.payment_bank);
        btnPaypal.setOnClickListener(this);
        btnCreditCard.setOnClickListener(this);
        btnGooglePay.setOnClickListener(this);
        btnBank.setOnClickListener(this);
        mAPIService = ApiUtils.getAPIService();
        selCon = getIntent().getStringExtra("selCon");
        myBroadcastReceiver=new MyBroadcastReceiver();
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        paymentsClient =
                Wallet.getPaymentsClient(this,
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.payment_paypal:
                getPayment();
                break;
            case R.id.payment_creditcard:
                Intent i = new Intent(PaymentSelection.this, CardDemoDesign.class);
                i.putExtra("selCon","true");
                startActivity(i);
                //call();
//                curl https://api.stripe.com/v1/transfers \
//                -u sk_test_BQokikJOvBiI2HlWgH4olfQ2: \
//                -d amount=500 \
//                -d currency=usd \
//                -d recipient=rp_1040gJ2eZvKYlo2Cs2z8Excq \
//                -d card=card_1045042eZvKYlo2CqHA03rvb \
//                -d description="Transfer to siddarth@stripe.com" \
//                -d statement_descriptor="Sales for 22 May 2014"
                break;
            case R.id.payment_googlepay:
                PaymentDataRequest request = createPaymentDataRequest();
                if (request != null) {
                    AutoResolveHelper.resolveTask(
                            paymentsClient.loadPaymentData(request),
                            PaymentSelection.this,
                            LOAD_PAYMENT_DATA_REQUEST_CODE);
                    // LOAD_PAYMENT_DATA_REQUEST_CODE is a constant integer of your choice,
                    // similar to what you would use in startActivityForResult
                }
                break;
            case R.id.payment_bank:

//                Stripe stripe = new Stripe(PaymentSelection.this);
//                stripe.setDefaultPublishableKey("pk_test_mjxYxMlj4K2WZfR6TwlHdIXW");
//                BankAccount bankAccount = new BankAccount("000123456789", "US", "usd", "110000000");
//                stripe.createBankAccountToken(bankAccount, new TokenCallback() {
//                    @Override
//                    public void onError(Exception error) {
//                        Log.e("Stripe Error", error.getMessage());
//                    }
//
//                    @Override
//                    public void onSuccess(com.stripe.android.model.Token token) {
//                        Log.e("Bank Token", token.getId());
//                        token.getBankAccount();
//
//                    }
//                });
                intent = new Intent(PaymentSelection.this, BankList.class);
                startActivity(intent);

                break;
        }
    }


    private void uploadContractor() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(PaymentSelection.this);
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
        sp.showProgressDialog(PaymentSelection.this);
        if(cr.isConnected(PaymentSelection.this)){
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
                    sp.setPreferencesObject(PaymentSelection.this, u1);
                    sp.setPreferences(PaymentSelection.this, "token", response.body().getToken());
                    User u2 = (User) sp.getPreferencesObject(PaymentSelection.this);
                    String t = sp.getPreferences(PaymentSelection.this, "token");

                    intent = new Intent(PaymentSelection.this, MainDrawer.class);
                    intent.putExtra("data", jsonString);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    sp.ShowDialog(PaymentSelection.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.i(TAG, "error register-->");
                sp.ShowDialog(PaymentSelection.this, "Server is down. Come back later!!");
            }
        });
        }else{
            sp.ShowDialog(PaymentSelection.this,"Please check your internet connection");
        }
    }




    private void CallApi() {
        String token = "Bearer " + sp.getPreferences(PaymentSelection.this, "token");
        int payment2 = Integer.parseInt(sp.getPreferences(PaymentSelection.this, "payment")) * 100;
        rc.setAmount(payment2);
        //String token=
        if(cr.isConnected(PaymentSelection.this)){
        mAPIService.createAcc(token, "US", "usd", "Jane Austen", "individual", "110000000", "000123456789").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "response register-->");
                intent = new Intent(PaymentSelection.this, BankList.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "response register-->");
            }
        });
        }else{
            sp.ShowDialog(PaymentSelection.this,"Please check your internet connection");
        }
    }

    private void call() {
        Card card = new Card("4242424242424242", 12, 2018, "123");
        Stripe stripe = new Stripe(PaymentSelection.this, "pk_test_mjxYxMlj4K2WZfR6TwlHdIXW");
        if(cr.isConnected(PaymentSelection.this)){
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onError(Exception error) {
                        Log.e("Stripe Error", error.getMessage());
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        Log.e("Bank Token", token.getId());
                        Intent intent = new Intent(PaymentSelection.this, MainDrawer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        token.getBankAccount();
                    }
                }
        );
        }else{
            sp.ShowDialog(PaymentSelection.this,"Please check your internet connection");
        }
    }

    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()
                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice("10.00")
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", "pk_test_mjxYxMlj4K2WZfR6TwlHdIXW")
                .addParameter("stripe:version", "5.1.0")
                .build();
    }


    private void getPayment() {
        //Getting the amount from editText
        // paymentAmount = editTextAmount.getText().toString();

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf("5")), "USD", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    String token = paymentData.getPaymentMethodToken().getToken();
                    if (selCon != null) {
                        uploadContractor();
                    } else {
                        intent = new Intent(PaymentSelection.this, MainDrawer.class);
                        startActivity(intent);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    // Log the status for debugging.
                    // Generally, there is no need to show an error to
                    // the user as the Google Payment API will do that.
                    break;
                default:
                    // Do nothing.
            }
        }

        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        if (selCon != null) {
                            uploadContractor();
                        } else {
                            intent = new Intent(PaymentSelection.this, MainDrawer.class);
                            startActivity(intent);
                        }


//                        uploadContractor();
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", 5));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
        LocalBroadcastManager.getInstance(PaymentSelection.this).unregisterReceiver(myBroadcastReceiver);

    }
    @Override
    protected void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(PaymentSelection.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(PaymentSelection.this).unregisterReceiver(myBroadcastReceiver);

    }


}
