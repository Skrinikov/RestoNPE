package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import npe.com.restonpe.BaseActivity;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.R;
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
        getRestaurants(latitude, longitude);
    }

    /**
     * Gets a list of nearby restaurants to display on the ListView
     *
     * @param latitude The latitude to search for the nearby restaurants
     * @param longitude The longitude to search for the nearby restaurants
     */
    private void getRestaurants(final String latitude, final String longitude) {
        ZomatoRestos zomatoRestos = new ZomatoRestos(activity) {
            @Override
            public void handleResults(List<?> list) {
                if (list != null && list.size() > 0) {
                    List<RestoItem> restos = (List<RestoItem>) list;
                    ListView listView = (ListView) activity.findViewById(R.id.near_list);

                    RestoAdapter adapter = new RestoAdapter(activity, restos, longitude, latitude);
                    listView.setAdapter(adapter);
                }
            }
        };

        if (latitude != null && longitude != null) {
            zomatoRestos.findNearbyRestos(latitude, longitude);
        } else {
            // Tell user there were no results
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
