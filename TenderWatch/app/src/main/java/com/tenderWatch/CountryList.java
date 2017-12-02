package com.tenderWatch;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

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
    private RelativeLayout rlCountry;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        sideSelector = (SideSelector) findViewById(R.id.side_selector);
        rlCountry = (RelativeLayout) findViewById(R.id.rlCountry);
        mAPIService = ApiUtils.getAPIService();

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
                        //alphabetlist.append(value);


                        Log.i("array-------", String.valueOf(list));
                        alpha2.add(value);
                        alpha2.add(name);

                        //set Country Header (Like:-A,B,C,...)
                        countryList.add(new SectionItem(value, ""));
                        //set Country Name
                        countryList.add(new EntryItem(name, flag));

                        //Log.i("array section-------",alpha.get(n).getTitle());

                    } else {
                        alpha2.add(name);

                        //set Country Name
                        countryList.add(new EntryItem(name, flag));
                    }
                }
                String str = list.toString().replaceAll(",", "");
                alphabetlist = str.substring(1, str.length() - 1).replaceAll(" ", "").toCharArray();
                // set adapter

            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {
                Log.i(TAG, "post submitted to API.");

            }
        });
        final IndexingArrayAdapter adapter = new IndexingArrayAdapter(getApplicationContext(), R.id.lvCountry, countryList);
        lvCountry.setAdapter(adapter);
        lvCountry.setTextFilterEnabled(true);
        if (sideSelector != null)
            sideSelector.setListView(lvCountry);

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
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item {
        private final String title;
        private final String flag;


        public SectionItem(String title, String flag) {
            this.title = title;
            this.flag = flag;
        }

        public String getFlag() {
            return flag;
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


        public EntryItem(String title, String flag) {
            this.title = title;
            this.flag = flag;
        }

        public String getTitle() {
            return title;
        }

        public String getFlag() {
            return flag;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }


    public class IndexingArrayAdapter extends BaseAdapter implements SectionIndexer {

        private Context context;
        private ArrayList<Item> item;
        private ArrayList<Item> originalItem;
        private int textViewResourceId;

        public IndexingArrayAdapter(Context context, int textViewResourceId, ArrayList<Item> item) {
            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.item = item;
        }


        public Object[] getSections() {
            String[] chars = new String[SideSelector.ALPHABET2.length];
            for (int i = 0; i < SideSelector.ALPHABET2.length; i++) {
                chars[i] = String.valueOf(SideSelector.ALPHABET2[i]);
            }

            return chars;
        }


        @Override
        public int getPositionForSection(int i) {
            //String indexer= String.valueOf(SideSelector.ALPHABET[i]);
            String indexer = String.valueOf(list.get(i));
            Log.d(TAG, "getPositionForSection " + i);

            int retval = alpha2.indexOf(indexer);
            //int retval=alpha.indexOf("G");
            // int g = (int) (getCount() * ((float) i / (float) getSections().length));
            return retval;
            //return 0;
        }


        @Override
        public int getSectionForPosition(int i) {
            return 0;
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
            //scroollpos(position);
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
                ImageView flag = convertView.findViewById(R.id.img);
                String t = item.get(position).getFlag();
                tvItemTitle.setText(item.get(position).getTitle());
                Bitmap flag1 = StringToBitMap(item.get(position).getFlag());
                flag.setImageBitmap(flag1);
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

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Adapter
     */

}
