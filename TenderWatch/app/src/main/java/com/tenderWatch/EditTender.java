package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.crazyorr.zoomcropimage.CropShape;
import com.github.crazyorr.zoomcropimage.ZoomCropImageActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.ClientDrawer.Home;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.GetTenderDetail;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.UpdateTender;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.Validation.MyScrollView;
import com.tenderWatch.Validation.Validation;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom47 on 2/2/18.
 */

public class EditTender extends AppCompatActivity {
    Api mApiService;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> countryName = new ArrayList<String>();

    private static final ArrayList<String> alpha2 = new ArrayList<String>();
    private static final ArrayList<String> categoryName = new ArrayList<String>();
    private static final ArrayList<String> falpha = new ArrayList<String>();
    private static final ArrayList<String> fcountryName = new ArrayList<String>();

    private static final ArrayList<String> falpha2 = new ArrayList<String>();
    private static final ArrayList<String> fcategoryName = new ArrayList<String>();
    private static final String TAG = EditTender.class.getSimpleName();
    private List Data, Data2;
    CustomList countryAdapter, categoryAdapter;
    ListView spinner, spinner2;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2, down_arrow3, up_arrow3,tenderImage;
    LinearLayout country_home, category_home;
    TextView country, category;
    String countryCode, categoryname, countryname,Conid1,Catid1, follow = "false",id;
    SharedPreference sp = new SharedPreference();
    MyScrollView scrollView;
    MultipartBody.Part name1, id1, email1, countryId1,image1, categoryId1, landlineNo1, contactNo1, city1, description1, address1, isFollowTendeer1, tenderPhono1;
    EditText city,title,description;
    Button btnUploadTender;
    private Uri mPictureUri;
    private static final int PICTURE_WIDTH = 1100;
    private static final int PICTURE_HEIGHT = 600;
    private static final String KEY_PICTURE_URI = "KEY_PICTURE_URI";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_SELECT_PICTURE = 0;
    private static final int REQUEST_CODE_CROP_PICTURE = 1;

    ConnectivityReceiver cr = new ConnectivityReceiver();
    GetTenderDetail object;
    private MyBroadcastReceiver myBroadcastReceiver;
    RelativeLayout sel_cat,sel_con,sel_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tender_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myBroadcastReceiver=new MyBroadcastReceiver();
        mApiService= ApiUtils.getAPIService();
        spinner = (ListView) findViewById(R.id.spinner);
        spinner2 = (ListView) findViewById(R.id.spinner3);
        sel_con=(RelativeLayout) findViewById(R.id.rlcon_select);
        sel_cat=(RelativeLayout) findViewById(R.id.rlcat_select);
        sel_detail=(RelativeLayout) findViewById(R.id.sel_cli_detail);
        city=(EditText) findViewById(R.id.home_city);
        title=(EditText) findViewById(R.id.home_title);
        description=(EditText) findViewById(R.id.home_address);

        btnUploadTender=(Button) findViewById(R.id.btn_uploadTender);

        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        up_arrow = (ImageView) findViewById(R.id.up_arrow);
        down_arrow2 = (ImageView) findViewById(R.id.down_arrow2);
        up_arrow2 = (ImageView) findViewById(R.id.up_arrow2);
        down_arrow3 = (ImageView) findViewById(R.id.down_arrow3);
        up_arrow3 = (ImageView) findViewById(R.id.up_arrow3);
        tenderImage = (ImageView) findViewById(R.id.tender_image);

