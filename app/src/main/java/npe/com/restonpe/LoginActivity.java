package npe.com.restonpe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Creates an instance of the login activity.
 * Authenticates that the user input of the login fields is valid. If it is valid saved the information
 * to shared preferences and calls async task to verify that the information is valid with the server.
 *
 * @author Danieil Skrinikov
 * @version 1.0
 * @since 04/12/2016
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText email;
    EditText name;
    EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.password);

        Log.i(TAG, "onCreate()");
    }

    /**
     * Validates that all the fields are provided
     * @param view The view upon which the event was called
     */
    public void attemptLogin(View view) {
        if (validateInputs()) {
            Log.d(TAG, "input valid, attempting to login");
            SharedPreferences prefs = getSharedPreferences(BaseActivity.SHARED_PREFS, MODE_PRIVATE);
            prefs.edit()
                    .putString(SettingActivity.USERNAME, name.getText().toString().trim())
                    .putString(SettingActivity.EMAIL, email.getText().toString().trim())
                    .putString(SettingActivity.PASSWORD,pwd.getText().toString().trim())
                    .apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    /**
     * Validates the login screen input.
     *
     * @return {@code True} if all fields in the screen have valid input, {@code False} otherwise.
     */
    private boolean validateInputs() {
        boolean isValid = true;

        if (email.getText().toString().trim().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.emailLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.login_email_error));
        }

        if (name.getText().toString().trim().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.nameLbl);
            temp.setError(getString(R.string.login_name_error));
            temp.setErrorEnabled(true);
        }

        if (pwd.getText().toString().trim().length() < 6) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.passwordLbl);
            temp.setError(getString(R.string.login_password_error));
            temp.setErrorEnabled(true);
        }

        return isValid;
    }
}
