package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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
    String alphabetS="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvCountry = (ListView) findViewById(R.id.lvCountry);

        sideSelector = (SideSelector) findViewById(R.id.side_selector);
        mAPIService = ApiUtils.getAPIService();
        lvCountry.setDivider(null);
        btn_next = (Button) findViewById(R.id.btn_CountryNext);


        mAPIService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                Log.i("array-------", response.body().get(0).getCountryName().toString());

                Data = response.body();


                for (int i = 0; i < Data.size(); i++) {
                    //JSONObject obj = array.getJSONObject(n);
                    String name = response.body().get(i).getCountryName().toString();
                    String flag = response.body().get(i).getImageString().toString();
                    String value = String.valueOf(name.charAt(0));
                    alpha.add(name + '~' + flag);


                    // }
                }

                Collections.sort(alpha);
                for (int i = 0; i < Data.size(); i++) {
                    String name = alpha.get(i).split("~")[0];
                    String flag = alpha.get(i).split("~")[1];
                    String value = String.valueOf(name.charAt(0));
                    if (!list.contains(value)) {
                        list.add(value);
                        alphabetS.concat(value);//alphabetlist.append(value);


                        Log.i("array-------", String.valueOf(list));
                        alpha2.add(value);
                        alpha2.add(name);

                        //set Country Header (Like:-A,B,C,...)
                        countryList.add(new SectionItem(value, "",false));
                        //set Country Name
                        countryList.add(new EntryItem(name, flag,false));

                        //Log.i("array section-------",alpha.get(n).getTitle());

                    } else {
                        alpha2.add(name);

                        //set Country Name
                        countryList.add(new EntryItem(name, flag,false));
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


        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                adapter.setItemSelected(position);

//                    Log.i(TAG, "Clicked"+position);
//                    LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    view = inflater.inflate(R.layout.layout_item, parent, false);
//                    ImageView img = view.findViewById(R.id.imgtrue);
//                    img.setVisibility(view.VISIBLE);
//                    RelativeLayout rl1=view.findViewById(R.id.itemlayout);
//                    rl1.setBackgroundColor(Color.YELLOW);
//                adapter.notifyDataSetChanged();

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

    /**
     * row item
     */
    public interface Item {
        boolean isSection();

        String getTitle();

        String getFlag();

        boolean getSelected();

        void setSelected(boolean isSelected);
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item {
        private final String title;
        private final String flag;
        private boolean isSelected;

        public SectionItem(String title, String flag, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.isSelected = isSelected;
        }

        public String getFlag() {
            return flag;
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
        private boolean isSelected;

        public EntryItem(String title, String flag, boolean isSelected) {
            this.title = title;
            this.flag = flag;
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
