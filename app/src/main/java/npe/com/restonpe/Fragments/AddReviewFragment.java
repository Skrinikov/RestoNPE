package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import npe.com.restonpe.AddReviewActivity;
import npe.com.restonpe.BaseActivity;
import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.R;
import npe.com.restonpe.SettingActivity;
import npe.com.restonpe.ShowRestoActivity;

/**
 * Fragment class that will load the content of the AddReviewActivity.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 09/12/2016
 */
public class AddReviewFragment extends Fragment {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();

    // The URL to hit to add a review to a restaurant in Heroku
    private static final String RESTO_URL_ADD_REVIEW_HEROKU = "http://shrouded-thicket-29911.herokuapp.com/api/resto/reviews/create";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String TYPE = "application/json; charset=UTF-8;";
    private static final String CONTENT_LENGTH = "Content-Length";
    // Json for HTTP POST request to Heroku
    private String jsonData = "{\"email\":\"%1$s\",\"password\":\"%2$s\",\"title\":\"%3$s\" , \"content\":\"%4$s\" , \"rating\":\"%5$s\" , \"id\":\"%6$s\"}";
    private AddReviewActivity activity;
    private SharedPreferences prefs;
    private long id;

    /**
     * Inflates a layout to be the content layout of the AddReviewActivity.
     * <p>
     * Used as reference
     * source: https://developer.android.com/guide/components/fragments.html
     *
     * @param inflater           Layout inflater needed to inflate the xml file.
     * @param container          View where the xml file will be loaded into.
     * @param savedInstanceState bundle where values are stored.
     * @return The View inflated.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");

        return inflater.inflate(R.layout.activity_add_review, container, false);
    }

    /**
     * Gets the calling activity and bundle.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        activity = (AddReviewActivity) getActivity();
        prefs = activity.getSharedPreferences(BaseActivity.SHARED_PREFS, Activity.MODE_PRIVATE);

        Bundle bundle = activity.getIntent().getExtras();

        // Get the Heroku id of the resto
        id = bundle.getLong(ShowRestoFragment.RESTO_ID);
    }

    /**
     * Adds the given review to Heroku.
     *
     * @param review The review to add to Heroku.
     */
    public void addReview(Review review) {
        // Set resto id
        String name = prefs.getString(SettingActivity.USERNAME, null);
        String email = prefs.getString(SettingActivity.EMAIL, null);
        final String password = prefs.getString(SettingActivity.PASSWORD, null);
        review.setRestoId(id);
        review.setSubmitter(name);
        review.setSubmitterEmail(email);

        if (email == null || password == null) {
            Toast.makeText(activity, R.string.add_review_invalid, Toast.LENGTH_LONG).show();
            Log.i(TAG, "Username or password is null. Username: " + name + ". Password: " + password);
        } else {
            AsyncTask<Review, Void, Void> task = new AsyncTask<Review, Void, Void>() {
                @Override
                protected Void doInBackground(Review... reviews) {
                Review review = reviews[0];
                HttpURLConnection conn = null;

                try {
                    Log.i(TAG, "Hitting " + RESTO_URL_ADD_REVIEW_HEROKU);
                    URL url = new URL(RESTO_URL_ADD_REVIEW_HEROKU);

                    if (isNetworkAccessible()) {
                        conn = (HttpURLConnection) url.openConnection();

                        // Format Json string
                        jsonData = String.format(jsonData, review.getSubmitterEmail(), password, review.getTitle(), review.getContent(), review.getRating(), review.getRestoId());
                        byte[] bytes = jsonData.getBytes("UTF-8");
                        int bytesLeng = bytes.length;

                        // Set HTTP request
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty(CONTENT_TYPE, TYPE);
                        conn.addRequestProperty(CONTENT_LENGTH, String.valueOf(bytesLeng));
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        OutputStream out = new BufferedOutputStream(conn.getOutputStream());

                        out.write(bytes);
                        out.flush();
                        out.close();

                        // Get response
                        int response = conn.getResponseCode();
                        if (response != HttpURLConnection.HTTP_OK) {
                            Log.e(TAG, "Something went wrong. The URL was " + url + " The HTTP response was " + response + " " + conn.getResponseMessage());
                        } else {
                            Log.i(TAG, "Success!");
                            activity.finish();
                        }

                        conn.disconnect();
                    }
                } catch (MalformedURLException e) {
                    Log.e(TAG, "Malformed URL: " + RESTO_URL_ADD_REVIEW_HEROKU);
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

                return null;
                }
            };

            task.execute(review);
        }
    }

    /**
     * Checks if the network is up and usable.
     *
     * @return {@code True} if the network is up and can be used, {@code False} otherwise
     */
    public boolean isNetworkAccessible() {
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
