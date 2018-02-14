package com.example.lcom47.animation;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by lcom47 on 9/2/18.
 */

public class Progress {
    ProgressDialog mProgressDialog;
    MyDialog dialog;
    public ProgressBar pb;


    public void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading....");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    public void showProgress(Context context) {
      pb.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
     pb.setVisibility(View.INVISIBLE);
    }
}
