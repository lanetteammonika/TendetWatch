package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Card;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.PayPalConfig;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSelection extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PaymentSelection";

    Button btnPaypal, btnCreditCard, btnGooglePay, btnBank, btnPesaPal;
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
    Api mApiService;
    CustomList countryAdapter;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2, down_arrow3, up_arrow3, tenderImage;
    LinearLayout country_home, llbankType;
    ListView spinner, spinnerbanktype;
    private ArrayAdapter<String> listAdapter;
    SharedPreference sp = new SharedPreference();
    WebView mWebViewPesaPal;
    LinearLayout mLlPbLoader;
    private HashMap<String, ArrayList<String>> selection;
    private int amount = 0;
    LinearLayout llToolbar;
    public static HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private int subscriptionType = 1;
    LinearLayout llPaymentSucess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        getDataFromIntent();

        btnPaypal = (Button) findViewById(R.id.payment_paypal);
        btnCreditCard = (Button) findViewById(R.id.payment_creditcard);
        btnGooglePay = (Button) findViewById(R.id.payment_googlepay);
        btnBank = (Button) findViewById(R.id.payment_bank);
        btnPesaPal = (Button) findViewById(R.id.payment_pesapal);
        mWebViewPesaPal = (WebView) findViewById(R.id.webview_pesapal);
        mLlPbLoader = (LinearLayout) findViewById(R.id.ll_pb_loader);
        llToolbar = (LinearLayout) findViewById(R.id.ll_toolbar);
        llPaymentSucess = (LinearLayout) findViewById(R.id.ll_payment_success);

        mWebViewPesaPal.setVisibility(View.GONE);
        mLlPbLoader.setVisibility(View.GONE);
        llPaymentSucess.setVisibility(View.GONE);

        btnPaypal.setOnClickListener(this);
        btnCreditCard.setOnClickListener(this);
        btnGooglePay.setOnClickListener(this);
        btnBank.setOnClickListener(this);
        btnPesaPal.setOnClickListener(this);
        llToolbar.setOnClickListener(this);

        mApiService = ApiUtils.getAPIService();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        paymentsClient =
                Wallet.getPaymentsClient(this,
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());
    }

    public void getDataFromIntent() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getSerializable("selections") != null) {
                selection = (HashMap<String, ArrayList<String>>) getIntent().getExtras().getSerializable("selections");
            }
            amount = getIntent().getExtras().getInt("amount", 0);
            subscriptionType = getIntent().getExtras().getInt("subscriptionType", 1);
        }
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
                Stripe stripe = new Stripe(PaymentSelection.this);
                stripe.setDefaultPublishableKey("pk_test_mjxYxMlj4K2WZfR6TwlHdIXW");
                BankAccount bankAccount = new BankAccount("000123456789", "US", "usd", "110000000");
                stripe.createBankAccountToken(bankAccount, new TokenCallback() {
                    @Override
                    public void onError(Exception error) {
                        Log.e("Stripe Error", error.getMessage());
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        Log.e("Bank Token", token.getId());
                        token.getBankAccount();

                    }
                });
                //GetBankDetail();
                break;
            case R.id.payment_pesapal:
                Intent intent = new Intent(PaymentSelection.this, PesapalListActivity.class);
                intent.putExtra("selections", selection);
                intent.putExtra("amount", amount);
                intent.putExtra("subscriptionType", subscriptionType);
                startActivity(intent);
                break;
            case R.id.ll_toolbar:
                onBackPressed();
                break;
        }
    }

    private void GetBankDetail() {
        final Dialog dialog = new Dialog(PaymentSelection.this);
        dialog.setContentView(R.layout.bankdetail);

        spinner = (ListView) dialog.findViewById(R.id.spinner3);
        country_home = (LinearLayout) dialog.findViewById(R.id.category_home);
        down_arrow = (ImageView) dialog.findViewById(R.id.bank_down_arrow);
        up_arrow = (ImageView) dialog.findViewById(R.id.bank_up_arrow);
        down_arrow2 = (ImageView) dialog.findViewById(R.id.bank_down_arrow2);
        up_arrow2 = (ImageView) dialog.findViewById(R.id.bank_up_arrow2);
        spinnerbanktype = (ListView) dialog.findViewById(R.id.spinner4);
        llbankType = (LinearLayout) dialog.findViewById(R.id.bank_type);
        String[] planets = new String[]{"Individual", "Company"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));
        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
        sp.showProgressDialog(PaymentSelection.this);

        mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                sp.hideProgressDialog();
                Data = response.body();
                for (int i = 0; i < Data.size(); i++) {
                    alpha.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getImageString());
                    countryName.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getCountryCode() + "~" + response.body().get(i).getId());
                }
                Collections.sort(alpha);
                Collections.sort(countryName);
                spinnerbanktype.setAdapter(listAdapter);
                countryAdapter = new CustomList(PaymentSelection.this, alpha);
                spinner.setAdapter(countryAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });

        up_arrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                country_home.setVisibility(View.GONE);
                up_arrow.setVisibility(View.INVISIBLE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                country_home.setVisibility(View.VISIBLE);
                up_arrow.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.INVISIBLE);
            }
        });

        up_arrow2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                down_arrow2.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });

        down_arrow2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                llbankType.setVisibility(View.VISIBLE);
                up_arrow2.setVisibility(View.VISIBLE);
                down_arrow2.setVisibility(View.INVISIBLE);
            }
        });
        dialog.show();
    }

    private void call() {
        Card card = new Card("4242424242424242", 12, 2018, "123");
        Stripe stripe = new Stripe(PaymentSelection.this, "pk_test_mjxYxMlj4K2WZfR6TwlHdIXW");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onError(Exception error) {
                        Log.e("Stripe Error", error.getMessage());
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        Log.e("Bank Token", token.getId());
                        token.getBankAccount();
                    }
                }
        );
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
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", 5));

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
    }
}
