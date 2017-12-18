package com.tenderWatch.ClientDrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenderWatch.Login;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom48 on 14/12/17.
 */

public class TenderList extends Fragment {
    Api mAPIService;
    SharedPreference sp=new SharedPreference();
    private static final String TAG = TenderList.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAPIService= ApiUtils.getAPIService();
        GetAllTender();

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Profile");
    }

    private void GetAllTender() {
        String token="Bearer " +sp.getPreferences(getActivity(),"token");
        mAPIService.getAllTender(token).enqueue(new Callback<Tender>() {
            @Override
            public void onResponse(Call<Tender> call, Response<Tender> response) {
                Log.i(TAG, "post submitted to API." + response.body());

            }

            @Override
            public void onFailure(Call<Tender> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);

            }
        });
    }
}