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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tenderWatch.Adapters.IndexingArrayAdapter;
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
    IndexingArrayAdapter adapter,bAdapter;
    Button btn_next;
    String alphabetS="";
    LinearLayout lltext,back;
    TextView txtSelectedContract;
    Intent intent;
    String check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
         lltext = (LinearLayout) findViewById(R.id.lltext);
         back=(LinearLayout) findViewById(R.id.country_toolbar);

        sideSelector = (SideSelector) findViewById(R.id.side_selector);
        mAPIService = ApiUtils.getAPIService();
        lvCountry.setDivider(null);
        btn_next = (Button) findViewById(R.id.btn_CountryNext);
        txtSelectedContract=(TextView) findViewById(R.id.txt_selectedContract);
        lvCountry.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Intent show = getIntent();
        check=show.getStringExtra("check");
        if(check == null) {
          CallContractorSignUp();
        }
        mAPIService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                Log.i("array-------", response.body().get(0).getCountryName().toString());

                Data = response.body();


                for (int i = 0; i < Data.size(); i++) {
                    //JSONObject obj = array.getJSONObject(n);
                    String name = response.body().get(i).getCountryName().toString();
                    String flag = response.body().get(i).getImageString().toString();
                    String countryCode=response.body().get(i).getCountryCode().toString();
                    String value = String.valueOf(name.charAt(0));
                    alpha.add(name + '~' + countryCode+ '`' +flag);


                    // }
                }

                Collections.sort(alpha);
                for (int i = 0; i < Data.size(); i++) {
                    String name = alpha.get(i).split("~")[0];
                    String countryCode = alpha.get(i).split("~")[1].split("`")[0];

                     String   flag=alpha.get(i).split("`")[1];

                    String value = String.valueOf(name.charAt(0));
                    if (!list.contains(value)) {
                        list.add(value);
                        alphabetS.concat(value);//alphabetlist.append(value);


                        Log.i("array-------", String.valueOf(list));
                        alpha2.add(value);
                        alpha2.add(name);

                        //set Country Header (Like:-A,B,C,...)
                        countryList.add(new SectionItem(value, "","",false));
                        //set Country Name
                        countryList.add(new EntryItem(name, flag,countryCode,false));

                        //Log.i("array section-------",alpha.get(n).getTitle());

                    } else {
                        alpha2.add(name);

                        //set Country Name
                        countryList.add(new EntryItem(name, flag,countryCode,false));
                    }
                }

                String str = list.toString().replaceAll(",", "");
                char[] chars = str.toCharArray();
                Log.i(TAG, "post submitted to API."+chars);
                char[] al = new char[27];
                for(int j=1, i=0;j<chars.length;j=j+2, i++){
                    al[i]=chars[j];
                    Log.i(TAG, "post."+chars[j]);
                }

                Log.i(TAG, "post submitted to API."+al);

                SideSelector ss=new SideSelector(getApplicationContext());
                ss.setAlphabet(al);
                alphabetlist = str.substring(1, str.length() - 1).replaceAll(" ", "").toCharArray();
                // set adapter
                adapter = new IndexingArrayAdapter(getApplicationContext(), R.id.lvCountry, countryList, alpha2, list,chars);

                lvCountry.setAdapter(adapter);
                lvCountry.setTextFilterEnabled(true);
                if (sideSelector != null)
                    sideSelector.setListView(lvCountry);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {
                Log.i(TAG, "post submitted to API.");

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CountryList.this, SignUp.class);

                startActivity(intent);
            }
        });

        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView tvItemTitle=(TextView) view.findViewById(R.id.tvItemTitle);
                String value= (String) lvCountry.getItemAtPosition(position).toString();
                String Country=tvItemTitle.getText().toString();
                adapter.setItemSelected(position);
               adapter.setCheckedItem(position);

            }
        });
        //IndexingArrayAdapter bAdapter = null;


btn_next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ArrayList<String> a_country = new ArrayList<String>();
        SharedPreference ss =new SharedPreference();

        HashMap<String, String> items=adapter.getallitems();
        for (Map.Entry<String, String> entry : items.entrySet()) {
            a_country.add(entry.getValue());
        }
        if(txtSelectedContract.getText().toString().equals("$0 / year")){
//            if(a_country.size()>1){
//                if(check == null) {
//                    ss.ShowDialog(CountryList.this, "During Free Trial Period you can choose only 1 country");
//                }else{
//                    ss.ShowDialog(CountryList.this, "Choose one country");
//                }
//            }
//            else{
                if(check == null) {
                    intent = new Intent(CountryList.this, Category.class);
                    intent.putExtra("CountryAtContractor", a_country);
                    startActivity(intent);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    intent = new Intent(CountryList.this, SignUp.class);
                    intent.putExtra("Country", a_country);

//                finish();;
//                startActivity(intent);
                    setResult(Activity.RESULT_OK, intent);
//
//            intent = new Intent(CountryList.this, SignUp.class);
//            intent.putExtra("Country",a_country);
//            startActivity(intent);
                    finish();
                }

            }
        //}
        else{
//            intent = new Intent(CountryList.this, SignUp.class);
//            intent.putExtra("Country",a_country);
//            startActivity(intent);
//            finish();
            intent = new Intent(CountryList.this, Category.class);
            intent.putExtra("Country",a_country);
//                finish();;
//                startActivity(intent);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
//        for(int y=0;y<items.size();y++){
//            a_country.add(items.get(y).toString());
//        }
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
        txtTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSelectedContract.setText("$0 / year");
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

    public boolean isEnabled(int position) {
        if(lvCountry.getCount()>0){
            return false;
        }
        return true;
    }

    /**
     * row item
     */
    public interface Item {
        boolean isSection();

        String getTitle();

        String getFlag();

        String getCode();

        boolean getSelected();

        void setSelected(boolean isSelected);
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item {
        private final String title;
        private final String flag;
        private final String code;

        private boolean isSelected;

        public SectionItem(String title, String flag, String code, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.code = code;
            this.isSelected = isSelected;
        }

        public String getFlag() {
            return flag;
        }

        @Override
        public String getCode() {
            return code;
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
    public class EntryItem implements Item {
        public final String title;
        private final String flag;
        private final String code;
        private boolean isSelected;

        public EntryItem(String title, String flag, String code, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.code = code;
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
