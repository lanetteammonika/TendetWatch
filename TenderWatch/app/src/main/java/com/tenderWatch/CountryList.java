package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryList extends AppCompatActivity {

    private ListView lvCountry;
    private EditText edtSearch;
    private static final String TAG = CountryList.class.getSimpleName();
    private Api mAPIService;
    Map<String, Integer> mapIndex;
    private static final ArrayList<Item> countryList = new ArrayList<CountryList.Item>();
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> alpha2 = new ArrayList<String>();
    public static final ArrayList<String> list = new ArrayList<String>();
    public static char[] alphabetlist = new char[27];
    private List Data;
    //ArrayList list;
    public static final String JSON_STRING = "{\"employee\":{\"name\":\"Sachin\",\"salary\":56000}}";
    private SideSelector sideSelector = null;
    IndexingArrayAdapter adapter;
    Button btn_next;
    String alphabetS = "";
    LinearLayout lltext, back, subscription;
    TextView txtSelectedContract;
    Intent intent;
    String check, s;
    SharedPreference sp = new SharedPreference();
    ArrayList<String> a_country = new ArrayList<String>();
    ImageView imgClose;

    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        lltext = (LinearLayout) findViewById(R.id.lltext);
        back = (LinearLayout) findViewById(R.id.country_toolbar);
        subscription = (LinearLayout) findViewById(R.id.subscription);
        imgClose = (ImageView) findViewById(R.id.img_close);

        sideSelector = (SideSelector) findViewById(R.id.side_selector);
        mAPIService = ApiUtils.getAPIService();
        lvCountry.setDivider(null);
        btn_next = (Button) findViewById(R.id.btn_CountryNext);
        txtSelectedContract = (TextView) findViewById(R.id.txt_selectedContract);
        lvCountry.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Intent show = getIntent();
        check = show.getStringExtra("check");
        s = show.getStringExtra("sub");
        if (check == null) {
            CallContractorSignUp();
        }
        if (s != null) {
            subscription.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            txtSelectedContract.setVisibility(View.GONE);
        }
        if (!this.isFinishing())
            sp.showProgressDialog(CountryList.this);

        mAPIService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {

                Data = response.body();
                if (Data != null && !Data.isEmpty() && Data.size() > 0) {
                    for (int i = 0; i < Data.size(); i++) {
                        String name = response.body().get(i).getCountryName();
                        String flag = response.body().get(i).getImageString();
                        String countryCode = response.body().get(i).getCountryCode();
                        String id = response.body().get(i).getId();
                        alpha.add(name + '~' + id + '~' + countryCode + '`' + flag);
                    }

                    Collections.sort(alpha);
                    for (int i = 0; i < Data.size(); i++) {
                        String name = alpha.get(i).split("~")[0];
                        String id = alpha.get(i).split("~")[1].split("~")[0];
                        String countryCode = alpha.get(i).split("~")[2].split("`")[0];
                        String flag = alpha.get(i).split("~")[2].split("`")[1];
                        String value = String.valueOf(name.charAt(0));
                        if (!list.contains(value)) {
                            list.add(value);
                            alphabetS.concat(value);//alphabetlist.append(value);
                            Log.i("array-------", String.valueOf(list));
                            alpha2.add(value);
                            alpha2.add(name);

                            //set Country Header (Like:-A,B,C,...)
                            countryList.add(new SectionItem(value, "", "", "", false));
                            //set Country Name
                            countryList.add(new EntryItem(name, flag, countryCode, id, false));

                            //Log.i("array section-------",alpha.get(n).getTitle());

                        } else {
                            alpha2.add(name);

                            //set Country Name
                            countryList.add(new EntryItem(name, flag, countryCode, id, false));
                        }
                    }
                }

                alpha.clear();
                String str = null;
                char[] chars = new char[0];
                char[] al = new char[0];
                if (!list.isEmpty() && list.size() > 0) {
                    str = list.toString().replaceAll(",", "");
                    chars = str.toCharArray();
                    Log.i(TAG, "post submitted to API." + chars);
                    al = new char[27];

                    for (int j = 1, i = 0; j < chars.length; j = j + 2, i++) {
                        al[i] = chars[j];
                        Log.i(TAG, "post." + chars[j]);
                    }

                    Log.i(TAG, "post submitted to API." + al);
                    SideSelector ss = new SideSelector(getApplicationContext());
                    ss.setAlphabet(al);
                    alphabetlist = str.substring(1, str.length() - 1).replaceAll(" ", "").toCharArray();
                    adapter = new IndexingArrayAdapter(getApplicationContext(), R.id.lvCountry, countryList, alpha2, list, chars);
                    lvCountry.clearChoices();
                    lvCountry.setAdapter(adapter);
                    lvCountry.clearChoices();
                    lvCountry.setTextFilterEnabled(true);
                }
                sp.hideProgressDialog();
                if (sideSelector != null && lvCountry != null)
                    sideSelector.setListView(lvCountry);
                else {
                    sp.ShowDialog(CountryList.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {
                sp.ShowDialog(CountryList.this, "Server is down. Come back later!!");
                Log.i(TAG, "post submitted to API.");
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CountryList.this, MainDrawer.class);
                i.putExtra("nav_sub", "true");
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CountryList.this, SignUp.class);
                countryList.clear();
                alpha2.clear();
                list.clear();
                alpha.clear();
                intent.putExtra("Country", a_country);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItemSelected(pos);
                    }
                });
                countryList.clear();
                alpha2.clear();
                list.clear();
                alpha.clear();
                setResult(Activity.RESULT_OK, intent);
            }
        });

        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                adapter.setItemSelected(position);
                adapter.setCheckedItem(position);
                pos = position;
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> a_countryID = new ArrayList<String>();

                SharedPreference ss = new SharedPreference();

                if (adapter != null && adapter.getallitems() != null) {
                    HashMap<String, String> items = adapter.getallitems();
                    for (Map.Entry<String, String> entry : items.entrySet()) {
                        a_country.add(entry.getValue());
                    }
                    HashMap<String, String> items2 = adapter.getallitems();
                    for (Map.Entry<String, String> entry : items2.entrySet()) {
                        a_countryID.add(entry.getValue().split("~")[2]);
                    }
                }

                if (txtSelectedContract.getText().toString().equals("Trial Version")) {
                    if (a_country.size() > 1) {
                        if (check == null) {
                            ss.ShowDialog(CountryList.this, "During Free Trial Period you can choose only 1 country");
                        } else {
                            ss.ShowDialog(CountryList.this, "Choose one country");
                        }
                    } else {
                        if (check == null) {
                            intent = new Intent(CountryList.this, Category.class);
                            intent.putExtra("sub", s);
                            intent.putExtra("CountryAtContractor", a_countryID);
                            intent.putExtra("Country", a_country);
                            intent.putExtra("version", txtSelectedContract.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setItemSelected(pos);
                                }
                            });

                            countryList.clear();
                            alpha2.clear();
                            list.clear();
                            alpha.clear();
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent(CountryList.this, SignUp.class);
                            intent.putExtra("Country", a_country);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setItemSelected(pos);
                                }
                            });

                            countryList.clear();
                            alpha2.clear();
                            list.clear();
                            alpha.clear();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }
                } else {
                    intent = new Intent(CountryList.this, Category.class);
                    intent.putExtra("sub", s);
                    intent.putExtra("CountryAtContractor", a_countryID);
                    intent.putExtra("Country", a_country);
                    intent.putExtra("version", txtSelectedContract.getText().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setItemSelected(pos);
                        }
                    });

                    countryList.clear();
                    alpha2.clear();
                    list.clear();
                    alpha.clear();
                    startActivity(intent);
                    finish();
                }
            }
        });
        edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sideSelector.setVisibility(v.INVISIBLE);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null)
                    adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });

    }

    private void CallContractorSignUp() {
        final Dialog dialog = new Dialog(CountryList.this);
        dialog.setContentView(R.layout.select_contract);
        lltext.setVisibility(View.VISIBLE);
        final TextView txtTrial = (TextView) dialog.findViewById(R.id.txt_trial);
        TextView txtMonth = (TextView) dialog.findViewById(R.id.txt_month);
        TextView txtYear = (TextView) dialog.findViewById(R.id.txt_year);
        if (s != null) {
            txtTrial.setVisibility(View.GONE);
        }
        txtTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSelectedContract.setText("Trial Version");
                dialog.dismiss();
            }
        });
        txtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSelectedContract.setText("$15 / month");
                dialog.dismiss();
            }
        });
        txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSelectedContract.setText("$120 / year");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * row item
     */
    public interface Item {
        boolean isSection();

        String getTitle();

        String getFlag();

        String getCode();

        String getId();

        boolean getSelected();

        void setSelected(boolean isSelected);
    }

    /**
     * Section Item
     */
    public static class SectionItem implements Item {
        private final String title;
        private final String flag;
        private final String code;
        private final String id;

        private boolean isSelected;

        public SectionItem(String title, String flag, String code, String id, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.code = code;
            this.id = id;
            this.isSelected = isSelected;
        }

        public String getFlag() {
            return flag;
        }

        @Override
        public String getCode() {
            return code;
        }

        public String getId() {
            return id;
        }

        @Override
        public boolean getSelected() {
            return isSelected;
        }

        @Override
        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }
    }


    /**
     * Entry Item
     */
    public static class EntryItem implements Item {
        public final String title;
        private final String flag;
        private final String code;
        private final String id;
        private boolean isSelected;

        public EntryItem(String title, String flag, String code, String id, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.code = code;
            this.id = id;
            this.isSelected = isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getTitle() {
            return title;
        }

        public String getFlag() {
            return flag;
        }

        @Override
        public String getCode() {
            return code;
        }

        public String getId() {
            return id;
        }

        @Override
        public boolean getSelected() {
            return isSelected;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }


    /**
     * Adapter
     */

}
