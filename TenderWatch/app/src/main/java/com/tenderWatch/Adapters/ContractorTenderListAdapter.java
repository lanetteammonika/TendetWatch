package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.Models.AllContractorTender;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lcom47 on 4/1/18.
 */

public class ContractorTenderListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AllContractorTender> tenderList;


    public ContractorTenderListAdapter(Context context, ArrayList<AllContractorTender> tenderList){
        this.context=context;
        this.tenderList=tenderList;
    }

    @Override
    public int getCount() {
        return tenderList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.tender_list, parent, false);
        final CircleImageView tender_image=(CircleImageView) convertView.findViewById(R.id.tender_image2) ;
        TextView txtTenderName=(TextView) convertView.findViewById(R.id.tender_name);
        TextView txtTenderTitle=(TextView) convertView.findViewById(R.id.tender_title);
        TextView txtTenderExpDate=(TextView) convertView.findViewById(R.id.tender_expdate);
        LinearLayout stampRemove=(LinearLayout) convertView.findViewById(R.id.stamp_remove);
        if(!tenderList.get(position).getTenderUploader().getProfilePhoto().toString().equals("")) {
            Picasso.with(context).load(tenderList.get(position).getTenderUploader().getProfilePhoto().toString()).into(tender_image);

            //  Log.i(TAG, profilePicUrl);
            //}
        }

        Date startDateValue = null,endDateValue = null;
        try {
           // startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
              startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(tenderList.get(position).getCreatedAt().split("T")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(tenderList.get(position).getExpiryDate().split("T")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Date endDateValue = new Date(allTender.get(position).getExpiryDate().split("T")[0]);
        long diff = endDateValue.getTime() - startDateValue.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = (hours / 24) + 1;

        if(days==0 ){
            stampRemove.setVisibility(View.VISIBLE);
            txtTenderExpDate.setText("Expired");
        }else{
            txtTenderExpDate.setText(days+" days");
        }
        Log.d("days", "" + days);
        txtTenderName.setText(tenderList.get(position).getTenderUploader().getEmail().toString());
        txtTenderTitle.setText(tenderList.get(position).getEmail().toString());

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


}
