package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.Heroku.HerokuRestos;
import npe.com.restonpe.R;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.ShowRestoActivity;
import npe.com.restonpe.util.ReviewAdapter;

/**
 * Fragment class that will load the content of the ShowReviewActivity.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 07/12/2016
 */
public class ShowReviewFragment extends Fragment {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();

    private Activity activity;

    /**
     * Inflates a layout to be the content layout of the ShowRestoActivity.
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

        return inflater.inflate(R.layout.activity_show_review, container, false);
    }

    /**
     * Gets the restaurant's information and displays it on the screen.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        activity = getActivity();

        Bundle bundle = activity.getIntent().getExtras();

        int id = bundle.getInt(ReviewAdapter.ID);

        // Get nearby restaurants
        getReview(id);
    }

    /**
     * Gets the information of the restaurant with the given id.
     *
     * @param id The id of the restaurant whose information is to be retrieved.
     */
    private void getReview(int id) {
        RestoNetworkManager<Review> restoNetworkManager = new RestoNetworkManager<Review>(activity) {
            @Override
            public void onPostExecute(List<Review> list) {
                if (list.size() == 1) {
                    displayInformation(list.get(0));
                }
            }

            @Override
            protected List<Review> readJson(JsonReader reader) {
                Log.i(TAG, "Reading Json response...");

                try {
                    HerokuRestos herokuRestos = new HerokuRestos(activity);
                    return herokuRestos.readReviewJson(reader);
                } catch (IOException e) {
                    Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                }
                return null;
            }
        };

        restoNetworkManager.findRestoInformation(id);
    }

    /**
     * Displays the restaurants information on the screen.
     *
     * @param review The {@code Review} whose information is to be displayed on the screen
     */
    private void displayInformation(Review review) {

    }
}
