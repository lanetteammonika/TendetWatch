package com.tenderWatch.ClientDrawer;


import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.ClientDetail;
import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Drawer.Notification;
import com.tenderWatch.MainActivity;
import com.tenderWatch.Models.ResponseNotifications;
import com.tenderWatch.Models.User;
import com.tenderWatch.MyBroadcastReceiver;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.utils.ConnectivityReceiver;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Api mAPIService;
    SharedPreference sp = new SharedPreference();
    private static final String TAG = ClientDrawer.class.getSimpleName();
    Intent intent;
    CircleImageView circledrawerimage;
    User user;
    TextView emailText;
    NavigationView navigationView;
    private static MenuItem Cmenu2, CeditMenu;
    static Boolean Cdisplay = false;
    ConnectivityReceiver cr=new ConnectivityReceiver();
    private MyBroadcastReceiver myBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.clienttoolbar);

        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        mAPIService = ApiUtils.getAPIService();
myBroadcastReceiver=new MyBroadcastReceiver();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;

        toggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawer,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                GetNotification();
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        circledrawerimage = navigationView.getHeaderView(0).findViewById(R.id.circledrawerimage);
        emailText = navigationView.getHeaderView(0).findViewById(R.id.textView);
        user = (User) sp.getPreferencesObject(ClientDrawer.this);
        if (!user.getProfilePhoto().equals("no image")) {
            Picasso.with(this).load(user.getProfilePhoto()).into(circledrawerimage);
        }
        emailText.setText(user.getEmail());
        if (checkConnection()) {
            displaySelectedScreen(R.id.nav_home);
        } else {
            sp.ShowDialog(ClientDrawer.this, "Please Check your internet Connection");
        }

    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected(ClientDrawer.this);
        return isConnected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        CeditMenu = menu.findItem(R.id.menu_item2);
        Cmenu2 = menu.findItem(R.id.menu_item);
        CeditMenu.setTitle("Edit");
        Cmenu2.setVisible(false);
        CeditMenu.setVisible(false);
        // getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item) {
            //call();
            return true;
        }
        if (id == R.id.drawertoolbar) {
            GetNotification();
            return true;
        }

        if (id == R.id.menu_item2) {
            callEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callEdit() {
        Fragment fragment = null;
        fragment = new Notification();
        Bundle args = new Bundle();
        if (CeditMenu.getTitle().equals("Edit")) {
            CeditMenu.setTitle("Cancel");
            args.putString("edit", "true");
            fragment.setArguments(args);
        } else {
            CeditMenu.setTitle("Edit");
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                if (Cdisplay) {
                    CeditMenu.setVisible(false);
                    Cmenu2.setVisible(false);
                }
                fragment = new TenderList();
                Cdisplay = true;
                break;
            case R.id.nav_uploadtender:
                CeditMenu.setVisible(false);
                Cmenu2.setVisible(false);
                fragment = new Home();
                break;
            case R.id.nav_editprofile:
                CeditMenu.setVisible(false);
                Cmenu2.setVisible(false);
                fragment = new EditProfile();
                break;
            case R.id.nav_changepassword:
                CeditMenu.setVisible(false);
                Cmenu2.setVisible(false);
                fragment = new ChangePassword();
                break;
            case R.id.nav_favorites:
                CeditMenu.setVisible(false);
                Cmenu2.setVisible(false);
                fragment = new TenderList();
                Bundle bundle = new Bundle();
                bundle.putString("nav_fav", "true");
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                break;
            case R.id.nav_notifications:
                CeditMenu.setVisible(true);
                Cmenu2.setVisible(false);
                fragment = new Notification();
                break;
            case R.id.nav_contactsupportteam:
                CeditMenu.setVisible(false);
                Cmenu2.setVisible(false);
                fragment = new Support();
                break;
            case R.id.nav_logout:
                Logout();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setBackgroundResource(R.drawable.bg_red);
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    private void GetNotification() {
        String token = "Bearer " + sp.getPreferences(ClientDrawer.this, "token");
        if(cr.isConnected(ClientDrawer.this)){
        mAPIService.getNotifications(token).enqueue(new Callback<ArrayList<ResponseNotifications>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseNotifications>> call, Response<ArrayList<ResponseNotifications>> response) {
                Log.i(TAG, "post submitted to API." + response);
                int count = 0;
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (!response.body().get(i).getRead()) {
                            count += 1;
                        }
                    }
                }
                if (count > 0) {
                    setMenuCounter(R.id.nav_notifications, count);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseNotifications>> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);
            }
        });
        }else{
            sp.ShowDialog(ClientDrawer.this,"Please check your internet connection");
        }
    }

    private void Logout() {
        String token = "Bearer " + sp.getPreferences(ClientDrawer.this, "token");
        String deviceId = sp.getPreferences(getApplicationContext(), "deviceId");
        String role = sp.getPreferences(ClientDrawer.this, "role");
        sp.showProgressDialog(ClientDrawer.this);
        if(cr.isConnected(ClientDrawer.this)){
        mAPIService.logout(token, deviceId, role).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.i(TAG, "post submitted to API." + response);
                sp.hideProgressDialog();
                sp.removePreferences(ClientDrawer.this, "role");
                sp.removePreferences(ClientDrawer.this, "email");
                sp.removePreferences(ClientDrawer.this, "id");
                sp.removePreferences(ClientDrawer.this, "profile");
                sp.removePreferences(ClientDrawer.this, "MyObject");

                intent = new Intent(ClientDrawer.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.i(TAG, "post submitted to API." + t);

            }
        });
        }else{
            sp.ShowDialog(ClientDrawer.this,"Please check your internet connection");
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        // Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(ClientDrawer.this).registerReceiver(myBroadcastReceiver, new IntentFilter("android.content.BroadcastReceiver"));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        localBroadcastManager = LocalBroadcastManager.getInstance(MainDrawer.this);
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        if (localBroadcastManager != null && myBroadcastReceiver != null)
        LocalBroadcastManager.getInstance(ClientDrawer.this).unregisterReceiver(myBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(ClientDrawer.this).unregisterReceiver(myBroadcastReceiver);
    }

}
