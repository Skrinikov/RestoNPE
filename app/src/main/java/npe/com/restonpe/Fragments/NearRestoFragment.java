package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
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
 * @since 08/12/2016
 */
public class NearRestoFragment extends Fragment {

    private static final String TAG = NearRestoFragment.class.getSimpleName();

    private Activity activity;
    private RestoNetworkManager<RestoItem> restoNetworkManagerZomato;
    private RestoNetworkManager<RestoItem> restoNetworkManagerHeroku;
    private List<RestoItem> zomatoRestos;
    private List<RestoItem> herokuRestos;
    private List<RestoItem> allRestos;

    /**
     * Inflates a layout to be the content layout of the NearRestoActivity.
     * <p>
     * Used as reference
     * source: https://developer.android.com/guide/components/fragments.html
     *
     * @param inflater           Layout inflater needed to inflate the xml file.
     * @param container          View where the xml file will be loaded into.
     * @param savedInstanceState bundle where values are stored.
     * @return The View inflated.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");

        zomatoRestos = null;
        herokuRestos = null;
        allRestos = new ArrayList<>();

        return inflater.inflate(R.layout.activity_near_restos, container, false);
    }

    /**
     * Gets the nearby restaurants and displays them on the RestoListFragment.
     * Nearby to the longitude and latitude either in the shared preferences
     * or the bundle, with the bundle having priority.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        activity = getActivity();

        Bundle extras = activity.getIntent().getExtras();
        String latitude;
        String longitude;

        if (extras != null) {
            // Get location from intent extras
            latitude = String.valueOf(extras.getDouble("latitude"));
            longitude = String.valueOf(extras.getDouble("longitude"));
            Log.d(TAG, "lat : " + latitude + " long: " + longitude);
        } else {
            // Get location from shared prefs
            SharedPreferences prefs = activity.getSharedPreferences(BaseActivity.SHARED_PREFS, Activity.MODE_PRIVATE);
            latitude = prefs.getString(BaseActivity.LATITUDE, null);
            longitude = prefs.getString(BaseActivity.LONGITUDE, null);
        }

        // Get nearby restaurants
        getNearbyRestaurants(latitude, longitude);
    }

    /**
     * Gets a list of nearby restaurants to display on the ListView
     *
     * @param latitude  The user's latitude to use to search for the nearby restaurants
     * @param longitude The user's longitude to use to search for the nearby restaurants
     */
    private void getNearbyRestaurants(final String latitude, final String longitude) {
        if (latitude != null && longitude != null) {
            restoNetworkManagerZomato = new RestoNetworkManager<RestoItem>(activity) {
                @Override
                public void onPostExecute(List<RestoItem> list) {
                    if (list != null && list.size() > 0) {
                        zomatoRestos = list;

                        if (herokuRestos != null) {
                            allRestos.addAll(zomatoRestos);
                            allRestos.addAll(herokuRestos);
                            ListView listView = (ListView) activity.findViewById(R.id.near_list);

                            RestoAdapter adapter = new RestoAdapter(activity, allRestos, longitude, latitude);
                            listView.setAdapter(adapter);
                        }
                    }
                }

                @Override
                protected List<RestoItem> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Json response...");

                    try {
                        Log.i(TAG, "Json response came from Zomato");
                        ZomatoRestos zomatoRestos = new ZomatoRestos(activity);
                        return zomatoRestos.readResto(reader, "nearby_restaurants");
                    } catch (IOException e) {
                        Log.e(TAG, "An IO exception occurred: " + e.getMessage());
                    }

                    return null;
                }
            };
            restoNetworkManagerHeroku = new RestoNetworkManager<RestoItem>(activity) {
                @Override
                public void onPostExecute(List<RestoItem> list) {
                    if (list != null && list.size() > 0) {
                        herokuRestos = list;

                        if (zomatoRestos != null) {
                            allRestos.addAll(zomatoRestos);
                            allRestos.addAll(herokuRestos);
                            ListView listView = (ListView) activity.findViewById(R.id.near_list);

                            RestoAdapter adapter = new RestoAdapter(activity, allRestos, longitude, latitude);
                            listView.setAdapter(adapter);
                        }
                    }
                }

                @Override
                protected List<RestoItem> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Json response...");

                    try {
                        Log.i(TAG, "Json response came from Heroku");
                        HerokuRestos herokuRestos = new HerokuRestos(activity);
                        return herokuRestos.readRestoJson(reader);
                    } catch (IOException e) {
                        Log.e(TAG, "An IO exception occurred: " + e.getMessage());
                    }

                    return null;
                }
            };

            restoNetworkManagerZomato.findNearbyRestos(latitude, longitude);
            restoNetworkManagerHeroku.findNearbyRestosFromHeroku(latitude, longitude);
        } else {
            // Tell user there were no results because there was no location
            new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.no_result))
                    .show();
        }
    }

    /**
     * Cancels the network manager request.
     *
     * @param mayInterruptIfRunning {@code True} if the thread executing this task should be
     *                              interrupted; otherwise, in-progress tasks are allowed to complete.
     */
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (restoNetworkManagerZomato != null && restoNetworkManagerHeroku != null) {
            return restoNetworkManagerZomato.cancel(mayInterruptIfRunning) && restoNetworkManagerHeroku.cancel(mayInterruptIfRunning);
        }
        return false;
    }
}
