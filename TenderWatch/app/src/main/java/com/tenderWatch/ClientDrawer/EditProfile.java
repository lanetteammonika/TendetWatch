package com.tenderWatch.ClientDrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.CountryList;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SideSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom48 on 14/12/17.
 */

public class EditProfile extends Fragment implements View.OnClickListener{
    EditText txtCountry,txtMobileNo,txtOccupation,txtAboutMe;
    Api mAPIService;
    private List Data;
    ArrayList<String> countryName;

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
    }

    @Override
    public void onClick(View v) {

    }
}