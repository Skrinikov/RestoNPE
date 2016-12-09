package npe.com.restonpe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import npe.com.restonpe.Fragments.SettingFragment;

/**
 * Creates an instance of the SettingActivity which will display basic
 * saved information that are modifiable.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.1
 * @since 29/11/2016
 */
public class SettingActivity extends BaseActivity {
    private TextView username, emailAdr, password;
    private SettingFragment fragment;
    private final String TAG = "SettingActivity";
    private SharedPreferences pref;

    public static final String USERNAME = "username";
    public static final String EMAIL = "emailAdr";
    public static final String PASSWORD = "password";

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.action_settings);
        }

        fragment = new SettingFragment();
        createFragments(fragment);
    }

    /**
     * When the Activity has started, the fragment will have been loaded, so
     * get a handle on the TextViews to set the saved SharedPreferences into them,
     * if there is one.
     * <p>
     * <p>
     * Used as reference
     * source: http://stackoverflow.com/questions/24188050/how-to-access-fragments-child-views-inside-fragments-parent-activity
     */
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart called");
        Log.d(TAG, "fragment is: " + fragment);
        super.onStart();

        username = (TextView) fragment.getView().findViewById(R.id.username_input);
        emailAdr = (TextView) fragment.getView().findViewById(R.id.email_adr_input);
        password = (TextView) fragment.getView().findViewById(R.id.password_input);
        pref = getSharedPreferences("Settings", MODE_PRIVATE);

        if (pref != null) {
            username.setText(pref.getString(USERNAME, null));
            emailAdr.setText(pref.getString(EMAIL, null));
            password.setText(pref.getString(PASSWORD, null));
        }

        Log.d(TAG, "TextViews are: " + username + "\t" + emailAdr + "\t" + password);
    }

    /**
     * Inflates the setting menu using an inflater.
     *
     * @param menu The view where the xml will be inflated into.
     * @return boolean representing the success of the inflation.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called");
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
        Log.d(TAG, "onOptionsItemSelected called");
        int id = item.getItemId();

        if (id == R.id.save) {
            saveInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieves the fields' data and stored them in the shared preferences.
     */
    private void saveInfo() {
        Log.d(TAG, "saveInfo called");
        SharedPreferences.Editor editor = pref.edit();

        if (username.getText().length() > 0 && emailAdr.getText().length() > 0 && password.getText().length() > 0) {
            Log.d(TAG, "Fields are: " + username.getText() + "\t" + emailAdr.getText() + "\t" + password.getText());
            Log.d(TAG, "Fields contain: " + username.getText().toString() + "\t" + emailAdr.getText().toString() + "\t" + password.getText().toString());

            if (username.getText().toString().trim().length() > 0 && emailAdr.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
                editor.putString(USERNAME, username.getText().toString().trim());
                editor.putString(EMAIL, emailAdr.getText().toString().trim());
                editor.putString(PASSWORD, password.getText().toString().trim());

                editor.apply();
                Toast.makeText(this, R.string.setting_saved, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.incomplete, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.incomplete, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Overridden onBackPressed which verifies for any unsaved changes.
     * <p>
     * If there are unsaved changes, ask the user if they want to discard the
     * changes before finish() the activity or stay.
     * <p>
     * If there is no unsaved changes, calls super's onBackPressed.
     * <p>
     * Used as reference
     * source: http://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed called" + pref.toString());
        boolean dataSaved = true;

        // Check if there the text fields are the same as in the prefs in there is one.
        if (pref != null) {
            Log.d(TAG, "onBackPressed: prefs is not null");
            if (!username.getText().toString().trim().equals(pref.getString(USERNAME, ""))) {
                dataSaved = false;
            } else if (!emailAdr.getText().toString().trim().equals(pref.getString(EMAIL, ""))) {
                dataSaved = false;
            } else if (!password.getText().toString().trim().equals(pref.getString(PASSWORD, ""))) {
                dataSaved = false;
            }
        } else if (username.getText().length() < 1 && emailAdr.getText().length() < 1 && password.getText().length() < 1) {
            Log.d(TAG, "onBackPressed: prefs is null but empty fields");
            dataSaved = true;
        } else {
            Log.d(TAG, "onBackPressed: prefs is null but NOT empty fields");
            dataSaved = false;
        }

        // There is unsaved data.
        if (!dataSaved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.unsaved));
            builder.setMessage(getString(R.string.not_saved));

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                /**
                 * Call finish() on the activity where reside the dialog, which will also
                 * discard unsaved data.
                 *
                 * @param dialog The dialog that is currently shown / the on pressed on.
                 * @param which The button pressed.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton(R.string.no, null);

            Dialog dialog = builder.create();
            dialog.show();
        } else {
            //No unsaved data.
            super.onBackPressed();
        }
    }
}
