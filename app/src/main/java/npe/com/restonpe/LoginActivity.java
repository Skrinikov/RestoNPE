package npe.com.restonpe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
