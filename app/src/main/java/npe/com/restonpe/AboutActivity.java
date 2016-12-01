package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import npe.com.restonpe.Fragments.AboutFragment;

/**
 * Creates an instance of the About Activity that will display information
 * about the authors.
 *
 * @author Uen Yi Cindy Hung
 * @since 24/11/2016
 * @version 1.0
 */
public class AboutActivity extends BaseActivity {

    /**
     * Loads the layout and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.action_about);
        createFragments();
    }

    /**
     * Inserts the about fragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AboutFragment fragment = new AboutFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
