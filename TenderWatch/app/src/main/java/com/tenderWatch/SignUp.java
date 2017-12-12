package com.tenderWatch;

import android.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.crazyorr.zoomcropimage.CropShape;
import com.github.crazyorr.zoomcropimage.ZoomCropImageActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.Validation.Validation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tenderWatch.R.drawable.avtar;

/**
 * Created by lcom48 on 5/12/17.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    Intent intent;
    RelativeLayout rlcountry;
    EditText country,mobileNo,aboutMe,occupation;
    String countryName,countryCode,txtaboutMe;
    Button btnSignUp;
    ArrayList<String> empNo;
    CreateUser user=new CreateUser();
    SharedPreference sp = new SharedPreference();
    MultipartBody.Part email1,password1,country1,contactNo1,occupation1,aboutMe1,role1,deviceId1,image1;
    //TextView txtcountryCode;
    CircleImageView profileImg;
    private static final String TAG = SignUp.class.getSimpleName();

    private static final String KEY_PICTURE_URI = "KEY_PICTURE_URI";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_SELECT_PICTURE = 0;
    private static final int REQUEST_CODE_CROP_PICTURE = 1;

    private static final int PICTURE_WIDTH = 600;
    private static final int PICTURE_HEIGHT = 600;

    private LinearLayout back;
    String email,password;
    private Uri mPictureUri;
    private Api mAPIService;
    String URL = "https://www.w3schools.com/css/paris.jpg";
    Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        savedInstanceState.putBoolean("MyBoolean", true);
//        savedInstanceState.putDouble("myDouble", 1.9);
//        savedInstanceState.putInt("MyInt", 1);
//        savedInstanceState.putString("MyString", "Welcome back to Android");
        setContentView(R.layout.activity_client_signup2);
//        Intent init=getIntent();
//        email=init.getStringExtra("email");
//        password=init.getStringExtra("password");
        InitView();
        InitListener();
      //  Picasso.with(SignUp.this).load("https://www.w3schools.com/css/paris.jpg").into(target);

     //   new DownloadImage().execute(URL);
       // show.getCharExtra()


    }

    private void InitListener() {
        rlcountry.setOnClickListener(this);
        aboutMe.setOnClickListener(this);
        profileImg.setOnClickListener(this);
        back.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void InitView() {
       rlcountry=(RelativeLayout) findViewById(R.id.selectCountry);
       country=(EditText) findViewById(R.id.country);
        mobileNo=(EditText) findViewById(R.id.mobileNo) ;
       aboutMe=(EditText) findViewById(R.id.edt_aboutme);
        profileImg=(CircleImageView) findViewById(R.id.circleView);
        btnSignUp=(Button) findViewById(R.id.btn_Next);
        occupation=(EditText) findViewById(R.id.occupation) ;
        Intent show = getIntent();
        Bitmap newimg=(Bitmap)show.getParcelableExtra("bitmap");
        if(newimg != null){
        profileImg.setImageBitmap(newimg);}
        empNo = show.getStringArrayListExtra("Country");
        txtaboutMe=show.getStringExtra("aboutMe");
        back=(LinearLayout) findViewById(R.id.signup2_toolbar);
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

                startActivityForResult(intent, 1);
                break;
            case R.id.edt_aboutme:
                intent = new Intent(SignUp.this, AboutMe.class);

                if(aboutMe.getText().toString().equals("About Me")){
                    intent.putExtra("about","About Me");
                }else{
                    intent.putExtra("about",txtaboutMe);
                }

                startActivityForResult(intent, 1);
                break;

            case R.id.circleView:
                SetProfile();
                break;
            case R.id.signup2_toolbar:
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(
                                SignUp.this, SignUpSelection.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.btn_Next:
                SendData();

                if(sp.getPreferences(SignUp.this,"role").equals("contractor")){
                    intent = new Intent(
                            SignUp.this, CountryList.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(
                            SignUp.this, Agreement.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void SendData() {

        Intent init=getIntent();
       String email1=sp.getPreferences(SignUp.this,"email");
       String password1=sp.getPreferences(SignUp.this,"password");
       String country1=country.getText().toString();
       String mobile =mobileNo.getText().toString();
       String occupation1=occupation.getText().toString();
       String aboutMe1=aboutMe.getText().toString();
       String deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
       String role=sp.getPreferences(SignUp.this,"role");
       user.setCountry(country1);
       user.setContactNo(mobile);
       user.setOccupation(occupation1);
       user.setDeviceId(deviceId);
       user.setRole(role);
       user.setAboutMe(aboutMe1);

    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            File file = new File(Environment.getExternalStorageDirectory().getPath()  + "/saved.jpg");
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
                ostream.close();
               // uploadImage(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    };


    private void uploadImage(File file) {

        /**
         * Progressbar to Display if you need
         */
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        //Create Upload Server Client
       // ApiService service = RetroClient.getApiService();
        mAPIService = ApiUtils.getAPIService();

        //File creating from selected URL
        String imagePath="";
        //File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

         email1 =MultipartBody.Part.createFormData("email", "hgsdfv@sdf.sd");
         password1 =MultipartBody.Part.createFormData("password", "dfdfcvds");
         country1 =MultipartBody.Part.createFormData("country","dfvgfd");
        contactNo1 =MultipartBody.Part.createFormData("contactNo", "+91-4567899632");
        occupation1 =MultipartBody.Part.createFormData("occupation", "fvfdvdfv");
         aboutMe1 =MultipartBody.Part.createFormData("aboutMe","dfvgdfvbdf");
         role1 =MultipartBody.Part.createFormData("role", "contractor");
         deviceId1 =MultipartBody.Part.createFormData("deviceId","sbdfbvcs214" );
         image1 =MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<Register> resultCall = mAPIService.uploadImage(email1,password1,country1,contactNo1,occupation1,aboutMe1,role1,deviceId1,image1);
        resultCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Log.i(TAG,"response register-->");
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.i(TAG,"error register-->");

            }
        });

    }
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
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

                       // result=data.getStringExtra("Country");
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
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), croppedPicture);
                            user.setProfilePhoto(croppedPicture);
                            image1 =MultipartBody.Part.createFormData("image", croppedPicture.getName(), requestFile);

                           // uploadImage(croppedPicture);
                        }
                        startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
                        break;
                }
                break;
            case REQUEST_CODE_CROP_PICTURE:
                if (requestCode == 1) {
                    if(resultCode == Activity.RESULT_OK){
//                        ArrayList<String> result=data.getStringArrayListExtra("Country");
//
                        if(data.getStringExtra("aboutMe")!=null){
                            txtaboutMe=data.getStringExtra("aboutMe");
                            if(txtaboutMe.equals("About Me")){
                                aboutMe.setText(txtaboutMe);

                            }else {
                                if(txtaboutMe.length()>10) {
                                    aboutMe.setText(txtaboutMe.substring(0, 10) + "....");
                                }else{
                                    aboutMe.setText(txtaboutMe);
                                }
                            }
                        }
                        if(data.getStringArrayListExtra("Country")!=null){
                            //String AboutMe=data.getStringExtra("aboutMe");
                            ArrayList<String> result=data.getStringArrayListExtra("Country");

                            Log.i(TAG, String.valueOf(result));
                        //if(empNo != null) {
                            countryName = result.get(0).toString().split("~")[0];
                            countryCode = result.get(0).toString().split("~")[1].split("~")[0];
                        //}
                        country.setText(countryName);
                        mobileNo.setText(countryCode+'-');
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
                            //ImageView iv = (ImageView) findViewById(R.id.id_iv);
                            // workaround for ImageView to refresh cache
                            profileImg.setImageURI(null);
                            profileImg.setImageURI(croppedPictureUri);

//                            Uri imageUri = intent.getData();
try {

                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),croppedPictureUri);
   // BitmapToString(bitmap);

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
    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
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
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog mProgressDialog = new ProgressDialog(SignUp.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();

                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            //image.setImageBitmap(result);
            // Close progressdialog
            //Dialog mProgressDialog;
            mProgressDialog.dismiss();
        }
    }
}
