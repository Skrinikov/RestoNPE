package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import npe.com.restonpe.Fragments.AddRestoFragment;

/**
 * Creates an instance of the AddRestoActivity which will
 * allow the user to add a resto to the database by entering
 * the proper needed information.
 *
 * @author Danieil Skirnikov
 * @since 30/11/2016
 * @version 1.0
 *
 */
public class AddRestoActivity extends BaseActivity {

    private static final String TAG = "AddRestoActivity";

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_activity_restos_add);
        createFragments();
    }

    /**
     * Inserts the FindRestoFragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AddRestoFragment fragment = new AddRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * Test
     *
     * @param view
     */
    public void addRestaurant(View view){
        Log.d(TAG,"addRestaurant()");

    }
}
