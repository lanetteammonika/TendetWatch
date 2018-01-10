package com.tenderWatch;

import android.accounts.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.braintreepayments.cardform.view.CardForm;
import com.stripe.android.RequestOptions;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import java.util.HashMap;
import java.util.Map;

public class CardDemoDesign extends AppCompatActivity {
    Button btnClick;
    String number,cvc;
    int expMonth,expYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_demo_design);
        btnClick=(Button) findViewById(R.id.btnClick);
        final CardForm cardForm = (CardForm) findViewById(R.id.card_form);
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
                        token.getBankAccount();

                    }
                }
        );

    }

}

