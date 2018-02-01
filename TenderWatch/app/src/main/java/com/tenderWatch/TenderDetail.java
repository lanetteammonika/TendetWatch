package com.tenderWatch;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.GetTenderDetail;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.UpdateTender;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom47 on 1/2/18.
 */

public class TenderDetail extends AppCompatActivity {
    Api mApiService;
    private static final String TAG = TenderDetail.class.getSimpleName();

    GetTenderDetail object;

    ImageView flag3, imagetender;

    TextView tenderTitle, Country, Category, ExpDay, Description, City, Contact, LandLine, Email, Address;
    LinearLayout rlEmail, rlContact, rlLandline, rlAddress;
    Button removeTender, editTender;
    SharedPreference sp = new SharedPreference();
    ConnectivityReceiver cr = new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_tender_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tender Detail");
        mApiService = ApiUtils.getAPIService();
        myBroadcastReceiver = new MyBroadcastReceiver();
        String id = getIntent().getStringExtra("id");
        String token = "Bearer " + sp.getPreferences(TenderDetail.this, "token");
        mApiService.getTender2(token, id).enqueue(new Callback<GetTenderDetail>() {
            @Override
            public void onResponse(Call<GetTenderDetail> call, Response<GetTenderDetail> response) {
                Log.v(TAG, String.valueOf(response.body()));
                object = response.body();
                InitView();
            }

            @Override
            public void onFailure(Call<GetTenderDetail> call, Throwable t) {
                Log.v(TAG, String.valueOf(t));
            }
        });

    }

    private void InitView() {
        tenderTitle = (TextView) findViewById(R.id.preview_tender_title);
        Country = (TextView) findViewById(R.id.preview_country_name);
        Category = (TextView) findViewById(R.id.preview_category);
        ExpDay = (TextView) findViewById(R.id.preview_exp);
        Description = (TextView) findViewById(R.id.preview_description);
        City = (TextView) findViewById(R.id.preview_tender_city);
        Contact = (TextView) findViewById(R.id.preview_tender_mobile);
        LandLine = (TextView) findViewById(R.id.preview_tender_landline);
        Email = (TextView) findViewById(R.id.preview_tender_email);
        Address = (TextView) findViewById(R.id.preview_tender_address);
        rlAddress = (LinearLayout) findViewById(R.id.rl_preview_address);
        rlContact = (LinearLayout) findViewById(R.id.rl_preview_mobile);
        rlLandline = (LinearLayout) findViewById(R.id.rl_preview_landline);
        rlEmail = (LinearLayout) findViewById(R.id.rl_preview_email);
        removeTender = (Button) findViewById(R.id.remove_tender);
        editTender = (Button) findViewById(R.id.edit_tender);
        imagetender = (ImageView) findViewById(R.id.preview_tender_image);
        flag3 = (ImageView) findViewById(R.id.preview_flag_image);

        if (object != null) {
            tenderTitle.setText(object.getTenderName());


            Description.setText(object.getDescription());
            City.setText(object.getCity());
            if (object.getContactNo().equals("")) {
                rlContact.setVisibility(View.GONE);
            } else {
                Contact.setText(object.getContactNo());
            }

            if (object.getLandlineNo().equals("")) {
                rlLandline.setVisibility(View.GONE);
            } else {
                LandLine.setText(object.getLandlineNo());
            }

            if (object.getEmail().equals("")) {
                rlEmail.setVisibility(View.GONE);
            } else {
                Email.setText(object.getEmail());
            }

            if (object.getAddress().equals("")) {
                rlAddress.setVisibility(View.GONE);
            } else {
                Address.setText(object.getAddress());
            }
            // Category.setText(object.getCategory().getCategoryName());
            Country.setText(object.getCountry().getCountryName());
            Bitmap flag = StringToBitMap(object.getCountry().getImageString());
            SpannableStringBuilder ssb = new SpannableStringBuilder(object.getCategory().getCategoryName() + " ");
            Bitmap smiley = StringToBitMap(object.getCategory().getImgString());
            smiley = Bitmap.createScaledBitmap(smiley, 60, 60, false);
            ImageSpan imagespan = new ImageSpan(TenderDetail.this, smiley);
            ssb.setSpan(imagespan, ssb.length() - 1, ssb.length(), 0);
            Category.setText(ssb);
            flag3.setImageBitmap(flag);
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();

            String formattedDate = null;
            formattedDate = df.format(c.getTime());

            Date startDateValue = null;
            Date endDateValue = null;

            try {
                startDateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                endDateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(object.getExpiryDate().split("T")[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Date endDateValue = new Date(allTender.get(position).getExpiryDate().split("T")[0]);
            long diff = endDateValue.getTime() - startDateValue.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = (hours / 24) + 1;
            ExpDay.setText(days + " days");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Intent intent = new Intent(TenderDetail.this, MainDrawer.class);
            startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(TenderDetail.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(TenderDetail.this).unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(TenderDetail.this).unregisterReceiver(myBroadcastReceiver);
    }
}
