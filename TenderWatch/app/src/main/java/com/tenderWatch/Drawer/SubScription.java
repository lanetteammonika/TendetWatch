package com.tenderWatch.Drawer;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tenderWatch.Models.SubScriptionResponse;
import com.tenderWatch.Models.User;
import com.tenderWatch.MyBroadcastReceiver;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcom47 on 26/12/17.
 */

public class SubScription extends Fragment {
    Api mAPIServices;
    private static final String TAG = SubScription.class.getSimpleName();
    SharedPreference sp=new SharedPreference();
    WebView mWebView;
    User user;
    String url;
    ConnectivityReceiver cr=new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_subscription, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAPIServices= ApiUtils.getAPIService();
        getActivity().setTitle("SubScription Detail");
        mWebView= (WebView) view.findViewById(R.id.webview23);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        sp.showProgressDialog(getActivity());
        myBroadcastReceiver=new MyBroadcastReceiver();
        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        user = (User) sp.getPreferencesObject(getActivity());
        String userId=user.getId();
        if(cr.isConnected(getActivity())){
        mAPIServices.getSubscriptionDetails(token).enqueue(new Callback<SubScriptionResponse>() {
            @Override
            public void onResponse(Call<SubScriptionResponse> call, Response<SubScriptionResponse> response) {
                Log.i(TAG, "post submitted to API." + response);
                url="http://docs.google.com/gview?embedded=true&url="+response.body().getInvoiceURL();
                startWebView(url);
                mWebView.loadUrl(url);
                sp.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<SubScriptionResponse> call, Throwable t) {
                sp.hideProgressDialog();
                Log.i(TAG, "post submitted to API." + t);
            }
        });
        }else{
            sp.ShowDialog(getActivity(),"Please check your internet connection");
        }

    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        mWebView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
                progressDialog.setCanceledOnTouchOutside(false);

            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    public void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadcastReceiver);
    }
}