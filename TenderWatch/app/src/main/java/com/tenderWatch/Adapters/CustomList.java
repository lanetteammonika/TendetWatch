package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenderWatch.R;

import java.util.ArrayList;

public class CustomList extends BaseAdapter{
    private Context context;
    private ArrayList<String> countryNameList;
    private ArrayList<Bitmap> flagList;

    public CustomList(Context context,ArrayList countryNameList,ArrayList<Bitmap> flagList){
        this.context=context;
        this.countryNameList=countryNameList;
        this.flagList=flagList;
    }

    @Override
    public int getCount() {
        return 0;
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
        txtCountryName.setText(countryNameList.get(position).toString());
        Bitmap flag1=flagList.get(position);
        flag_img.setImageBitmap(flag1);
        return null;
    }
}

