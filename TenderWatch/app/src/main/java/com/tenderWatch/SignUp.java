package com.tenderWatch;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lcom48 on 5/12/17.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    Intent intent;
    RelativeLayout rlcountry;
    EditText country,mobileNo,aboutMe;
    String countryName,countryCode;
    ArrayList<String> empNo;
    TextView txtcountryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup2);
        InitView();
        InitListener();


       // show.getCharExtra()


    }

    private void InitListener() {
        rlcountry.setOnClickListener(this);
        aboutMe.setOnClickListener(this);
    }

    private void InitView() {
       rlcountry=(RelativeLayout) findViewById(R.id.selectCountry);
       country=(EditText) findViewById(R.id.country);
       txtcountryCode=(EditText) findViewById(R.id.mobileNo) ;
       aboutMe=(EditText) findViewById(R.id.edt_aboutme);
        Intent show = getIntent();
        empNo = show.getStringArrayListExtra("Country");
        if(empNo != null) {
            countryName = empNo.get(0).toString().split("~")[0];
            countryCode = empNo.get(0).toString().split("~")[1];
        }
       if(empNo != null) {
           country.setText(countryName);
           txtcountryCode.setText(countryCode+'-');
       }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.selectCountry:
                intent = new Intent(SignUp.this, CountryList.class);
                intent.putExtra("check","signup");
                startActivity(intent);
                break;
            case R.id.edt_aboutme:
                intent = new Intent(SignUp.this, AboutMe.class);
                intent.putExtra("check","signup");
                startActivity(intent);
                break;

        }
    }
}
