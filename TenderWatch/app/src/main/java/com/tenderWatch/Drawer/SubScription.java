package com.tenderWatch.Drawer;

import android.graphics.Bitmap;
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
    SharedPreference sp = new SharedPreference();
    WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_subscription, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAPIServices = ApiUtils.getAPIService();
        getActivity().setTitle("SubScription Detail");
        mWebView = (WebView) view.findViewById(R.id.webview23);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
//        sp.showProgressDialog(getActivity());

        User user = (User) SharedPreference.getPreferencesObject(getActivity());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                sp.showProgressDialog(getActivity());
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                sp.hideProgressDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + user.getInvoiceURL());

        /*String token = "Bearer " + sp.getPreferences(getActivity(), "token");
        mAPIServices.getSubscriptionDetails(token).enqueue(new Callback<SubScriptionResponse>() {
            @Override
            public void onResponse(Call<SubScriptionResponse> call, Response<SubScriptionResponse> response) {
                Log.i(TAG, "post submitted to API." + response);
                sp.hideProgressDialog();
              mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="+response.body().getInvoiceURL().toString());

                //  new DownloadTask(getActivity(), response.body().getInvoiceURL().toString());
            }

            @Override
            public void onFailure(Call<SubScriptionResponse> call, Throwable t) {
                sp.hideProgressDialog();

                Log.i(TAG, "post submitted to API." + t);
            }
        });*/
    }
}

