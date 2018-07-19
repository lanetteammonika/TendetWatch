package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.crazyorr.zoomcropimage.CropShape;
import com.github.crazyorr.zoomcropimage.ZoomCropImageActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.ClientDrawer.Home;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.UpdateTender;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.Validation.MyScrollView;
import com.tenderWatch.Validation.Validation;

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

public class EditTenderDetail extends AppCompatActivity {
    Api mApiService;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> countryName = new ArrayList<String>();

    private static final ArrayList<String> alpha2 = new ArrayList<String>();
    private static final ArrayList<String> categoryName = new ArrayList<String>();
    private static final ArrayList<String> falpha = new ArrayList<String>();
    private static final ArrayList<String> fcountryName = new ArrayList<String>();

    private static final ArrayList<String> falpha2 = new ArrayList<String>();
    private static final ArrayList<String> fcategoryName = new ArrayList<String>();
    private static final String TAG = Home.class.getSimpleName();
    private List Data, Data2;
    CustomList countryAdapter, categoryAdapter;
    ListView spinner, spinner2;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2, down_arrow3, up_arrow3, tenderImage;
    LinearLayout country_home, category_home;
    TextView country, category;
    String countryCode, categoryname, countryname, follow = "false", id;
    SharedPreference sp = new SharedPreference();
    MyScrollView scrollView;
    MultipartBody.Part name1, id1, email1, countryId1, image1, categoryId1, landlineNo1, contactNo1, city1, description1, address1, isFollowTendeer1, tenderPhono1;
    EditText city, title, description;
    Button btnUploadTender;
    private Uri mPictureUri;
    private static final int PICTURE_WIDTH = 1100;
    private static final int PICTURE_HEIGHT = 600;
    private static final String KEY_PICTURE_URI = "KEY_PICTURE_URI";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_SELECT_PICTURE = 0;
    private static final int REQUEST_CODE_CROP_PICTURE = 1;
    Tender object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tender_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mApiService = ApiUtils.getAPIService();
        spinner = (ListView) findViewById(R.id.spinner);
        spinner2 = (ListView) findViewById(R.id.spinner3);

        city = (EditText) findViewById(R.id.home_city);
        title = (EditText) findViewById(R.id.home_title);
        description = (EditText) findViewById(R.id.home_address);

        btnUploadTender = (Button) findViewById(R.id.btn_uploadTender);

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
        String json = getIntent().getStringExtra("data");
        Gson gson = new Gson();
        object = gson.fromJson(json, Tender.class);
        setTitle("Edit Tender");
        id = object.getId();
        FGetAllCountry();
        FGetCategory();
        city.setText(object.getCity());
        title.setText(object.getTenderName());
        description.setText(object.getDescription());
        String cid = object.getCountry();
        String caid = object.getCategory();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(object);

