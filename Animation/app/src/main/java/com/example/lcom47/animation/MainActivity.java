package com.example.lcom47.animation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
public Progress p=new Progress();
public ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=(Button) findViewById(R.id.click);
        pb=(ProgressBar) findViewById(R.id.id2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               p.showProgressDialog(MainActivity.this);
//p.showProgress(MainActivity.this);
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
       // p.showProgressDialog(MainActivity.this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // p.showProgressDialog(MainActivity.this);

    }

    @Override
    protected void onDestroy() {
        //p.showProgressDialog(MainActivity.this);

        super.onDestroy();
    }
}
