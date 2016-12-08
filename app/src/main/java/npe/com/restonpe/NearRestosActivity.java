package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;

import npe.com.restonpe.Fragments.NearRestoFragment;

/**
 * Creates an instance of the NearRestos Activity. This {@code Activity} will show the user the
 * restaurants nearest to their current location, or an inputted postal code, according to the
 * Zomato API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 21/11/2016
 */
public class NearRestosActivity extends BaseActivity {

    private static final String TAG = NearRestosActivity.class.getSimpleName();
    private NearRestoFragment fragment;

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_restos_near);
        }
        createFragments();
    }

    /**
     * Inserts the NearRestoFragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        Log.i(TAG, "createFragments called");
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new NearRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Cancel network request
        Log.i(TAG, "Cancelling call to network");
        fragment.cancel(true);
    }
}
