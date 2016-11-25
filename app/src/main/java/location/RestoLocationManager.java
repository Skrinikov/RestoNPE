package location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public abstract class RestoLocationManager implements LocationListener {

    private static final String TAG = RestoLocationManager.class.getSimpleName();

    public static final int LOCATION_MANAGER_REQUEST_CODE = 1;

    private final Context mContext;

    // The distance required to travel to update, in meters
    private static final long UPDATE_DISTANCE = 10; // 10 meters

    // The time between updates, in milliseconds
    private static final long UPDATE_TIME = 60000; // 1 minute in milliseconds

    private Location location;

    public RestoLocationManager(Context context) {
        this.mContext = context;
    }

    /**
     * Gets the device's location, if permissions and device allows. If coarse location permission
     * or fine location permission is not granted, or the device does not support location services
     * this method will return {@code null}.
     *
     * @return The Location of the device, or {@code null} if the required permissions are not
     * granted or if the device does not support location services
     */
    public Location getLocation() {

        // Get location manager service
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Service.LOCATION_SERVICE);

        // Get coarse location permission granted
        boolean isCoarseLocationGranted = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.i(TAG, "Coarse location granted: " + isCoarseLocationGranted);
        // Get fine location permission granted
        boolean isFineLocationGranted = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.i(TAG, "Fine location granted: " + isFineLocationGranted);

        // If device supports location services, and user has granted permission, continue.
        if (locationManager != null && (isCoarseLocationGranted || isFineLocationGranted)) {

            // Get GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.i(TAG, "Is GPS enabled: " + isGPSEnabled);
            // Get network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.i(TAG, "Is Network Provider enabled: " + isNetworkEnabled);

            if (isNetworkEnabled) {
                Log.i(TAG, "Using Network");

                // Get location from network provider
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (isGPSEnabled) {
                Log.i(TAG, "Using GPS");

                // Get location from GPS
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            // Permission was not granted to use location services, or device does not support location services.
            return null;
        }

        return location;
    }
}