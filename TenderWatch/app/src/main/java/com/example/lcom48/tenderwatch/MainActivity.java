package com.example.lcom48.tenderwatch;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
public TextView aboutapp;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aboutapp = (TextView) findViewById(R.id.aboutapp);
        context=this;
        //aboutapp.setOnClickListener(new onClickListe);
        aboutapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //final Dialog dialog = new Dialog(MainActivity.this);
                final Dialog dialog=new Dialog(context,R.style.full_screen_dialog);
                //setting custom layout to dialog
                dialog.setContentView(R.layout.aboutapp);

                //dialog.setTitle("Custom Dialog");
                //LinearLayout ll = (LinearLayout) findViewById(R.id.relativeLayout1);
               // ll.setAlpha();
                //View backgroundImage = findViewById(R.id.relativeLayout1);
                //Drawable background = backgroundImage.getBackground();
                //background.setAlpha(80);
//                adding text dynamically
//                TextView txt = (TextView) dialog.findViewById(R.id.textView);
//                txt.setText("Put your dialog text here.");
//
//                ImageView image = (ImageView)dialog.findViewById(R.id.image);
//                image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
//
//                //adding button click event
                Button dismissButton = (Button) dialog.findViewById(R.id.button_close);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
