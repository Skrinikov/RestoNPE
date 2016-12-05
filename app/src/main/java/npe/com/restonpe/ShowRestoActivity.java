package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import npe.com.restonpe.Fragments.ShowRestoFragment;

/**
 * Creates an instance of the ShowResto Activity. This {@code Activity} will allow the user to
 * view details of a restaurant.
 *
 * @author Jeegna Patel
 * @since 04/12/2016
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

    /**
     * Inflates the show resto menu using an inflater.
     *
     * @param menu The view where the xml will be inflated into.
     * @return boolean representing the success of the inflation.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.resto_detail_menu, menu);
        return true;
    }

    /**
     * Starts the add resto activity.
     *
     * @param item The selected item.
     * @return boolean depicting that an item has been clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected called");
        int id = item.getItemId();

        if (id == R.id.add_resto) {
            Intent intent = new Intent(this,AddRestoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
