package npe.com.restonpe;

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

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_show_restos);
        }

        ShowReviewFragment fragment = new ShowReviewFragment();
        createFragments(fragment);
    }
}
