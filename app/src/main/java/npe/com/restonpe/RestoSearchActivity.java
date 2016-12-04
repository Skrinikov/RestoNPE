package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

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

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_activity_search_restos);
        createFragments();

        findCuisines();
    }

    public void buttonSearchClick(View v) {

        String name;
        String city;
        String[] cuisines;


//        ZomatoRestos zomatoRestos = new ZomatoRestos(this);
//        zomatoRestos.findRestos(name, city, cuisines);
    }

    private void findCuisines() {
        SharedPreferences preferences = getSharedPreferences(BaseActivity.SHARED_PREFS, MODE_PRIVATE);
        String latitude = preferences.getString(BaseActivity.LATITUDE, null);
        String longitude = preferences.getString(BaseActivity.LONGITUDE, null);

//        ZomatoRestos zomatoRestos = new ZomatoRestos(this);
//        zomatoRestos.findCuisines(latitude, longitude);
    }

    /**
     * Inserts the findresto fragment into the content view using the
     * fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RestoSearchFragment fragment = new RestoSearchFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
