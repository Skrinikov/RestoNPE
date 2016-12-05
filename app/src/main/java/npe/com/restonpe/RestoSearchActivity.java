package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import npe.com.restonpe.Beans.Cuisine;
import npe.com.restonpe.Fragments.RestoSearchFragment;

/**
 * Creates an instance of the RestoSearch Activity. This {@code Activity} will allow the user to
 * find restaurants using the Zomato API.
 *
 * @author Jeegna Patel
 * @since 21/11/2016
 * @version 1.0
 */
public class RestoSearchActivity extends BaseActivity {

    private static final String TAG = RestoSearchActivity.class.getSimpleName();

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");

        getSupportActionBar().setTitle(R.string.title_activity_search_restos);
        createFragments();
    }

    /**
     * Inserts the FindResto fragment into the content view using the
     * fragment manager.
     */
    private void createFragments() {
        Log.i(TAG, "createFragments called");

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RestoSearchFragment fragment = new RestoSearchFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    public void buttonSearchClick(View v) {

        EditText nameEditText = (EditText) findViewById(R.id.editTextName);
        EditText cityEditText = (EditText) findViewById(R.id.editTextCity);
        Spinner cuisineSpinner = (Spinner) findViewById(R.id.cuisines_spinner);

        String name = nameEditText.getText().toString();
        String city = cityEditText.getText().toString();
        Cuisine cuisine = (Cuisine)cuisineSpinner.getSelectedItem();

        Log.i(TAG, "Searching for name: " + name);
        Log.i(TAG, "Searching for city: " + city);
        Log.i(TAG, "Searching for cuisine: " + cuisine);

        if (cuisine.getName().equals(getString(R.string.search_cuisines))) {
            // The user selected no cuisines.
        }

//        ZomatoRestos zomatoRestos = new ZomatoRestos(this);
//        zomatoRestos.findRestos(name, city, cuisine.getId());
    }
}
