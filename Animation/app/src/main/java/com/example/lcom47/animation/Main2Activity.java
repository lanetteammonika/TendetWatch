package com.example.lcom47.animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   p.showProgressDialog(Main2Activity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        p.hideProgressDialog();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
       // p.showProgressDialog(Main2Activity.this);

        super.onDestroy();
    }
}
