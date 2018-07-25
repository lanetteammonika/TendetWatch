package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.User;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesapalListActivity extends AppCompatActivity {

    private static final String TAG = "PesapalListActivity";

    LinearLayout mLlPbLoader, llPaymentSucess;
    WebView mWebViewPesaPal;
    RecyclerView mRvPesapalList;

    private HashMap<String, ArrayList<String>> selection;
    private int amount = 0;
    private int subscriptionType = 1;
    private PesapalAdapter pesapalAdapter;
    private Api mApiService;
    private ProgressDialog progressDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesapal_list);

        getDataFromIntent();
        initViews();
        callPesapalURL();
//        callApiToGetPesapalPaymentDetails();
    }

    public void getDataFromIntent() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getSerializable("selections") != null) {
                selection = (HashMap<String, ArrayList<String>>) getIntent().getExtras().getSerializable("selections");
            }
            amount = getIntent().getExtras().getInt("amount", 0);
            subscriptionType = getIntent().getExtras().getInt("subscriptionType", 1);
        }
    }

    public void initViews() {

        mApiService = ApiUtils.getAPIService();

        progressDialog = new ProgressDialog(PesapalListActivity.this);
        progressDialog.setMessage("Please wait");


        mWebViewPesaPal = (WebView) findViewById(R.id.webview_pesapal);
        mLlPbLoader = (LinearLayout) findViewById(R.id.ll_pb_loader);
//        llToolbar = (LinearLayout) findViewById(R.id.ll_toolbar);
        llPaymentSucess = (LinearLayout) findViewById(R.id.ll_payment_success);
        mRvPesapalList = (RecyclerView) findViewById(R.id.rv_pesapal_list);

        mWebViewPesaPal.setVisibility(View.GONE);
        mLlPbLoader.setVisibility(View.GONE);
        llPaymentSucess.setVisibility(View.GONE);
        mRvPesapalList.setVisibility(View.GONE);
    }

    public void initRecyclerView() {
        mRvPesapalList.setVisibility(View.VISIBLE);
        mWebViewPesaPal.setVisibility(View.GONE);
        mLlPbLoader.setVisibility(View.GONE);
        llPaymentSucess.setVisibility(View.GONE);

        pesapalAdapter = new PesapalAdapter(PesapalListActivity.this, user.getPesapalDetails());
        mRvPesapalList.setAdapter(pesapalAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PesapalListActivity.this);
        mRvPesapalList.setLayoutManager(layoutManager);
        mRvPesapalList.setHasFixedSize(true);
    }

    public void callPesapalURL() {

        List<String> values = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (selection != null && selection.size() > 0) {
                for (String currentKey : selection.keySet()) {
                    values = selection.get(currentKey);
                    jsonObject.put(currentKey, values);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jObj = new JSONObject();

        try {
            jObj.put("selections", jsonObject);
            jObj.put("subscriptionPackage", subscriptionType);
            jObj.put("register", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mLlPbLoader.setVisibility(View.VISIBLE);

        User user = (User) SharedPreference.getPreferencesObject(PesapalListActivity.this);

        final String token = "Bearer " + SharedPreference.getPreferences(PesapalListActivity.this, "token");

        mApiService.getPesaPalURL(token, "PesaPal Payment", amount, user.getEmail(), jObj).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    mLlPbLoader.setVisibility(View.GONE);
                    String responseString = response.body().string();
                    if (!TextUtils.isEmpty(responseString)) {
                        if (!PesapalListActivity.this.isFinishing())
                            progressDialog.show();
                        mWebViewPesaPal.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jObj = new JSONObject(responseString);
                            final String url = jObj.optString("URL");

                            mWebViewPesaPal.setWebViewClient(new WebViewClient() {

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    Log.e(TAG, "onPageFinished: " + url);
                                    if (url.contains("http://tenderwatchweb.s3-website.ap-south-1.amazonaws.com/pesapaldetails?pesapal_transaction_tracking_id")) {
                                        mWebViewPesaPal.setVisibility(View.GONE);
//                                        llPaymentSucess.setVisibility(View.VISIBLE);
                                        callApiToGetPesapalPaymentDetails();

                                    }
                                    super.onPageFinished(view, url);
                                    if (!PesapalListActivity.this.isFinishing())
                                        progressDialog.dismiss();
                                }

                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);
                                }

                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    return super.shouldOverrideUrlLoading(view, url);
                                }
                            });

                            mWebViewPesaPal.getSettings().setJavaScriptEnabled(true);
                            mWebViewPesaPal.loadUrl(url);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                mLlPbLoader.setVisibility(View.GONE);
                if (!PesapalListActivity.this.isFinishing())
                    progressDialog.dismiss();
            }
        });
    }

    public void callApiToGetPesapalPaymentDetails() {
        progressDialog.show();
        String token = "Bearer " + SharedPreference.getPreferences(PesapalListActivity.this, "token");
        String userId = ((User) SharedPreference.getPreferencesObject(PesapalListActivity.this)).getId();

        mApiService.getUserDetail(token, userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if (!PesapalListActivity.this.isFinishing())
                    progressDialog.dismiss();

                initRecyclerView();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (!PesapalListActivity.this.isFinishing())
                    progressDialog.dismiss();
            }
        });
    }

    public class PesapalAdapter extends RecyclerView.Adapter<PesapalAdapter.PesapalHolder> {

        private Context context;
        private List<User.PesapalDetail> pesapalPaymentList;

        public PesapalAdapter(Context context, List<User.PesapalDetail> paypalPaymentList) {
            this.context = context;
            this.pesapalPaymentList = paypalPaymentList;
        }

        @Override
        public PesapalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesapal_payment, parent, false);
            PesapalHolder holder = new PesapalHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PesapalHolder holder, int position) {
            User.PesapalDetail pesapalDetail = pesapalPaymentList.get(position);

            if (!TextUtils.isEmpty(pesapalDetail.getPayment())) {
                holder.mTvPesapalPayment.setText("Pesapal payment " + pesapalDetail.getPayment());
            } else {
                holder.mTvPesapalPayment.setText(context.getResources().getString(R.string.pesapal_payment_initiated));
            }
            holder.mTvReferenceNo.setText("Reference no : " + pesapalDetail.getReference());

            long date = Long.parseLong(pesapalDetail.getReference());
            if (date > 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date);

                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                int mMinute = cal.get(Calendar.MINUTE);

                String dateToFormat = mYear + "-" + (mMonth + 1) + "-" + mDay + " " + mHour + ":" + mMinute;
                holder.mTvPaymentTime.setText(formatDate(dateToFormat));
            }

            if (!TextUtils.isEmpty(pesapalDetail.getPesapalStatus())) {
                holder.mTvPaymentStatus.setText(pesapalDetail.getPesapalStatus());
            }

            holder.mIvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return pesapalPaymentList.size();
        }

        public class PesapalHolder extends RecyclerView.ViewHolder {

            ImageView mIvRefresh;
            TextView mTvReferenceNo;
            TextView mTvPaymentTime;
            TextView mTvPaymentStatus;
            TextView mTvPesapalPayment;

            public PesapalHolder(View itemView) {
                super(itemView);
                mIvRefresh = itemView.findViewById(R.id.ivPaymentRefresh);
                mTvReferenceNo = itemView.findViewById(R.id.tvReferenceNo);
                mTvPaymentTime = itemView.findViewById(R.id.tvPaymentTime);
                mTvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
                mTvPesapalPayment = itemView.findViewById(R.id.tvPesapalPayment);
            }
        }
    }

    public String formatDate(String yourDateAsString) {
        String yourFormatedDateString = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = sdf.parse(yourDateAsString);

            if (android.text.format.DateFormat.is24HourFormat(PesapalListActivity.this))
                sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            else
                sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            yourFormatedDateString = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yourFormatedDateString;
    }
}
