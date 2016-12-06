package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import npe.com.restonpe.Beans.Cuisine;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Fragments.FindRestoFragment;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.util.RestoAdapter;

/**
 * Creates an instance of the RestoSearch Activity. This {@code Activity} will allow the user to
 * find restaurants using the Zomato API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 21/11/2016
 */
public class FindRestoActivity extends BaseActivity {

    private static final String TAG = FindRestoActivity.class.getSimpleName();

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_search_restos);
        }
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
        FindRestoFragment fragment = new FindRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    public void buttonSearchClick(View v) {

        EditText nameEditText = (EditText) findViewById(R.id.editTextName);
        EditText cityEditText = (EditText) findViewById(R.id.editTextCity);
        Spinner cuisineSpinner = (Spinner) findViewById(R.id.cuisines_spinner);

        String name = nameEditText.getText().toString();
        String city = cityEditText.getText().toString();
        Cuisine cuisine = (Cuisine) cuisineSpinner.getSelectedItem();

        if (name.isEmpty()) {
            name = null;
        }
        if (city.isEmpty()) {
            city = null;
        }
        if (cuisine != null && cuisine.getName().equals(getString(R.string.search_cuisines))) {
            // The user selected default cuisine, and therefore does not want to search with cuisine
            cuisine = null;
        }

        // This location is used to determine the distance between the user and the restaurant
        SharedPreferences sharedPreferences = getSharedPreferences(BaseActivity.SHARED_PREFS, MODE_PRIVATE);
        final String latitude = sharedPreferences.getString(BaseActivity.LATITUDE, null);
        final String longitude = sharedPreferences.getString(BaseActivity.LONGITUDE, null);
        final Context context = this;

        ZomatoRestos zomatoRestos = new ZomatoRestos(this) {
            @Override
            public void handleResults(List<?> list) {
                if (list != null && list.size() > 0) {
                    List<RestoItem> restos = (List<RestoItem>) list;
                    ListView listView = (ListView) findViewById(R.id.find_list);

                    RestoAdapter adapter = new RestoAdapter(context, restos, longitude, latitude, true);
                    listView.setAdapter(adapter);
                } else {
                    // Tell user there were no results
                    new AlertDialog.Builder(context)
                            .setMessage(getString(R.string.no_result))
                            .show();
                }
            }
        };
        zomatoRestos.findRestos(name, city, cuisine);
    }
}
