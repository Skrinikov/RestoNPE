package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.BaseActivity;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Heroku.HerokuRestos;
import npe.com.restonpe.R;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.util.RestoAdapter;

/**
 * Fragment class that will load the content of the NearRestosActivity.
 *
 * @author Uen Yi Cindy Hung, Jeegna Patel
 * @version 1.0
 * @since 11/29/2016
 */
public class NearRestoFragment extends Fragment {

    private static final String TAG = NearRestoFragment.class.getSimpleName();

    private Activity activity;

    /**
     * Inflates a layout to be the content layout of the NearRestoActivity.
     *
     * Used as reference
     * source: https://developer.android.com/guide/components/fragments.html
     *
     * @param inflater Layout inflater needed to inflate the xml file.
     * @param container View where the xml file will be loaded into.
     * @param savedInstanceState bundle where values are stored.
     * @return The View inflated.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.activity_near_restos, container, false);
    }

    /**
     * Gets the nearby restaurants and displays them on the RestoListFragment
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        activity = getActivity();

        // Get location from shared prefs
        SharedPreferences sharedPreferences = activity.getSharedPreferences(BaseActivity.SHARED_PREFS, Activity.MODE_PRIVATE);
        String latitude = sharedPreferences.getString(BaseActivity.LATITUDE, null);
        String longitude = sharedPreferences.getString(BaseActivity.LONGITUDE, null);

        displayLocationInformation(latitude, longitude);

        // Get nearby restaurants
        getNearbyRestaurants(latitude, longitude);
    }

    /**
     * Gets a list of nearby restaurants to display on the ListView
     *
     * @param latitude The user's latitude to use to search for the nearby restaurants
     * @param longitude The user's longitude to use to search for the nearby restaurants
     */
    private void getNearbyRestaurants(final String latitude, final String longitude) {
        if (latitude != null && longitude != null) {
            RestoNetworkManager<RestoItem> networkManager = new RestoNetworkManager<RestoItem>(activity) {
                @Override
                public void onPostExecute(List<RestoItem> list) {
                    // FIXME only shows results from heroku. I've tried making private fields in this abstract class, and in NearRestoFragment. Nothing works :C
                    if (list != null && list.size() > 0) {
                        ListView listView = (ListView) activity.findViewById(R.id.near_list);

                        RestoAdapter adapter = new RestoAdapter(activity, list, longitude, latitude);
                        listView.setAdapter(adapter);
                    }
                }

                @Override
                protected List<RestoItem> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Json response...");

                    try {
                        // Check first token. If it's an object, it was from Zomato, and if it's an
                        // array, it's from Heroku
                        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                            Log.i(TAG, "Json response came from Heroku");
                            HerokuRestos herokuRestos = new HerokuRestos(activity);
                            return herokuRestos.readRestoJson(reader);
                        } else if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                            Log.i(TAG, "Json response came from Zomato");
                            ZomatoRestos zomatoRestos = new ZomatoRestos(activity);
                            return zomatoRestos.readResto(reader, "nearby_restaurants");
                        }
                    } catch (IOException e) {
                        Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                    }

                    return null;
                }
            };

            networkManager.findNearbyRestos(latitude, longitude);
        } else {
            // Tell user there were no results because there was no location
            new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.no_result))
                    .show();
        }
    }

    /**
     * Displays the latitude and longitude on the screen.
     *
     * @param latitude The latitude to display on the screen
     * @param longitude The longitude to display on the screen
     */
    private void displayLocationInformation(String latitude, String longitude) {
        if (latitude != null && longitude != null) {
            Log.i(TAG, "Location found: " + latitude + ", " + longitude);
            ((TextView) activity.findViewById(R.id.textViewLatitude)).setText(String.format(getString(R.string.latitude), latitude));
            ((TextView) activity.findViewById(R.id.textViewLongitude)).setText(String.format(getString(R.string.longitude), longitude));
        } else {
            Log.i(TAG, "Location not found");
            ((TextView) activity.findViewById(R.id.textViewLatitude)).setText(activity.getString(R.string.location_not_found));
            ((TextView) activity.findViewById(R.id.textViewLongitude)).setText("");
        }
    }
}
