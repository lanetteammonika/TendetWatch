package com.tenderWatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tenderWatch.Adapters.ArrayAdapter;
import com.tenderWatch.Adapters.IndexingArrayAdapter;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category extends AppCompatActivity {
    private ListView lvCountry;
    private static final String TAG = CountryList.class.getSimpleName();
    private Api mAPIService;
    private SideSelector sideSelector = null;
    private List Data;
    private static final ArrayList<String> alpha = new ArrayList<String>();
    private static final ArrayList<String> alpha2 = new ArrayList<String>();
    public static final ArrayList<String> list = new ArrayList<String>();
    String alphabetS="";
    private static final ArrayList<Item> countryList = new ArrayList<Item>();
    ArrayAdapter bAdapter;
    public static char[] alphabetlist = new char[27];
    ArrayList<String> empNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        lvCountry = (ListView) findViewById(R.id.lvCategory);


        sideSelector = (SideSelector) findViewById(R.id.category_side_selector);
        mAPIService = ApiUtils.getAPIService();
        lvCountry.setDivider(null);
        Intent show = getIntent();

        empNo = show.getStringArrayListExtra("CountryAtContractor");
        mAPIService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                Log.i("array-------", response.body().get(0).getCategoryName().toString());

                Data = response.body();


                for (int i = 0; i < Data.size(); i++) {
                    //JSONObject obj = array.getJSONObject(n);
                    String name = response.body().get(i).getCategoryName().toString();
                    String flag = response.body().get(i).getImgString().toString();

                    alpha.add(name +'~'+flag);


                    // }
                }

                for(int y=0;y<empNo.size();y++){
                    String categoryName=empNo.get(y).toString().split("~")[0];
                    String value = String.valueOf(categoryName.charAt(0));
                    countryList.add(new SectionItem(categoryName, "",false));

                    for (int i = 0; i < Data.size(); i++) {
                        String name = alpha.get(i).split("~")[0];
                        //   String countryCode = alpha.get(i).split("~")[1].split("`")[0];

                        String   flag=alpha.get(i).split("~")[1];

                        //String value = String.valueOf(name.charAt(0));
                        if (!list.contains(value)) {
                            list.add(value);
                            alphabetS.concat(value);//alphabetlist.append(value);


                            Log.i("array-------", String.valueOf(list));
                            alpha2.add(categoryName);
                            alpha2.add(name);

                            //set Country Header (Like:-A,B,C,...)
                            //set Country Name
                            countryList.add(new EntryItem(name, flag,false));

                            //Log.i("array section-------",alpha.get(n).getTitle());

                        } else {
                            alpha2.add(name);

                            //set Country Name
                            countryList.add(new EntryItem(name, flag,false));
                        }
                    }
                }




               // Collections.sort(alpha);
//                for (int i = 0; i < Data.size(); i++) {
//                    String name = alpha.get(i).split("~")[0];
//                 //   String countryCode = alpha.get(i).split("~")[1].split("`")[0];
//
//                    String   flag=alpha.get(i).split("~")[1];
//
//                    String value = String.valueOf(name.charAt(0));
//                    if (!list.contains(value)) {
//                        list.add(value);
//                        alphabetS.concat(value);//alphabetlist.append(value);
//
//
//                        Log.i("array-------", String.valueOf(list));
//                        alpha2.add(value);
//                        alpha2.add(name);
//
//                        //set Country Header (Like:-A,B,C,...)
//                        countryList.add(new SectionItem(value, "",false));
//                        //set Country Name
//                        countryList.add(new EntryItem(name, flag,false));
//
//                        //Log.i("array section-------",alpha.get(n).getTitle());
//
//                    } else {
//                        alpha2.add(name);
//
//                        //set Country Name
//                        countryList.add(new EntryItem(name, flag,false));
//                    }
//                }

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
                bAdapter =new ArrayAdapter(Category.this,R.id.lvCategory,countryList, alpha2, list,chars);
                        // set adapter
               // adapter

                lvCountry.setAdapter(bAdapter);
                lvCountry.setTextFilterEnabled(true);
                if (sideSelector != null)
                    sideSelector.setListView(lvCountry);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {
                Log.i(TAG, "post submitted to API.");

            }
        });
        //btn_next = (Button) findViewById(R.id.btn_CountryNext);
    }
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

}
