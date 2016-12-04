package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import npe.com.restonpe.Fragments.ShowRestoFragment;

/**
 * Creates an instance of the ShowResto Activity. This {@code Activity} will allow the user to
 * view details of a restaurant.
 *
 * @author Jeegna Patel
 * @since 21/11/2016
 * @version 1.0
 */
public class ShowRestoActivity extends BaseActivity {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.title_activity_show_restos);
        createFragments();
    }

    /**
     * Inserts the ShowResto fragment into the content view using the
     * fragment manager.
     */
    private void createFragments() {
        Log.d(TAG, "createFragments called");

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ShowRestoFragment fragment = new ShowRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
