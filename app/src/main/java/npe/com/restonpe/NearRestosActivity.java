package npe.com.restonpe;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.Zomato.RestoZomato;

/**
 * Creates an instance of the NearRestos Activity. This {@code Activity} will show the user the
 * restaurants nearest to their current location, or an inputted postal code, according to the
 * Zomatp API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 21/11/2016
 */
public class NearRestosActivity extends AppCompatActivity {

    private static final String TAG = NearRestosActivity.class.getSimpleName();

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_restos);

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

        RestoZomato restoZomato = new RestoZomato(this);
        // TODO return something
        restoZomato.getRestos(location.getLatitude(), location.getLongitude());
    }

    /**
     * Displays the latitude and longitude on the screen.
     *
     * @param location The location information to display on the screen
     */
    // FIXME This method is temporary. It is only used in phase 1.
    private void displayInformation(Location location) {
        if (location != null) {
            Log.i(TAG, location.getLatitude() + " " + location.getLongitude());

            ((TextView) findViewById(R.id.textView)).setText("Latitude " + location.getLatitude());
            ((TextView) findViewById(R.id.textView2)).setText("Longitude " + location.getLongitude());
        } else {
            ((TextView) findViewById(R.id.textView)).setText("Location not found");
            ((TextView) findViewById(R.id.textView2)).setText("Location not found");
            Log.i(TAG, "Location was not found");
        }
    }
}
