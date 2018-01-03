package com.tenderWatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lcom47 on 3/1/18.
 */

public class ClientDetail extends AppCompatActivity {
    ImageView flag,eRat1,eRat2,eRat3,eRat4,eRat5,fRat1,fRat2,fRat3,fRat4,fRat5;
    TextView email,mobile,country,occcupation,aboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_client_drawer);
        flag=(ImageView) findViewById(R.id.c_flag);
        eRat1=(ImageView) findViewById(R.id.e_s1);
        eRat2=(ImageView) findViewById(R.id.e_s2);
        eRat3=(ImageView) findViewById(R.id.e_s3);
        eRat4=(ImageView) findViewById(R.id.e_s4);
        eRat5=(ImageView) findViewById(R.id.e_s5);
        fRat1=(ImageView) findViewById(R.id.f_s1);
        fRat2=(ImageView) findViewById(R.id.f_s2);
        fRat3=(ImageView) findViewById(R.id.f_s3);
        fRat4=(ImageView) findViewById(R.id.f_s4);
        fRat5=(ImageView) findViewById(R.id.f_s5);
        email=(TextView) findViewById(R.id.txt_client_email);
        mobile=(TextView) findViewById(R.id.txt_client_mobileNo);
        country=(TextView) findViewById(R.id.txt_client_country);
        occcupation=(TextView) findViewById(R.id.txt_client_occupation);
        aboutMe=(TextView) findViewById(R.id.txt_client_aboutme);

        eRat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.GONE);
                fRat3.setVisibility(View.GONE);
                fRat4.setVisibility(View.GONE);
                fRat5.setVisibility(View.GONE);
            }
        });
        eRat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.VISIBLE);
                fRat3.setVisibility(View.GONE);
                fRat4.setVisibility(View.GONE);
                fRat5.setVisibility(View.GONE);
            }
        });
        eRat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.VISIBLE);
                fRat3.setVisibility(View.VISIBLE);
                fRat4.setVisibility(View.GONE);
                fRat5.setVisibility(View.GONE);
            }
        });
        eRat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.VISIBLE);
                fRat3.setVisibility(View.VISIBLE);
                fRat4.setVisibility(View.VISIBLE);
                fRat5.setVisibility(View.GONE);
            }
        });
        eRat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.VISIBLE);
                fRat3.setVisibility(View.VISIBLE);
                fRat4.setVisibility(View.VISIBLE);
                fRat5.setVisibility(View.VISIBLE);
            }
        });
    }
}
