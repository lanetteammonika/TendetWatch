package com.tenderWatch.ClientDrawer;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tenderWatch.Adapters.NotificationAdapter;
import com.tenderWatch.ClientDetail;
import com.tenderWatch.ContractorTenderDetail;
import com.tenderWatch.Models.Sender;
import com.tenderWatch.Models.UpdateTender;
import com.tenderWatch.MyBroadcastReceiver;
import com.tenderWatch.Models.ResponseNotifications;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom47 on 1/1/18.
 */

public class Notification extends Fragment {
    ListView notificationList;
    private static final String TAG = Notification.class.getSimpleName();
    Api mAPIServices;
    SharedPreference sp = new SharedPreference();
    ArrayList<ResponseNotifications> notification_list;
    NotificationAdapter adapter;
    Button delNotification;
    ArrayList<String> idList = new ArrayList<String>();
    String edit;
    Tender obj;
    ConnectivityReceiver cr=new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Notification");
        notificationList = (ListView) view.findViewById(R.id.notificationlist);
        mAPIServices = ApiUtils.getAPIService();
        delNotification = (Button) view.findViewById(R.id.btn_del_notification);
        Bundle args = getArguments();
        myBroadcastReceiver=new MyBroadcastReceiver();
        if (args != null) {
            edit = args.getString("edit");
            delNotification.setVisibility(View.VISIBLE);
        }

        delNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idList = adapter.getCheckedItem();
                DeleteNotification();
            }
        });

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                obj=notification_list.get(i).getTender();
                Sender s=notification_list.get(i).getSender();
                Gson gson = new Gson();
                String jsonString = gson.toJson(obj);
                String sender=gson.toJson(s);
                Intent intent = new Intent(getActivity(),ClientDetail.class);
                String token = "Bearer " + sp.getPreferences(getActivity(), "token");
                String id2 = notification_list.get(i).getId().toString();
                sp.showProgressDialog(getActivity());
                if(cr.isConnected(getActivity())){
                    mAPIServices.readNotification(token, id2).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            sp.hideProgressDialog();
                            Log.i(TAG, "post submitted to API." + response.body());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            sp.hideProgressDialog();
                            Log.i(TAG, "post submitted to API." + t);
                        }
                    });
                }else{
                    sp.ShowDialog(getActivity(),"Please check your internet connection");
                }
                intent.putExtra("id",obj.getId());
                intent.putExtra("uid",notification_list.get(i).getSender().getId());
                startActivity(intent);
            }
        });
        GetNotification();
    }



    private void DeleteNotification(){
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        sp.showProgressDialog(getActivity());
        if(cr.isConnected(getActivity())){
            mAPIServices.deleteNotification(token,idList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    sp.hideProgressDialog();
                    Log.i(TAG, "post submitted to API." + response);
                    GetNotification();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    sp.hideProgressDialog();
                    Log.i(TAG, "post submitted to API." + t);
                }
            });
        }else{
            sp.ShowDialog(getActivity(),"Please check your internet connection");
        }
    }

    private void GetNotification() {
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        sp.showProgressDialog(getActivity());
        if(cr.isConnected(getActivity())){
            mAPIServices.getNotifications(token).enqueue(new Callback<ArrayList<ResponseNotifications>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseNotifications>> call, Response<ArrayList<ResponseNotifications>> response) {
                    sp.hideProgressDialog();
                    Log.i(TAG, "post submitted to API." + response);
                    int size = response.body().size();
                    if(size==0){
                        delNotification.setVisibility(View.GONE);
                    }
                    notification_list = response.body();
                    if (edit != null) {
                        adapter = new NotificationAdapter(getActivity(), notification_list, edit);
                    } else {
                        adapter = new NotificationAdapter(getActivity(), notification_list, "");
                    }
                    notificationList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseNotifications>> call, Throwable t) {
                    sp.hideProgressDialog();
                    Log.i(TAG, "post submitted to API." + t);
                }
            });
        }else{
            sp.ShowDialog(getActivity(),"Please check your internet connection");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadcastReceiver);
    }
}
