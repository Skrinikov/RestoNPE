package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import npe.com.restonpe.Fragments.AddRestoFragment;

/**
 * Creates an instance of the AddRestoActivity which will
 * allow the user to add a resto to the database by entering
 * the proper needed information.
 *
 * @author Danieil Skirnikov
 * @since 30/11/2016
 * @version 1.0
 *
 */
public class AddRestoActivity extends BaseActivity {

    private static final String TAG = "AddRestoActivity";

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_activity_restos_add);
        createFragments();
    }

    /**
     * Inserts the FindRestoFragment into the content view using
     * the fragment manager.
     */
    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AddRestoFragment fragment = new AddRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * Test
     *
     * @param view
     */
    public void addRestaurant(View view){
        Log.d(TAG,"addRestaurant()");

        if(validateInputFields()){
            
        }

        Log.i(TAG,"addRestaurant()");
    }

    private boolean validateInputFields() {
        boolean isValid = true;

        //Name
        TextInputEditText name = (TextInputEditText) findViewById(R.id.restoName);
        if(name.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoNameLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.name_error));
        }

        /*
        Since address is civic + street need to do more extensive validation.

        // Address
        TextInputEditText addr = (TextInputEditText) findViewById(R.id.restoAddress);
        if(addr.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoNameLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.name_error));
        }
        */

        // Province
        TextInputEditText province = (TextInputEditText) findViewById(R.id.restoProvince);
        if(province.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoProvinceLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.province_error));
        }

        //City
        TextInputEditText city = (TextInputEditText) findViewById(R.id.restoName);
        if(city.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoCityLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.city_error));
        }

        // Postal Code
        TextInputEditText postal = (TextInputEditText) findViewById(R.id.restoName);
        if(postal.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoPostalLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.postal_error));
        }

        // Country
        TextInputEditText country = (TextInputEditText) findViewById(R.id.restoName);
        if(country.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoCountryLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.name_error));
        }

        // Checking if email is in correct format if it is not empty.
        TextInputEditText email = (TextInputEditText) findViewById(R.id.restoName);
        if(!email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoEmailLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.email_error));
        }

        return isValid;
    }
}
