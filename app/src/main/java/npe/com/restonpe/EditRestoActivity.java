package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Fragments.AddRestoFragment;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.database.RestoDAO;
import npe.com.restonpe.util.RestoAdapter;

public class EditRestoActivity extends BaseActivity {
    private final String TAG = "EditRestoActivity";
    private EditText name, phone, email, link, adr;
    private Spinner genre, price;
    private SpinnerAdapter genreAdapter, priceAdapter;
    private Button submitResto;
    private SharedPreferences pref;

    /**
     * Loads the fragment and changes the action bar's title.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_restos_add);
        }

        pref = getSharedPreferences("Settings", MODE_PRIVATE);
        createFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupData();
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

    private void setupData() {
        RestoDAO dao = RestoDAO.getDatabase(this);
        Resto resto = dao.getSingleRestaurant(getIntent().getExtras().getLong("id"));

        name = (EditText) findViewById(R.id.restoName);
        phone = (EditText) findViewById(R.id.restoPhone);
        email = (EditText) findViewById(R.id.restoEmail);
        link = (EditText) findViewById(R.id.restoLink);
        adr = (EditText) findViewById(R.id.restoAddress);
        genre = (Spinner) findViewById(R.id.genresSpinner);
        price = (Spinner) findViewById(R.id.priceRangeSpinner);
        submitResto = (Button) findViewById(R.id.submitResto);

        name.setText(resto.getName());
        phone.setText(String.valueOf(resto.getPhone() > 0 ? resto.getPhone() : ""));
        email.setText(resto.getEmail());
        link.setText(resto.getLink());
        adr.setText(resto.getAddress().getAddress());

        submitResto.setText(R.string.save);
        submitResto.setOnClickListener(null);
        submitResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputFields()){
                    Resto resto = buildResto();

                    RestoDAO db = RestoDAO.getDatabase(EditRestoActivity.this);
                    db.updateRestaurant(resto);

                    finish();
                }
            }
        });

        genreAdapter = genre.getAdapter();
        priceAdapter = price.getAdapter();

        int genreSize = genreAdapter.getCount();
        int priceSize = priceAdapter.getCount();

        for (int i = 0; i < genreSize; i++) {
            int comma = resto.getGenre().indexOf(',') > 0 ? resto.getGenre().indexOf(',') : resto.getGenre().length();
            String cuisine = resto.getGenre().substring(0, comma);
            if (genreAdapter.getItem(i).toString().contains(cuisine)) {
                genre.setSelection(i);
            }
        }

        for (int i = 0; i < priceSize; i++) {
            if (priceAdapter.getItem(i).equals(resto.getPriceRange())) {
                price.setSelection(i);
            }
        }
    }

    /**
     * Creates a Resto bean using the information from the gui.
     *
     * @return resto bean with all available information.
     */
    private Resto buildResto() {
        Resto resto = new Resto();

        resto.setId(getIntent().getExtras().getLong("id"));
        resto.setName(name.getText().toString());

        if (!phone.getText().toString().isEmpty())
            resto.setPhone(Long.valueOf(phone.getText().toString().isEmpty() ? "0" : phone.getText().toString()));

        resto.setEmail(email.getText().toString());

        resto.setLink(link.getText().toString());

        npe.com.restonpe.Beans.Address address = new npe.com.restonpe.Beans.Address();
        address.setAddress(adr.getText().toString());
        resto.setAddress(address);

        //Spinners;
        resto.setGenre(genre.getSelectedItem().toString());
        resto.setPriceRange(price.getSelectedItem().toString());

        // User
        resto.setSubmitterName(pref.getString("username", ""));
        resto.setSubmitterEmail(pref.getString("emailAdr", ""));

        return resto;
    }

    /**
     * Validates that all the required input fields were set. If any errors occur, displays an appropriate
     * error message next to the edit text field.
     *
     * @return {@code True} if all the required fields were set, {@code False} otherwise.
     */
    private boolean validateInputFields() {
        boolean isValid = true;

        //Name
        if (name.getText().toString().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoNameLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.name_error));
        }

        // Address
        if (adr.getText().toString().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoAddressLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.address_error));
        }

        // Province
        TextInputEditText province = (TextInputEditText) findViewById(R.id.restoProvince);
        if (!province.getText().toString().isEmpty()) {
            adr.setText(adr.getText() + ", " + province.getText());
        }

        //City
        TextInputEditText city = (TextInputEditText) findViewById(R.id.restoCity);
        if (!city.getText().toString().isEmpty()) {
            adr.setText(adr.getText() + ", " + city.getText());
        }

        // Country
        TextInputEditText country = (TextInputEditText) findViewById(R.id.restoCountry);
        if (!country.getText().toString().isEmpty()) {
            adr.setText(adr.getText() + ", " + country.getText());
        }

        // Postal Code
        TextInputEditText postal = (TextInputEditText) findViewById(R.id.restoPostal);
        if (!postal.getText().toString().isEmpty()) {
            adr.setText(adr.getText() + ", " + postal.getText());
        }

        // Checking if email is in correct format if it is not empty.
        if (!email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoEmailLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.email_error));
        }

        //if (isValid && !validateAddress())
          //  isValid = false;

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
        String address = ((EditText) findViewById(R.id.restoAddress)).getText().toString();
        String city = ((EditText) findViewById(R.id.restoCity)).getText().toString();
        String province = ((EditText) findViewById(R.id.restoProvince)).getText().toString();
        String country = ((EditText) findViewById(R.id.restoCountry)).getText().toString();
        String postal = ((EditText) findViewById(R.id.restoPostal)).getText().toString();


        RestoLocationManager lm = new RestoLocationManager(this) {
            @Override
            public void onLocationChanged(Location location) {
                // Is not used.
            }
        };

        Address addr = lm.getLocationFromName(postal.toUpperCase());
        String addressString = address + ", " + city + ", " + province + ", " + country;
        if (addr == null) {
            Log.d(TAG, "Did not find address with Postal code");
            addr = lm.getLocationFromName(address + ", " + city + " " + province + ", " + country);
            if (addr == null) {
                Log.d(TAG, "Did not find address with all variables");
                return false;
            }
        }

        // Should have named the address bean something else :/
       /* restoAddress = new npe.com.restonpe.Beans.Address();
        restoAddress.setLatitude(addr.getLatitude());
        restoAddress.setLongitude(addr.getLongitude());
        restoAddress.setCity(city);
        restoAddress.setAddress(addressString);
        restoAddress.setPostal(postal);
        restoAddress.setCountry(country);
        restoAddress.setProvince(province);
        Log.d(TAG, "latitude: " + addr.getLatitude());
        Log.d(TAG, "longitude: " + addr.getLongitude());
*/

        return true;

    }
}
