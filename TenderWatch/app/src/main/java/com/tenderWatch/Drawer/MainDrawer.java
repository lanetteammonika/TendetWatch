package com.tenderWatch.Drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tenderWatch.CountryList;
import com.tenderWatch.MainActivity;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Api mAPIService;
    SharedPreference sp = new SharedPreference();
    private static final String TAG = MainDrawer.class.getSimpleName();
    Intent intent;
    MenuItem menu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.drawertoolbar);
        mAPIService= ApiUtils.getAPIService();
        setSupportActionBar(toolbar); //NO PROBLEM !!!!

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        menu2 = menu.findItem(R.id.menu_item);
        menu2.setVisible(false);

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
            call();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void call(){
        Intent i=new Intent(getApplicationContext(), CountryList.class);
        i.putExtra("sub","1234");
        startActivity(i);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new Home();
                break;
            case R.id.nav_subscriptiondetails:
                menu2.setVisible(true);
                fragment = new SubScription();
                break;
            case R.id.nav_editprofile:
                fragment = new EditProfile();
                break;
            case R.id.nav_changepassword:
                fragment = new ChangePassword();
                break;
            case R.id.nav_favorites:
                fragment = new ChangePassword();
                break;
            case R.id.nav_notifications:
                fragment = new ChangePassword();
                break;
            case R.id.nav_contactsupportteam:
                fragment = new ChangePassword();
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

    private void Logout() {
        String token="Bearer "+sp.getPreferences(MainDrawer.this,"token");
        String deviceId = sp.getPreferences(MainDrawer.this, "deviceId");
        String role=sp.getPreferences(MainDrawer.this,"role");

        mAPIService.logout(token,deviceId,role).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.i(TAG, "post submitted to API." + response);
                sp.removePreferences(MainDrawer.this,"role");
                sp.removePreferences(MainDrawer.this,"email");
                sp.removePreferences(MainDrawer.this,"id");
                sp.removePreferences(MainDrawer.this,"profile");
                sp.removePreferences(MainDrawer.this,"MyObject");
                sp.removePreferences(MainDrawer.this,"token");

                intent=new Intent(MainDrawer.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "post submitted to API." + t);

            }
        });
    }
}