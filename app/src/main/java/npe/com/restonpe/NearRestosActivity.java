package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import npe.com.restonpe.Fragments.NearRestoFragment;
import npe.com.restonpe.Zomato.ZomatoRestos;

/**
 * Creates an instance of the NearRestos Activity. This {@code Activity} will show the user the
 * restaurants nearest to their current location, or an inputted postal code, according to the
 * Zomato API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 21/11/2016
 */
public class NearRestosActivity extends BaseActivity {

    private static final String TAG = NearRestosActivity.class.getSimpleName();

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_activity_restos_near);
        createFragments();

        // Get location from shared prefs
        SharedPreferences sharedPreferences = getSharedPreferences(BaseActivity.SHARED_PREFS, MODE_PRIVATE);
        String latitude = sharedPreferences.getString(BaseActivity.LATITUDE, null);
        String longitude = sharedPreferences.getString(BaseActivity.LONGITUDE, null);
        displayInformation(latitude, longitude);

        ZomatoRestos zomatoRestos = new ZomatoRestos(this);
        if (latitude != null && longitude != null) {
            zomatoRestos.findNearbyRestos(latitude, longitude);
        } else {
            // TODO Somehow tell user location was not found
        }
    }

    /**
     * Displays the latitude and longitude on the screen.
     *
     * @param latitude The latitude to display on the screen
     * @param longitude The longitude to display on the screen
     */
    // FIXME This method is temporary. It is only used in phase 1.
    private void displayInformation(String latitude, String longitude) {

        Log.i(TAG, "Location found: " + latitude + ", " + longitude);
//        ((TextView) findViewById(R.id.textView)).setText(String.format(getString(R.string.latitude), latitude));
//        ((TextView) findViewById(R.id.textView2)).setText(String.format(getString(R.string.longitude), longitude));
    }

    /**
     * Inserts the NearRestoFragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NearRestoFragment fragment = new NearRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
