package com.tenderWatch.ClientDrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tenderWatch.Adapters.ContractorTenderListAdapter;
import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.Adapters.TenderListAdapter;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.EditTenderDetail;
import com.tenderWatch.Login;
import com.tenderWatch.Models.AllContractorTender;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.PreviewTenderDetail;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
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
    ContractorTenderListAdapter Con_adapter;
    ListView list_tender;
    ArrayList<AllContractorTender> contractoradapter=new ArrayList<AllContractorTender>();
    Tender tender;
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
        String role=sp.getPreferences(getActivity(),"role");
        if(role.equals("client")){
            GetAllTender();
        }else{
            AllContractorTender();
        }

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
            }
        });
        //String role=sp.getPreferences(getActivity(),"role");
        if(role.equals("client")) {
            fab.setVisibility(View.VISIBLE);
        }else {
            fab.setVisibility(View.GONE);
        }
        list_tender.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               ShowBox(position);
                return true;
            }
        });

        list_tender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tender=allTender.get(position);
                Gson gson = new Gson();
                String jsonString = gson.toJson(tender);
                Date startDateValue = null,endDateValue = null;
                try {
                    // startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
                    startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(tender.getCreatedAt().split("T")[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(tender.getExpiryDate().split("T")[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Date endDateValue = new Date(allTender.get(position).getExpiryDate().split("T")[0]);
                long diff = endDateValue.getTime() - startDateValue.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = (hours / 24) + 1;

                if(days==0){
                    sp.ShowDialog(getActivity(),"Tender is not Activated.");
                }else{
                    Intent intent = new Intent(getActivity(),PreviewTenderDetail.class);
                    intent.putExtra("data",jsonString);
                    startActivity(intent);
                }


            }
        });
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Tender Watch");
    }

    private void AllContractorTender() {
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        mAPIService.getAllContractorTender(token).enqueue(new Callback<ArrayList<AllContractorTender>>() {
            @Override
            public void onResponse(Call<ArrayList<AllContractorTender>> call, Response<ArrayList<AllContractorTender>> response) {
                Log.i(TAG, "post submitted to API." + response.body());
                if(response.body()!=null) {
                    contractoradapter = response.body();
                    Con_adapter = new ContractorTenderListAdapter(getActivity(), response.body());
                    list_tender.setAdapter(Con_adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllContractorTender>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);

            }
        });
    }

    private void GetAllTender() {
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        mAPIService.getAllTender(token).enqueue(new Callback<ArrayList<Tender>>() {
            @Override
            public void onResponse(Call<ArrayList<Tender>> call, Response<ArrayList<Tender>> response) {
               // Log.i(TAG, "post submitted to API." + response.body());
                if(response.body()!=null) {
                    allTender = response.body();
                    adapter = new TenderListAdapter(getActivity(), response.body());
                    list_tender.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tender>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);

            }
        });
    }
    private void RemoveTender(int i, final AlertDialog alertDialog){
        tender=allTender.get(i);
        final String token="Bearer "+sp.getPreferences(getActivity(),"token");
        String tenderid=tender.getId().toString();
        mAPIService.removeTender(token,tenderid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG,"response---"+response.body());
                final Fragment fragment3 = new TenderList();
                FragmentManager fragmentManager = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment3);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                alertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"response---"+t);
            }
        });
    }
    private void ShowBox(final int i){
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setTitle("Tender Watch");

        alertDialog.setMessage("Are you sure to delete or edit record?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String Con_msg="Tender will be completely removed from TenderWatch.are you sure you want to remove?";
                String Client_msg="Are you sure you want to remove this Tender completely from your Account?";
                tender=allTender.get(i);
                final String token="Bearer "+sp.getPreferences(getActivity(),"token");
                String tenderid=tender.getId().toString();
                mAPIService.removeTender(token,tenderid).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i(TAG,"response---"+response.body());
                        final Fragment fragment3 = new TenderList();
                        FragmentManager fragmentManager = getFragmentManager();
                        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment3);
                        //fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG,"response---"+t);
                    }
                });
            } });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Edit", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int id) {
                tender=allTender.get(i);
                Gson gson = new Gson();
                String jsonString = gson.toJson(tender);
                Intent intent = new Intent(getActivity(),EditTenderDetail.class);
                intent.putExtra("data",jsonString);
                startActivity(intent);
            }});

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }});
        alertDialog.show();
    }
}