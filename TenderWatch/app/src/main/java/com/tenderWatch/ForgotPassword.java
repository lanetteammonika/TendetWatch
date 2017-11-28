package com.tenderWatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tenderWatch.Models.LoginPost;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.Validation.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{
    private Button btnSubmit;
    private EditText txtEmail;
    private static final String TAG = Login.class.getSimpleName();
    private Api mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        InitView();
        InitListener();
    }

    private void InitListener() {
        btnSubmit.setOnClickListener(this);
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
    }

    private void InitView() {
        btnSubmit=(Button) findViewById(R.id.btn_submit);
        txtEmail=(EditText) findViewById(R.id.txt_forgotemail);
        mAPIService = ApiUtils.getAPIService();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_submit:
               // Toast.makeText(ForgotPassword.this, "Form contains not error", Toast.LENGTH_LONG).show();

                if ( checkValidation () ){
                    forgotPassword();
                    Toast.makeText(ForgotPassword.this, "Form contains not error", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(ForgotPassword.this, "Form contains error", Toast.LENGTH_LONG).show();
                break;

        }
    }
    private boolean checkValidation() {
        boolean ret = true;

        // if (!Validation.hasText(etNormalText)) ret = false;
        if (!Validation.isEmailAddress(txtEmail, true)) ret = false;


        return ret;
    }
    private void forgotPassword() {
        String email=txtEmail.getText().toString();
        String role="contractor";
        mAPIService.forgotPassword(email,role).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {

                if(response.isSuccessful()) {
                    // showResponse(response.body().toString());
                    int res=response.code();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginPost> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}
