package npe.com.restonpe.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestoNetworkManager extends AsyncTask<String, Void, String> {

    private static final String TAG = RestoNetworkManager.class.getSimpleName();

    private static final String RESTO_ACCEPT_HEADER = "Accept";
    private static final String RESTO_ACCEPT = "application/json; charset=UTF-8";
    private static final String RESTO_KEY_HEADER = "user-key";
    private static final String RESTO_KEY = "cbbc8e6762babbfaa8b3e4722ac97404";

    private final Context mContext;

    public RestoNetworkManager(Context context) {
        this.mContext = context;
    }

    public boolean netIsUp() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty(RESTO_ACCEPT_HEADER, RESTO_ACCEPT);
            conn.setRequestProperty(RESTO_KEY_HEADER, RESTO_KEY);
            conn.setDoInput(true);

            conn.connect();

            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {

                // Get JSON reader
                JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                Log.i(TAG, "Reading JSON response");
                readJSON(reader);
                Log.i(TAG, "JSON response read");

                reader.close();
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            Log.w(TAG, "Malformed URL: " + urls[0]);
        } catch (IOException e) {
            Log.e(TAG, "exception" + Log.getStackTraceString(e));
            return null;
        }

        return null;
    }

    private void readJSON(JsonReader reader) throws  IOException {

        reader.beginObject();

        while (reader.hasNext()) {

            // Get next token
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {
                String name = reader.nextName();
                Log.i(TAG, "NAME: " + name);

                if (name.equals("nearby_restaurants")) {
                    // Get each restaurant from the response
                    getRestaurant(reader);
                } else {
                    Log.i(TAG, "Skipping...");
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();
    }

    private void getRestaurant(JsonReader reader) throws IOException {
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
    }
}
