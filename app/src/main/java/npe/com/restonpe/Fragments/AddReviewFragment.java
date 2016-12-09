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

import java.io.IOException;
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

    // Headers for HTTP request to Heroku
    private static final String REVIEW_USER_HEADER = "email";
    private static final String REVIEW_PASSWORD_HEADER = "password";
    private static final String REVIEW_RESTO_ID_HEADER = "id";
    private static final String REVIEW_TITLE_HEADER = "title";
    private static final String REVIEW_CONTENT_HEADER = "content";
    private static final String REVIEW_RATING_HEADER = "rating";

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

                    try {
                        Log.i(TAG, "Hitting " + RESTO_URL_ADD_REVIEW_HEROKU);
                        URL url = new URL(RESTO_URL_ADD_REVIEW_HEROKU);

                        if (isNetworkAccessible()) {
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            // Put review information and user email and password
                            conn.setRequestProperty(REVIEW_USER_HEADER, review.getSubmitterEmail());
                            conn.setRequestProperty(REVIEW_PASSWORD_HEADER, password);
                            conn.setRequestProperty(REVIEW_RESTO_ID_HEADER, String.valueOf(review.getRestoId()));
                            conn.setRequestProperty(REVIEW_TITLE_HEADER, review.getTitle());
                            conn.setRequestProperty(REVIEW_CONTENT_HEADER, review.getContent());
                            conn.setRequestProperty(REVIEW_RATING_HEADER, String.valueOf(review.getRating()));

                            conn.connect();

                            int response = conn.getResponseCode();
                            if (response == HttpURLConnection.HTTP_OK) {

                                conn.disconnect();
                            } else {
                                Log.e(TAG, "Something went wrong. The URL was " + url + " The HTTP response was " + response);
                            }
                        }
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "Malformed URL: " + RESTO_URL_ADD_REVIEW_HEROKU);
                    } catch (IOException e) {
                        Log.e(TAG, "An IOException occurred while reading the JSON file: " + e.getMessage());
                    }

                    return null;
                }
            };

            task.execute(review);
        }
    }

    public boolean isNetworkAccessible() {
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
