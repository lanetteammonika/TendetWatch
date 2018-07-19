package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenderWatch.CountryList;
import com.tenderWatch.R;

import java.util.ArrayList;
import java.util.Locale;

public class CustomList extends BaseAdapter{
    private Context context;
    private ArrayList<String> countryNameList;
    private ArrayList<String> originalItem;

    public CustomList(Context context,ArrayList<String> countryNameList){
        this.context=context;
        this.countryNameList=countryNameList;
    }

    @Override
    public int getCount() {
        return countryNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.country_name, parent, false);

        TextView txtCountryName=(TextView) convertView.findViewById(R.id.name);
        ImageView flag_img=(ImageView) convertView.findViewById(R.id.flag_img);

        txtCountryName.setText(countryNameList.get(position).split("~")[0]);

        Bitmap flag1 = StringToBitMap(countryNameList.get(position).split("~")[1]);
        flag_img.setImageBitmap(flag1);

        return convertView;
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
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                countryNameList = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

            @SuppressWarnings("null")
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<String> filteredArrayList = new ArrayList<String>();


                if (originalItem == null || originalItem.size() == 0) {
                    originalItem = new ArrayList<String>(countryNameList);
                }

                    /*
                     * if constraint is null then return original value
                     * else return filtered value
                     */
                if (constraint == null || constraint.length() == 0) {
                    results.count = originalItem.size();
                    results.values = originalItem;
                } else {
                    constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
                    for (int i = 0; i < originalItem.size(); i++) {
                        String title = originalItem.get(i).split("~")[0].toLowerCase(Locale.ENGLISH);
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

