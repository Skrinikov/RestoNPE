package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import npe.com.restonpe.Fragments.NearRestoFragment;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.Zomato.ZomatoRestos;

/**
 * Creates an instance of the NearRestos Activity. This {@code Activity} will show the user the
 * restaurants nearest to their current location, or an inputted postal code, according to the
 * Zomatp API.
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

        RestoLocationManager restoLocationManager = new RestoLocationManager(this) {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "Location changed");
                Log.i(TAG, "New location: " + location.toString());

                displayInformation(location);
            }
        };

        Location location = restoLocationManager.getLocation();
        displayInformation(location);

        ZomatoRestos zomatoRestos = new ZomatoRestos(this);
        zomatoRestos.findRestosNear(location.getLatitude(), location.getLongitude());
    }

    /**
     * Displays the latitude and longitude on the screen.
     *
     * @param location The location information to display on the screen
     */
    // FIXME This method is temporary. It is only used in phase 1.
    private void displayInformation(Location location) {
        if (location != null) {
            Log.i(TAG, "Location found: " + location.toString());
//            ((TextView) findViewById(R.id.textView)).setText(String.format(getString(R.string.latitude), location.getLatitude()));
//            ((TextView) findViewById(R.id.textView2)).setText(String.format(getString(R.string.longitude), location.getLongitude()));
        } else {
            Log.i(TAG, "Location was not found");
//            ((TextView) findViewById(R.id.textView)).setText(getString(R.string.location_not_found));
//            ((TextView) findViewById(R.id.textView2)).setText(getString(R.string.location_not_found));
        }
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
