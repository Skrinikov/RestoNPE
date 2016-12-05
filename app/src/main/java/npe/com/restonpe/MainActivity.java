package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

import npe.com.restonpe.Fragments.IndexFragment;

/**
 * Creates an instance of the Main Activity which will display
 * the main actions/activities of the application.
 *
 * Checks if the app was launched for the first time. If it is launches a login activity.
 *
 * @author Uen Yi Cindy Hung
 * @author Danieil Skrinikov
 * @since 24/11/2016
 * @version 1.0
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForFirstLogin();
	    createFragments();
    }

    /**
     * Checks if the application contains any shared preferences. If it does, continues with the
     * normal execution of the app. Else launches the login activity.
     */
    private void checkForFirstLogin() {
        Log.i(TAG,"checkForFirstLogin");
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        if(!preferences.contains("emailAdr") || !preferences.contains("username")){
            Log.d(TAG,"Prefs to not exist");
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /**
     * Inserts the index fragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        IndexFragment fragment = new IndexFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
