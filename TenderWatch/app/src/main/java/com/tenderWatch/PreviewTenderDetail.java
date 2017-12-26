package com.tenderWatch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.ClientDrawer.PreviewTender;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final String TAG = PreviewTender.class.getSimpleName();
    private List Data, Data2;
    Tender object;
    String day,flag,countryName1,categoryName1;
    Bitmap Bflag;
    ImageView flag3,imagetender;

    TextView tenderTitle,Country,Category,ExpDay,Description,City,Contact,LandLine,Email,Address;
    RelativeLayout rlEmail,rlContact,rlLandline,rlAddress;
    Button removeTender,editTender;
    SharedPreference sp=new SharedPreference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_tender_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tender Detail");
        mApiService= ApiUtils.getAPIService();
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
        String json=getIntent().getStringExtra("data");
        Gson gson=new Gson();
        object=gson.fromJson(json, Tender.class);
        editTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(object);

                Intent intent = new Intent(PreviewTenderDetail.this,EditTenderDetail.class);
                intent.putExtra("data",jsonString);
                startActivity(intent);
//                Bundle arguments = new Bundle();
//
//                arguments.putParcelable( "object" , object);
//
//                arguments.putString("day", String.valueOf(day));
//                fragment2.setArguments(arguments);
//                fragmentTransaction.replace(R.id.content_frame, fragment2);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

        removeTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token="Bearer "+sp.getPreferences(PreviewTenderDetail.this,"token");
                String id=object.getId().toString();
                mApiService.removeTender(token,id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i(TAG,"response---"+response.body());
                        Intent intent = new Intent(PreviewTenderDetail.this,ClientDrawer.class);
                       // intent.putExtra("data",jsonString);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG,"response---"+t);

                    }
                });

            }
        });


//        Bundle args = PreviewTenderDetail.this;
//        if (args != null) {
//            object = args.getParcelable("object");
//            day=args.getString("day");
//        } else {
//            Log.w("GetObject", "Arguments expected, but missing");
//        }
        if(!object.getTenderPhoto().toString().equals("")){
            Picasso.with(PreviewTenderDetail.this)
                    .load(object.getTenderPhoto().toString())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            Log.v("Main", String.valueOf(bitmap));
                            // main = bitmap;
                            imagetender.setImageBitmap(bitmap);
                            //  imagetender.getLayoutParams().height = 300;
                            //imagetender.getLayoutParams().width = 100;
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

        ExpDay.setText(day+" days");
        Description.setText(object.getDescription().toString());
        City.setText(object.getCity().toString());
        if(object.getContactNo().toString().equals("")){
            rlContact.setVisibility(View.GONE);
        }else{
            Contact.setText(object.getContactNo().toString());}

        if(object.getLandlineNo().toString().equals("")){
            rlLandline.setVisibility(View.GONE);
        }else{
            LandLine.setText(object.getLandlineNo().toString());
        }

        if(object.getEmail().toString().equals("")){
            rlEmail.setVisibility(View.GONE);
        }else{
            Email.setText(object.getEmail().toString());
        }

        if(object.getAddress().toString().equals("")){
            rlAddress.setVisibility(View.GONE);
        }else{
            Address.setText(object.getAddress().toString());
        }


        flag3=(ImageView) findViewById(R.id.preview_flag_image);

    }

    private void GetCategory() {
        mApiService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                Data2 = response.body();
                for (int i = 0; i < Data2.size(); i++) {
                    alpha2.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getImgString().toString());
                    categoryName.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getId().toString());

                    // CountryFlag.add(response.body().get(i).getImageString().toString());
                }
                //Collections.sort(alpha);
                for (int i = 0; i < Data2.size(); i++) {
                    if(categoryName.get(i).split("~")[1].toString().equals(object.getCategory().toString())){

                        categoryName1=response.body().get(i).getCategoryName().toString();
                        if(categoryName1.length()>45){
                            Category.setText(categoryName1.substring(0,45)+"...");
                        }else {
                            Category.setText(categoryName1);

                        }
                        break;
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {

            }
        });
    }

    private void GetAllCountry() {
        mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                Data = response.body();
                for (int i = 0; i < Data.size(); i++) {
                    alpha.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getImageString().toString());
                    countryName.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getCountryCode().toString() + "~" + response.body().get(i).getId().toString());

                    // CountryFlag.add(response.body().get(i).getImageString().toString());
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
                        break;
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });

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


}
