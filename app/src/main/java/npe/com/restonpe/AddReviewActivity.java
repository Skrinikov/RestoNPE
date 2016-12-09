package npe.com.restonpe;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.Fragments.AddReviewFragment;

public class AddReviewActivity extends BaseActivity {

    private static final String TAG = AddReviewActivity.class.getSimpleName();

    private Context mContext;
    private AddReviewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        this.mContext = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_show_restos);
        }

        fragment = new AddReviewFragment();
        createFragments(fragment);
    }

    public void addReviewToHeroku(View v) {
        // Get input
        TextView tvTitle = (TextView) findViewById(R.id.add_review_title);
        TextView tvContent = (TextView) findViewById(R.id.add_review_content);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        String title = tvTitle.getText().toString();
        String content = tvContent.getText().toString();
        double rating = ratingBar.getRating();

        Review review = new Review();
        review.setTitle(title);
        review.setContent(content);
        review.setRating(rating);

        Log.i(TAG, "Adding title " + title);
        Log.i(TAG, "Adding content " + content);
        Log.i(TAG, "Adding rating " + rating);

        fragment.addReview(review);
    }
}
