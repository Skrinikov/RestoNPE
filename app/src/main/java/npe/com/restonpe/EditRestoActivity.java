package npe.com.restonpe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Fragments.AddRestoFragment;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.database.RestoDAO;

/**
 * Creates an instance of the AddRestoActivity which will
 * allow the user to edit a resto that is in the database.
 * <p>
 * Used as reference
 * source: Danieil's AddRestoActivity
 *
 * @author Uen yi Cindy Hung
 * @version 1.0
 * @Since 09/12/2016
 */
public class EditRestoActivity extends BaseActivity {
    private final String TAG = "EditRestoActivity";
    private EditText name, phone, email, link, adr, city, province, country, postal;
    private Spinner genre, price;
    private SpinnerAdapter genreAdapter, priceAdapter;
    private Button submitResto;
    private SharedPreferences pref;
    private Resto resto;
    private String[] restoGenre;
    private npe.com.restonpe.Beans.Address restoAddress;

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

    /**
     * Done in onResume else, the views aren't loaded yet.
     * Calls a method that will get a handle on the views and set the
     * resto data into them.
     */
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

    /**
     * Get a handle on the views so we can set the resto data
     * into them therefore visible on screen. Plus changes the event listener
     * for the submit button.
     */
    private void setupData() {
        Log.d(TAG, "setupData called");
        RestoDAO dao = RestoDAO.getDatabase(this);
        resto = dao.getSingleRestaurant(getIntent().getExtras().getLong("id"));

        Log.d(TAG, "setupData - resto is: " + resto);

        name = (EditText) findViewById(R.id.restoName);
        phone = (EditText) findViewById(R.id.restoPhone);
        email = (EditText) findViewById(R.id.restoEmail);
        link = (EditText) findViewById(R.id.restoLink);
        adr = (EditText) findViewById(R.id.restoAddress);
        city = (EditText) findViewById(R.id.restoCity);
        province = (EditText) findViewById(R.id.restoProvince);
        country = (EditText) findViewById(R.id.restoCountry);
        postal = (EditText) findViewById(R.id.restoPostal);

        genre = (Spinner) findViewById(R.id.genresSpinner);
        price = (Spinner) findViewById(R.id.priceRangeSpinner);

        submitResto = (Button) findViewById(R.id.submitResto);

        name.setText(resto.getName());
        phone.setText(String.valueOf(resto.getPhone() > 0 ? resto.getPhone() : ""));
        email.setText(resto.getEmail());
        link.setText(resto.getLink());
        adr.setText(resto.getAddress().getAddress());
        city.setText(resto.getAddress().getCity());
        province.setText(resto.getAddress().getProvince());
        country.setText(resto.getAddress().getCountry());
        postal.setText(resto.getAddress().getPostal());

        submitResto.setText(R.string.save);
        submitResto.setOnClickListener(null);
        submitResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputFields()) {
                    Resto resto = buildResto();

                    Log.d(TAG, "resto is: " + resto.toString());

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

        // Because Zomato resto may contain more than one cuisine
        if (resto.getGenre().contains(",")) {
            if (resto.getGenre().indexOf(',') > 0) {
                restoGenre = resto.getGenre().split(",");
            }
        } else {
            restoGenre = new String[]{resto.getGenre()};
        }

        for (int i = 0; i < genreSize; i++) {
            for (String cuisine : restoGenre) {
                if (genreAdapter.getItem(i).toString().contains(cuisine.trim())) {
                    genre.setSelection(i);
                    break;
                }
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

        resto.setAddress(restoAddress);

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
        if (province.getText().toString().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoProvinceLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.province_error));
        }

        //City
        if (city.getText().toString().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoCityLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.city_error));
        }

        // Postal Code
        if (postal.getText().toString().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoPostalLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.postal_error));
        }

        // Country
        if (country.getText().toString().isEmpty()) {
            isValid = false;
            TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoCountryLbl);
            temp.setErrorEnabled(true);
            temp.setError(getString(R.string.name_error));
        }

        // Checking if email is in correct format if it is not empty.
        if (!email.getText().toString().isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                isValid = false;
                TextInputLayout temp = (TextInputLayout) findViewById(R.id.restoEmailLbl);
                temp.setErrorEnabled(true);
                temp.setError(getString(R.string.email_error));
            }
        }

        if (isValid && !validateAddress())
            isValid = false;

        return isValid;
    }

    /**
     * Looks to see if the user has entered any data into any of the input fields of the activity.
     * If the back button is pressed while there is still any information in the fields it will
     * warn him that all data will be lost.
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed called");
        boolean isEmpty = checkIfInputsAreSame();

        // There is unsaved data.
        if (!isEmpty) {
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

    /**
     * Checks if any of the input fields have a user defined value in them.
     * If any of the input fields has a changed value, it will return false.
     *
     * @return boolean representing if the inputs are empty.
     */
    private boolean checkIfInputsAreSame() {
        Log.d(TAG, "checkIfInputsAreEmpty called");

        if (!name.getText().toString().equals(resto.getName()))
            return false;
        if (!adr.getText().toString().trim().equals(resto.getAddress().getAddress()))
            return false;
        if (!city.getText().toString().trim().equals(resto.getAddress().getCity()))
            return false;
        if (!province.getText().toString().trim().equals(resto.getAddress().getProvince()))
            return false;
        if (!country.getText().toString().trim().equals(resto.getAddress().getCountry()))
            return false;
        if (!postal.getText().toString().trim().equals(resto.getAddress().getPostal()))
            return false;
        if (!(phone.getText().toString().isEmpty() && resto.getPhone() < 1)) {
            if (!phone.getText().toString().equals(String.valueOf(resto.getPhone())))
                return false;
        }
        if (!email.getText().toString().equals(resto.getEmail()))
            return false;
        if (!link.getText().toString().equals(resto.getLink()))
            return false;
        if (!price.getSelectedItem().toString().equals(resto.getPriceRange()))
            return false;


        boolean nochangeGenre = false;
        for (String cuisine : restoGenre) {
            if (genre.getSelectedItem().toString().contains(cuisine.trim())) {
                Log.d(TAG, "In the cuisine check");
                nochangeGenre = true;
                break;
            }
        }

        return true;
    }

    /**
     * Validates only the address using the RestoLocationManager. If it is valid stores the Location
     * in an instance variable.
     *
     * @return true if the address is valid and was found.
     */
    private boolean validateAddress() {
        Log.d(TAG, "ValidateAddress");
        String address = this.adr.getText().toString();
        String city = this.city.getText().toString();
        String province = this.province.getText().toString();
        String country = this.country.getText().toString();
        String postal = this.postal.getText().toString();


        RestoLocationManager lm = new RestoLocationManager(this) {
            @Override
            public void onLocationChanged(Location location) {
                // Is not used.
            }
        };

        Address addr = lm.getLocationFromName(postal.toUpperCase());
        if (addr == null) {
            Log.d(TAG, "Did not find address with Postal code");
            addr = lm.getLocationFromName(address + ", " + city + " " + province + ", " + country);
            if (addr == null) {
                Log.d(TAG, "Did not find address with all variables");
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
        Log.d(TAG, "latitude: " + addr.getLatitude());
        Log.d(TAG, "longitude: " + addr.getLongitude());


        return true;

    }
}
