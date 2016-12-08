package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.R;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.ShowRestoActivity;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.util.RestoAdapter;
import npe.com.restonpe.util.ReviewAdapter;

/**
 * Fragment class that will load the content of the ShowRestoActivity.
 *
 * @author Uen Yi Cindy Hung, Jeegna Patel
 * @version 1.0
 * @since 01/12/2016
 */
public class ShowRestoFragment extends Fragment {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();

    private Activity activity;

    /**
     * Inflates a layout to be the content layout of the ShowRestoActivity.
     * <p>
     * Used as reference
     * source: https://developer.android.com/guide/components/fragments.html
     *
     * @param inflater           Layout inflater needed to inflate the xml file.
     * @param container          View where the xml file will be loaded into.
     * @param savedInstanceState bundle where values are stored.
     * @return The View inflated.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");

        return inflater.inflate(R.layout.activity_show_resto, container, false);
    }

    /**
     * Gets the restaurant's information and displays it on the screen.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        activity = getActivity();

        Bundle bundle = activity.getIntent().getExtras();

        int id = bundle.getInt(RestoAdapter.ID);

        // Get nearby restaurants
        getRestaurant(id);
    }

    /**
     * Gets the information of the restaurant with the given id.
     *
     * @param id The id of the restaurant whose information is to be retrieved.
     */
    private void getRestaurant(int id) {
        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(activity) {
            @Override
            public void onPostExecute(List<Resto> list) {
                if (list.size() == 1) {
                    displayInformation(list.get(0));
                }
            }

            @Override
            protected List<Resto> readJson(JsonReader reader) {
                Log.i(TAG, "Reading Json response...");

                try {
                    ZomatoRestos zomatoRestos = new ZomatoRestos(activity);
                    return zomatoRestos.readRestoInformation(reader);
                } catch (IOException e) {
                    Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                }
                return null;
            }
        };

        restoNetworkManager.findRestoInformation(id);
    }

    /**
     * Displays the restaurants information on the screen.
     *
     * @param resto The {@code Resto} whose information is to be displayed on the screen
     */
    private void displayInformation(Resto resto) {
        TextView name = (TextView) activity.findViewById(R.id.textViewShowName);
        TextView address = (TextView) activity.findViewById(R.id.textViewShowAddress);
        TextView cuisines = (TextView) activity.findViewById(R.id.textViewShowCuisines);
        TextView priceRange = (TextView) activity.findViewById(R.id.textViewShowPriceRange);
        TextView email = (TextView) activity.findViewById(R.id.textViewShowEmail);
        TextView link = (TextView) activity.findViewById(R.id.textViewShowLink);
        TextView phone = (TextView) activity.findViewById(R.id.textViewShowPhone);

        name.setText(resto.getName());
        address.setText(resto.getAddress().getAddress());
        cuisines.setText(resto.getGenre());
        priceRange.setText(resto.getPriceRange());
        email.setText(resto.getEmail());
        link.setText(resto.getLink());

        long phoneNumber = resto.getPhone();
        if (phoneNumber != 0) {
            phone.setText(String.valueOf(resto.getPhone()));
        } else {
            phone.setText(getString(R.string.show_phone_error));
        }

        List<Review> reviewsList = resto.getReviews();
        if (reviewsList == null || reviewsList.size() == 0) {
            ReviewAdapter adapter = new ReviewAdapter(activity, reviewsList);

            ListView listView = (ListView) activity.findViewById(R.id.review_list);
            listView.setAdapter(adapter);
        }
    }
}
