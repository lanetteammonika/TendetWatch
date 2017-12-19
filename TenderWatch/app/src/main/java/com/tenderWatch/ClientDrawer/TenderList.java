package com.tenderWatch.ClientDrawer;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.Adapters.TenderListAdapter;
import com.tenderWatch.Login;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom48 on 14/12/17.
 */

public class TenderList extends Fragment {
    Api mAPIService;
    SharedPreference sp = new SharedPreference();
    private static final String TAG = TenderList.class.getSimpleName();
    Intent intent;
    ArrayList<Tender> allTender = new ArrayList<Tender>();
    TenderListAdapter adapter;
    ListView list_tender;
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
        mAPIService = ApiUtils.getAPIService();
        GetAllTender();
        list_tender=(ListView) view.findViewById(R.id.list_tender);
        final Fragment fragment2 = new Home();
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.content_frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                intent=new Intent(getActivity(),Home.class);
//                startActivity(intent);
            }
        });

        list_tender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object tender=allTender.get(position);
                Date startDateValue = null,endDateValue = null;
                try {
                    startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(allTender.get(position).getCreatedAt().split("T")[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(allTender.get(position).getExpiryDate().split("T")[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Date endDateValue = new Date(allTender.get(position).getExpiryDate().split("T")[0]);
                long diff = endDateValue.getTime() - startDateValue.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = (hours / 24) + 1;
                Log.d("days", "" + days);
                Log.i(TAG, "post submitted to API." + tender.toString());

            }
        });
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Profile");
    }

    private void GetAllTender() {
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        mAPIService.getAllTender(token).enqueue(new Callback<ArrayList<Tender>>() {
            @Override
            public void onResponse(Call<ArrayList<Tender>> call, Response<ArrayList<Tender>> response) {
               // Log.i(TAG, "post submitted to API." + response.body());
                allTender=response.body();
                adapter=new TenderListAdapter(getActivity(),response.body());
                list_tender.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Tender>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);

            }
        });
    }
}