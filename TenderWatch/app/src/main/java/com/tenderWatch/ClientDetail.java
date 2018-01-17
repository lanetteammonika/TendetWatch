package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Drawer.Notification;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.ResponseRating;
import com.tenderWatch.Models.Sender;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom47 on 3/1/18.
 */

public class ClientDetail extends AppCompatActivity {
    CircleImageView clientImage;
    ImageView close,flag,eRat1,eRat2,eRat3,eRat4,eRat5,fRat1,fRat2,fRat3,fRat4,fRat5;
    TextView email,mobile,country,occcupation,aboutMe,txtRate;
    String sender,jsonString;
    Sender obj;
    Api mApiService;
    private List Data;
    private static final ArrayList<String> falpha = new ArrayList<String>();
    private static final ArrayList<String> fcountryName = new ArrayList<String>();
    SharedPreference sp=new SharedPreference();
    private static final String TAG = ClientDetail.class.getSimpleName();
    String rate;
    Button btnClientSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_client_drawer);
        mApiService= ApiUtils.getAPIService();
        sender=getIntent().getStringExtra("sender");
        jsonString=getIntent().getStringExtra("data");
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
        close=(ImageView) findViewById(R.id.img_close);
        email=(TextView) findViewById(R.id.txt_client_email);
        mobile=(TextView) findViewById(R.id.txt_client_mobileNo);
        country=(TextView) findViewById(R.id.txt_client_country);
        occcupation=(TextView) findViewById(R.id.txt_client_occupation);
        aboutMe=(TextView) findViewById(R.id.txt_client_aboutme);
        clientImage=(CircleImageView) findViewById(R.id.client_circleView);
        btnClientSubmit=(Button) findViewById(R.id.btn_client_submit);
        txtRate=(TextView) findViewById(R.id.txt_avgRate);

        eRat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.GONE);
                fRat3.setVisibility(View.GONE);
                fRat4.setVisibility(View.GONE);
                fRat5.setVisibility(View.GONE);
                rate= String.valueOf(1);
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
                rate= String.valueOf(2);
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
                rate= String.valueOf(3);
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
                rate= String.valueOf(4);
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
                rate= String.valueOf(5);
            }
        });
        DisplayDetail();
        GetUser();
        btnClientSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRatingApi();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void GetUser() {
        String token = "Bearer " + sp.getPreferences(ClientDetail.this, "token");
        String userId=obj.getId().toString();
        sp.showProgressDialog(ClientDetail.this);

        mApiService.getUserDetail(token,userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                sp.hideProgressDialog();
                Log.i(TAG, "post submitted to API." + response);
                txtRate.setText(response.body().getAvg().toString()+"/5.0");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);
            }
        });
    }


    private void callRatingApi() {
        String token="Bearer " + sp.getPreferences(ClientDetail.this,"token");
        String clientId=obj.getId().toString();
        sp.showProgressDialog(ClientDetail.this);

        mApiService.giveRating(token,clientId,rate).enqueue(new Callback<ResponseRating>() {
            @Override
            public void onResponse(Call<ResponseRating> call, Response<ResponseRating> response) {
                sp.hideProgressDialog();
                Log.i(TAG, "post submitted to API." + response);
                Intent intent = new Intent(ClientDetail.this,MainDrawer.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseRating> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);
            }
        });
    }

    private void FGetAllCountry() {
        sp.showProgressDialog(ClientDetail.this);

        mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
               sp.hideProgressDialog();
                Data = response.body();
                for (int i = 0; i < Data.size(); i++) {
                    falpha.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getImageString().toString());
                    fcountryName.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getCountryCode().toString() + "~" + response.body().get(i).getImageString().toString());
                }
                Collections.sort(falpha);
                Collections.sort(fcountryName);
                for (int i = 0; i < Data.size(); i++) {
                    if(fcountryName.get(i).split("~")[0].toString().equals(obj.getCountry().toString())){
                        country.setText(fcountryName.get(i).split("~")[0].toString());
                        Bitmap bitmap=StringToBitMap(fcountryName.get(i).split("~")[2].toString());
                        flag.setImageBitmap(bitmap);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void DisplayDetail() {

        Gson gson=new Gson();
        obj=gson.fromJson(sender, Sender.class);
        Picasso.with(this).load(obj.getProfilePhoto()).into(clientImage);FGetAllCountry();
        email.setText(obj.getEmail().toString());
        mobile.setText(obj.getContactNo().toString());
        occcupation.setText(obj.getOccupation().toString());
        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ClientDetail.this);
                dialog.setContentView(R.layout.client_detail);
                final TextView code=(TextView) dialog.findViewById(R.id.dialog_aboutMe);
                code.setText(obj.getAboutMe().toString());
                dialog.show();
            }
        });

    }

}
