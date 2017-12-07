package com.tenderWatch;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.crazyorr.zoomcropimage.CropShape;
import com.github.crazyorr.zoomcropimage.ZoomCropImageActivity;
import com.tenderWatch.Validation.Validation;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lcom48 on 5/12/17.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    Intent intent;
    RelativeLayout rlcountry;
    EditText country,mobileNo,aboutMe;
    String countryName,countryCode,txtaboutMe;
    ArrayList<String> empNo;
    //TextView txtcountryCode;
    CircleImageView profileImg;
    private static final String TAG = SignUp.class.getSimpleName();

    private static final String KEY_PICTURE_URI = "KEY_PICTURE_URI";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_SELECT_PICTURE = 0;
    private static final int REQUEST_CODE_CROP_PICTURE = 1;

    private static final int PICTURE_WIDTH = 600;
    private static final int PICTURE_HEIGHT = 600;

    private Uri mPictureUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup2);
        InitView();
        InitListener();


       // show.getCharExtra()

        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // Validation.isPhoneNumber(mobileNo,true);
              String mobileNumber=  mobileNo.getText().toString();
              mobileNumber=mobileNumber.split("-")[1];
            int l=  mobileNumber.length();
            if(l<10) {
                mobileNo.setError("up to 10 digit");
            }
                if(l>10) {
                    mobileNo.setError("up to 10 digit");
                }
            }
        });
    }

    private void InitListener() {
        rlcountry.setOnClickListener(this);
        aboutMe.setOnClickListener(this);
        profileImg.setOnClickListener(this);
    }

    private void InitView() {
       rlcountry=(RelativeLayout) findViewById(R.id.selectCountry);
       country=(EditText) findViewById(R.id.country);
        mobileNo=(EditText) findViewById(R.id.mobileNo) ;
       aboutMe=(EditText) findViewById(R.id.edt_aboutme);
        profileImg=(CircleImageView) findViewById(R.id.circleView);
        Intent show = getIntent();
        empNo = show.getStringArrayListExtra("Country");
        txtaboutMe=show.getStringExtra("aboutMe");
        if(txtaboutMe!= null) {
            if(txtaboutMe.equals("About Me")){
                aboutMe.setText(txtaboutMe);

            }else {
                aboutMe.setText(txtaboutMe.substring(0, 10) + "....");
            }
        }
        if(empNo != null) {
            countryName = empNo.get(0).toString().split("~")[0];
            countryCode = empNo.get(0).toString().split("~")[1];
        }
       if(empNo != null) {
           country.setText(countryName);
           mobileNo.setText(countryCode+'-');
       }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.selectCountry:
                intent = new Intent(SignUp.this, CountryList.class);
                intent.putExtra("check","signup");
                startActivity(intent);
                break;
            case R.id.edt_aboutme:
                intent = new Intent(SignUp.this, AboutMe.class);
                if(aboutMe.getText().toString().equals("About Me")){
                    intent.putExtra("about","About Me");
                }else{
                    intent.putExtra("about",txtaboutMe);
                }
                startActivity(intent);
                break;
            case R.id.circleView:
                SetProfile();
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

                        Intent intent = new Intent(this, ZoomCropImageActivity.class);
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
                        }
                        startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
                        break;
                }
                break;
            case REQUEST_CODE_CROP_PICTURE:
                switch (resultCode) {
                    case ZoomCropImageActivity.CROP_SUCCEEDED:
                        if (data != null) {
                            Uri croppedPictureUri = data
                                    .getParcelableExtra(ZoomCropImageActivity.INTENT_EXTRA_URI);
                            //ImageView iv = (ImageView) findViewById(R.id.id_iv);
                            // workaround for ImageView to refresh cache
                            profileImg.setImageURI(null);
                            profileImg.setImageURI(croppedPictureUri);
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
                    finish();
                }
                break;
            }
        }
    }

    private void requestPermissionWriteExternalStorage() {
        final String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        final int requestCode = MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

}
