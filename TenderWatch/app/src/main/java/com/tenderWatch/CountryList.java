package com.tenderWatch;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.LoginPost;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.Validation.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryList extends AppCompatActivity {

    private ListView lvCountry;
    private EditText edtSearch;
    private static final String TAG = CountryList.class.getSimpleName();
    private Api mAPIService;


    //ArrayList list;
    public static final String JSON_STRING = "{\"employee\":{\"name\":\"Sachin\",\"salary\":56000}}";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        mAPIService = ApiUtils.getAPIService();
        //Call<List<GetCountry>> call =  mAPIService.getCountryData();
        //
        mAPIService.getCountryData().enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                Log.i("array-------", response.body().get(0).getCountryName().toString());

            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {
                Log.i(TAG, "post submitted to API.");

            }
        });
        String jsonString = "{\"Array\":  [{\"name\":\"Anguilla\"},{\"name\":\"Albania\"},{\"name\":\"India\"}]}";
        ArrayList<Item> countryList = new ArrayList<CountryList.Item>();

        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray array = object.getJSONArray("Array");
            //JSONObject obj= new JSONObject();

            ArrayList<String> list = new ArrayList<String>();
            ArrayList<Item> alpha = new ArrayList<CountryList.Item>();

            for (int n = 0; n < array.length(); n++) {
                JSONObject obj = array.getJSONObject(n);
                String name = obj.getString("name");
                String value = String.valueOf(name.charAt(0));

                if (!list.contains(value)) {
                    list.add(value);
                    Log.i("array-------", String.valueOf(list));
                    //  alpha.add(value);
                    alpha.add(new SectionItem(value));
                    //set Country Header (Like:-A,B,C,...)
                    countryList.add(new SectionItem(value));
                    //set Country Name
                    countryList.add(new EntryItem(name));

                    //Log.i("array section-------",alpha.get(n).getTitle());

                } else {
                    //set Country Name
                    countryList.add(new EntryItem(name));
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // set adapter
        final CountryAdapter adapter = new CountryAdapter(this, countryList);
        lvCountry.setAdapter(adapter);
        lvCountry.setTextFilterEnabled(true);

        // filter on text change
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
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item {
        private final String title;

        public SectionItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }


        @Override
        public boolean isSection() {
            return true;
        }
    }

    public class SectionChar implements Item {
        private final char alphabet;

        public SectionChar(char title) {
            this.alphabet = title;
        }

        public char getAlphabet() {
            return alphabet;
        }

        @Override
        public boolean isSection() {
            return true;
        }

        @Override
        public String getTitle() {
            return null;
        }


    }

    /**
     * Entry Item
     */
    public class EntryItem implements Item {
        public final String title;

        public EntryItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }

    /**
     * Adapter
     */
    public class CountryAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Item> item;
        private ArrayList<Item> originalItem;


        public CountryAdapter(Context context, ArrayList<Item> item) {
            this.context = context;
            this.item = item;
            //this.originalItem = item;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()) {
                // if section header
                convertView = inflater.inflate(R.layout.layout_section, parent, false);
                TextView tvSectionTitle = convertView.findViewById(R.id.tvSectionTitle);
                tvSectionTitle.setText(item.get(position).getTitle());
            } else {
                // if item
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = convertView.findViewById(R.id.tvItemTitle);
                tvItemTitle.setText(item.get(position).getTitle());
            }

            return convertView;
        }

        /**
         * Filter
         */
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    item = (ArrayList<Item>) results.values;
                    notifyDataSetChanged();
                }

                @SuppressWarnings("null")
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    ArrayList<Item> filteredArrayList = new ArrayList<Item>();


                    if (originalItem == null || originalItem.size() == 0) {
                        originalItem = new ArrayList<Item>(item);
                    }

                    /*
                     * if constraint is null then return original value
                     * else return filtered value
                     */
                    if (constraint == null && constraint.length() == 0) {
                        results.count = originalItem.size();
                        results.values = originalItem;
                    } else {
                        constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
                        for (int i = 0; i < originalItem.size(); i++) {
                            String title = originalItem.get(i).getTitle().toLowerCase(Locale.ENGLISH);
                            if (title.startsWith(constraint.toString())) {
                                filteredArrayList.add(originalItem.get(i));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }
            };

            return filter;
        }
    }
}
