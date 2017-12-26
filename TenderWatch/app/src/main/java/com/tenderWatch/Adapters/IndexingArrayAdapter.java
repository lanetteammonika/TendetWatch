package com.tenderWatch.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tenderWatch.Category;
import com.tenderWatch.CountryList;
import com.tenderWatch.R;
import com.tenderWatch.SideSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class IndexingArrayAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private ArrayList<CountryList.Item> item;
    private ArrayList<CountryList.Item> originalItem;

    private ArrayList<String> alpha2, list;
    private int textViewResourceId;
    ViewHolder holder;
    ViewHolderSection holderSection;
    int position;
    private static final String TAG = "IndexingArrayAdapter";
    char[] chars;
    public HashMap<String,String> checked = new HashMap<String,String>();

    public IndexingArrayAdapter(Context context, int textViewResourceId, ArrayList<CountryList.Item> item, ArrayList<String> alpha2, ArrayList<String> list,char[] chars) {
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.item = item;
        this.alpha2 = alpha2;
        this.list = list;
        this.chars=chars;
        holder = new ViewHolder();
        holderSection = new ViewHolderSection();
    }

    public Object[] getSections() {
        String[] chars = new String[SideSelector.ALPHABET2.length];
        for (int i = 0; i < SideSelector.ALPHABET2.length; i++) {
            chars[i] = String.valueOf(SideSelector.ALPHABET2[i]);
        }

        return chars;
    }

    public void setCheckedItem(int i) {


        if (checked.containsKey(String.valueOf(i))){
            checked.remove(String.valueOf(i));
        }

        else {
            checked.put(String.valueOf(i), String.valueOf(item.get(i).getTitle())+"~"+String.valueOf(item.get(i).getCode())+"~"+String.valueOf(item.get(i).getId()));
        }
    }

    public HashMap<String, String> getallitems(){
        return checked;
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
        this.position = position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //if (convertView == null) {
            if (item.get(position).isSection()) {
                convertView = inflater.inflate(R.layout.layout_section, parent, false);
                holderSection.tvSectionTitle = convertView.findViewById(R.id.tvSectionTitle);
                holderSection.tvSectionTitle.setText((item.get(position).getTitle()));
                holderSection.sectionLayout = convertView.findViewById(R.id.itemlayout);
//                holderSection.sectionLayout.setBackgroundColor(Color.GREEN);
                convertView.setTag(holderSection);
            } else {
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                holder.itemLayout=(RelativeLayout) convertView.findViewById(R.id.itemlayout);
                holder.flag = (ImageView) convertView.findViewById(R.id.img);
                holder.tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
                holder.tvItemCode = (TextView) convertView.findViewById(R.id.tvItemCode);

                holder.imgtrue = (ImageView) convertView.findViewById(R.id.imgtrue);
                holder.tvItemTitle.setText(item.get(position).getTitle());
                Bitmap flag1 = StringToBitMap(item.get(position).getFlag());
               String code=item.get(position).getCode();
                holder.tvItemCode.setText(item.get(position).getCode());
                holder.flag.setImageBitmap(flag1);
               // holder.itemLayout.setBackgroundColor(Color.argb(255, 207, 207, 207));

                if (item.get(position).getSelected()) {
                    holder.imgtrue.setVisibility(View.VISIBLE);
                    holder.itemLayout.setBackgroundColor(Color.argb(255, 207, 207, 207));
                } else {
                    holder.imgtrue.setVisibility(View.GONE);
                    holder.itemLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
                }
                convertView.setTag(holder);

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

                item = (ArrayList<CountryList.Item>) results.values;
                notifyDataSetChanged();
            }

            @SuppressWarnings("null")
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<CountryList.Item> filteredArrayList = new ArrayList<CountryList.Item>();


                if (originalItem == null || originalItem.size() == 0) {
                    originalItem = new ArrayList<CountryList.Item>(item);
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

    public void setItemSelected(int pos) {
        if( item.get(pos).getSelected()){
            item.get(pos).setSelected(false);
        }else {
            item.get(pos).setSelected(true);
            item.get(pos).getSelected();
        }
        notifyDataSetChanged();
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


    class ViewHolder {
        TextView tvItemTitle;
        ImageView flag;
        ImageView imgtrue;
        RelativeLayout itemLayout;
        TextView tvItemCode;
    }

    class ViewHolderSection {
        TextView tvSectionTitle;
        RelativeLayout sectionLayout;
    }

}
