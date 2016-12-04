package npe.com.restonpe;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG,"onCreate()");
    }

    public void attemptLogin(View view) {
        if (validateInputs()) {
            Log.d(TAG,"input valid, attempting to login");

        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        EditText email = (EditText) findViewById(R.id.email);
        if (email.getText().toString().trim().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.emailLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.login_email_error));
        }

        EditText name = (EditText) findViewById(R.id.email);
        if (name.getText().toString().trim().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.nameLbl);
            temp.setError(getString(R.string.login_name_error));
            temp.setErrorEnabled(true);
        }

        EditText pass = (EditText) findViewById(R.id.email);
        if (pass.getText().toString().trim().length() < 6) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.passwordLbl);
            temp.setError(getString(R.string.login_password_error));
            temp.setErrorEnabled(true);
        }

        return isValid;
    }
}
