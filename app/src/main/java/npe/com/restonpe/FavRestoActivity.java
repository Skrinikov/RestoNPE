package npe.com.restonpe;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import npe.com.restonpe.Fragments.FavRestoFragment;

/**
 * Creates an instance of FavRestoActivity which will
 * display in a list format, all the restaurant which have
 * been favorited by the user.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.0
 * @since 30/11/2016
 */
public class FavRestoActivity extends BaseActivity {
    private FavRestoFragment fragment;

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

        fragment = new FavRestoFragment();
        super.createFragments(fragment);
    }

    public void updateDbList() {
        fragment.getDbRestoList();
    }
}
