package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import npe.com.restonpe.Fragments.AboutFragment;
import npe.com.restonpe.Fragments.TipFragment;

/**
 * Creates an instance of the TipActivity which allows the user
 * to input some values (subtotal,tip percentage, number of people to split with)
 * in order to get the computed grand total, and the amounts to pay per person.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.0
 * @since 24/11/2016
 */

public class TipActivity extends BaseActivity {

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.tip_calculator);
        createFragments();
    }

    /**
     * Inserts the tip fragment into the content view using the fragment
     * manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TipFragment fragment = new TipFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