                Intent intent = new Intent(EditTenderDetail.this, PreviewTenderDetail.class);
                intent.putExtra("data", jsonString);
                startActivity(intent);
            }
        });


        if (!TextUtils.isEmpty(object.getTenderPhoto())) {
            Picasso.with(EditTenderDetail.this)
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

        tenderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetProfile();
            }
        });

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
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

        down_arrow2.setOnClickListener(new View.OnClickListener() {
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
                //homeScroll.setEnabled(false);
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
        down_arrow3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (country.getText().toString().equals("") || category.getText().toString().equals("")) {
                    sp.ShowDialog(EditTenderDetail.this, "First Select Country and Category");
                } else {
                    final Dialog dialog = new Dialog(EditTenderDetail.this);
                    dialog.setContentView(R.layout.contact_to_tender);
                    final Button dismissButton = dialog.findViewById(R.id.contact_save);
                    final EditText mobile = dialog.findViewById(R.id.contact_mobile);
                    final EditText landline = dialog.findViewById(R.id.contact_landline);
                    final EditText email2 = dialog.findViewById(R.id.contact_email);
                    final EditText address = dialog.findViewById(R.id.contact_address);
                    final ImageView box = dialog.findViewById(R.id.home_box);
                    final ImageView boxright = dialog.findViewById(R.id.home_box_checked);
                    TextView code = dialog.findViewById(R.id.contact_code);

                    if (TextUtils.isEmpty(object.getContactNo())) {
                        mobile.setText("");
                    } else {
                        mobile.setText(object.getContactNo());
                    }

                    if (TextUtils.isEmpty(object.getLandlineNo())) {
                        landline.setText("");
                    } else {
                        landline.setText(object.getLandlineNo());
                    }

                    if (TextUtils.isEmpty(object.getEmail())) {
                        email2.setText("");
                    } else {
                        email2.setText(object.getEmail());
                    }

                    if (TextUtils.isEmpty(object.getAddress())) {
                        address.setText("");
                    } else {
                        address.setText(object.getAddress());
                    }

                    if (object.getIsFollowTender()) {
                        boxright.setVisibility(View.VISIBLE);
                        box.setVisibility(View.GONE);
                        dismissButton.setAlpha((float) 1);
                        follow = "true";
                    } else {
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
                            Validation.isEmailAddress(email2, true);

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
                            Validation.isPhoneNumber(mobile, true);

                        }
                    });

                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (follow.equals("true")) {
                                //submit
                                if (email2.getText().toString().equals("") && mobile.getText().toString().equals("") && address.getText().toString().equals("") && landline.getText().toString().equals("")) {
                                    sp.ShowDialog(EditTenderDetail.this, "please fill at least one information");
                                } else {

                                    String e = email2.getText().toString();
                                    String m = mobile.getText().toString();
                                    String l = landline.getText().toString();
                                    String a = address.getText().toString();

                                    email1 = MultipartBody.Part.createFormData("email", e);
                                    contactNo1 = MultipartBody.Part.createFormData("contactNo", m);
                                    landlineNo1 = MultipartBody.Part.createFormData("landlineNo", l);
                                    address1 = MultipartBody.Part.createFormData("address", a);
                                    dialog.dismiss();
                                }

                            } else {
                                sp.ShowDialog(EditTenderDetail.this, "please check the agree");
                            }


                        }
                    });

                    dialog.show();
                }
            }
        });

        alpha.clear();
        alpha2.clear();
        countryName.clear();
        categoryName.clear();
        GetAllCountry();
        GetCategory();

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryname = countryName.get(position).split("~")[0];
                countryCode = countryName.get(position).split("~")[1];
                String id1 = countryName.get(position).split("~")[2];
                countryId1 = MultipartBody.Part.createFormData("country", id1);
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
                String id1 = categoryName.get(position).split("~")[1];
                categoryId1 = MultipartBody.Part.createFormData("category", id1);
                categoryname = alpha2.get(position).split("~")[0];
                category.setText(categoryname);
                category_home.setVisibility(View.GONE);
                scrollView.setScrolling(true);
                up_arrow2.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
            }
        });
    }

    private void FGetCategory() {
        sp.showProgressDialog(EditTenderDetail.this);

        mApiService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                Data2 = response.body();
                sp.hideProgressDialog();
                for (int i = 0; i < Data2.size(); i++) {
                    falpha2.add(response.body().get(i).getCategoryName() + "~" + response.body().get(i).getImgString());
                    fcategoryName.add(response.body().get(i).getCategoryName() + "~" + response.body().get(i).getId());
                }
                for (int i = 0; i < Data2.size(); i++) {
                    if (fcategoryName.get(i).split("~")[1].equals(object.getCategory())) {
                        category.setText(response.body().get(i).getCategoryName());
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {

            }
        });
    }

    private void FGetAllCountry() {
        sp.showProgressDialog(EditTenderDetail.this);

        mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                sp.hideProgressDialog();
                Data = response.body();
                for (int i = 0; i < Data.size(); i++) {
                    falpha.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getImageString());
                    fcountryName.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getCountryCode() + "~" + response.body().get(i).getId());
                }
                Collections.sort(falpha);
                Collections.sort(fcountryName);
                for (int i = 0; i < Data.size(); i++) {
                    if (fcountryName.get(i).split("~")[2].equals(object.getCountry())) {
                        country.setText(response.body().get(i).getCountryName());
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });

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

    //-working on add subscription,-Research for Payment with paypal in android,-starting integrating payment with paypal in application.
    private void CallApi() {
        String City = city.getText().toString();
        String Title = title.getText().toString();
        String Des = description.getText().toString();
        if (City.equals("") || Title.equals("") || Des.equals("")) {
            sp.ShowDialog(EditTenderDetail.this, "fill all detail");
        } else {
            city1 = MultipartBody.Part.createFormData("city", City);
            name1 = MultipartBody.Part.createFormData("tenderName", Title);
            description1 = MultipartBody.Part.createFormData("description", Des);
        }
        String token = "Bearer " + SharedPreference.getPreferences(EditTenderDetail.this, "token");
        id = object.getId();

        if (image1 == null) {
            image1 = MultipartBody.Part.createFormData("image", "");
        }
        isFollowTendeer1 = MultipartBody.Part.createFormData("isFollowTender", "true");
        countryId1 = MultipartBody.Part.createFormData("country", object.getCountry());
        categoryId1 = MultipartBody.Part.createFormData("category", object.getCategory());
        mApiService.updateTender(token, id, email1, name1, city1, description1, contactNo1, landlineNo1, address1, countryId1, categoryId1, isFollowTendeer1, image1)
                .enqueue(new Callback<UpdateTender>() {
                    @Override
                    public void onResponse(Call<UpdateTender> call, Response<UpdateTender> response) {
                        Log.i(TAG, "response---" + response.body());
                        Intent intent = new Intent(EditTenderDetail.this, ClientDrawer.class);
                        startActivity(intent);
                        Log.i(TAG, "response---" + response.body());
                    }

                    @Override
                    public void onFailure(Call<UpdateTender> call, Throwable t) {
                        Log.i(TAG, "response---" + t);
                    }
                });

    }

    private void GetCategory() {
        sp.showProgressDialog(EditTenderDetail.this);

        mApiService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                Data2 = response.body();
                sp.hideProgressDialog();
                for (int i = 0; i < Data2.size(); i++) {
                    alpha2.add(response.body().get(i).getCategoryName() + "~" + response.body().get(i).getImgString());
                    categoryName.add(response.body().get(i).getCategoryName() + "~" + response.body().get(i).getId());
                }
                categoryAdapter = new CustomList(EditTenderDetail.this, alpha2);
                spinner2.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {

            }
        });
    }

    private void GetAllCountry() {
        sp.showProgressDialog(EditTenderDetail.this);

        mApiService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                sp.hideProgressDialog();
                Data = response.body();
                for (int i = 0; i < Data.size(); i++) {
                    alpha.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getImageString());
                    countryName.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getCountryCode() + "~" + response.body().get(i).getId());
                }
                Collections.sort(alpha);
                Collections.sort(countryName);
                countryAdapter = new CustomList(EditTenderDetail.this, alpha);
                spinner.setAdapter(countryAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });
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

                        Intent intent = new Intent(EditTenderDetail.this, ZoomCropImageActivity.class);
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

}
