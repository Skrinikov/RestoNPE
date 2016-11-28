package npe.com.restonpe.Zomato;

import android.content.Context;
import android.util.Log;

import npe.com.restonpe.Services.RestoNetworkManager;

public class RestoZomato {

    private static final String TAG = RestoZomato.class.getSimpleName();

    // The url to hit with placeholders for latitude and longitude
    private static final String RESTO_URL = "https://developers.zomato.com/api/v2.1/geocode?lat=%1$s&lon=%2$s";

    private RestoNetworkManager restoNetworkManager;

    public RestoZomato(Context context) {
        restoNetworkManager = new RestoNetworkManager(context);
    }

    public void getRestos(double latitude, double longitude) {

        if (restoNetworkManager.netIsUp()) {
            // Add latitude and longitude to url
            String newURL = String.format(RESTO_URL, latitude, longitude);

            Log.i(TAG, "Finding restos near: " + latitude + ", " + longitude);
            Log.i(TAG, newURL);
            restoNetworkManager.execute(newURL);
        }
    }
}
