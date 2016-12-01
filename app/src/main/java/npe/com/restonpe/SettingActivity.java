package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import npe.com.restonpe.Fragments.SettingFragment;

/**
 * Creates an instance of the SettingActivity which will display basic
 * saved information that are modifiable.
 *
 * @author Uen Yi Cindy Hung
 * @since 29/11/2016
 * @version 1.1
 */
public class SettingActivity extends BaseActivity {

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.action_settings);
        createFragments();
    }

    /**
     * Inserts the setting fragment into the content view using the fragment
     * manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SettingFragment fragment = new SettingFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * Inflates the setting menu using an inflater.
     *
     * @param menu The view where the xml will be infalted into.
     * @return boolean representing the success of the inflation.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    /**
     * Saves the input fields into the shared preferences.
     *
     * @param item The selected item.
     * @return boolean depicting that an item has been clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            Toast.makeText(this,R.string.setting_saved,Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
