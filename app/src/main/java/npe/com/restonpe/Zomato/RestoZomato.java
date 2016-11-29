package npe.com.restonpe.Zomato;

import android.content.Context;
import android.util.Log;

import npe.com.restonpe.Services.RestoNetworkManager;

/**
 * Handles any calls to the Zomato API
 *
 * @author Jeegna Patel
 * @since 28/11/2016
 * @version 1.0
 */
public class RestoZomato {

    private static final String TAG = RestoZomato.class.getSimpleName();

    // The URL to hit with placeholders for latitude and longitude
    private static final String RESTO_URL = "https://developers.zomato.com/api/v2.1/geocode?lat=%1$s&lon=%2$s";

    private RestoNetworkManager restoNetworkManager;

    /**
     * Creates a {@code RestoZomato} class which can be used to access the Zomato API
     *
     * @param context The {@code Context} of the calling {@code Activity}
     */
    public RestoZomato(Context context) {
        restoNetworkManager = new RestoNetworkManager(context);
    }

    /**
     * Gets the restaurants nearest the given latitude and longitude from the Zomato API
     *
     * @param latitude  The latitude from where to get the restaurants
     * @param longitude The longitude from where to get the restaurants
     */
    public void getRestos(double latitude, double longitude) {

        // Add latitude and longitude to url
        String newURL = String.format(RESTO_URL, latitude, longitude);

        Log.i(TAG, "Finding restos near: " + latitude + ", " + longitude);
        Log.i(TAG, newURL);
        restoNetworkManager.execute(newURL);
    }
}
