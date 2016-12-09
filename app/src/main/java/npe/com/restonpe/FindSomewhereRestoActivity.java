package npe.com.restonpe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * @author Uen Yi Cindy Hung
 * @since 08/12/2016
 * @version 1.0
 */
public class FindSomewhereRestoActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = FindSomewhereRestoActivity.class.getSimpleName();
    private int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Code provided by Google.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Log.d(TAG, "before startActivityForResult for Google Picker");
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 50% code provided by Google.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult called");
        if (requestCode == PLACE_PICKER_REQUEST) {
            Log.d(TAG, "Place Picker Request Returned");
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                Intent intent = new Intent(this, NearRestosActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
