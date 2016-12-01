package npe.com.restonpe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private TextView username, emailAdr, postalCode;
    private SettingFragment fragment;
    private final String TAG = "SettingActivity";
    private SharedPreferences pref;

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.action_settings);
        createFragments();
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
        postalCode = (TextView) fragment.getView().findViewById(R.id.postal_code_input);
        pref = getSharedPreferences("Settings", MODE_PRIVATE);

        if (pref != null) {
            username.setText(pref.getString("username", ""));
            emailAdr.setText(pref.getString("emailAdr", ""));
            postalCode.setText(pref.getString("postalCode", ""));
        }

        Log.d(TAG, "TextViews are: " + username + "\t" + emailAdr + "\t" + postalCode);
    }

    /**
     * Inserts the setting fragment into the content view using the fragment
     * manager.
     */
    private void createFragments() {
        Log.d(TAG, "createFragments called");
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new SettingFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
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

        if (username.getText() != null && emailAdr.getText() != null && postalCode.getText() != null) {
            Log.d(TAG, "Fields are: " + username.getText() + "\t" + emailAdr.getText() + "\t" + postalCode.getText());
            Log.d(TAG, "Fields contain: " + username.getText().toString() + "\t" + emailAdr.getText().toString() + "\t" + postalCode.getText().toString());

            if (username.getText().toString().trim().length() > 0 && emailAdr.getText().toString().trim().length() > 0 && postalCode.getText().toString().trim().length() > 0) {
                editor.putString("username", username.getText().toString().trim());
                editor.putString("emailAdr", emailAdr.getText().toString().trim());
                editor.putString("postalCode", postalCode.getText().toString().trim());

                editor.commit();
                Toast.makeText(this, R.string.setting_saved, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.incomplete, Toast.LENGTH_LONG).show();
            }
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
        Log.d(TAG, "onBackPressed called");
        boolean dataSaved = true;

        // Check if there the text fields are the same as in the prefs in there is one.
        if (pref != null) {
            if (!username.getText().toString().trim().equals(pref.getString("username", null))) {
                dataSaved = false;
            } else if (!emailAdr.getText().toString().trim().equals(pref.getString("emailAdr", null))) {
                dataSaved = false;
            } else if (!postalCode.getText().toString().trim().equals(pref.getString("postalCode", null))) {
                dataSaved = false;
            }
        } else {
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
