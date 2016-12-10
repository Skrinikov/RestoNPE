package npe.com.restonpe;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import npe.com.restonpe.Beans.Address;
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.database.RestoDAO;

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
        TextView hello = (TextView) header.findViewById(R.id.hello_user);
        hello.setText(getString(R.string.welcome) + " " + prefs.getString(SettingActivity.USERNAME, "User"));

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

        switch (id) {
            case R.id.home:
                Log.d(TAG, "onNavigationItemSelected - home");
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.about:
                Log.d(TAG, "onNavigationItemSelected - about");
                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.dawson:
                Log.d(TAG, "onNavigationItemSelected - dawson");
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
                break;
            case R.id.setting:
                Log.d(TAG, "onNavigationItemSelected - setting");
                intent = new Intent(this, SettingActivity.class);
                break;
            case R.id.tip:
                Log.d(TAG, "onNavigationItemSelected - tip");
                intent = new Intent(this, TipActivity.class);
                break;
            case R.id.heroku:
                Log.d(TAG, "onNavigationItemSelected - heroku");
                RetrieveData retrieveData = new RetrieveData();
                retrieveData.execute();
                break;
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

    /**
     * Inserts the given fragment into the content view using
     * the fragment manager.
     */
    public void createFragments(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * Retrieves all the resto in the local database and add them to
     * heroku's database.
     */
    private void syncHeroku(List<Resto> list) {
        // url is https://shrouded-thicket-29911.herokuapp.com/api/resto/create
    }

    public class RetrieveData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            RestoDAO db = RestoDAO.getDatabase(BaseActivity.this);
            List<Resto> restos = db.getAllRestaurants();
            String herokuURL = "https://shrouded-thicket-29911.herokuapp.com/api/resto/create";

            HttpsURLConnection conn = null;
            OutputStream out = null;
            try {
                for (Resto resto : restos) {
                    if (isNetworkAccessible()) {
                        URL url = new URL(herokuURL);
                        conn = (HttpsURLConnection) url.openConnection();

                        Address address = resto.getAddress();
                        String[] str = address.getAddress().split(" ");
                        String jsonData = "{\"name\":\"%1$s\",\"phone\":\"%2$s\",\"resto_email\":\"%3$s\",\"link\":\"%4$s\",\"price\":\"%5$s\",\"genre\":\"%6$s\"," +
                                "\"civic_num\":\"%7$s\",\"street\":\"%8$s\",\"suite\":\"%9$s\",\"city\":\"%10$s\",\"country\":\"%11$s\",\"postal_code\":\"%12$s\"," +
                                "\"province\":\"%13$s\",\"submitterName\":\"%14$s\",\"submitterEmail\":\"%15$s\",\"password\":\"%16$s\",\"email\":\"%17$s\"}";

                        jsonData = String.format(jsonData, resto.getName(), resto.getPhone(), resto.getEmail(), resto.getLink(), resto.getPriceRange().length(),
                                resto.getGenre(), str[0], str[1], address.getSuite(), address.getCity(), address.getCountry(), address.getPostal(), address.getProvince(),
                                resto.getSubmitterName(), resto.getSubmitterEmail(),prefs.getString(SettingActivity.PASSWORD,null),prefs.getString(SettingActivity.EMAIL,null));

                        Log.d(TAG, "data is: " + jsonData);

                        byte[] bytes = jsonData.getBytes("UTF-8");
                        int bytesLeng = bytes.length;

                        // Set headers
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        conn.setRequestProperty("Content-Length", String.valueOf(bytesLeng));

                        out = new BufferedOutputStream(conn.getOutputStream());

                        out.write(bytes);
                        out.flush();
                        out.close();
                        /*// Set headers
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                        //conn.connect();

                        Log.d(TAG, "resto is: " + resto.toString());

                        JSONObject obj = new JSONObject();
                        obj.put("name", resto.getName());
                        obj.put("phone", String.valueOf(resto.getPhone()));
                        obj.put("resto_email", resto.getEmail());
                        obj.put("link", resto.getLink());
                        obj.put("price", resto.getPriceRange().length());
                        obj.put("genre", resto.getGenre());
                        String[] str = resto.getAddress().getAddress().split(" ");
                        obj.put("civic_num", str[0]);
                        obj.put("street", str[1]);
                        obj.put("suite", resto.getAddress().getSuite());
                        obj.put("city", resto.getAddress().getCity());
                        obj.put("country", resto.getAddress().getCountry());
                        obj.put("postal_code", resto.getAddress().getPostal());
                        obj.put("province", resto.getAddress().getProvince());
                        obj.put("submitterName", resto.getSubmitterName());
                        obj.put("submitterEmail", resto.getSubmitterEmail());

                        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                        out.write(obj.toString());
                        out.close();*/

                        int httpResult = conn.getResponseCode();

                        if (httpResult != HttpURLConnection.HTTP_OK) {
                            Log.e(TAG, "Something went wrong. The URL was " + url + " The HTTP response was " + httpResult);
                            return 0;
                        }
                    }
                }
            } catch (MalformedURLException mue) {
                Log.e(TAG, "Malformed URL: " + herokuURL);
                return 0;
            } catch (IOException ioe) {
                Log.e(TAG, "An IOException occurred while reading the JSON file: " + ioe.getMessage());
                return 0;
            /*} catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());*/
            } finally {
                if (conn != null)
                    conn.disconnect();
            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer syncOK) {
            if (syncOK == 1) {
                Toast.makeText(BaseActivity.this, R.string.sync, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BaseActivity.this, R.string.failed, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if the network is up and usable.
     *
     * @return {@code True} if the network is up and can be used, {@code False} otherwise
     */
    public boolean isNetworkAccessible() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
