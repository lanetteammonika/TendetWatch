package com.tenderWatch.ClientDrawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.crazyorr.zoomcropimage.CropShape;
import com.github.crazyorr.zoomcropimage.ZoomCropImageActivity;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.BuildConfig;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.UploadTender;
import com.tenderWatch.R;
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

/**
 * Created by lcom48 on 14/12/17.
 */

public class Home extends Fragment implements AdapterView.OnItemSelectedListener {

    Api mApiService;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> countryName = new ArrayList<String>();

    private static final ArrayList<String> alpha2 = new ArrayList<String>();
    private static final ArrayList<String> categoryName = new ArrayList<String>();
    private static final String TAG = Home.class.getSimpleName();
    private List Data, Data2;
    CustomList countryAdapter, categoryAdapter;
    ListView spinner, spinner2;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2, down_arrow3, up_arrow3,tenderImage;
    LinearLayout country_home, category_home;
    TextView country, category;
    String countryCode, categoryname, countryname, follow = "false";
    SharedPreference sp = new SharedPreference();
    MyScrollView scrollView;
    MultipartBody.Part name1, email1, countryId1,image1, categoryId1, landlineNo1, contactNo1, city1, description1, address1, isFollowTendeer1, tenderPhono1;
    EditText city,title,description;
    Button btnUploadTender;
    private Uri mPictureUri;
    private static final int PICTURE_WIDTH = 1100;
    private static final int PICTURE_HEIGHT = 600;
    private static final String KEY_PICTURE_URI = "KEY_PICTURE_URI";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_SELECT_PICTURE = 0;
    private static final int REQUEST_CODE_CROP_PICTURE = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_teander, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
        spinner = (ListView) view.findViewById(R.id.spinner);
        spinner2 = (ListView) view.findViewById(R.id.spinner3);

        city=(EditText) view.findViewById(R.id.home_city);
        title=(EditText) view.findViewById(R.id.home_title);
        description=(EditText) view.findViewById(R.id.home_address);

        btnUploadTender=(Button) view.findViewById(R.id.btn_uploadTender);

        down_arrow = (ImageView) view.findViewById(R.id.down_arrow);
        up_arrow = (ImageView) view.findViewById(R.id.up_arrow);
        down_arrow2 = (ImageView) view.findViewById(R.id.down_arrow2);
        up_arrow2 = (ImageView) view.findViewById(R.id.up_arrow2);
        down_arrow3 = (ImageView) view.findViewById(R.id.down_arrow3);
        up_arrow3 = (ImageView) view.findViewById(R.id.up_arrow3);
        tenderImage = (ImageView) view.findViewById(R.id.tender_image);

        country_home = (LinearLayout) view.findViewById(R.id.country_home);
        category_home = (LinearLayout) view.findViewById(R.id.category_home);
        country = (TextView) view.findViewById(R.id.txt_home_country_name);
        category = (TextView) view.findViewById(R.id.txt_contact_category_name);
        mApiService = ApiUtils.getAPIService();
        spinner.setOnItemSelectedListener(this);
        scrollView = (MyScrollView) view.findViewById(R.id.home_scroll);


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
                // homeScroll.setScrollbarFadingEnabled(false);
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
                if (countryname == null || countryname == null) {
                    sp.ShowDialog(getActivity(), "First Select Country and Category");
                } else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.contact_to_tender);
                    final Button dismissButton = (Button) dialog.findViewById(R.id.contact_save);
                    final EditText mobile = (EditText) dialog.findViewById(R.id.contact_mobile);
                    final EditText landline = (EditText) dialog.findViewById(R.id.contact_landline);
                    final EditText email2 = (EditText) dialog.findViewById(R.id.contact_email);
                    final EditText address = (EditText) dialog.findViewById(R.id.contact_address);
                    final ImageView box = (ImageView) dialog.findViewById(R.id.home_box);
                    final ImageView boxright = (ImageView) dialog.findViewById(R.id.home_box_checked);
                    final TextView code=(TextView) dialog.findViewById(R.id.contact_code);


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
                    code.setText("+" + countryCode + "-");

                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (follow.equals("true")) {
                                //submit
                                if (email2.getText().toString().equals("") && mobile.getText().toString().equals("") && address.getText().toString().equals("") && landline.getText().toString().equals("")) {
                                    sp.ShowDialog(getActivity(), "please fill at least one information");
                                } else {
                                    String e = email2.getText().toString() != "" ? email2.getText().toString() : "";
                                    String m = mobile.getText().toString() != "" ? mobile.getText().toString() : "";
                                    String l = landline.getText().toString() != "" ? landline.getText().toString() : "";
                                    String a = address.getText().toString() != "" ? address.getText().toString() : "";

                                    email1 = MultipartBody.Part.createFormData("email", e);
                                    contactNo1 = MultipartBody.Part.createFormData("contactNo", "+" + countryCode + "-"+m);
                                    landlineNo1 = MultipartBody.Part.createFormData("landlineNo", l);
                                    address1 = MultipartBody.Part.createFormData("address", a);
                                    dialog.dismiss();
                                }

                            } else {
                                sp.ShowDialog(getActivity(), "please check the agree");
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
        GetAllCountry(view);
        GetCategory(view);

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


    private void CallApi() {
        String City=city.getText().toString();
        String Title=title.getText().toString();
        String Des=description.getText().toString();
        if(City.equals("") || Title.equals("") || Des.equals("")){
            sp.ShowDialog(getActivity(),"fill all detail");
        }else {
            city1 = MultipartBody.Part.createFormData("city",City);
            name1=MultipartBody.Part.createFormData("tenderName",Title);
            description1=MultipartBody.Part.createFormData("description",Des);
        }
        String token="Bearer " +sp.getPreferences(getActivity(),"token");

        if(image1==null) {
            image1 = MultipartBody.Part.createFormData("image", "");
        }
        isFollowTendeer1=MultipartBody.Part.createFormData("isFollowTender","true");

        mApiService.uploadTender(token,email1,name1,city1,description1,contactNo1,landlineNo1,address1,countryId1,categoryId1,isFollowTendeer1,image1)
                .enqueue(new Callback<UploadTender>() {
                    @Override
                    public void onResponse(Call<UploadTender> call, Response<UploadTender> response) {


                        Log.i(TAG,"response---"+response.body());

                    }

                    @Override
                    public void onFailure(Call<UploadTender> call, Throwable t) {
                        Log.i(TAG,"response---"+t);

                    }
                });

    }

    private void GetCategory(final View v) {
        final View v1 = v;
        mApiService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                Data2 = response.body();
                for (int i = 0; i < Data2.size(); i++) {
                    alpha2.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getImgString().toString());
                    categoryName.add(response.body().get(i).getCategoryName().toString() + "~" + response.body().get(i).getId().toString());
               }
                categoryAdapter = new CustomList(getContext(), alpha2);
                spinner2.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {

            }
        });
    }

    private void GetAllCountry(final View v) {
        final View v1 = v;
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
                countryAdapter = new CustomList(getContext(), alpha);

                spinner.setAdapter(countryAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) != null) {
            String item = parent.getItemAtPosition(position).toString();

            // Showing selected spinner item
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

                        Intent intent = new Intent(getActivity(), ZoomCropImageActivity.class);
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
                            //user.setProfilePhoto(croppedPicture);
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

                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), croppedPictureUri);

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
