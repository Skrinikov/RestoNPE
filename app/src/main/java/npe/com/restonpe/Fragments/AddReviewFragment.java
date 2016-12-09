package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import npe.com.restonpe.AddReviewActivity;
import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.R;
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

    private AddReviewActivity activity;
    private long id;

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

        return inflater.inflate(R.layout.activity_add_review, container, false);
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
        activity = (AddReviewActivity) getActivity();

        Bundle bundle = activity.getIntent().getExtras();

        // Get the heroku id of the resto
        id = bundle.getLong(ShowRestoFragment.RESTO_ID);
    }

    public void addReview(Review review) {
        review.setRestoId(id);
    }
}
