package npe.com.restonpe.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages network connections for the application, and calls to the Zomato API. This class extends
 * ASyncTask so that it may do networking on a separate thread
 *
 * @author Jeegna Patel
 * @since 28/11/2016
 * @version 1.0
 */
public class RestoNetworkManager extends AsyncTask<String, Void, List<Object>> {

    private static final String TAG = RestoNetworkManager.class.getSimpleName();

    // The URL to hit for finding nearby restaurants with placeholders for latitude and longitude.
    private static final String RESTO_NEAR_URL = "https://developers.zomato.com/api/v2.1/geocode?lat=%1$s&lon=%2$s";

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
    public RestoNetworkManager(Context context) {
        this.mContext = context;
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

    public void findRestos(ListView listView) {

    }

    /**
     * Gets the restaurants nearest the given latitude and longitude from the Zomato API
     *
     * @param latitude  The latitude from where to get the restaurants
     * @param longitude The longitude from where to get the restaurants
     * @param listView The {@code ListView} on which to display the results
     */
    public void getRestos(double latitude, double longitude, ListView listView) {

        // Add latitude and longitude to url
        String newURL = String.format(RESTO_NEAR_URL, latitude, longitude);

        Log.i(TAG, "Finding restos near: " + latitude + ", " + longitude);
        Log.i(TAG, newURL);

        execute(newURL);
    }

    /**
     * Reads the given JSON text and TODO returns a List of Restaurant objects
     *
     * @param stream The JSON input stream
     *
     * @return A list of Restaurant objects. May return an empty list if there are no results found.
     *
     * @throws IOException If an IOException occurs with the stream
     */
    private List<Object> readJson(InputStream stream) throws IOException {
        List<Object> list = new ArrayList<>();

        // Get JSON reader
        JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));

        // Start reading the text.
        reader.beginObject();
        while (reader.hasNext()) {

            // Get next token in the reader
            JsonToken token = reader.peek();

            // If token is a NAME, continue, otherwise, skip it
            if (token.name().equals(JsonToken.NAME.toString())) {
                String name = reader.nextName();

                // Find "nearby_restaurants"
                if (name.equals("nearby_restaurants")) {
                    Log.i(TAG, "Found: " + name);

                    reader.beginArray();

                    // Read all nearby restaurants
                    while (reader.hasNext()) {
                        Log.i(TAG, "Found a restaurant!");
                        Log.i(TAG, "Getting its information...");

                        // Get each restaurant from the response
                        reader.beginObject();
                        // FIXME this is just adding null to the list because I don't have the database beans yet
                        list.add(getRestaurant(reader));
                        reader.endObject();
                    }

                    reader.endArray();
                } else {
                    Log.i(TAG, "Skipping " + name);
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();
        reader.close();

        return list;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private Object getRestaurant(JsonReader reader) throws IOException {
        Object restaurant = null;

        if (reader.nextName().equals("restaurant")) {
            // restaurant object
            reader.beginObject();

            // Read all fields
            // FIXME
            for (int i = 0; i < 42; i++) {
                JsonToken token = reader.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        Log.i(TAG, "Found BEGIN_ARRAY");
                        reader.skipValue();
                        break;
                    case END_ARRAY:
                        Log.i(TAG, "Found END_ARRAY");
                        reader.skipValue();
                        break;
                    case BEGIN_OBJECT:
                        Log.i(TAG, "Found BEGIN_OBJECT");
                        reader.skipValue();
                        break;
                    case END_OBJECT:
                        Log.i(TAG, "Found END_OBJECT");
                        reader.skipValue();
                        break;
                    case NAME:
                        Log.i(TAG, "NAME : " + reader.nextName());
                        break;
                    case NUMBER:
                        Log.i(TAG, "NUMBER: " + reader.nextInt());
                        break;
                    case STRING:
                        Log.i(TAG, "STRING " + reader.nextString());
                        break;
                    default:
                        Log.w(TAG, "Something went wrong while trying to parse the JsonToken: " + token.toString());
                        reader.skipValue();
                }
            }

            reader.endObject();

        } else {
            reader.skipValue();
        }

        // TODO return database bean
        return restaurant;
    }

    /**
     * Performs this method in a separate thread
     *
     * @param urls A list of URLs to hit. There should only be one url in the list. If there are
     *             others, they will be ignored.
     * @return
     */
    @Override
    protected List<Object> doInBackground(String... urls) {
        List<Object> list = null;
        try {
            if (isNetworkAccessible()) {
                // Only use first URL, all others are ignored
                URL url = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // Set headers for HTTP request.
                conn.setRequestProperty(RESTO_ACCEPT_HEADER, RESTO_ACCEPT);
                conn.setRequestProperty(RESTO_KEY_HEADER, RESTO_KEY);

                conn.connect();

                int response = conn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "Reading JSON response");
                    list = readJson(conn.getInputStream());
                    Log.i(TAG, "JSON response read");

                    conn.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            Log.w(TAG, "Malformed URL: " + urls[0]);
        } catch (IOException e) {
            // FIXME invalid error handling
            Log.w(TAG, "Exception" + Log.getStackTraceString(e));
        }

        return list;
    }

    /**
     * Displays the results from the search on the ListView
     *
     * @param result The list of Restaurant objects
     */
    @Override
    public void onPostExecute(List<Object> result) {
        // TODO display results on ListView or AdapterView or whatever it is
    }
}
