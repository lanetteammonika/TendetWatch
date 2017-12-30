package com.tenderWatch;

import android.app.Activity;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.tenderWatch.SharedPreference.PayPalConfig;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Arrays;

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
//                new MercadoPago.StartActivityBuilder()
//                    .setActivity(PaymentSelection.this)
//                    .setPublicKey("pk_test_mjxYxMlj4K2WZfR6TwlHdIXW")
//                    .setAmount(BigDecimal.valueOf(15))
//                    .setSite(Sites.USA)
//                    .setInstallmentsEnabled(true)
//                    .startCardVaultActivity();
                call();
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
                break;
        }
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
