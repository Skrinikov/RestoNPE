package npe.com.restonpe.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages network connections for the application. This class extends ASyncTask so that it may do
 * networking on a separate thread
 *
 * @author Jeegna Patel
 * @since 28/11/2016
 * @version 1.0
 */
public class RestoNetworkManager extends AsyncTask<String, Void, String> {

    private static final String TAG = RestoNetworkManager.class.getSimpleName();

    private static final String RESTO_ACCEPT_HEADER = "Accept";
    private static final String RESTO_ACCEPT = "application/json; charset=UTF-8";
    private static final String RESTO_KEY_HEADER = "user-key";
    private static final String RESTO_KEY = "cbbc8e6762babbfaa8b3e4722ac97404";

    private final Context mContext;

    /**
     * Creates a network manager for the Resto App
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

    /**
     * Performs this method in a separate thread
     *
     * @param urls A list of URLs to hit. There should only be one url in the list. If there are
     *             others, they will be ignored
     * @return
     */
    @Override
    protected String doInBackground(String... urls) {
        try {
            if (isNetworkAccessible()) {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(RESTO_ACCEPT_HEADER, RESTO_ACCEPT);
                conn.setRequestProperty(RESTO_KEY_HEADER, RESTO_KEY);
                conn.setDoInput(true);

                conn.connect();

                int response = conn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {

                    Log.i(TAG, "Reading JSON response");
                    readJSON(conn.getInputStream());
                    Log.i(TAG, "JSON response read");

                    conn.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            Log.w(TAG, "Malformed URL: " + urls[0]);
        } catch (IOException e) {
            Log.e(TAG, "exception" + Log.getStackTraceString(e));
            return null;
        }

        return null;
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
    private List<Object> readJSON(InputStream stream) throws IOException {

        List<Object> list = new ArrayList<>();

        // Get JSON reader
        JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));

        // Start reading the text.
        reader.beginObject();
        while (reader.hasNext()) {

            // Get next token
            JsonToken token = reader.peek();

            // If token is a NAME, continue, otherwise, skip it
            if (token.name().equals(JsonToken.NAME.toString())) {
                String name = reader.nextName();
                Log.i(TAG, "NAME: " + name);

                if (name.equals("nearby_restaurants")) {
                    // Get each restaurant from the response
                    list.add(getRestaurant(reader));
                } else {
                    Log.i(TAG, "Skipping...");
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

    private Object getRestaurant(JsonReader reader) throws IOException {
        // FIXME Does not work
        // Handle nearby restaurants array
        reader.beginArray();

        // Begin restaurant object
        reader.beginObject();

        // restaurant
        reader.nextName();
        // R
        reader.beginObject();
        reader.nextName();

        // TODO return database bean
        return null;
    }
}
