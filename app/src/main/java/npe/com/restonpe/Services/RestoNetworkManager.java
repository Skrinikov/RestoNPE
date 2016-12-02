package npe.com.restonpe.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Manages network connections for the application. This class extends ASyncTask so that it may do
 * networking on a separate thread.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 28/11/2016
 */
public abstract class RestoNetworkManager<T> extends AsyncTask<URL, Void, List<T>> {

    private static final String TAG = RestoNetworkManager.class.getSimpleName();

    // The URL to hit for finding nearby restaurants with placeholders for latitude and longitude.
    private static final String RESTO_NEAR_URL = "https://developers.zomato.com/api/v2.1/geocode?lat=%1$s&lon=%2$s";
    // The URL to hit for restaurant information with a placeholder for the restaurant id
    private static final String RESTO_URL = "https://developers.zomato.com/api/v2.1/restaurant?res_id=%1$s";
    // TODO The URL to hit to search for restaurants with place holder for search terms
    private static final String RESTO_SEARCH_URL = "https://developers.zomato.com/api/v2.1/search?q=%1$s&count=50&lat=%2$s&lon=%3$s&radius=50&cuisines=%4$s&sort=real_distance&order=asc";

    // HTTP request constants
    private static final String RESTO_ACCEPT_HEADER = "Accept";
    private static final String RESTO_ACCEPT = "application/json; charset=UTF-8";
    private static final String RESTO_KEY_HEADER = "user-key";
    private static final String RESTO_KEY = "cbbc8e6762babbfaa8b3e4722ac97404";

    private final Context mContext;

    /**
     * Creates a network manager for the App
     *
     * @param context The {@code Context} of the calling {@code Activity}
     */
    protected RestoNetworkManager(Context context) {
        this.mContext = context;
    }

    /**
     * Performs this method in a separate thread
     *
     * @param urls A list of URLs to hit. There should only be one url in the list. If there are
     *             others, they will be ignored.
     * @return An {@code InputStream} which holds data in JSON format
     */
    @Override
    protected List<T> doInBackground(URL... urls) {
        List<T> list = null;

        try {
            if (isNetworkAccessible()) {
                // Only use first URL, all others are ignored
                URL url = urls[0];

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // Set headers for HTTP request.
                conn.setRequestProperty(RESTO_ACCEPT_HEADER, RESTO_ACCEPT);
                conn.setRequestProperty(RESTO_KEY_HEADER, RESTO_KEY);

                conn.connect();

                int response = conn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream()));

                    // This method must be implemented in the calling class. It will take care of
                    // how the JSON is parsed depending on what find... method is called
                    list = readJson(reader);

                    conn.disconnect();
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "An IOException occurred while reading the JSON file: " + e.getMessage());
        }

        return list;
    }

    /**
     * Gets the restaurants nearest the given latitude and longitude from the Zomato API
     *
     * @param latitude  The latitude from where to get the restaurants
     * @param longitude The longitude from where to get the restaurants
     */
    public void findNearbyRestos(double latitude, double longitude) {
        // Add latitude and longitude to url
        String updatedURL = String.format(RESTO_NEAR_URL, latitude, longitude);

        try {
            URL url = new URL(updatedURL);

            Log.i(TAG, "Hitting " + updatedURL);

            execute(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + updatedURL);
        }
    }

    /**
     * Gets the restaurant with the given id from the Zomato API
     *
     * @param id The id of the restaurant to get
     */
    public void findRestoInformation(int id) {
        // Add id to url
        String updatedURL = String.format(RESTO_URL, id);

        try {
            URL url = new URL(updatedURL);

            Log.i(TAG, "Hitting " + updatedURL);

            execute(url);
        } catch (MalformedURLException e) {
            Log.w(TAG, "Malformed URL: " + updatedURL);
        }
    }

    /**
     * Finds the restaurants that matches the given search terms
     *
     * @param name The name of the restaurant for which to search
     * @param genre The type of cuisine for which to search
     * @param latitude The latitude of the area of which to search
     * @param latitude The longitude of the area of which to search
     */
    public void findRestos(String name, String latitude, String longitude, String genre) {
        // Add search terms to url
        String updatedURL = String.format(RESTO_SEARCH_URL, name, latitude, longitude, genre);

        try {
            URL url = new URL(updatedURL);

            Log.i(TAG, "Hitting " + updatedURL);

            execute(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + updatedURL);
        }
    }

    /**
     * Checks whether the network can be accessed
     *
     * @return {@code True} if the network is running and can be accessed, {@code False} otherwise.
     */
    private boolean isNetworkAccessible() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * This method should be used to parse the data
     *
     * @param list A list populated with data beans from the JSON response
     */
    @Override
    public abstract void onPostExecute(List<T> list);

    protected abstract List<T> readJson(JsonReader reader);
}
