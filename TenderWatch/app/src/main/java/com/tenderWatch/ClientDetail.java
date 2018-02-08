package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import com.tenderWatch.ClientDrawer.ClientDrawer;
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
import com.tenderWatch.utils.ConnectivityReceiver;

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
    ImageView close, flag, eRat1, eRat2, eRat3, eRat4, eRat5, fRat1, fRat2, fRat3, fRat4, fRat5;
    TextView email, mobile, country, occcupation, aboutMe, txtRate, txtDetailLabel;
    Sender obj;
    Api mApiService;
    private List Data;
    private static final ArrayList<String> falpha = new ArrayList<String>();
    private static final ArrayList<String> fcountryName = new ArrayList<String>();
    SharedPreference sp = new SharedPreference();
    private static final String TAG = ClientDetail.class.getSimpleName();
    String rate, uId;
    Button btnClientSubmit;
    User user;
    ConnectivityReceiver cr = new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_client_drawer);
        mApiService = ApiUtils.getAPIService();
        myBroadcastReceiver = new MyBroadcastReceiver();
        uId = getIntent().getStringExtra("uid");
        if (uId == null) {
            uId = getIntent().getStringExtra("uId");
        }
        txtDetailLabel = (TextView) findViewById(R.id.detailLabel);
        flag = (ImageView) findViewById(R.id.c_flag);
        eRat1 = (ImageView) findViewById(R.id.e_s1);
        eRat2 = (ImageView) findViewById(R.id.e_s2);
        eRat3 = (ImageView) findViewById(R.id.e_s3);
        eRat4 = (ImageView) findViewById(R.id.e_s4);
        eRat5 = (ImageView) findViewById(R.id.e_s5);
        fRat1 = (ImageView) findViewById(R.id.f_s1);
        fRat2 = (ImageView) findViewById(R.id.f_s2);
        fRat3 = (ImageView) findViewById(R.id.f_s3);
        fRat4 = (ImageView) findViewById(R.id.f_s4);
        fRat5 = (ImageView) findViewById(R.id.f_s5);
        close = (ImageView) findViewById(R.id.img_close);
        email = (TextView) findViewById(R.id.txt_client_email);
        mobile = (TextView) findViewById(R.id.txt_client_mobileNo);
        country = (TextView) findViewById(R.id.txt_client_country);
        occcupation = (TextView) findViewById(R.id.txt_client_occupation);
        aboutMe = (TextView) findViewById(R.id.txt_client_aboutme);
        clientImage = (CircleImageView) findViewById(R.id.client_circleView);
        btnClientSubmit = (Button) findViewById(R.id.btn_client_submit);
        txtRate = (TextView) findViewById(R.id.txt_avgRate);

        String role = sp.getPreferences(ClientDetail.this, "role");
        if (role.equals("client")) {
            txtDetailLabel.setText("Contractor Detail");
        } else {
            txtDetailLabel.setText("Client Detail");
        }

        eRat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fRat1.setVisibility(View.VISIBLE);
                fRat2.setVisibility(View.GONE);
                fRat3.setVisibility(View.GONE);
                fRat4.setVisibility(View.GONE);
                fRat5.setVisibility(View.GONE);
                rate = String.valueOf(1);
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
                rate = String.valueOf(2);
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
                rate = String.valueOf(3);
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
                rate = String.valueOf(4);
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
                rate = String.valueOf(5);
            }
        });

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
        String userId = uId;
        sp.showProgressDialog(ClientDetail.this);
        if (cr.isConnected(ClientDetail.this)) {
            mApiService.getUserDetail(token, userId).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    sp.hideProgressDialog();
                    Log.i(TAG, "post submitted to API." + response);
                    txtRate.setText(response.body().getAvg().toString() + "/5.0");
                    user = response.body();
                    FGetAllCountry();
                    DisplayDetail();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i(TAG, "post submitted to API." + t);
                }
            });
        } else {
            sp.ShowDialog(ClientDetail.this, "Please check your internet connection");
        }
    }

    private void callRatingApi() {
        String token = "Bearer " + sp.getPreferences(ClientDetail.this, "token");
        String clientId = user.getId();
        sp.showProgressDialog(ClientDetail.this);
        if (cr.isConnected(ClientDetail.this)) {
            mApiService.giveRating(token, clientId, rate).enqueue(new Callback<ResponseRating>() {
                @Override
                public void onResponse(Call<ResponseRating> call, Response<ResponseRating> response) {
                    sp.hideProgressDialog();
                    Log.i(TAG, "post submitted to API." + response);
                    ShowDialog2(ClientDetail.this, "Rating");
                }

                @Override
                public void onFailure(Call<ResponseRating> call, Throwable t) {
                    Log.i(TAG, "post submitted to API." + t);
                }
            });
        } else {
            sp.ShowDialog(ClientDetail.this, "Please check your internet connection");
        }
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

    private void FGetAllCountry() {
        sp.showProgressDialog(ClientDetail.this);
        if (cr.isConnected(ClientDetail.this)) {
            mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                    sp.hideProgressDialog();
                    Data = response.body();
                    for (int i = 0; i < Data.size(); i++) {
                        falpha.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getImageString());
                        fcountryName.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getCountryCode() + "~" + response.body().get(i).getImageString());
                    }
                    Collections.sort(falpha);
                    Collections.sort(fcountryName);
                    for (int i = 0; i < Data.size(); i++) {
                        if (fcountryName.get(i).split("~")[0].equals(user.getCountry())) {
                            country.setText(fcountryName.get(i).split("~")[0]);
                            Bitmap bitmap = StringToBitMap(fcountryName.get(i).split("~")[2]);
                            flag.setImageBitmap(bitmap);
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

                }
            });
        } else {
            sp.ShowDialog(ClientDetail.this, "Please check your internet connection");
        }
    }

    private void DisplayDetail() {
        if (!user.getProfilePhoto().equals("no image"))
            Picasso.with(this).load(user.getProfilePhoto()).into(clientImage);
        email.setText(user.getEmail());
        mobile.setText(user.getContactNo());
        occcupation.setText(user.getOccupation());
        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ClientDetail.this);
                dialog.setContentView(R.layout.client_detail);
                final TextView code = (TextView) dialog.findViewById(R.id.dialog_aboutMe);
                code.setText(user.getAboutMe());
                dialog.show();
            }
        });

        int r = user.getReview().getRating();
        if (r == 1) {
            fRat1.setVisibility(View.VISIBLE);
            fRat2.setVisibility(View.GONE);
            fRat3.setVisibility(View.GONE);
            fRat4.setVisibility(View.GONE);
            fRat5.setVisibility(View.GONE);
        } else if (r == 2) {
            fRat1.setVisibility(View.VISIBLE);
            fRat2.setVisibility(View.VISIBLE);
            fRat3.setVisibility(View.GONE);
            fRat4.setVisibility(View.GONE);
            fRat5.setVisibility(View.GONE);
        } else if (r == 3) {
            fRat1.setVisibility(View.VISIBLE);
            fRat2.setVisibility(View.VISIBLE);
            fRat3.setVisibility(View.VISIBLE);
            fRat4.setVisibility(View.GONE);
            fRat5.setVisibility(View.GONE);
        } else if (r == 4) {
            fRat1.setVisibility(View.VISIBLE);
            fRat2.setVisibility(View.VISIBLE);
            fRat3.setVisibility(View.VISIBLE);
            fRat4.setVisibility(View.VISIBLE);
            fRat5.setVisibility(View.GONE);
        } else if (r == 5) {
            fRat1.setVisibility(View.VISIBLE);
            fRat2.setVisibility(View.VISIBLE);
            fRat3.setVisibility(View.VISIBLE);
            fRat4.setVisibility(View.VISIBLE);
            fRat5.setVisibility(View.VISIBLE);
        } else {
            fRat1.setVisibility(View.GONE);
            fRat2.setVisibility(View.GONE);
            fRat3.setVisibility(View.GONE);
            fRat4.setVisibility(View.GONE);
            fRat5.setVisibility(View.GONE);
        }


    }


    private void ShowDialog2(Context context, String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Tender Watch");
        builder.setMessage(Msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent;
                        if (sp.getPreferences(ClientDetail.this, "role").equals("client")) {
                            intent = new Intent(ClientDetail.this, ClientDrawer.class);
                        } else {
                            intent = new Intent(ClientDetail.this, MainDrawer.class);
                        }
                        startActivity(intent);
                        //  Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
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
        LocalBroadcastManager.getInstance(ClientDetail.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(ClientDetail.this).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(ClientDetail.this).unregisterReceiver(myBroadcastReceiver);
    }
}
