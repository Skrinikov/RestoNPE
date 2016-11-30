package npe.com.restonpe.Zomato;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import npe.com.restonpe.Services.RestoNetworkManager;

public class RestoZomato {

    private static final String TAG = RestoZomato.class.getSimpleName();

    private static final String RESTO_ACCEPT_HEADER = "Accept";
    private static final String RESTO_KEY_HEADER = "user-key";


    // The url to hit with placeholders for latitude and longitude
    private static final String RESTO_URL = "https://developers.zomato.com/api/v2.1/geocode?lat=%1$s&lon=%2$s";
    private static final String RESTO_ACCEPT = "application/json; charset=UTF-8";
    private static final String RESTO_KEY = "cbbc8e6762babbfaa8b3e4722ac97404";

    private RestoNetworkManager restoNetworkManager;

    public RestoZomato(Context context) {
        restoNetworkManager = new RestoNetworkManager(context);
    }

    public void getRestos(double latitude, double longitude) {

        if (restoNetworkManager.netIsUp()) {
            // Add latitude and longitude to url
            String newURL = String.format(RESTO_URL, latitude, longitude);

            try {
                URL url = new URL(newURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(RESTO_ACCEPT_HEADER, RESTO_ACCEPT);
                conn.setRequestProperty(RESTO_KEY_HEADER, RESTO_KEY);

                conn.connect();

                int response = conn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    // Get JSON reader
                    JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                    reader.beginArray();
                    while (reader.hasNext()) {
                        Log.i(TAG, reader.nextString());
                    }
                    reader.endArray();

                    reader.close();
                    conn.disconnect();
                }
            } catch (MalformedURLException e) {
                Log.w(TAG, "Malformed URL: " + newURL);
            } catch (IOException e) {
                Log.w(TAG, "An IOException occurred");
                e.printStackTrace();
            }
        }
    }
}
