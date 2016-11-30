package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import npe.com.restonpe.Fragments.AboutFragment;
import npe.com.restonpe.Fragments.TipFragment;

/**
 * Creates an instance of the About Activity
 *
 * @author Uen Yi Cindy Hung
 * @version 1.0
 * @since 24/11/2016
 */

public class TipActivity extends BaseActivity {

    /**
     * Loads the layout and changes the action bar's title.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.tip_calculator);
        createFragments();
    }

    /**
     * Inserts the tip fragment into the content view.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TipFragment fragment = new TipFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
