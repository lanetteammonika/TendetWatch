package com.tenderWatch;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewTenderDetail extends AppCompatActivity {
    Api mApiService;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> countryName = new ArrayList<String>();

    private static final ArrayList<String> alpha2 = new ArrayList<String>();
    private static final ArrayList<String> categoryName = new ArrayList<String>();
    private static final String TAG = PreviewTenderDetail.class.getSimpleName();
    private List Data, Data2;
    Tender object;
    String day,flag,countryName1,categoryName1;
    Bitmap Bflag,catBflag;
    ImageView flag3,imagetender,catFlag;

    TextView tenderTitle,Country,Category,ExpDay,Description,City,Contact,LandLine,Email,Address;
    RelativeLayout rlEmail,rlContact,rlLandline,rlAddress;
    Button removeTender,editTender;
    SharedPreference sp=new SharedPreference();
    ConnectivityReceiver cr=new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_tender_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tender Detail");
        mApiService= ApiUtils.getAPIService();
        myBroadcastReceiver=new MyBroadcastReceiver();
        tenderTitle=(TextView) findViewById(R.id.preview_tender_title);
        Country=(TextView) findViewById(R.id.preview_country_name);
        Category=(TextView) findViewById(R.id.preview_category);
        ExpDay=(TextView) findViewById(R.id.preview_exp);
        Description=(TextView) findViewById(R.id.preview_description);
        City=(TextView) findViewById(R.id.preview_tender_city);
        Contact=(TextView) findViewById(R.id.preview_tender_mobile);
        LandLine=(TextView) findViewById(R.id.preview_tender_landline);
        Email=(TextView) findViewById(R.id.preview_tender_email);
        Address=(TextView) findViewById(R.id.preview_tender_address);
        rlAddress=(RelativeLayout) findViewById(R.id.rl_preview_address);
        rlContact=(RelativeLayout) findViewById(R.id.rl_preview_mobile);
        rlLandline=(RelativeLayout) findViewById(R.id.rl_preview_landline);
        rlEmail=(RelativeLayout) findViewById(R.id.rl_preview_email);
        removeTender=(Button) findViewById(R.id.remove_tender);
        editTender=(Button) findViewById(R.id.edit_tender);
        imagetender=(ImageView) findViewById(R.id.preview_tender_image);
        catFlag=(ImageView) findViewById(R.id.preview_catflag_image);
        String json=getIntent().getStringExtra("data");
        Gson gson=new Gson();
        object=gson.fromJson(json, Tender.class);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("yyyy-MM-dd");
        }
        String formattedDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            formattedDate = df.format(c.getTime());
        }
        Date startDateValue = null,endDateValue = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
            }
            //  startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(tenderList.get(position).getCreatedAt().split("T")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                endDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(object.getExpiryDate().split("T")[0]);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Date endDateValue = new Date(allTender.get(position).getExpiryDate().split("T")[0]);
        long diff = endDateValue.getTime() - startDateValue.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = (hours / 24) + 1;
        editTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(object);

                Intent intent = new Intent(PreviewTenderDetail.this,EditTenderDetail.class);
                intent.putExtra("data",jsonString);
                startActivity(intent);
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(object);
                String role=sp.getPreferences(PreviewTenderDetail.this,"role");
                Intent intent;
                if(role.equals("client")) {
                    intent = new Intent(PreviewTenderDetail.this, ClientDrawer.class);
                }else{
                    intent = new Intent(PreviewTenderDetail.this, MainDrawer.class);
                }
                intent.putExtra("data",jsonString);
                startActivity(intent);
            }
        });
        removeTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token="Bearer "+sp.getPreferences(PreviewTenderDetail.this,"token");
                String id=object.getId().toString();
                sp.showProgressDialog(PreviewTenderDetail.this);
