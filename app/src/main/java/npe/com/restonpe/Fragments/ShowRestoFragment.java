package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.Heroku.HerokuRestos;
import npe.com.restonpe.R;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.ShowRestoActivity;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.database.RestoDAO;
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
    private Bundle bundle;
    private ShowRestoActivity activity;
    private Resto resto;

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
        activity = (ShowRestoActivity) getActivity();

        bundle = activity.getIntent().getExtras();

        long local_id = bundle.getLong(RestoAdapter.LOCAL_ID);
        long zomato_id = bundle.getLong(RestoAdapter.ZOMATO_ID);
        long heroku_id = bundle.getLong(RestoAdapter.HEROKU_ID);

        // Get nearby restaurants
        getRestaurant(local_id, zomato_id, heroku_id);
    }

    @Override
    public void onResume() {
        super.onResume();

        long local_id = bundle.getLong(RestoAdapter.LOCAL_ID);
        long zomato_id = bundle.getLong(RestoAdapter.ZOMATO_ID);
        long heroku_id = bundle.getLong(RestoAdapter.HEROKU_ID);

        // Get nearby restaurants
        getRestaurant(local_id, zomato_id, heroku_id);
    }

    /**
     * Gets the information of the restaurant with the given id.
     *
     * @param local_id  The local database id of the restaurant whose information is to be retrieved.
     * @param zomato_id The Zomato id of the restaurant whose information is to be retrieved.
     * @param heroku_id The Heroku id of the restaurant whose information is to be retrieved.
     */
    private void getRestaurant(long local_id, long zomato_id, final long heroku_id) {
        Log.d(TAG, "Local id: " + local_id);
        Log.d(TAG, "Zomato id: " + zomato_id);
        Log.d(TAG, "Heroku id: " + heroku_id);

        if (zomato_id > 0) {
            // Resto is not from Heroku remove add comment button
            activity.removeReviewButton();

            // Get resto from Zomato
            Log.i(TAG, "Getting Resto with id " + zomato_id + " from Zomato");
            RestoNetworkManager<Resto> zomatoNetworkManager = new RestoNetworkManager<Resto>(activity) {
                @Override
                public void onPostExecute(List<Resto> list) {
                    if (list.size() > 0) {
                        resto = list.get(0);
                        displayInformation(resto);
                    }
                }

                @Override
                protected List<Resto> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Zomato Resto information Json response...");

                    try {
                        ZomatoRestos zomatoRestos = new ZomatoRestos(activity);
                        return zomatoRestos.readRestoInformation(reader);
                    } catch (IOException e) {
                        Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                    }
                    return null;
                }
            };
            zomatoNetworkManager.findRestoInformation(zomato_id);
        } else if (heroku_id > 0) {
            // Get resto from heroku
            Log.i(TAG, "Getting Resto with id " + heroku_id + " from Heroku");
            RestoNetworkManager<Resto> herokuNetworkManager = new RestoNetworkManager<Resto>(activity) {
                @Override
                public void onPostExecute(List<Resto> list) {
                    if (list != null && list.size() > 0) {
                        resto = list.get(0);
                        displayInformation(resto);
                    }
                }

                @Override
                protected List<Resto> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Heroku Resto information Json response...");

                    try {
                        HerokuRestos herokuRestos = new HerokuRestos(activity);
                        return herokuRestos.readRestoInformation(reader);
                    } catch (IOException e) {
                        Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                    }
                    return null;
                }
            };

            // Get reviews from heroku
            RestoNetworkManager<Review> restoNetworkManager = new RestoNetworkManager<Review>(activity) {
                @Override
                public void onPostExecute(List<Review> list) {
                    if (list != null && list.size() > 0) {
                        displayReviews(list);
                    }
                }

                @Override
                protected List<Review> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Heroku Reviews Json response...");

                    try {
                        HerokuRestos herokuRestos = new HerokuRestos(activity);
                        return herokuRestos.readReviewJson(reader);
                    } catch (IOException e) {
                        Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                    }
                    return null;
                }
            };

            herokuNetworkManager.findRestoInformationFromHeroku(heroku_id);
            restoNetworkManager.findReviews(heroku_id);

        } else if (local_id > 0) {
            // Resto is not from Heroku remove add comment button
            activity.removeReviewButton();

            // Get resto from local db
            Log.i(TAG, "Getting Resto with id " + local_id + " from local database");
            RestoDAO restoDAO = RestoDAO.getDatabase(activity);
            resto = restoDAO.getSingleRestaurant(local_id);
            displayInformation(resto);
        } else {
            Log.e(TAG, "An error occurred. The given id's are invalid");
        }
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
    }

    private void displayReviews(List<Review> reviews) {
        if (reviews != null && reviews.size() > 0) {
            ReviewAdapter adapter = new ReviewAdapter(activity, reviews);

            ListView listView = (ListView) activity.findViewById(R.id.review_list);
            listView.setAdapter(adapter);
        }
    }

    public void addRestoToFavourites() {
        // Get form local db
        Log.d(TAG, "Adding resto to local database");
        RestoDAO dao = RestoDAO.getDatabase(activity);
        resto.setSubmitterName("Zomato");
        resto.setSubmitterEmail("ZomatoEmail");
        dao.addRestaurant(resto);
        Toast.makeText(activity, R.string.added, Toast.LENGTH_LONG).show();
    }

    /**
     * @return long the resto id.
     */
    public long getRestoID() {
        return this.resto.getId();
    }
}
