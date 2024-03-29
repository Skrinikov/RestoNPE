package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.R;
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

        long id = bundle.getLong(ReviewAdapter.ID, -1);
        String title = bundle.getString(ReviewAdapter.TITLE, "");
        String content = bundle.getString(ReviewAdapter.CONTENT, "");
        double rating = bundle.getDouble(ReviewAdapter.RATING, 0);
        String submitter = bundle.getString(ReviewAdapter.SUBMITTER, "");
        String submitterEmail = bundle.getString(ReviewAdapter.SUBMITTER_EMAIL, "");
        int likes = bundle.getInt(ReviewAdapter.LIKES, 0);
        long restoId = bundle.getLong(ReviewAdapter.RESTO_ID, -1);

        Review review = new Review(id, title, content, rating, submitter, submitterEmail, likes, restoId);
        displayInformation(review);
    }

    /**
     * Displays the restaurants information on the screen.
     *
     * @param review The {@code Review} whose information is to be displayed on the screen
     */
    private void displayInformation(Review review) {
        String title = review.getTitle();
        String content = review.getContent();
        String submitter = review.getSubmitter();
        String submitterEmail = review.getSubmitterEmail();
        int likes = review.getLikes();
        double rating = review.getRating();

        TextView tvTitle = (TextView)activity.findViewById(R.id.textViewReviewTitle);
        TextView tvContent = (TextView)activity.findViewById(R.id.textViewReviewContent);
        TextView tvSubmitter = (TextView)activity.findViewById(R.id.textViewReviewSubmitter);
        TextView tvSubmitterEmail = (TextView)activity.findViewById(R.id.textViewReviewSubmitterEmail);
        TextView tvLikes = (TextView)activity.findViewById(R.id.textViewReviewLikes);
        RatingBar tvRating = (RatingBar)activity.findViewById(R.id.ratingBarReviewRating);

        tvTitle.setText(title);
        tvContent.setText(content);
        if (submitter.isEmpty()) {
            tvSubmitter.setText("");
        } else {
            tvSubmitter.setText(String.format(activity.getString(R.string.review_submitter), submitter));
        }
        tvSubmitterEmail.setText(submitterEmail);
        tvLikes.setText(String.format(activity.getString(R.string.review_likes), likes));
        tvRating.setRating((float)rating);
    }
}
