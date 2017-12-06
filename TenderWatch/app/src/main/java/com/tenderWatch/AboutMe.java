package com.tenderWatch;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tenderWatch.SharedPreference.SharedPreference;

public class AboutMe extends AppCompatActivity {

    EditText edtAbotMe;
    TextView txtCount;
    int textCount=0;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
//        toolbar = (Toolbar) findViewById(R.id.toolbar3);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setActionBar(toolbar);
//        }

        mToolbar = (Toolbar) findViewById(R.id.mtoolbar2);
        setSupportActionBar(mToolbar);

        setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
//        
//        ActionBar mActionBar = getSupportActionBar();
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
//        LayoutInflater mInflater = LayoutInflater.from(this);
//
//        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
//        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.txt_count);
//        mTitleTextView.setText("My Own Title");
//
//        txtCount = (TextView) mCustomView
//                .findViewById(R.id.txt_count);
//        mActionBar.setCustomView(mCustomView);
//        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.
       // getSupportActionBar().setDisplayShowCustomEnabled(true);
        edtAbotMe=(EditText) findViewById(R.id.txt_aboutme);
        txtCount=(TextView) findViewById(R.id.txt_count);
        edtAbotMe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               //
                if(s==" "){
                    textCount=textCount+1;
                }else{
                    textCount=textCount+1;
                }
                String counttext= String.valueOf(textCount);

                txtCount.setText(counttext);
                if(textCount==1000){
                    txtCount.setText(String.valueOf(textCount));
                    SharedPreference ss= new SharedPreference();
                    ss.ShowDialog(AboutMe.this,"Up tp 1000 Characters");
                   // edtAbotMe.setFocusable(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


}
