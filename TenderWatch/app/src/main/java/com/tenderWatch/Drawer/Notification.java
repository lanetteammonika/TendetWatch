package com.tenderWatch.Drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tenderWatch.Adapters.NotificationAdapter;
import com.tenderWatch.Adapters.TenderListAdapter;
import com.tenderWatch.Models.ResponseNotifications;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;

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
    SharedPreference sp= new SharedPreference();
    ArrayList<ResponseNotifications> notification_list;
    NotificationAdapter adapter;
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
        notificationList=(ListView) view.findViewById(R.id.notificationlist);
        mAPIServices= ApiUtils.getAPIService();
        View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.layout_notification, null);
        final ImageView tick=(ImageView) inflatedView.findViewById(R.id.round_checked);
        final ImageView tick2=(ImageView) inflatedView.findViewById(R.id.round);
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        GetNotification();
    }

    private void GetNotification() {
        String token="Bearer "+sp.getPreferences(getActivity(),"token");
        mAPIServices.getNotifications(token).enqueue(new Callback<ArrayList<ResponseNotifications>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseNotifications>> call, Response<ArrayList<ResponseNotifications>> response) {
                Log.i(TAG, "post submitted to API." + response);
                int size=response.body().size();
                notification_list=response.body();

                adapter = new NotificationAdapter(getActivity(), notification_list,"true");
                notificationList.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<ResponseNotifications>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);
            }
        });
    }
}
