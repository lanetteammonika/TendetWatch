package com.tenderWatch.ClientDrawer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;
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

/**
 * Created by lcom48 on 20/12/17.
 */

public class PreviewTender extends Fragment {
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_preview_tender, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiService= ApiUtils.getAPIService();
        final Fragment fragment2 = new EditTender();
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        tenderTitle=(TextView) view.findViewById(R.id.preview_tender_title);
        Country=(TextView) view.findViewById(R.id.preview_country_name);
        Category=(TextView) view.findViewById(R.id.preview_category);
        ExpDay=(TextView) view.findViewById(R.id.preview_exp);
        Description=(TextView) view.findViewById(R.id.preview_description);
        City=(TextView) view.findViewById(R.id.preview_tender_city);
        Contact=(TextView) view.findViewById(R.id.preview_tender_mobile);
        LandLine=(TextView) view.findViewById(R.id.preview_tender_landline);
        Email=(TextView) view.findViewById(R.id.preview_tender_email);
        Address=(TextView) view.findViewById(R.id.preview_tender_address);
        rlAddress=(RelativeLayout) view.findViewById(R.id.rl_preview_address);
        rlContact=(RelativeLayout) view.findViewById(R.id.rl_preview_mobile);
        rlLandline=(RelativeLayout) view.findViewById(R.id.rl_preview_landline);
        rlEmail=(RelativeLayout) view.findViewById(R.id.rl_preview_email);
        removeTender=(Button) view.findViewById(R.id.remove_tender);
        editTender=(Button) view.findViewById(R.id.edit_tender);
        imagetender=(ImageView) view.findViewById(R.id.preview_tender_image);


        editTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();

                arguments.putParcelable( "object" , object);

                arguments.putString("day", String.valueOf(day));
                fragment2.setArguments(arguments);
                fragmentTransaction.replace(R.id.content_frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        removeTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token="Bearer "+sp.getPreferences(getActivity(),"token");
                String id=object.getId().toString();
                mApiService.removeTender(token,id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i(TAG,"response---"+response.body());

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG,"response---"+t);

                    }
                });

            }
        });


        Bundle args = getArguments();
        if (args != null) {
            object = args.getParcelable("object");
            day=args.getString("day");
        } else {
            Log.w("GetObject", "Arguments expected, but missing");
        }
        if(!object.getTenderPhoto().toString().equals("")){
            Picasso.with(getActivity())
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
        GetCategory(view);
        GetAllCountry(view);
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


        flag3=(ImageView) view.findViewById(R.id.preview_flag_image);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Tender Detail");
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

    private void GetAllCountry(final View v) {
        final View v1 = v;
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
