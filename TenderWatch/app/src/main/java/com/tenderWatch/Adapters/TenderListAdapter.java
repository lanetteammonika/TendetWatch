package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
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
        txtTenderName.setText(tenderList.get(position).getTenderName().toString());
        txtTenderTitle.setText(tenderList.get(position).getTenderName().toString());

        txtTenderExpDate.setText(tenderList.get(position).getExpiryDate().toString());

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


