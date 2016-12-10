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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    // Authentication Variables.
    private static final String LOGIN_URL = "http://shrouded-thicket-29911.herokuapp.com/api/userlogin";
    private String jsonData = "{\"email\":\"%1$s\",\"password\":\"%2$s\"}";

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String TYPE = "application/json; charset=UTF-8;";
    private static final String CONTENT_LENGTH = "Content-Length";

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
     * Validates that all the fields are provided, IF everything has a valid format checks with the
     * the api to see if this user exists.
     *
     * @param view The view upon which the event was called
     */
    public void attemptLogin(View view) {
        if (validateInputs()) {
            new AuthAsync().execute(email.getText().toString().trim(),pwd.getText().toString().trim());
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

    public class AuthAsync extends AsyncTask<String, Void, Boolean> {

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
                    jsonData = String.format(jsonData, params[0], params[1]);
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

        @Override
        public void onPostExecute(Boolean result){
            if(result)
                saveToPreferencesAndLaunchMain();
            else
                displayFailedLogin();
        }
    }

    private void displayFailedLogin() {
        Log.i(TAG, "displayFailedLogin");
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
}