        country_home = (LinearLayout) findViewById(R.id.country_home);
        category_home = (LinearLayout) findViewById(R.id.category_home);
        country = (TextView) findViewById(R.id.txt_home_country_name);
        category = (TextView) findViewById(R.id.txt_contact_category_name);
        mApiService = ApiUtils.getAPIService();
        scrollView = (MyScrollView) findViewById(R.id.home_scroll);
        String id = getIntent().getStringExtra("id");
        String token = "Bearer " + sp.getPreferences(EditTender.this, "token");
        mApiService.getTender2(token, id).enqueue(new Callback<GetTenderDetail>() {
            @Override
            public void onResponse(Call<GetTenderDetail> call, Response<GetTenderDetail> response) {
                Log.v(TAG, String.valueOf(response.body()));
                object = response.body();
               SetView();
            }

            @Override
            public void onFailure(Call<GetTenderDetail> call, Throwable t) {
                Log.v(TAG, String.valueOf(t));
            }
        });
    }

    private void SetView() {
        category.setText(object.getCategory().getCategoryName());
        country.setText(object.getCountry().getCountryName());
        city.setText(object.getCity());
        title.setText(object.getTenderName());
        description.setText(object.getDescription());
        Conid1=object.getCountry().getId();
        Catid1=object.getCategory().getId();

        sel_detail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (country.getText().toString().equals("") || category.getText().toString().equals("")) {
                    sp.ShowDialog(EditTender.this, "First Select Country and Category");
                } else {
                    final Dialog dialog = new Dialog(EditTender.this);
                    dialog.setContentView(R.layout.contact_to_tender);
                    final Button dismissButton = (Button) dialog.findViewById(R.id.contact_save);
                    final EditText mobile = (EditText) dialog.findViewById(R.id.contact_mobile);
                    final EditText landline = (EditText) dialog.findViewById(R.id.contact_landline);
                    final EditText email2 = (EditText) dialog.findViewById(R.id.contact_email);
                    final EditText address = (EditText) dialog.findViewById(R.id.contact_address);
                    final ImageView box = (ImageView) dialog.findViewById(R.id.home_box);
                    final ImageView boxright = (ImageView) dialog.findViewById(R.id.home_box_checked);
                    TextView code=(TextView) dialog.findViewById(R.id.contact_code);

                    if(object.getContactNo().equals("")){
                        mobile.setText("");
                    }else{
                        mobile.setText(object.getContactNo());
                    }

                    if(object.getLandlineNo().equals("")){
                        landline.setText("");
                    }else{
                        landline.setText(object.getLandlineNo());
                    }

                    if(object.getEmail().equals("")){
                        email2.setText("");
                    }else{
                        email2.setText(object.getEmail());
                    }

                    if(object.getAddress().equals("")){
                        address.setText("");
                    }else{
                        address.setText(object.getAddress());
                    }

                    if(object.getIsFollowTender()){
                        boxright.setVisibility(View.VISIBLE);
                        box.setVisibility(View.GONE);
                        dismissButton.setAlpha((float) 1);
                        follow = "true";
                    }else{
                        boxright.setVisibility(View.GONE);
                        box.setVisibility(View.VISIBLE);
                        dismissButton.setAlpha((float) 0.7);
                        follow = "false";
                    }

                    box.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boxright.setVisibility(View.VISIBLE);
                            box.setVisibility(View.GONE);
                            dismissButton.setAlpha((float) 1);
                            follow = "true";
                        }
                    });
                    boxright.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boxright.setVisibility(View.GONE);
                            box.setVisibility(View.VISIBLE);
                            dismissButton.setAlpha((float) 0.7);
                            follow = "false";
                        }
                    });
                    if (countryCode != null) {

                        code.setText("+" + countryCode + "-");
                        mobile.setText("");
                    }

                    email2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Validation.isEmailAddress(email2,true);

                        }
                    });
                    mobile.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Validation.isPhoneNumber(mobile,true);

                        }
                    });

                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (follow.equals("true")) {
                                //submit
                                if (email2.getText().toString().equals("") && mobile.getText().toString().equals("") && address.getText().toString().equals("") && landline.getText().toString().equals("")) {
                                    sp.ShowDialog(EditTender.this, "please fill at least one information");
                                } else {

                                    String e = email2.getText().toString() != "" ? email2.getText().toString() : "";
                                    String m = mobile.getText().toString() != "" ? mobile.getText().toString() : "";
                                    String l = landline.getText().toString() != "" ? landline.getText().toString() : "";
                                    String a = address.getText().toString() != "" ? address.getText().toString() : "";

                                    email1 = MultipartBody.Part.createFormData("email", e);
                                    contactNo1 = MultipartBody.Part.createFormData("contactNo", m);
                                    landlineNo1 = MultipartBody.Part.createFormData("landlineNo", l);
                                    address1 = MultipartBody.Part.createFormData("address", a);
                                    dialog.dismiss();
                                }

                            } else {
                                sp.ShowDialog(EditTender.this, "please check the agree");
                            }


                        }
                    });

                    dialog.show();
                }
            }
        });

        sel_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country_home.setVisibility(View.VISIBLE);
                up_arrow.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.INVISIBLE);
                category_home.setVisibility(View.GONE);
                up_arrow2.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
                scrollView.setScrolling(false);
            }
        });


        btnUploadTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallApi();
                alpha.clear();
                alpha2.clear();
                countryName.clear();
                categoryName.clear();
                falpha.clear();
                falpha2.clear();
                fcountryName.clear();
                fcategoryName.clear();
            }
        });


        up_arrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                country_home.setVisibility(View.GONE);
                up_arrow.setVisibility(View.INVISIBLE);
                down_arrow.setVisibility(View.VISIBLE);
                scrollView.setScrolling(true);
            }
        });

        sel_cat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                category_home.setVisibility(View.VISIBLE);
                up_arrow2.setVisibility(View.VISIBLE);
                down_arrow2.setVisibility(View.GONE);
                country_home.setVisibility(View.GONE);
                up_arrow.setVisibility(View.INVISIBLE);
                down_arrow.setVisibility(View.VISIBLE);
                scrollView.setScrolling(false);
            }
        });

        up_arrow2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                category_home.setVisibility(View.GONE);
                up_arrow2.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
                scrollView.setScrolling(true);
            }
        });

        if(!object.getTenderPhoto().equals("")){
            Picasso.with(EditTender.this)
                    .load(object.getTenderPhoto())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            Log.v("Main", String.valueOf(bitmap));
                            // main = bitmap;
                            tenderImage.setImageBitmap(bitmap);
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

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryname = countryName.get(position).split("~")[0];
                countryCode = countryName.get(position).split("~")[1];
                Conid1 = countryName.get(position).split("~")[2];
                countryId1 = MultipartBody.Part.createFormData("country", Conid1);
                country.setText(countryname);
                country_home.setVisibility(View.GONE);
                scrollView.setScrolling(true);
                up_arrow.setVisibility(View.GONE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });
        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Catid1 = categoryName.get(position).split("~")[1];
                categoryId1 = MultipartBody.Part.createFormData("category", Catid1);
                categoryname = alpha2.get(position).split("~")[0];
                category.setText(categoryname);
                category_home.setVisibility(View.GONE);
                scrollView.setScrolling(true);
                up_arrow2.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
            }
        });

        GetAllCountry();
        GetCategory();

        tenderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetProfile();
            }
        });
    }

    private void SetProfile() {
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPictureUri = Uri.fromFile(createPictureFile("picture.png"));

        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);

        Intent chooserIntent = Intent.createChooser(pickIntent,
                getString(R.string.take_or_select_a_picture));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                new Intent[]{takePhotoIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE_SELECT_PICTURE);
    }

    public static File createPictureFile(String fileName) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File picture = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + BuildConfig.APPLICATION_ID,
                fileName);

        File dirFile = picture.getParentFile();
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        return picture;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_SELECT_PICTURE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri selectedImageUri = null;
                        if (data != null) {
                            selectedImageUri = data.getData();
                        }
                        if (selectedImageUri == null) {
                            selectedImageUri = mPictureUri;
                        }

                        Intent intent = new Intent(EditTender.this, ZoomCropImageActivity.class);
                        intent.putExtra(ZoomCropImageActivity.INTENT_EXTRA_URI, selectedImageUri);
                        intent.putExtra(ZoomCropImageActivity.INTENT_EXTRA_OUTPUT_WIDTH, PICTURE_WIDTH);
                        intent.putExtra(ZoomCropImageActivity.INTENT_EXTRA_OUTPUT_HEIGHT, PICTURE_HEIGHT);

                        intent.putExtra(ZoomCropImageActivity.INTENT_EXTRA_CROP_SHAPE, CropShape.SHAPE_RECTANGLE);   //optional
                        File croppedPicture = createPictureFile("cropped.png");
                        if (croppedPicture != null) {
                            intent.putExtra(ZoomCropImageActivity.INTENT_EXTRA_SAVE_DIR,
                                    croppedPicture.getParent());   //optional
                            intent.putExtra(ZoomCropImageActivity.INTENT_EXTRA_FILE_NAME,
                                    croppedPicture.getName());   //optional
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), croppedPicture);
                            image1 = MultipartBody.Part.createFormData("image", croppedPicture.getName(), requestFile);
                        }
                        startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
                        break;
                }
                break;
            case REQUEST_CODE_CROP_PICTURE:
                if (requestCode == 1) {

                    if (resultCode == Activity.RESULT_CANCELED) {
                        //Write your code if there's no result
                    }
                }
                switch (resultCode) {

                    case ZoomCropImageActivity.CROP_SUCCEEDED:

                        if (data != null) {
                            Uri croppedPictureUri = data
                                    .getParcelableExtra(ZoomCropImageActivity.INTENT_EXTRA_URI);
                            tenderImage.setImageURI(null);
                            tenderImage.setImageURI(croppedPictureUri);

                            try {

                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedPictureUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ZoomCropImageActivity.CROP_CANCELLED:
                    case ZoomCropImageActivity.CROP_FAILED:
                        break;
                }
                break;
        }
    }

    private void GetCategory() {
        sp.showProgressDialog(EditTender.this);
        if(cr.isConnected(EditTender.this)){
            mApiService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
                @Override
                public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                    Data2 = response.body();
                    sp.hideProgressDialog();
                    for (int i = 0; i < Data2.size(); i++) {
                        alpha2.add(response.body().get(i).getCategoryName() + "~" + response.body().get(i).getId()+"~" +"dfsf~"+ response.body().get(i).getImgString());
                        categoryName.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getId().toString());
                    }
                    categoryAdapter = new CustomList(EditTender.this, alpha2);
                    spinner2.setAdapter(categoryAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {

                }
            });
        }else{
            sp.ShowDialog(EditTender.this,"Please check your internet connection");
        }
    }

    private void GetAllCountry() {
        sp.showProgressDialog(EditTender.this);
        if(cr.isConnected(EditTender.this)){
            mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
                @Override
                public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                    sp.hideProgressDialog();
                    Data = response.body();
                    for (int i = 0; i < Data.size(); i++) {
                        alpha.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getCountryCode() + "~" + response.body().get(i).getId()+"~" + response.body().get(i).getImageString());
                        countryName.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getCountryCode().toString() + "~" + response.body().get(i).getId().toString());
                    }
                    Collections.sort(alpha);
                    Collections.sort(countryName);
                    countryAdapter = new CustomList(EditTender.this, alpha);
                    spinner.setAdapter(countryAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

                }
            });
        }else{
            sp.ShowDialog(EditTender.this,"Please check your internet connection");
        }
    }


    private void CallApi() {
        String City=city.getText().toString();
        String Title=title.getText().toString();
        String Des=description.getText().toString();
        if(City.equals("") || Title.equals("") || Des.equals("")){
            sp.ShowDialog(EditTender.this,"fill all detail");
        }else {
            city1 = MultipartBody.Part.createFormData("city",City);
            name1=MultipartBody.Part.createFormData("tenderName",Title);
            description1=MultipartBody.Part.createFormData("description",Des);
        }
        String token="Bearer " +sp.getPreferences(EditTender.this,"token");
        id=object.getId().toString();

        if(image1==null) {
            image1 = MultipartBody.Part.createFormData("image", "");
        }
        isFollowTendeer1=MultipartBody.Part.createFormData("isFollowTender","true");
        countryId1 = MultipartBody.Part.createFormData("country", Conid1);
        categoryId1 = MultipartBody.Part.createFormData("category", Catid1);
        if(cr.isConnected(EditTender.this)){
            mApiService.updateTender(token,id,email1,name1,city1,description1,contactNo1,landlineNo1,address1,countryId1,categoryId1,isFollowTendeer1,image1)
                    .enqueue(new Callback<UpdateTender>() {
                        @Override
                        public void onResponse(Call<UpdateTender> call, Response<UpdateTender> response) {
                            Log.i(TAG,"response---"+response.body());
                            sp.ShowDialog(EditTender.this,"Tender Amended Successfully");
                            Intent intent = new Intent(EditTender.this,ClientDrawer.class);
                            startActivity(intent);
                            Log.i(TAG,"response---"+response.body());
                        }

                        @Override
                        public void onFailure(Call<UpdateTender> call, Throwable t) {
                            Log.i(TAG,"response---"+t);
                        }
                    });
        }else{
            sp.ShowDialog(EditTender.this,"Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(EditTender.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(EditTender.this).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(EditTender.this).unregisterReceiver(myBroadcastReceiver);
    }
}
