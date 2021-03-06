package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.tenderWatch.Models.User;
import com.tenderWatch.R;
import com.tenderWatch.SharedPreference.SharedPreference;

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
    SharedPreference sp = new SharedPreference();

    public ContractorTenderListAdapter(Context context, ArrayList<AllContractorTender> tenderList) {
        this.context = context;
        this.tenderList = tenderList;
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
        final CircleImageView tender_image = (CircleImageView) convertView.findViewById(R.id.tender_image2);
        TextView txtTenderName = (TextView) convertView.findViewById(R.id.tender_name);
        TextView txtTenderTitle = (TextView) convertView.findViewById(R.id.tender_title);
        TextView txtTenderExpDate = (TextView) convertView.findViewById(R.id.tender_expdate);
        LinearLayout stampRemove = (LinearLayout) convertView.findViewById(R.id.stamp_remove);
        CircleImageView imgTrue = (CircleImageView) convertView.findViewById(R.id.tender_image3);

        if (!tenderList.get(position).getTenderPhoto().equals("no image")) {
            Picasso.with(context).load(tenderList.get(position).getTenderPhoto()).into(tender_image);
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date startDateValue = null, endDateValue = null;
        try {
            startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
            //  startDateValue = new SimpleDateFormat("yyyy-MM-dd").parse(tenderList.get(position).getCreatedAt().split("T")[0]);
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

        if (days == 0) {
            stampRemove.setVisibility(View.VISIBLE);
            txtTenderExpDate.setText("Expired");
        } else {
            txtTenderExpDate.setText(days + " days");
        }
        Log.d("days", "" + days);
        txtTenderName.setText(tenderList.get(position).getTenderUploader().getEmail());
        txtTenderTitle.setText(tenderList.get(position).getEmail());
        User user = (User) sp.getPreferencesObject(context);
        if (tenderList.get(position).getInterested().size() > 0) {
            for (int i = 0; i < tenderList.get(position).getInterested().size(); i++) {
                String id = user.getId();
                if (tenderList.get(position).getInterested().contains(id)) {
                    imgTrue.setVisibility(View.VISIBLE);
                }
            }
        }
        if (tenderList.get(position).getAmendRead() != null) {

            if (tenderList.get(position).getAmendRead().size() > 0) {
                for (int i = 0; i < tenderList.get(position).getAmendRead().size(); i++) {
                    String id = user.getId();
                    if (!tenderList.get(position).getAmendRead().contains(id)) {
                        tender_image.setBorderColor(Color.RED);
                        tender_image.setBorderWidth(2);

                    }
                }
            }

            if (tenderList.get(position).getAmendRead().size() == 0) {
                tender_image.setBorderColor(Color.RED);
                tender_image.setBorderWidth(2);

            }
        }
        return convertView;
    }

}
