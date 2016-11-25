package npe.com.restonpe;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import location.RestoLocationManager;

public class NearRestosActivity extends AppCompatActivity {

    private static final String TAG = NearRestosActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_restos);

        // This needs to go in MainActivity, so user can submit postal code if location is  not found
        RestoLocationManager restoLocationManager = new RestoLocationManager(this);
        Location location = restoLocationManager.getLocation();
        if (location != null) {
            Log.i(TAG, location.getLatitude() + " " + location.getLongitude());
        } else {
            Log.i(TAG, "Location was not found");
        }

        // TODO With the long/lat use Zomato API

    }
}