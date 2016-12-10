package npe.com.restonpe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Displays the register activity. Lets a user register to a remote api. Before the user registers
 * validates that all the provided data is in the right format and can be used as authentication.
 *
 * @author Danieil Skrinikov
 * @since 2016-12-10
 * @version 1.0.0
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText email;
    EditText name;
    EditText pwd;
    EditText pwdConfirm;
    EditText postal;

    // Authentication Variables.
    private static final String LOGIN_URL = "http://shrouded-thicket-29911.herokuapp.com/api/user_register";
    private String jsonData = "{\"email\":\"%1$s\",\"password\":\"%2$s\",\"name\":\"%3$s\",\"postal_code\":\"%4$s\"}";

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String TYPE = "application/json; charset=UTF-8;";
    private static final String CONTENT_LENGTH = "Content-Length";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.password);
        pwdConfirm = (EditText) findViewById(R.id.passwordConfirmation);
        postal = (EditText) findViewById(R.id.postal);
    }

    /**
     * Validates that all the inputs are correct. Then tried to register the user with the server online.
     * If the server does register the user. Saves the information to shared preferences and then launches main activity.
     *
     * @param view not used.
     */
    public void attemptRegister(View view){
        if(validateEditTexts()){
            new RegisterAsync().execute(email.getText().toString().trim(),pwd.getText().toString().trim(), name.getText().toString().trim(), postal.getText().toString().trim());
            Toast.makeText(this, getString(R.string.register_wait), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Verifies that the data in all the edit texts is present and that it is correctly formatted.
     *
     * @return true if all data is in correct form.
     */
    private boolean validateEditTexts() {
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

        if(pwdConfirm.getText().toString().trim().length() < 6 || !pwdConfirm.getText().toString().trim().equals(pwd.getText().toString().trim())){
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.passwordConfirmationLbl);
            temp.setError(getString(R.string.login_password_error_nonmatch));
            temp.setErrorEnabled(true);
        }

        if (postal.getText().toString().trim().length() < 6){
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.postalLbl);
            temp.setError(getString(R.string.postal_error));
            temp.setErrorEnabled(true);
        }

        return isValid;
    }


    /**
     * Async class to do network registration in a background thread.
     */
    public class RegisterAsync extends AsyncTask<String, Void, Boolean> {

        /**
         * Tries to authenticate the given email password combination with the server. If the server
         * authenticates the combination returns true. If the server cannot authenticate the user returns
         * false.
         *
         * @param params Inputs to use for the authentication. First param=email, second=password
         * @return True if server authenticates the user.
         */
        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection conn = null;
            OutputStream out;
            boolean isFound = false;

            try {

                Log.i(TAG, "Requesting: "+LOGIN_URL);
                URL url = new URL(LOGIN_URL);

                if(isNetworkAccessible()){
                    conn = (HttpURLConnection) url.openConnection();

                    // Format Json string
                    jsonData = String.format(jsonData, params[0], params[1], params[2], params[3]);
                    Log.i(TAG, jsonData);
                    byte[] bytes = jsonData.getBytes("UTF-8");
                    int bytesLeng = bytes.length;

                    // Set HTTP request
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty(CONTENT_TYPE, TYPE);
                    conn.addRequestProperty(CONTENT_LENGTH, String.valueOf(bytesLeng));
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    out = new BufferedOutputStream(conn.getOutputStream());

                    out.write(bytes);
                    out.flush();
                    out.close();

                    // Get response
                    int response = conn.getResponseCode();
                    if (response != HttpURLConnection.HTTP_CREATED) {
                        Log.e(TAG, "Something went wrong. The URL was " + url + " The HTTP response was " + response + " " + conn.getResponseMessage());
                        isFound = false;
                    } else {
                        Log.i(TAG, "Success!");
                        isFound = true;
                    }

                    conn.disconnect();
                }

            } catch (MalformedURLException mue) {
                Log.e(TAG, "Malformed URL: " + LOGIN_URL);
            } catch (IOException ioe) {
                Log.e(TAG, "An IOException occurred while reading the JSON file: " + ioe.getMessage());

            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return isFound;
        }

        /**
         * Runs after the background thread executes. Calls saveToPreferencesAndLaunchMain if the result
         * is true and displayFailedLogin if it is not.
         *
         * @param result result of the background thread.
         */
        @Override
        public void onPostExecute(Boolean result){
            if(result)
                saveToPreferencesAndLaunchMain();
            else
                displayFailedRegister();
        }
    }

    /**
     * Displays an error to the user when the server cannot register the user.
     */
    private void displayFailedRegister() {
        TextInputLayout temp = (TextInputLayout) findViewById(R.id.postalLbl);
        temp.setErrorEnabled(true);
        temp.setError(getString(R.string.login_authen_postal_error));
    }

    /**
     * Saves the current preferences into the database and then launches the main activity with a new
     * task flags.
     */
    private void saveToPreferencesAndLaunchMain(){
        Log.d(TAG, "input valid, attempting to login");
        SharedPreferences prefs = getSharedPreferences(BaseActivity.SHARED_PREFS, MODE_PRIVATE);
        prefs.edit()
                .putString(SettingActivity.USERNAME, name.getText().toString().trim())
                .putString(SettingActivity.EMAIL, email.getText().toString().trim())
                .putString(SettingActivity.PASSWORD, pwd.getText().toString().trim())
                .apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Checks if the network is up and usable.
     *
     * @return {@code True} if the network is up and can be used, {@code False} otherwise
     */
    public boolean isNetworkAccessible() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
