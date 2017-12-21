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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;
import com.tenderWatch.SignUpSelection;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lcom48 on 18/12/17.
 */

public class TenderListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Tender> tenderList;


    public TenderListAdapter(Context context, ArrayList<Tender> tenderList){
        this.context=context;
        this.tenderList=tenderList;
    }
    public void updateReceiptsList(ArrayList<Tender> tenderList) {
        this.tenderList.clear();
        this.tenderList.addAll(tenderList);
        this.notifyDataSetChanged();
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
        if(!tenderList.get(position).getTenderPhoto().toString().equals("")) {
            Picasso.with(context)
                    .load(tenderList.get(position).getTenderPhoto().toString())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            Log.v("Main", String.valueOf(bitmap));
                            Bitmap main = bitmap;
                            tender_image.setImageBitmap(main);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.v("Main", "errrorrrr");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
          //  Log.i(TAG, profilePicUrl);
        //}
        }
        Date startDateValue = null,endDateValue = null;
        try {
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
        Log.d("days", "" + days);
        txtTenderName.setText(tenderList.get(position).getTenderName().toString());
        txtTenderTitle.setText(tenderList.get(position).getTenderName().toString());
        txtTenderExpDate.setText(days+" days");
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


