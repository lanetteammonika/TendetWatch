package com.tenderWatch.ClientDrawer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.Validation.MyScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private List Data, Data2;
    CustomList countryAdapter, categoryAdapter;
    ListView spinner, spinner2;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2, down_arrow3, up_arrow3;
    LinearLayout country_home, category_home;
    TextView country, category;
    String countryCode;
    MyScrollView scrollView;

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

        down_arrow = (ImageView) view.findViewById(R.id.down_arrow);
        up_arrow = (ImageView) view.findViewById(R.id.up_arrow);
        down_arrow2 = (ImageView) view.findViewById(R.id.down_arrow2);
        up_arrow2 = (ImageView) view.findViewById(R.id.up_arrow2);
        down_arrow3 = (ImageView) view.findViewById(R.id.down_arrow3);
        up_arrow3 = (ImageView) view.findViewById(R.id.up_arrow3);
        country_home = (LinearLayout) view.findViewById(R.id.country_home);
        category_home = (LinearLayout) view.findViewById(R.id.category_home);
        country = (TextView) view.findViewById(R.id.txt_home_country_name);
        category = (TextView) view.findViewById(R.id.txt_contact_category_name);
        mApiService = ApiUtils.getAPIService();
        spinner.setOnItemSelectedListener(this);
        scrollView = (MyScrollView) view.findViewById(R.id.home_scroll);
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

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.contact_to_tender);
                final Button dismissButton = (Button) dialog.findViewById(R.id.contact_save);
                EditText mobile = (EditText) dialog.findViewById(R.id.contact_mobile);
                final ImageView box = (ImageView) dialog.findViewById(R.id.home_box);
                final ImageView boxright = (ImageView) dialog.findViewById(R.id.home_box_checked);
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boxright.setVisibility(View.VISIBLE);
                        box.setVisibility(View.GONE);
                        dismissButton.setAlpha((float) 1);
                    }
                });
                boxright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boxright.setVisibility(View.GONE);
                        box.setVisibility(View.VISIBLE);
                        dismissButton.setAlpha((float) 0.7);
                    }
                });

                mobile.setText("+" + countryCode + "-");
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        GetAllCountry(view);
        GetCategory(view);

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = countryName.get(position).split("~")[0];
                countryCode = countryName.get(position).split("~")[1];
                country.setText(name);
                country_home.setVisibility(View.GONE);
                scrollView.setScrolling(true);
                up_arrow.setVisibility(View.GONE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });
        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = alpha2.get(position).split("~")[0];
                category.setText(name);
                category_home.setVisibility(View.GONE);
                scrollView.setScrolling(true);
                up_arrow2.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
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


                    // CountryFlag.add(response.body().get(i).getImageString().toString());
                }
                //Collections.sort(alpha);
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
                    countryName.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getCountryCode().toString());

                    // CountryFlag.add(response.body().get(i).getImageString().toString());
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
}
