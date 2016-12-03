package npe.com.restonpe.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * An abstract class that handles the location services.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 25/11/2016
 */
public abstract class RestoLocationManager implements LocationListener {

    private static final String TAG = RestoLocationManager.class.getSimpleName();

    private final Context mContext;

    // The distance required to travel to update, in meters
    private static final long UPDATE_DISTANCE = 10; // 10 meters
    // The time between updates, in milliseconds
    private static final long UPDATE_TIME = 60000; // 1 minute in milliseconds

    private Location location;

    /**
     * Creates a location manager for the Resto App
     *
     * @param context The {@code Context} of the calling {@code Activity}
     */
    protected RestoLocationManager(Context context) {
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

    /**
     * Finds the location from the given name. If many locations are found with the same name,
     * {@code null} will be returned as the location was too vague.
     *
     * @param name The name to search for. Could be a city name, country, specific address,
     *             postal/zip code, etc.
     *
     * @return An address with the found location's information, or {@code null} if the results were
     * ambiguous.
     */
    public Address getLocationFromName(String name) {
        if(Geocoder.isPresent()){
            try {
                Geocoder gc = new Geocoder(mContext);
                List<Address> addresses = gc.getFromLocationName(name, 5);

                if (addresses.size() != 1) {
                    // Many addresses were returned, so the location was too vague

                    // For some reason, entering London, only returns London, England. But entering
                    // London, Ontario, returns London, Ontario
                    return null;
                } else {
                    // Get the first and only result
                    return addresses.get(0);
                }
            } catch (IOException e) {
                Log.e(TAG, "An IOException occurred while trying to get the location: " + e.getMessage());
            }
        }
        return null;
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
}
