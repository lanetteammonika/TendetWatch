package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.BankAccount;
import com.tenderWatch.Adapters.BankListAdapter;
import com.tenderWatch.Adapters.CustomList;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.Datum;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.RequestCharges;
import com.tenderWatch.Models.RequestPayment;
import com.tenderWatch.Models.ResponseBankList;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankList extends AppCompatActivity {
    SharedPreference sp = new SharedPreference();
    Intent intent;
    private Api mAPIService;
    String selCon;
    TextView country, bankType;
    String countryCode, categoryname, countryname;
    RequestCharges rc = new RequestCharges();
    CustomList countryAdapter;
    ImageView down_arrow, up_arrow, down_arrow2, up_arrow2;
    LinearLayout country_home, llbankType;
    ListView spinner, spinnerbanktype;
    private ArrayAdapter<String> listAdapter;
    private static final ArrayList<String> countryName = new ArrayList<String>();
    private List Data, Data2;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final String TAG = BankList.class.getSimpleName();
    BankListAdapter adapter;
    ListView bank;
    Button btnAdd;
    ArrayList<GetCountry> gc = new ArrayList<>();
    String coCurrency, coCode, accName, accType, routingNum, accNum;
    EditText txtAccNum, txtAccName, txtRountngNum;
    RequestPayment rp=new RequestPayment();
    CreateUser user = new CreateUser();
    ConnectivityReceiver cr = new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        mAPIService = ApiUtils.getAPIService();
        bank = (ListView) findViewById(R.id.list_bank);
        btnAdd = (Button) findViewById(R.id.add_bank);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetBankDetail();

            }
        });
        //
        GetBankList();
        bank.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowBox(i);
                return true;
            }
        });
        bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String b_id = adapter.bankList.get(i).getId();
                String token = "Bearer " + sp.getPreferences(BankList.this, "token");

                int payment = Integer.parseInt(sp.getPreferences(BankList.this, "payment")) * 100;
                rc.setSource(b_id);
                rc.setAmount(payment);
                if(cr.isConnected(BankList.this)) {
                    mAPIService.payCharges(token, rc).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.i(TAG, "response register-->");
                            CallUpdateServices();


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.i(TAG, "response register-->");
                        }
                    });
                }else{
                    sp.ShowDialog(BankList.this,"Please check your internet connection.");
                }
            }
        });
        myBroadcastReceiver = new MyBroadcastReceiver();

    }

    public void ShowMsg(Context context, String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Tender Watch");
        builder.setMessage(Msg);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(BankList.this, MainDrawer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        builder.show();
    }

    private void CallUpdateServices() {
        String token="Bearer " +sp.getPreferences(BankList.this,"token");

        int payment2= Integer.parseInt(sp.getPreferences(BankList.this,"payment"));
        rp.setPayment(payment2);
        String subscribe2;
        if(payment2==15){
            subscribe2="2";
        }else {
            subscribe2="0";
        }
        rp.setSubscribe(subscribe2);
        rp.setSelections(user.getSubscribe());
        if(cr.isConnected(BankList.this)){
        mAPIService.updateService(token,rp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "response register-->");
                ShowMsg(BankList.this, "Payment Successfull ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "response register-->");
            }
        });
        }else{
            sp.ShowDialog(BankList.this,"Please check your internet connection.");
        }
    }


    private void ShowBox(final int i) {
        final AlertDialog alertDialog = new AlertDialog.Builder(BankList.this).create();

        alertDialog.setTitle("Tender Watch");

        alertDialog.setMessage("Are you sure to delete record?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String token = "Bearer " + sp.getPreferences(BankList.this, "token");
                String tenderid = String.valueOf(adapter.bankList.get(0).getId());
                if(cr.isConnected(BankList.this)){
                mAPIService.deleteBank(token, tenderid).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        sp.hideProgressDialog();
                        Log.i(TAG, "response---" + response);
                        GetBankList();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "response---" + t);
                    }
                });
                }else{
                    sp.ShowDialog(BankList.this,"Please check your internet connection");
                }
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void GetBankList() {
//        sp.showProgressDialog(getApplicationContext());
        if( sp.getPreferences(BankList.this, "token")!=null) {
            String token = "Bearer " + sp.getPreferences(BankList.this, "token");
            if(cr.isConnected(BankList.this)){
            mAPIService.getBankList(token).enqueue(new Callback<ResponseBankList>() {
                @Override
                public void onResponse(Call<ResponseBankList> call, Response<ResponseBankList> response) {
                    Log.i(TAG, "response register-->");
                    adapter = new BankListAdapter(getApplicationContext(), (ArrayList<Datum>) response.body().getData());
                    bank.setAdapter(adapter);
                    sp.hideProgressDialog();
                }

                @Override
                public void onFailure(Call<ResponseBankList> call, Throwable t) {
                    Log.i(TAG, "response register-->");
                }
            });
            }else{
                sp.ShowDialog(BankList.this,"Please check your internet connection.");
            }
        }
    }

    private void GetBankDetail() {
        final Dialog dialog = new Dialog(BankList.this);
        dialog.setContentView(R.layout.bankdetail);

        txtAccName = (EditText) dialog.findViewById(R.id.bank_acc_holder_name);
        txtAccNum = (EditText) dialog.findViewById(R.id.bank_account_no);
        txtRountngNum = (EditText) dialog.findViewById(R.id.bnak_routing_no);

        spinner = (ListView) dialog.findViewById(R.id.spinner3);
        country_home = (LinearLayout) dialog.findViewById(R.id.category_home);
        down_arrow = (ImageView) dialog.findViewById(R.id.bank_down_arrow);
        up_arrow = (ImageView) dialog.findViewById(R.id.bank_up_arrow);
        down_arrow2 = (ImageView) dialog.findViewById(R.id.bank_down_arrow2);
        up_arrow2 = (ImageView) dialog.findViewById(R.id.bank_up_arrow2);
        spinnerbanktype = (ListView) dialog.findViewById(R.id.spinner4);
        llbankType = (LinearLayout) dialog.findViewById(R.id.bank_type);
        Button save = (Button) dialog.findViewById(R.id.btn_Save);
        country = (TextView) dialog.findViewById(R.id.txt_bank_country_name);
        bankType = (TextView) dialog.findViewById(R.id.txt__name);
        String[] planets = new String[]{"individual", "company"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));
        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
        spinnerbanktype.setAdapter(listAdapter);

        sp.showProgressDialog(BankList.this);

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                sp.showProgressDialog(getApplicationContext());
                String token = "Bearer " + sp.getPreferences(BankList.this, "token");
                accName = txtAccName.getText().toString();
                routingNum = txtRountngNum.getText().toString();
                accNum = txtAccNum.getText().toString();
                if(cr.isConnected(BankList.this)){
                mAPIService.createAcc(token, coCode, coCurrency, accName, accType, routingNum, accNum).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i(TAG, "response register-->");
                        sp.ShowDialog(BankList.this,"Your Bank Account created Successfully");
                        GetBankList();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "response register-->");
                    }
                });
                }else{
                    sp.ShowDialog(BankList.this,"Please check your internet connection.");
                }
            }
        });
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryAdapter.setItemSelected(position);
                countryAdapter.getItem(position);
                countryname = countryAdapter.getItem(position).toString().split("~")[0];
                countryCode = countryAdapter.getItem(position).toString().split("~")[1];
                String id1 = countryName.get(position).split("~")[2];
                //countryId1 = MultipartBody.Part.createFormData("country", id1);
                coCode = countryAdapter.getItem(position).toString().split("~")[4];
                coCurrency = countryAdapter.getItem(position).toString().split("~")[5];
                country.setText(countryname);
                country_home.setVisibility(View.GONE);
                up_arrow.setVisibility(View.GONE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });
        spinnerbanktype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bankType.setText(listAdapter.getItem(i));
                accType = listAdapter.getItem(i);
                llbankType.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.VISIBLE);
                up_arrow2.setVisibility(View.GONE);
            }
        });

        up_arrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                country_home.setVisibility(View.GONE);
                up_arrow.setVisibility(View.GONE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                GetAllCountry();
                country_home.setVisibility(View.VISIBLE);
                up_arrow.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.GONE);
            }
        });

        up_arrow2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                llbankType.setVisibility(View.GONE);
                down_arrow2.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.VISIBLE);
                up_arrow2.setVisibility(View.GONE);
            }
        });

        down_arrow2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                country_home.setVisibility(View.GONE);
                llbankType.setVisibility(View.VISIBLE);
                up_arrow2.setVisibility(View.VISIBLE);
                down_arrow2.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }
    private void GetAllCountry() {
        if(cr.isConnected(BankList.this)){
            mAPIService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
                @Override
                public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                    sp.hideProgressDialog();
                    Data = response.body();
                    alpha.clear();
                    gc = response.body();
                    for (int i = 0; i < Data.size(); i++) {
                        alpha.add(response.body().get(i).getCountryName() + "~" + response.body().get(i).getCountryCode() + "~" + response.body().get(i).getId()+"~" + response.body().get(i).getImageString()+ "~" + response.body().get(i).getIsoCode() + "~" + response.body().get(i).getIsoCurrencyCode());
                        countryName.add(response.body().get(i).getCountryName().toString() + "~" + response.body().get(i).getCountryCode().toString() + "~" + response.body().get(i).getId() + "~" + response.body().get(i).getIsoCode() + "~" + response.body().get(i).getIsoCurrencyCode());
                    }
                    Collections.sort(alpha);
                    Collections.sort(countryName);
                    countryAdapter = new CustomList(BankList.this, alpha);
                    spinner.setAdapter(countryAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {

                }
            });
        }else{
            sp.ShowDialog(BankList.this,"Please check your internet connection.");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(BankList.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(BankList.this).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(BankList.this).unregisterReceiver(myBroadcastReceiver);
    }
}
