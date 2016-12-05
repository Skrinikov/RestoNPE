package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Fragments.AddRestoFragment;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.database.RestoDAO;

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

    private npe.com.restonpe.Beans.Address restoAddress;
    private SharedPreferences pref;

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_activity_restos_add);
        pref = getSharedPreferences("Settings", MODE_PRIVATE);
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
     * Method is invoked every time the user clicks the add resto button. Validates that all the required
     * fields are set, and if they are adds the restaurant to the local database.
     *
     * TODO use async task later to message the server to add the resto.
     *
     * @param view
     */
    public void addRestaurant(View view){
        Log.d(TAG,"addRestaurant()");

        if(validateInputFields()){
            Resto resto = buildResto();
            RestoDAO db = RestoDAO.getDatabase(this);
            db.addRestaurant(resto);
            Log.i(TAG,"restaurant added");
            //TODO open intent to display the new restaurant.
        }

        Log.i(TAG,"addRestaurant()");
    }

    /**
     * Creates a Resto bean using the information from the gui.
     * @return resto bean with all available information.
     */
    private Resto buildResto() {
        Resto resto = new Resto();

        String name = ((EditText)findViewById(R.id.restoName)).getText().toString();
        Log.d(TAG, name);
        resto.setName(name);
        Log.d(TAG, "Returned: "+resto.getName());
        String phone = ((EditText)findViewById(R.id.restoPhone)).getText().toString();
        if(!phone.isEmpty())
            resto.setPhone(Long.parseLong(phone));
        String email = ((EditText)findViewById(R.id.restoEmail)).getText().toString();
        resto.setEmail(email);
        String link = ((EditText)findViewById(R.id.restoLink)).getText().toString();
        resto.setLink(link);
        resto.setAddress(restoAddress);

        //Spinners
        String genre = ((Spinner)findViewById(R.id.genresSpinner)).getSelectedItem().toString();
        String pricing = ((Spinner)findViewById(R.id.priceRangeSpinner)).getSelectedItem().toString();
        resto.setGenre(genre);
        resto.setPriceRange(pricing);

        // User
        resto.setSubmitterName(pref.getString("username",""));
        resto.setSubmitterEmail(pref.getString("emailAdr",""));

        return resto;
    }

    /**
     * Validates that all the required input fields were set. If any errors occur, displayes an appropriate
     * error message next to the edit text field.
     * @return
     */
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

        //Since address is civic + street need to do more extensive validation.

        // Province
        TextInputEditText province = (TextInputEditText) findViewById(R.id.restoProvince);
        if(province.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoProvinceLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.province_error));
        }

        //City
        TextInputEditText city = (TextInputEditText) findViewById(R.id.restoCity);
        if(city.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoCityLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.city_error));
        }

        // Postal Code
        TextInputEditText postal = (TextInputEditText) findViewById(R.id.restoPostal);
        if(postal.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoPostalLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.postal_error));
        }

        // Country
        TextInputEditText country = (TextInputEditText) findViewById(R.id.restoCountry);
        if(country.getText().toString().isEmpty()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoCountryLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.name_error));
        }

        // Checking if email is in correct format if it is not empty.
        TextInputEditText email = (TextInputEditText) findViewById(R.id.restoEmail);
        if(!email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            isValid = false;
            TextInputLayout temp = (TextInputLayout)findViewById(R.id.restoEmailLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.email_error));
        }

        if(isValid && !validateAddress())
            isValid = false;

        return isValid;
    }

    /**
     * Validates only the address using the RestoLocationManager. If it is valid stores the Location
     * in an instance variable.
     *
     * @return true if the address is valid and was found.
     */
    private boolean validateAddress() {
        Log.d(TAG, "ValidateAddress");
        String address = ((EditText)findViewById(R.id.restoAddress)).getText().toString();
        String city =((EditText)findViewById(R.id.restoCity)).getText().toString();
        String province =((EditText)findViewById(R.id.restoProvince)).getText().toString();
        String country = ((EditText)findViewById(R.id.restoCountry)).getText().toString();
        String postal = ((EditText)findViewById(R.id.restoPostal)).getText().toString();


        RestoLocationManager lm = new RestoLocationManager(this) {
            @Override
            public void onLocationChanged(Location location) {
                // Is not used.
            }
        };

        Address addr = lm.getLocationFromName(postal.toUpperCase());

        if(addr == null) {
            Log.d(TAG,"Did not find address with Postal code");
            addr = lm.getLocationFromName(address+", "+city+" "+province+", "+country);
            if(addr == null){
                Log.d(TAG,"Did not find address with all variables");
                return false;
            }
        }

        // Should have named the address bean something else :/
        restoAddress = new npe.com.restonpe.Beans.Address();
        restoAddress.setLatitude(addr.getLatitude());
        restoAddress.setLongitude(addr.getLongitude());
        restoAddress.setCity(city);
        restoAddress.setAddress(address);
        restoAddress.setPostal(postal);
        restoAddress.setCountry(country);
        restoAddress.setProvince(province);
        Log.d(TAG, "latitude: "+addr.getLatitude());
        Log.d(TAG, "longitude: "+addr.getLongitude());


        return true;

    }
}
