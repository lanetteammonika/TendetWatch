package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tenderWatch.Models.ResponseNotifications;
import com.tenderWatch.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lcom47 on 1/1/18.
 */

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ResponseNotifications> countryNameList;
    private String test;
    private ArrayList<String> selectedItem=new ArrayList<String>();;

    public NotificationAdapter(Context context,ArrayList<ResponseNotifications> countryNameList,String test){
        this.context=context;
        this.countryNameList=countryNameList;
        this.test=test;
    }

    public void setCheckedItem(String i) {
        if (!selectedItem.contains(i)) {
            selectedItem.add(i);
        }else{
            selectedItem.remove(i);
        }
    }

    public ArrayList<String> getCheckedItem(){
        return selectedItem;
    }

    @Override
    public int getCount() {
        return countryNameList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.layout_notification, parent, false);

        TextView txtCountryName=(TextView) convertView.findViewById(R.id.notification);
        TextView txtTime=(TextView) convertView.findViewById(R.id.time);

        CircleImageView flag_img=(CircleImageView) convertView.findViewById(R.id.not_tender_image2);
        final ImageView tick=(ImageView) convertView.findViewById(R.id.round_checked);
        final ImageView tick2=(ImageView) convertView.findViewById(R.id.round);

        String s1=countryNameList.get(position).getMessage().split("\"")[0];
        String s2=countryNameList.get(position).getMessage().split("\"")[1];
        int y=s2.length();
        int x=s1.length();
        int p=x+y+1;
        String s3=countryNameList.get(position).getMessage().split("\"")[2];
        int z=s3.length();
        Spannable spanText = new SpannableString(countryNameList.get(position).getMessage());
       // spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, changeString.length(), 0);
        spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorTender)), x+1,p, 0);
        txtCountryName.setText(spanText);
        txtTime.setText(countryNameList.get(position).getCreatedAt().split("T")[0]);

       if(!countryNameList.get(position).getSender().getProfilePhoto().equals("no image"))
        Picasso.with(context).load(countryNameList.get(position).getSender().getProfilePhoto()).into(flag_img);


        if(test.equals("true")){
            tick2.setVisibility(View.VISIBLE);
        }
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tick2.setVisibility(View.VISIBLE);
                tick.setVisibility(View.GONE);
               setCheckedItem(countryNameList.get(position).getId());
            }
        });
        tick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tick.setVisibility(View.VISIBLE);
                tick2.setVisibility(View.GONE);
                setCheckedItem(countryNameList.get(position).getId());
            }
        });

        return convertView;
    }
}
