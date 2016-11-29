package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import npe.com.restonpe.Fragments.SettingFragment;

/**
 * Creates an instance of the Setting Activity
 *
 * @author Uen Yi Cindy Hung
 * @since 24/11/2016
 * @version 1.0
 */
public class SettingActivity extends BaseActivity {

    /**
     * Loads the layout and changes the action bar's title.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.action_settings);
        createFragments();
    }

    /**
     * Inserts the setting fragment into the content view.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SettingFragment fragment = new SettingFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

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