if(cr.isConnected(PreviewTenderDetail.this)){
                mApiService.removeTender(token,id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        sp.hideProgressDialog();
                        Log.i(TAG,"response---"+response.body());
                        Intent intent = new Intent(PreviewTenderDetail.this,ClientDrawer.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG,"response---"+t);

                    }
                });
}else{
    sp.ShowDialog(PreviewTenderDetail.this,"Please check your internet connection");
}
            }
        });

       if(!object.getTenderPhoto().toString().equals("")){
            Picasso.with(PreviewTenderDetail.this)
                    .load(object.getTenderPhoto().toString())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            Log.v("Main", String.valueOf(bitmap));
                            imagetender.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.v("Main", "errrorrrr");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

        }
        GetCategory();
        GetAllCountry();
        tenderTitle.setText(object.getTenderName().toString());

        ExpDay.setText(days+" days");
        Description.setText(object.getDescription().toString());
        City.setText(object.getCity().toString());
        if(object.getContactNo().toString().equals("")){
            rlContact.setVisibility(View.GONE);
        }else{
            Contact.setText(object.getContactNo().toString());}

        if(object.getLandlineNo().equals("")){
            rlLandline.setVisibility(View.GONE);
        }else{
            LandLine.setText(object.getLandlineNo());
        }

        if(object.getEmail().equals("")){
            rlEmail.setVisibility(View.GONE);
        }else{
            Email.setText(object.getEmail());
        }

        if(object.getAddress().equals("")){
            rlAddress.setVisibility(View.GONE);
        }else{
            Address.setText(object.getAddress());
        }
        flag3=(ImageView) findViewById(R.id.preview_flag_image);
    }

    private void GetCategory() {
        sp.showProgressDialog(PreviewTenderDetail.this);
if(cr.isConnected(PreviewTenderDetail.this)){
        mApiService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                Data2 = response.body();
                for (int i = 0; i < Data2.size(); i++) {
                    alpha2.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getImgString().toString());
                    categoryName.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getId().toString());
                }

                for (int i = 0; i < Data2.size(); i++) {
                    if(categoryName.get(i).split("~")[1].toString().equals(object.getCategory().toString())){
                        categoryName1=response.body().get(i).getCategoryName().toString();

                            Category.setText(categoryName1);
                        day=response.body().get(i).getImgString();
                        catBflag = StringToBitMap(day);
                        catFlag.setImageBitmap(catBflag);
                        sp.hideProgressDialog();

                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {

            }
        });
}else{
    sp.ShowDialog(PreviewTenderDetail.this,"Please check your internet connection");
}
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                String role=sp.getPreferences(PreviewTenderDetail.this,"role");
                Intent i;
                if(role.equals("client")){
                    i=new Intent(PreviewTenderDetail.this, ClientDrawer.class);
                }else{
                    i=new Intent(PreviewTenderDetail.this, MainDrawer.class);
                }
                i.putExtra("nav_not","true");
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void GetAllCountry() {
        sp.showProgressDialog(PreviewTenderDetail.this);
if(cr.isConnected(PreviewTenderDetail.this)){
        mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {

                Data = response.body();
                for (int i = 0; i < Data.size(); i++) {
                    alpha.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getImageString().toString());
                    countryName.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getCountryCode().toString() + "~" + response.body().get(i).getId().toString());
                }
                Collections.sort(alpha);
                Collections.sort(countryName);
                for (int i = 0; i < Data2.size(); i++) {
                    if(countryName.get(i).split("~")[2].toString().equals(object.getCountry().toString())){
                        flag=response.body().get(i).getImageString().toString();
                        countryName1=response.body().get(i).getCountryName().toString();
                        Country.setText(countryName1);
                        Bflag = StringToBitMap(flag);
                        flag3.setImageBitmap(Bflag);
                        sp.hideProgressDialog();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });
}else{
    sp.ShowDialog(PreviewTenderDetail.this,"Please check your internet connection");
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
    @Override
    protected void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(PreviewTenderDetail.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(PreviewTenderDetail.this).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(PreviewTenderDetail.this).unregisterReceiver(myBroadcastReceiver);
    }
}
