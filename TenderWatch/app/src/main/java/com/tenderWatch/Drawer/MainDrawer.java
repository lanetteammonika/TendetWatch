package com.tenderWatch.Drawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tenderWatch.ClientDrawer.ClientDrawer;
import com.tenderWatch.ClientDrawer.Support;
import com.tenderWatch.ClientDrawer.TenderList;
import com.tenderWatch.CountryList;
import com.tenderWatch.MainActivity;
import com.tenderWatch.Models.User;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;
import com.tenderWatch.SignUpSelection;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private static MenuItem menu2,editMenu;
    CircleImageView circledrawerimage;
    User user;
    NavigationView navigationView;
    TextView emailText;
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
        circledrawerimage = navigationView.getHeaderView(0).findViewById(R.id.circledrawerimage2);
        emailText=navigationView.getHeaderView(0).findViewById(R.id.textView2);
        user= (User) sp.getPreferencesObject(MainDrawer.this);

        Picasso.with(this).load(user.getProfilePhoto()).into(circledrawerimage);
        emailText.setText(user.getEmail());
        String get=getIntent().getStringExtra("nav_sub");
        String getnot=getIntent().getStringExtra("nav_not");
        if(getnot !=null){
            displaySelectedScreen(R.id.nav_notifications);
        }
        else if(get !=null){
            displaySelectedScreen(R.id.nav_subscriptiondetails);
        }else{
            displaySelectedScreen(R.id.nav_home);
        }

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        editMenu=menu.findItem(R.id.menu_item2);
        menu2 = menu.findItem(R.id.menu_item);
        editMenu.setTitle("Edit");
        menu2.setVisible(false);
        editMenu.setVisible(false);
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
        if (id == R.id.menu_item2) {


            callEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callEdit() {
        Fragment fragment = null;
        fragment=new Notification();
        Bundle args = new Bundle();
        if(editMenu.getTitle().equals("Edit")){
            editMenu.setTitle("Cancel");
            args.putString("edit", "true");
            fragment.setArguments(args);
        }else{
            editMenu.setTitle("Edit");
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
                fragment = new TenderList();
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
                editMenu.setVisible(true);
                fragment = new Notification();
                break;
            case R.id.nav_contactsupportteam:
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

    private void Logout() {
        String token="Bearer "+sp.getPreferences(MainDrawer.this,"token");
        User u = (User) sp.getPreferencesObject(MainDrawer.this);
        String role=sp.getPreferences(MainDrawer.this,"role");
        String deviceId=sp.getPreferences(MainDrawer.this,"androidId");
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