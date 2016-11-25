package npe.com.restonpe;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import location.RestoLocationManager;

public class NearRestosActivity extends AppCompatActivity {

    private static final String TAG = NearRestosActivity.class.getSimpleName();

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

        displayInformation(restoLocationManager.getLocation());

        // TODO With the long/lat use Zomato API
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
}
