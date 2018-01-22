package com.tenderWatch.ClientDrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.github.crazyorr.zoomcropimage.CropShape;
import com.github.crazyorr.zoomcropimage.ZoomCropImageActivity;
import com.squareup.picasso.Picasso;
import com.tenderWatch.AboutMe;
import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.Agreement;
import com.tenderWatch.CountryList;
import com.tenderWatch.Login;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.User;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.SideSelector;
import com.tenderWatch.SignUp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tenderWatch.SignUp.createPictureFile;

/**
 * Created by lcom48 on 14/12/17.
 */

public class EditProfile extends Fragment implements View.OnClickListener{
    EditText txtCountry,txtMobileNo,txtOccupation,txtAboutMe;
    Api mAPIService;
    private List Data;
    CreateUser users=new CreateUser();
    ArrayList<String> countryName;
    RelativeLayout rlCountryList;
    Button btnUpdate;
    CircleImageView profileImg;
    Intent intent;
    String txtaboutMe,countryName1,countryCode1;
    private Uri mPictureUri;
    SharedPreference sp = new SharedPreference();
    Object user;
    MultipartBody.Part email1, password1, country1, contactNo1, occupation1, aboutMe1, role1, deviceId1, image1;

    private static final String TAG = EditProfile.class.getSimpleName();

    private static final String KEY_PICTURE_URI = "KEY_PICTURE_URI";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_SELECT_PICTURE = 0;
    private static final int REQUEST_CODE_CROP_PICTURE = 1;

    private static final int PICTURE_WIDTH = 600;
    private static final int PICTURE_HEIGHT = 600;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAPIService = ApiUtils.getAPIService();
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Profile");
        txtAboutMe = (EditText) view.findViewById(R.id.edit_aboutme);
        txtCountry = (EditText) view.findViewById(R.id.edit_country);
        txtMobileNo = (EditText) view.findViewById(R.id.edit_mobileNo);
        txtOccupation = (EditText) view.findViewById(R.id.edit_occupation);
        rlCountryList=(RelativeLayout) view.findViewById(R.id.edit_rlselectCountry);
        btnUpdate=(Button) view.findViewById(R.id.edit_btn_Update);
        profileImg = (CircleImageView) view.findViewById(R.id.edit_circleView);

        user=sp.getPreferencesObject(getActivity());
        Picasso.with(getApplicationContext()).load(((User) user).getProfilePhoto().toString())
                .placeholder(R.drawable.avtar).error(R.drawable.avtar)
                .into(profileImg);
        String c=((User) user).getCountry().toString();
        txtCountry.setText(c);
        txtaboutMe=((User) user).getAboutMe().toString();
        if(txtaboutMe.equals("")){
            txtaboutMe="About Me";
        }
        txtMobileNo.setText(((User) user).getContactNo().toString());
        txtAboutMe.setText(txtaboutMe);
        txtOccupation.setText(((User) user).getOccupation().toString());
        InitListener();
    }

    private void InitListener() {
        rlCountryList.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        profileImg.setOnClickListener(this);
        txtAboutMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_rlselectCountry:
                intent = new Intent(getActivity(), CountryList.class);
                intent.putExtra("check", "signup");
                startActivityForResult(intent, 1);
                break;
            case R.id.edit_btn_Update:
                CheckValidation();
                break;
            case R.id.edit_circleView:
                SetProfile();
                break;
            case R.id.edit_aboutme:
                intent = new Intent(getActivity(), AboutMe.class);
                if (txtAboutMe.getText().toString().equals("About Me")) {
                    intent.putExtra("about", "About Me");
                } else {
                    intent.putExtra("about", txtaboutMe);
                }
                startActivityForResult(intent, 1);
                break;
        }
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

    private void CheckValidation() {
        if (!txtMobileNo.getText().toString().isEmpty() && !txtOccupation.getText().toString().isEmpty()) {
            if (txtMobileNo.getText().toString().split("-")[1].length() < 9) {
                sp.ShowDialog(getActivity(), "Enter Mobiile number up to 9 digit");
            } else {
                ApiCall();
            }
        } else {
            sp.ShowDialog(getActivity(), "Enter Details");
        }
    }

    private void ApiCall() {
        File file1 = users.getProfilePhoto();

        RequestBody requestFile=null;
        String name="";
        if(file1!=null) {
           requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
           name= file1.getName();
        }
        String country = txtCountry.getText().toString();
        String mobile = txtMobileNo.getText().toString();
        String occupation = txtOccupation.getText().toString();
        String about=txtaboutMe;
        String Id=((User) user).getId();


        country1 = MultipartBody.Part.createFormData("country", country);
        contactNo1 = MultipartBody.Part.createFormData("contactNo", mobile);
        occupation1 = MultipartBody.Part.createFormData("occupation", occupation);
        aboutMe1 = MultipartBody.Part.createFormData("aboutMe", about);
        image1 = MultipartBody.Part.createFormData("image",name, requestFile);
       String token="Bearer "+sp.getPreferences(getActivity(),"token");
        Call<User> resultCall = mAPIService.UpdateUser(token,Id,country1, contactNo1, occupation1, aboutMe1, image1);
       sp.showProgressDialog(getActivity());
        resultCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i(TAG, "response register-->");
                sp.hideProgressDialog();;
                if (response.isSuccessful()) {
                    sp.setPreferencesObject(getActivity(),response.body());
                    sp.ShowDialog(getActivity(), "Profile Update Successful");
                    intent = new Intent(getActivity(), ClientDrawer.class);
                    startActivity(intent);
                } else {
                    sp.ShowDialog(getActivity(), response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "error register-->");
                sp.ShowDialog(getActivity(), "Server is down. Come back later!!");

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        requestPermissionWriteExternalStorage();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_PICTURE_URI, mPictureUri);
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
                            users.setProfilePhoto(croppedPicture);
                        }
                        startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
                        break;
                }
                break;
            case REQUEST_CODE_CROP_PICTURE:
                if (requestCode == 1) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data.getStringExtra("aboutMe") != null) {
                            txtaboutMe = data.getStringExtra("aboutMe");
                            if (txtaboutMe.equals("About Me")) {
                                txtAboutMe.setText(txtaboutMe);
                            } else {
                                if (txtaboutMe.length() > 10) {
                                    txtAboutMe.setText(txtaboutMe.substring(0, 10) + "....");
                                } else {
                                    txtAboutMe.setText(txtaboutMe);
                                }
                            }
                           // user.setAboutMe(txtaboutMe);
                        }
                        if (data.getStringArrayListExtra("Country") != null) {
                            ArrayList<String> result = data.getStringArrayListExtra("Country");
                            Log.i(TAG, String.valueOf(result));
                            countryName1 = result.get(0).toString().split("~")[0];
                            countryCode1 = result.get(0).toString().split("~")[1].split("~")[0];
                            txtCountry.setText(countryName1);
                            txtMobileNo.setText(countryCode1 + '-');
                        }
                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        //Write your code if there's no result
                    }
                }
                switch (resultCode) {

                    case ZoomCropImageActivity.CROP_SUCCEEDED:

                        if (data != null) {
                            Uri croppedPictureUri = data
                                    .getParcelableExtra(ZoomCropImageActivity.INTENT_EXTRA_URI);
                            profileImg.setImageURI(null);
                            profileImg.setImageURI(croppedPictureUri);

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getActivity().finish();                }
                break;
            }
        }
    }

    private void requestPermissionWriteExternalStorage() {
        final String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        final int requestCode = MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getActivity(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
    }
}