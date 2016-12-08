package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;

import npe.com.restonpe.Fragments.ShowReviewFragment;

/**
 * Creates an instance of the ShowReviewActivity which will display the information of the selected
 * Review.
 *
 * @author Jeegna Patel
 * @since 07/12/2016
 * @version 1.0
 */
public class ShowReviewActivity extends BaseActivity {

    private static final String TAG = ShowReviewActivity.class.getSimpleName();

    private Context mContext;

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        this.mContext = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_show_restos);
        }
        createFragments();
    }

    /**
     * Inserts the ShowReview fragment into the content view using the
     * fragment manager.
     */
    private void createFragments() {
        Log.d(TAG, "createFragments called");

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ShowReviewFragment fragment = new ShowReviewFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
