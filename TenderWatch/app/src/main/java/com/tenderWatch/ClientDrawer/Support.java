package com.tenderWatch.ClientDrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tenderWatch.Models.User;
import com.tenderWatch.R;
import com.tenderWatch.SharedPreference.SharedPreference;

/**
 * Created by lcom47 on 26/12/17.
 */

public class Support extends Fragment {

    EditText userEmail,userSubject,userBodyText;
    User user;
    SharedPreference sp=new SharedPreference();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_support_team, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Support");
        userEmail=(EditText) view.findViewById(R.id.userEmail);
        userSubject=(EditText) view.findViewById(R.id.subject);
        userBodyText=(EditText) view.findViewById(R.id.bodyText);
        user=(User)sp.getPreferencesObject(getActivity());
        userEmail.setText(user.getEmail().toString());
    }
}