package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import npe.com.restonpe.Fragments.IndexFragment;

/**
 * Creates an instance of the Main Activity
 *
 * @author Uen Yi Cindy Hung
 * @since 24/11/2016
 * @version 1.0
 */
public class MainActivity extends BaseActivity {

    /**
     * Loads the layout.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	createFragments();
    }
    
    /**
     * Inserts the index fragment into the content view.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        IndexFragment fragment = new IndexFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
