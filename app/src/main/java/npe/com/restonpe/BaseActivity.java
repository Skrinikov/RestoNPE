package npe.com.restonpe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import npe.com.restonpe.Services.RestoLocationManager;

/**
 * Template activity for all other activity to extend. Contains the
 * drawer menu and action bar. Most of the code is auto-generated with
 * the drawer activity.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.0
 * @since 01/12/2016
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHARED_PREFS = "Settings";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    private SharedPreferences prefs;

    private String TAG = "BaseActivity";

    /**
     * Creates the basic xml layout with a new main toolbar. Along with the
     * auto generated-code which creates the drawer and navigation view.
     * <p>
     * Used as reference
     * source: http://stackoverflow.com/questions/33194594/navigationview-get-find-header-layout
     *
     * @param savedInstanceState bundle where the values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate called");
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationSetting();
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //((TextView)drawer.findViewById(R.id.hello_user)).setText(prefs.getString("username","user"));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView hello = (TextView)header.findViewById(R.id.hello_user);
        hello.setText(getString(R.string.welcome) + " " + prefs.getString("username", "User"));

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Auto-generated code that came with the DrawerActivity.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /**
     * Event handler for all the different menu items for when they are clicked on.
     * Which action to execute is determined by the selected item's id.
     *
     * @param item The item which was selected.
     * @return boolean Depicts an item has been selected and have processed the intention.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.home) {
            Log.d(TAG, "onNavigationItemSelected - home");
            intent = new Intent(this, MainActivity.class);
        } else if (id == R.id.about) {
            Log.d(TAG, "onNavigationItemSelected - about");
            intent = new Intent(this, AboutActivity.class);
        } else if (id == R.id.dawson) {
            Log.d(TAG, "onNavigationItemSelected - dawson");
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
        } else if (id == R.id.setting) {
            Log.d(TAG, "onNavigationItemSelected - setting");
            intent = new Intent(this, SettingActivity.class);
        } else if (id == R.id.tip) {
            Log.d(TAG, "onNavigationItemSelected - tip");
            intent = new Intent(this, TipActivity.class);
        }

        if (intent != null)
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method that calls upon the RestoLocationManager to get current
     * longitude and latitude location then saved is to the sharedPreferences.
     * <p>
     * Used as reference
     * Source: Jeegna's NearRestoActivity
     * Source: https://developer.android.com/training/permissions/requesting.html
     */
    private void locationSetting() {
        // Request location permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                RestoLocationManager.LOCATION_MANAGER_REQUEST_CODE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RestoLocationManager.LOCATION_MANAGER_REQUEST_CODE);

        RestoLocationManager restoLocationManager = new RestoLocationManager(this) {
            @Override
            public void onLocationChanged(Location location) {
                saveToPrefs(location);
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Get location
                Location location = getLocation();
                saveToPrefs(location);
            }
        };

        Location location = restoLocationManager.getLocation();
        saveToPrefs(location);
    }

    /**
     * Apply the given location into the sharedPreferences, if not null, for later use.
     *
     * @param location User's current Location.
     */
    private void saveToPrefs(Location location) {
        if (location != null) {
            prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(LATITUDE, location.getLatitude() + "");
            editor.putString(LONGITUDE, location.getLongitude() + "");
            editor.apply();
        }
    }
}
