package com.example.lcom47.animation;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by lcom47 on 9/2/18.
 */

public class MyDialog extends Dialog {

    public MyDialog(Context context) {
        super(context, R.style.AppTheme);
        setContentView(R.layout.as);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //Optional
        //Any other code you want in your constructor
    }
}
