package com.tenderWatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tenderWatch.Adapters.ArrayAdapter;
import com.tenderWatch.Models.CreateUser;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category extends AppCompatActivity {
    private ListView lvCountry;
    private static final String TAG = CountryList.class.getSimpleName();
    private Api mAPIService;
    private SideSelector sideSelector = null;
    private List Data;
    private static ArrayList<String> alpha = new ArrayList<String>();
    private static ArrayList<String> alpha2 = new ArrayList<String>();
    public static final ArrayList<String> list = new ArrayList<String>();
    public static final ArrayList<String> countryId = new ArrayList<String>();

    String alphabetS = "";
    private static ArrayList<Item> countryList = new ArrayList<Item>();
    ArrayAdapter bAdapter;
    public static char[] alphabetlist = new char[27];
    ArrayList<String> empNo, countryListName;
    CreateUser user = new CreateUser();
    Button btnCategory;
    private static int p = 0, k = 0;
    public static HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private static ArrayList<String> a_category = new ArrayList<String>();
    Intent intent;
    SharedPreference sp = new SharedPreference();
    LinearLayout back;
    TextView txtContract;
    String contract, s;
    private int amount = 0;
    private int subscriptionType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getDataFromIntent();

        lvCountry = (ListView) findViewById(R.id.lvCategory);
        btnCategory = (Button) findViewById(R.id.btn_CategoryNext);
        sideSelector = (SideSelector) findViewById(R.id.category_side_selector);
        back = (LinearLayout) findViewById(R.id.category_toolbar);
        txtContract = (TextView) findViewById(R.id.txt_selectedContractcategory);
        mAPIService = ApiUtils.getAPIService();
        lvCountry.setDivider(null);
        lvCountry.clearChoices();

        txtContract.setText(contract);
        user.setSelections(empNo.size());
        sp.showProgressDialog(Category.this);

        alpha = new ArrayList<>();
        alpha2 = new ArrayList<>();
        countryList = new ArrayList<>();

        callApiForGetCategoryData();

        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bAdapter.setItemSelected(position);
                bAdapter.setCheckedItem(position);
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SharedPreference ss = new SharedPreference();
                HashMap<String, String> items = bAdapter.getallitems();

                for (Map.Entry<String, String> entry : items.entrySet()) {
                    populateMap(map, entry.getValue().split("~")[1], entry.getValue().split("~")[2]);
                }

                if (txtContract.getText().toString().equals("Trial Version")) {
                    if (items.size() > 1) {
                        ss.ShowDialog(Category.this, "During Free Trial Period you can choose only 1 category");
                    } else {
                        if (s != null) {
                            intent = new Intent(Category.this, PaymentSelection.class);
                            intent.putExtra("amount", amount);
                            intent.putExtra("subscriptionType", subscriptionType);
                            intent.putExtra("selections", map);
                        } else {
                            intent = new Intent(Category.this, Agreement.class);
                        }
                        user.setSubscribe(map);
                        startActivity(intent);
                        map.clear();
                    }
                } else {
                    if (map == null || map.size() == 0) {
                        ss.ShowDialog(Category.this, "Please choose one category");
                    } else {
                        user.setSubscribe(map);
                        if (s != null) {
                            intent = new Intent(Category.this, PaymentSelection.class);
                            intent.putExtra("amount", amount);
                            intent.putExtra("subscriptionType", subscriptionType);
                            intent.putExtra("selections", map);
                        } else {
                            intent = new Intent(Category.this, Agreement.class);
                        }
                        startActivity(intent);
                        map.clear();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Category.this, CountryList.class);
                intent.putExtra("sub", "1234");
                startActivity(intent);
                finish();
            }
        });
    }

    public void getDataFromIntent() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getStringArrayList("CountryAtContractor") != null) {
                empNo = getIntent().getExtras().getStringArrayList("CountryAtContractor");
            }
            if (getIntent().getExtras().getString("sub") != null) {
                s = getIntent().getExtras().getString("sub");
            }
            if (getIntent().getExtras().getStringArrayList("Country") != null) {
                countryListName = getIntent().getExtras().getStringArrayList("Country");
            }
            if (getIntent().getExtras().getString("version") != null) {
                contract = getIntent().getExtras().getString("version");
            }
            amount = getIntent().getExtras().getInt("amount", 0);
            subscriptionType = getIntent().getExtras().getInt("subscriptionType", 1);
        }
    }

    public void callApiForGetCategoryData() {
        mAPIService.getCategoryData().enqueue(new Callback<ArrayList<GetCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCategory>> call, Response<ArrayList<GetCategory>> response) {
                if (response.isSuccessful()) {
                    Log.i("array-------", response.body().get(0).getCategoryName());
                    Data = response.body();
                    for (int i = 0; i < Data.size(); i++) {
                        String name = response.body().get(i).getCategoryName();
                        String flag = response.body().get(i).getImgString();
                        String id = response.body().get(i).getId();
                        alpha.add(name + '~' + id + '~' + flag);
                    }

                    for (int y = 0; y < empNo.size(); y++) {
                        String categoryName = countryListName.get(y).split("~")[0];
                        String categoryId = countryListName.get(y).split("~")[2];
                        String value = String.valueOf(categoryName.charAt(0));

                        countryList.add(new SectionItem(categoryName, "", categoryId, categoryId, false));

                        for (int i = 0; i < Data.size(); i++) {
                            String name = alpha.get(i).split("~")[0];
                            String id = alpha.get(i).split("~")[1];
                            String flag = alpha.get(i).split("~")[2];
                            if (!list.contains(value)) {
                                list.add(value);
                                alphabetS.concat(value);//alphabetlist.append(value);
                                Log.i("array-------", String.valueOf(list));
                                alpha2.add(value);
                                alpha2.add(name);
                                //set Country Header (Like:-A,B,C,...)
                                //set Country Name
                                countryList.add(new EntryItem(name, flag, id, categoryId, false));
                            } else {
                                alpha2.add(name);
                                //set Country Name
                                countryList.add(new EntryItem(name, flag, id, categoryId, false));
                            }
                        }
                    }

                    String str = list.toString().replaceAll(",", "");
                    char[] chars = str.toCharArray();
                    Log.i(TAG, "post submitted to API." + chars);
                    char[] al = new char[27];
                    for (int j = 1, i = 0; j < chars.length; j = j + 2, i++) {
                        al[i] = chars[j];
                        Log.i(TAG, "post." + chars[j]);
                    }

                    Log.i(TAG, "post submitted to API." + al);

                    SideSelector ss = new SideSelector(getApplicationContext());
                    ss.setAlphabet(al);
                    alphabetlist = str.substring(1, str.length() - 1).replaceAll(" ", "").toCharArray();
                    bAdapter = new ArrayAdapter(Category.this, R.id.lvCategory, countryList, alpha2, list, chars);
                    // set adapter
                    // adapter
                    sp.hideProgressDialog();
                    lvCountry.setAdapter(bAdapter);
                    lvCountry.setTextFilterEnabled(true);
                    if (sideSelector != null)
                        sideSelector.setListView(lvCountry);
                } else {
                    sp.ShowDialog(Category.this, response.errorBody().source().toString().split("\"")[3]);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCategory>> call, Throwable t) {
                sp.ShowDialog(Category.this, "Server is down. Come back later!!");
                Log.i(TAG, "post submitted to API.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

        }
    }

    void populateMap(HashMap<String, ArrayList<String>> map, String value, String key) {
        ArrayList<String> myList;
        if (!map.containsKey(key)) {
            myList = new ArrayList<String>();
            myList.add(value);
            map.put(key, myList);
        } else {
            myList = map.get(key);
            myList.add(value);
        }
    }

    public interface Item {
        boolean isSection();

        String getTitle();

        String getFlag();

        String getId();

        String getcountryId();

        boolean getSelected();

        void setSelected(boolean isSelected);
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item {
        private final String title;
        private final String flag;
        private final String id;
        private final String countryId;

        private boolean isSelected;

        public SectionItem(String title, String flag, String id, String countryId, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.id = id;
            this.countryId = countryId;
            this.isSelected = isSelected;
        }

        public String getFlag() {
            return flag;
        }

        public String getId() {
            return id;
        }

        public String getcountryId() {
            return countryId;
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
        private final String id;
        private final String countryId;
        private boolean isSelected;

        public EntryItem(String title, String flag, String id, String countryId, boolean isSelected) {
            this.title = title;
            this.flag = flag;
            this.id = id;
            this.countryId = countryId;
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

        public String getId() {
            return id;
        }

        public String getcountryId() {
            return countryId;
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
