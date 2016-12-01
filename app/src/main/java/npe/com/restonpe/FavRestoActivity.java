package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import npe.com.restonpe.Fragments.FavRestoFragment;

public class FavRestoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_activity_restos_fav);
        createFragments();
    }

    /**
     * Inserts the findresto fragment into the content view.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FavRestoFragment fragment = new FavRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
