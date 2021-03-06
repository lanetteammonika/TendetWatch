package com.tenderWatch.ClientDrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
import com.tenderWatch.Adapters.TenderListAdapter;
import com.tenderWatch.ContractotTenderDetail;
import com.tenderWatch.EditTenderDetail;
import com.tenderWatch.Models.AllContractorTender;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.TenderUploader;
import com.tenderWatch.Models.UpdateTender;
import com.tenderWatch.Models.User;
import com.tenderWatch.PreviewTenderDetail;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.text.ParseException;
import java.util.ArrayList;
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
    ArrayList<AllContractorTender> contractoradapter = new ArrayList<AllContractorTender>();
    Tender tender;
    AllContractorTender contractorTender;
    String role;
    User user;

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
        role = sp.getPreferences(getActivity(), "role");

        if (role.equals("client")) {
            GetAllTender();
        }else{
            Bundle bundle = getArguments();
            if (bundle  != null) {
                String value = bundle.getString("nav_fav");
                if(value!=null) {
                   GetAllFavorite();
                }
            }
            else{
                AllContractorTender();
            }
        }

        list_tender = (ListView) view.findViewById(R.id.list_tender);
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
        if (role.equals("client")) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
        list_tender.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (role.equals("client")) {
                    ShowBox(position);
                }else{
                    Bundle bundle = getArguments();
                    if (bundle  != null) {
                        String value = bundle.getString("nav_fav");
                        if(value!=null) {
                            ShowBoxFavorite(position);
                        }
                    }
                    else{
                        ShowBoxForContractor(position);
                    }
                }
                return true;
            }
        });

        list_tender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (role.equals("client")) {
                    tender = allTender.get(position);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(tender);
                    Date startDateValue = null, endDateValue = null;
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

                    if (days == 0) {
                        sp.ShowDialog(getActivity(), "Tender is not Activated.");
                    } else {
                        Intent intent = new Intent(getActivity(), PreviewTenderDetail.class);
                        intent.putExtra("data", jsonString);
                        startActivity(intent);
                    }
                } else {
                    contractorTender = contractoradapter.get(position);
                    String token = "Bearer " + sp.getPreferences(getActivity(), "token");
                    String id2 = contractorTender.getId().toString();
                    mAPIService.getTender(token, id2).enqueue(new Callback<UpdateTender>() {
                        @Override
                        public void onResponse(Call<UpdateTender> call, Response<UpdateTender> response) {
                            Log.i(TAG, "post submitted to API." + response.body());
                        }

                        @Override
                        public void onFailure(Call<UpdateTender> call, Throwable t) {
                            Log.i(TAG, "post submitted to API." + t);
                        }
                    });


                    user = (User) sp.getPreferencesObject(getActivity());
                    TenderUploader client = contractorTender.getTenderUploader();
                    Log.i(TAG, "post submitted to API." + client);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(contractorTender);
                    String sender = gson.toJson(client);
                    Intent intent = new Intent(getActivity(), ContractotTenderDetail.class);
                    intent.putExtra("data", jsonString);
                    intent.putExtra("sender", sender);
                    if(contractoradapter.get(position).getAmendRead()!=null) {
                        if (contractoradapter.get(position).getAmendRead().size() > 0) {
                            for (int i = 0; i < contractoradapter.get(position).getAmendRead().size(); i++) {
                                String Con_id = user.getId();
                                if (!contractoradapter.get(position).getAmendRead().contains(Con_id)) {
                                    intent.putExtra("amended", "true");
                                }
                            }
                        }
                        if (contractoradapter.get(position).getAmendRead().size() == 0) {
                            intent.putExtra("amended", "true");
                        }
                    }
                    startActivity(intent);
                }
            }
        });
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Tender Watch");
    }

    private void ShowBoxFavorite(final int i) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setTitle("Tender Watch");

        alertDialog.setMessage("Are you sure to delete record?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                contractorTender = contractoradapter.get(i);
                final String token = "Bearer " + sp.getPreferences(getActivity(), "token");
                String tenderid = contractorTender.getId();
                sp.showProgressDialog(getActivity());
                mAPIService.removeFavorite(token,tenderid).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       sp.hideProgressDialog();
                        Log.i(TAG, "post submitted to API." + response.body());
                        GetAllFavorite();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "post submitted to API." + t);
                    }
                });
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void GetAllFavorite(){
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        sp.showProgressDialog(getActivity());
        adapter=null;
        mAPIService.getAllFavoriteTender(token).enqueue(new Callback<ArrayList<AllContractorTender>>() {
            @Override
            public void onResponse(Call<ArrayList<AllContractorTender>> call, Response<ArrayList<AllContractorTender>> response) {
                Log.i(TAG, "post submitted to API." + response.body());
                if (response.body() != null) {
                    contractoradapter = response.body();
                    Con_adapter = new ContractorTenderListAdapter(getActivity(), response.body());
                    list_tender.setAdapter(Con_adapter);
                }
                sp.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ArrayList<AllContractorTender>> call, Throwable t) {
                sp.hideProgressDialog();
                list_tender.setAdapter(null);

            }
        });
    }

    private void AllContractorTender() {
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        sp.showProgressDialog(getActivity());
        mAPIService.getAllContractorTender(token).enqueue(new Callback<ArrayList<AllContractorTender>>() {
            @Override
            public void onResponse(Call<ArrayList<AllContractorTender>> call, Response<ArrayList<AllContractorTender>> response) {
                sp.hideProgressDialog();
                Log.i(TAG, "post submitted to API." + response.body());
                if (response.body() != null) {
                    contractoradapter = response.body();
                    Con_adapter = new ContractorTenderListAdapter(getActivity(), response.body());
                    list_tender.setAdapter(Con_adapter);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllContractorTender>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);
                sp.hideProgressDialog();
                sp.ShowDialog(getActivity(),"server is down");
            }
        });
    }

    private void GetAllTender() {
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        sp.showProgressDialog(getActivity());
        adapter=null;
        mAPIService.getAllTender(token).enqueue(new Callback<ArrayList<Tender>>() {
            @Override
            public void onResponse(Call<ArrayList<Tender>> call, Response<ArrayList<Tender>> response) {
                // Log.i(TAG, "post submitted to API." + response.body());
                sp.hideProgressDialog();
                if (response.body() != null) {
                    allTender = response.body();
                    adapter = new TenderListAdapter(getActivity(), response.body());
                    list_tender.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tender>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);
                sp.hideProgressDialog();
                sp.ShowDialog(getActivity(),"Tender not Found");
            }
        });
    }

    private void RemoveTender(int i, final AlertDialog alertDialog) {
        tender = allTender.get(i);
        final String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        String tenderid = tender.getId().toString();
        mAPIService.removeTender(token, tenderid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "response---" + response.body());
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
                Log.i(TAG, "response---" + t);
            }
        });
    }

    private void ShowBox(final int i) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setTitle("Tender Watch");

        alertDialog.setMessage("Are you sure to delete or edit record?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String Con_msg = "Tender will be completely removed from TenderWatch.are you sure you want to remove?";
                String Client_msg = "Are you sure you want to remove this Tender completely from your Account?";
                tender = allTender.get(i);
                final String token = "Bearer " + sp.getPreferences(getActivity(), "token");
                String tenderid = tender.getId().toString();
                sp.showProgressDialog(getActivity());

                mAPIService.removeTender(token, tenderid).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       sp.hideProgressDialog();
                        if (role.equals("client")) {
                            GetAllTender();
                        }else{
                            Bundle bundle = getArguments();
                            if (bundle  != null) {
                                String value = bundle.getString("nav_fav");
                                if(value!=null) {
                                    GetAllFavorite();
                                }
                            }
                            else{
                                AllContractorTender();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "response---" + t);
                    }
                });
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Edit", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int id) {
                tender = allTender.get(i);
                Gson gson = new Gson();
                String jsonString = gson.toJson(tender);
                Intent intent = new Intent(getActivity(), EditTenderDetail.class);
                intent.putExtra("data", jsonString);
                startActivity(intent);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void ShowBoxForContractor(final int i) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setTitle("Tender Watch");

        alertDialog.setMessage("Are you sure to Delete or add Favorite record?");




        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                contractorTender = contractoradapter.get(i);
                final String token = "Bearer " + sp.getPreferences(getActivity(), "token");
                String tenderid = contractorTender.getId().toString();
                sp.showProgressDialog(getActivity());

                mAPIService.removeTender(token, tenderid).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        sp.hideProgressDialog();
                        Log.i(TAG, "response---" + response.body());
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
                        Log.i(TAG, "response---" + t);
                    }
                });
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Favorite", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int id) {
                contractorTender = contractoradapter.get(i);
                final String token = "Bearer " + sp.getPreferences(getActivity(), "token");
                String tenderid = contractorTender.getId().toString();
                sp.showProgressDialog(getActivity());

                mAPIService.addFavorite(token,tenderid).enqueue(new Callback<UpdateTender>() {
                    @Override
                    public void onResponse(Call<UpdateTender> call, Response<UpdateTender> response) {
                        sp.hideProgressDialog();
                        Log.i(TAG, "response---" + response.body());
                        final Fragment fragment3 = new TenderList();
                        FragmentManager fragmentManager = getFragmentManager();
                        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment3);
                        //fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<UpdateTender> call, Throwable t) {
                        Log.i(TAG, "response---" + t);
                    }
                });
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}