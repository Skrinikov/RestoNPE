package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import npe.com.restonpe.Fragments.NearRestoFragment;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.Zomato.RestoZomato;

public class NearRestosActivity extends BaseActivity {

    private static final String TAG = NearRestosActivity.class.getSimpleName();

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

            @Override
            public void onProviderDisabled(String provider) {
                Log.i(TAG, provider + " was disabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(TAG, provider + " was enabled");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(TAG, "Status of " + provider + " changed to " + status);
            }
        };

        Location location = restoLocationManager.getLocation();
        displayInformation(location);

        // TODO With the long/lat use Zomato API
        RestoZomato restoZomato = new RestoZomato(this);
        restoZomato.getRestos(location.getLatitude(), location.getLongitude());
    }

    private void displayInformation(Location location) {
        if (location != null) {
            Log.i(TAG, location.getLatitude() + " " + location.getLongitude());

            ((TextView)findViewById(R.id.textView)).setText("Latitude " + location.getLatitude());
            ((TextView)findViewById(R.id.textView2)).setText("Longitude " + location.getLongitude());
        } else {
            ((TextView)findViewById(R.id.textView)).setText("Location not found");
            ((TextView)findViewById(R.id.textView2)).setText("Location not found");
            Log.i(TAG, "Location was not found");
        }
    }

    /**
     * Inserts the nearresto fragment into the content view.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NearRestoFragment fragment = new NearRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
