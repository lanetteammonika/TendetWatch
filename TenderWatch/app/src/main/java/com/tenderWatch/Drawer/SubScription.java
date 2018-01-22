package com.tenderWatch.Drawer;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tenderWatch.Models.SubScriptionResponse;
import com.tenderWatch.Models.User;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

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

        String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        user = (User) sp.getPreferencesObject(getActivity());
        String userId=user.getId();

        mAPIServices.getUserDetail(token,userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i(TAG, "post submitted to API." + response);
                sp.hideProgressDialog();
                url="http://docs.google.com/gview?embedded=true&url="+response.body().getInvoiceURL();
                startWebView(url);
                mWebView.loadUrl(url);
                //mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="+response.body().getInvoiceURL());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                sp.hideProgressDialog();
                Log.i(TAG, "post submitted to API." + t);
            }
        });


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


        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview



    }
}