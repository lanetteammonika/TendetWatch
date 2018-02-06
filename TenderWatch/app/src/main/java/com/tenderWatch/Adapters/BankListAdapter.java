package com.tenderWatch.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenderWatch.Models.Datum;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.R;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;

public class BankListAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<Datum> bankList;
    SharedPreference sp = new SharedPreference();

    public BankListAdapter(Context context, ArrayList<Datum> bankList) {
        this.context = context;
        this.bankList = bankList;
    }

    @Override
    public int getCount() {
        return bankList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_banklist, viewGroup, false);
        TextView bankName = (TextView) view.findViewById(R.id.bankName);
        bankName.setText(bankList.get(i).getBankName());

        return view;
    }
}


