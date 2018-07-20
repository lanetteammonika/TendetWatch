package com.tenderWatch.Drawer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tenderWatch.ClientDrawer.Support;
import com.tenderWatch.ClientDrawer.TenderList;
import com.tenderWatch.CountryList;
import com.tenderWatch.MainActivity;
import com.tenderWatch.Models.ResponseNotifications;
import com.tenderWatch.Models.User;
import com.tenderWatch.R;
import com.tenderWatch.Retrofit.Api;
import com.tenderWatch.Retrofit.ApiUtils;
import com.tenderWatch.SharedPreference.SharedPreference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Api mAPIService;
    SharedPreference sp = new SharedPreference();
    private static final String TAG = MainDrawer.class.getSimpleName();
    Intent intent;
    private static MenuItem menu2, editMenu;
    CircleImageView circledrawerimage;
    User user;
    TextView emailText;
    NavigationView navigationView;
    Menu menu;
    static Boolean display = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.drawertoolbar);
        mAPIService = ApiUtils.getAPIService();
        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

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
        circledrawerimage = navigationView.getHeaderView(0).findViewById(R.id.circledrawerimage2);
        emailText = navigationView.getHeaderView(0).findViewById(R.id.textView2);
        user = (User) sp.getPreferencesObject(MainDrawer.this);
        if(!user.getProfilePhoto().equals("no image")){
        Picasso.with(this).load(user.getProfilePhoto()).into(circledrawerimage);}
        emailText.setText(user.getEmail());
        String get = getIntent().getStringExtra("nav_sub");
        String getnot = getIntent().getStringExtra("nav_not");
        if (getnot != null) {
            displaySelectedScreen(R.id.nav_notifications);
        } else if (get != null) {
            displaySelectedScreen(R.id.nav_subscriptiondetails);
        } else {
            emailText.setText(user.getEmail());
            displaySelectedScreen(R.id.nav_home);
        }
        GetNotification();

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
        editMenu = menu.findItem(R.id.menu_item2);
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

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setBackgroundResource(R.drawable.bg_red);
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    private void GetNotification() {
        String token = "Bearer " + sp.getPreferences(MainDrawer.this, "token");
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
    }

    private void callEdit() {
        Fragment fragment = null;
        fragment = new Notification();
        Bundle args = new Bundle();
        if (editMenu.getTitle().equals("Edit")) {
            editMenu.setTitle("Cancel");
            args.putString("edit", "true");
            fragment.setArguments(args);
        } else {
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

    private void call() {
        Intent i = new Intent(getApplicationContext(), CountryList.class);
        i.putExtra("sub", "1234");
        startActivity(i);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                if (display) {
                    editMenu.setVisible(false);
                    menu2.setVisible(false);
                }
                fragment = new TenderList();
                display = true;
                break;
            case R.id.nav_subscriptiondetails:
                menu2.setVisible(true);
                editMenu.setVisible(false);
                fragment = new SubScription();
                break;
            case R.id.nav_editprofile:
                editMenu.setVisible(false);
                menu2.setVisible(false);
                fragment = new EditProfile();
                break;
            case R.id.nav_changepassword:
                editMenu.setVisible(false);
                menu2.setVisible(false);
                fragment = new ChangePassword();
                break;
            case R.id.nav_favorites:
                editMenu.setVisible(false);
                menu2.setVisible(false);
                fragment = new TenderList();
                Bundle bundle = new Bundle();
                bundle.putString("nav_fav", "true");
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                break;
            case R.id.nav_notifications:
                editMenu.setVisible(true);
                menu2.setVisible(false);
                fragment = new Notification();
                break;
            case R.id.nav_contactsupportteam:
                editMenu.setVisible(false);
                menu2.setVisible(false);
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
        String token = "Bearer " + sp.getPreferences(MainDrawer.this, "token");
        User u = (User) sp.getPreferencesObject(MainDrawer.this);
        String role = sp.getPreferences(MainDrawer.this, "role");
        String deviceId = sp.getPreferences(MainDrawer.this, "androidId");
        sp.showProgressDialog(MainDrawer.this);

        mAPIService.logout(token, deviceId, role).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.i(TAG, "post submitted to API." + response);
                sp.hideProgressDialog();
                sp.removePreferences(MainDrawer.this, "role");
                sp.removePreferences(MainDrawer.this, "email");
                sp.removePreferences(MainDrawer.this, "id");
                sp.removePreferences(MainDrawer.this, "profile");
                sp.removePreferences(MainDrawer.this, "MyObject");
                sp.removePreferences(MainDrawer.this, "token");

                intent = new Intent(MainDrawer.this, MainActivity.class);
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