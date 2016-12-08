package npe.com.restonpe;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.Beans.Cuisine;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Fragments.FindRestoFragment;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.database.RestoDAO;
import npe.com.restonpe.util.RestoAdapter;

/**
 * Creates an instance of the RestoSearch Activity. This {@code Activity} will allow the user to
 * find restaurants using the Zomato API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 21/11/2016
 */
public class FindRestoActivity extends BaseActivity {

    private static final String TAG = FindRestoActivity.class.getSimpleName();

    private Context mContext;

    private String userLatitude;
    private String userLongitude;

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");

        // Get shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(BaseActivity.SHARED_PREFS, MODE_PRIVATE);

        // Initialise fields
        // This location is used to determine the distance between the user and the restaurant
        this.mContext = this;
        this.userLongitude = sharedPreferences.getString(BaseActivity.LONGITUDE, null);
        this.userLatitude = sharedPreferences.getString(BaseActivity.LATITUDE, null);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_search_restos);
        }

        FindRestoFragment fragment = new FindRestoFragment();
        super.createFragments(fragment);
    }

    public void buttonSearchClick(View v) {
        EditText nameEditText = (EditText) findViewById(R.id.editTextName);
        EditText cityEditText = (EditText) findViewById(R.id.editTextCity);
        Spinner cuisineSpinner = (Spinner) findViewById(R.id.cuisines_spinner);

        String name = nameEditText.getText().toString();
        String city = cityEditText.getText().toString();
        Cuisine cuisine = (Cuisine) cuisineSpinner.getSelectedItem();

        if (name.isEmpty()) {
            name = null;
        }
        if (city.isEmpty()) {
            city = null;
        }
        if (cuisine != null && cuisine.getName().equals(getString(R.string.search_cuisines)) && cuisine.getName().equals(getString(R.string.search_no_cuisines))) {
            // The user selected default cuisine or no cuisines, and therefore does not want to search with cuisine
            cuisine = null;
        }

        RestoDAO restoDAO = RestoDAO.getDatabase(mContext);
        final List<RestoItem> localDBRestos = restoDAO.getRestoByName(name);

        RestoNetworkManager<RestoItem> restoNetworkManager = new RestoNetworkManager<RestoItem>(mContext) {
            @Override
            public void onPostExecute(List<RestoItem> list) {
                ListView listView = (ListView) findViewById(R.id.find_list);
                if (list != null && list.size() > 0) {
                    if (localDBRestos != null) {
                        // Add local db RestoItems to beginning of list
                        list.addAll(0, localDBRestos);
                    }

                    RestoAdapter adapter = new RestoAdapter(mContext, list, userLongitude, userLatitude);
                    listView.setAdapter(adapter);
                } else if (localDBRestos != null && localDBRestos.size() > 0) {
                    // Only use local db results
                    list = localDBRestos;

                    RestoAdapter adapter = new RestoAdapter(mContext, list, userLongitude, userLatitude);
                    listView.setAdapter(adapter);
                } else {
                    // Tell user there were no results
                    new AlertDialog.Builder(mContext)
                            .setMessage(getString(R.string.no_result))
                            .show();
                }
            }

            @Override
            protected List<RestoItem> readJson(JsonReader reader) {
                Log.i(TAG, "Reading Json response...");

                try {
                    ZomatoRestos zomatoRestos = new ZomatoRestos(mContext);
                    return zomatoRestos.readResto(reader, "restaurants");
                } catch (IOException e) {
                    Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                }
                return null;
            }
        };

        String latitude = null;
        String longitude = null;

        Address address = getAddressOfCity(city);
        if (address != null) {
            latitude = String.valueOf(address.getLatitude());
            longitude = String.valueOf(address.getLongitude());
        }

        restoNetworkManager.findRestos(name, latitude, longitude, cuisine);
    }

    private Address getAddressOfCity(String city) {
        // Used to convert city to latitude/longitude
        RestoLocationManager restoLocationManager = new RestoLocationManager(mContext) {
            @Override
            public void onLocationChanged(Location location) {
                // Do nothing
            }
        };

        Address address = null;

        // Get latitude and longitude of given city.
        if (city != null) {
            // User wanted to search with a city
            address = restoLocationManager.getLocationFromName(city);
        }

        return address;
    }
}
