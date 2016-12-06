package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import npe.com.restonpe.Fragments.FavRestoFragment;

/**
 * Creates an instance of FavRestoActivity which will
 * display in a list format, all the restaurant which have
 * been favorited by the user.
 *
 * @author
 * @since 30/11/2016
 * @version 1.0
 */
public class FavRestoActivity extends BaseActivity {

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_restos_fav);
        }
        createFragments();
    }

    /**
     * Inserts the FavRestoFragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FavRestoFragment fragment = new FavRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
