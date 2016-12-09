package npe.com.restonpe.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import npe.com.restonpe.Beans.Cuisine;

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
    // The URL to hit to search for all cuisines in a given city's latitude and longitude
    private static final String RESTO_CUISINE_URL = "https://developers.zomato.com/api/v2.1/cuisines?lat=%1$s&lon=%2$s";
    // The URL to hit to search for restaurants with place holder for search terms
    private static final String RESTO_SEARCH_URL = "https://developers.zomato.com/api/v2.1/search?%1$s%2$s%3$scount=50&radius=50&sort=real_distance&order=asc";
    // The URL to hit to find specific information on a single restaurant with a placeholder for the restaurant's id
    private static final String RESTO_URL = "https://developers.zomato.com/api/v2.1/restaurant?res_id=%1$s";

    // The URL to hit for finding nearby restaurants from Heroku with placeholders for latitude and longitude.
    private static final String RESTO_NEAR_URL_HEROKU = "http://shrouded-thicket-29911.herokuapp.com/api/restos?lat=%1$s&long=%2$s";
    // The URL to hit for finding reviews from Heroku with a placeholder for the restaurant id
    private static final String RESTO_REVIEW_URL_HEROKU = "http://shrouded-thicket-29911.herokuapp.com/api/resto/reviews?id=%1$s";
    // The URL to hit to find specific information on a single restaurant with a placeholder for the restaurant's id
    private static final String RESTO_URL_HEROKU = "http://shrouded-thicket-29911.herokuapp.com/api/resto/details?id=%1$s";

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
     * @return A {@code List} which holds the specified data bean, or {@code null} if no data was
     * found from the request.
     */
    @Override
    protected List<T> doInBackground(URL... urls) {
        List<T> list = null;

        try {
            if (isNetworkAccessible()) {
                URL url = urls[0];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set headers for Zomato HTTP request.
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
                } else {
                    Log.e(TAG, "Something went wrong. The URL was " + url + " The HTTP response was " + response);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "An IOException occurred while reading the JSON file: " + e.getMessage());
        }

        return list;
    }

    /**
     * A convenience method that gets the restaurants nearest the given latitude and longitude from
     * the Zomato API
     *
     * @param latitude  The latitude from where to get the restaurants
     * @param longitude The longitude from where to get the restaurants
     */
    public void findNearbyRestos(String latitude, String longitude) {
        // Add latitude and longitude to url
        String zomatoURL = String.format(RESTO_NEAR_URL, latitude, longitude);

        try {
            URL zomato = new URL(zomatoURL);

            execute(zomato);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + zomatoURL);
        }
    }

    /**
     * A convenience method that gets the restaurants nearest the given latitude and longitude from
     * the Heroku API
     *
     * @param latitude  The latitude from where to get the restaurants
     * @param longitude The longitude from where to get the restaurants
     */
    public void findNearbyRestosFromHeroku(String latitude, String longitude) {
        // Add latitude and longitude to url
        String herokuURL = String.format(RESTO_NEAR_URL_HEROKU, latitude, longitude);

        try {
            URL heroku = new URL(herokuURL);

            Log.i(TAG, "Hitting " + herokuURL);

            execute(heroku);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + herokuURL);
        }
    }

    /**
     * A convenience method for finding specific information on the restaurant with the given id
     * from Heroku
     *
     * @param id The id of the restaurant whose information is to be found
     */
    public void findRestoInformationFromHeroku(long id) {
        // Add latitude and longitude to url
        String herokuURL = String.format(RESTO_URL_HEROKU, id);

        try {
            URL heroku = new URL(herokuURL);

            Log.i(TAG, "Hitting " + herokuURL);

            execute(heroku);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + herokuURL);
        }
    }

    /**
     * A convenience method for finding the restaurants that match the given search terms
     *
     * @param name      The name of the restaurant for which to search
     * @param latitude  The latitude of the area of which to search
     * @param longitude The longitude of the area of which to search
     * @param cuisine   A {@code Cuisine} object with the id of the cuisine for which to search
     */
    public void findRestos(String name, String latitude, String longitude, Cuisine cuisine) {
        String queryURL = "q=%1$s&";
        String latlonURL = "lat=%1$s&lon=%2$s&";
        String cuisineURL = "cuisines=%1$s&";

        // Update all GET urls with the appropriate value. If the given parameters are null, remove the GET completely
        if (name != null) {
            // Make search term valid for URLs. In other words, replace spaces with "%20".
            queryURL = String.format(queryURL, Uri.encode(name));
        } else {
            queryURL = "";
        }
        if (latitude != null && longitude != null) {
            latlonURL = String.format(latlonURL, latitude, longitude);
        } else {
            latlonURL = "";
        }
        if (cuisine != null) {
            cuisineURL = String.format(cuisineURL, cuisine.getId());
        } else {
            cuisineURL = "";
        }

        // Add search terms to url, or blanks if the parameter for it was null
        String updatedURL = String.format(RESTO_SEARCH_URL, queryURL, latlonURL, cuisineURL);

        try {
            URL url = new URL(updatedURL);

            Log.i(TAG, "Hitting " + updatedURL);

            execute(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + updatedURL);
        }
    }

    /**
     * A convenience method for finding specific information on the restaurant with the given id
     *
     * @param id The id of the restaurant whose information is to be found
     */
    public void findRestoInformation(long id) {
        // Add id to url
        String updatedURL = String.format(RESTO_URL, id);

        try {
            URL url = new URL(updatedURL);

            Log.i(TAG, "Hitting " + updatedURL);

            execute(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + updatedURL);
        }
    }

    /**
     * A convenience method for finding reviews from Heroku of the restaurant with the given id
     *
     * @param id The id of the restaurant whose information is to be found
     */
    public void findReviews(int id) {
        // Add id to url
        String updatedURL = String.format(RESTO_REVIEW_URL_HEROKU, id);

        try {
            URL url = new URL(updatedURL);

            Log.i(TAG, "Hitting " + updatedURL);

            execute(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + updatedURL);
        }
    }

    /**
     * A convenience method that finds all cuisines in the city in which the given latitude and
     * longitude fall
     *
     * @param latitude  Any latitudinal point in a city
     * @param longitude Any longitudinal point in the same city
     */
    public void findCuisines(String latitude, String longitude) {
        // Add latitude and longitude to url
        String updatedURL = String.format(RESTO_CUISINE_URL, latitude, longitude);

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
     * Displays the given list on the UI
     *
     * @param list A list populated with data beans from the JSON response
     */
    @Override
    public abstract void onPostExecute(List<T> list);

    /**
     * Parses the JSON response from Zomato
     *
     * @param reader The {@code JsonReader} with the JSON response
     * @return A {@code List} populated with data beans from the JSON response
     */
    protected abstract List<T> readJson(JsonReader reader);
}
