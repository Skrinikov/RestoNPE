package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Fragments.ShowRestoFragment;
import npe.com.restonpe.Services.RestoNetworkManager;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.database.RestoDAO;

/**
 * Creates an instance of the ShowResto Activity. This {@code Activity} will allow the user to
 * view details of a restaurant.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 07/12/2016
 */
public class ShowRestoActivity extends BaseActivity {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();

    private Context mContext;
    private Bundle extras;

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        this.mContext = this;
        this.extras = getIntent().getExtras();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_show_restos);
        }
        createFragments();
    }

    /**
     * Inserts the ShowResto fragment into the content view using the
     * fragment manager.
     */
    private void createFragments() {
        Log.d(TAG, "createFragments called");

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ShowRestoFragment fragment = new ShowRestoFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * Inflates the show resto menu using an inflater.
     *
     * @param menu The view where the xml will be inflated into.
     * @return boolean representing the success of the inflation.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.resto_detail_menu, menu);

        Object submitter = extras.get("submitter");

        if (submitter != null && submitter.toString().length() > 0) {
            menu.getItem(0).setIcon(R.drawable.ic_remove);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_add);
        }

        return true;
    }

    /**
     * Add or remove a resto to database based on if the resto object has
     * a submitter or not.
     *
     * @param item The selected item.
     * @return boolean depicting that an item has been clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected called");
        int id = item.getItemId();

        Object submitter = extras.get("submitter");

        if (submitter != null && submitter.toString().length() > 0) {
            RestoDAO.getDatabase(mContext).deleteRestaurant(extras.getLong("id"));
            Toast.makeText(mContext, R.string.removed, Toast.LENGTH_LONG).show();
        } else {
            RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(mContext) {
                @Override
                public void onPostExecute(List<Resto> list) {
                    if (list != null && list.size() == 1) {
                        RestoDAO dao = RestoDAO.getDatabase(mContext);
                        Resto resto = list.get(0);
                        resto.setSubmitterName("Zomato");
                        resto.setSubmitterEmail("ZomatoEmail");
                        dao.addRestaurant(resto);
                        Toast.makeText(mContext, R.string.added, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected List<Resto> readJson(JsonReader reader) {
                    Log.i(TAG, "Reading Json response...");

                    try {
                        ZomatoRestos zomato = new ZomatoRestos(mContext);
                        return zomato.readRestoInformation(reader);
                    } catch (IOException e) {
                        Log.i(TAG, "An IO exception occurred: " + e.getMessage());
                    }
                    return null;
                }
            };

            restoNetworkManager.findRestoInformation(extras.getInt("id"));
        }
        item.setVisible(false);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Launches a web browser intent that searches google.com for the name of the restaurant this activity is displaying
     *
     * @param v The TextView with the restaurant's name
     */
    public void searchGoogle(View v) {
        // Format for google search, with placeholder for query string
        String googleSearchURL = "https://www.google.ca/#q=%1$s";

        TextView textViewName = (TextView) v;
        String restoName = textViewName.getText().toString();

        googleSearchURL = String.format(googleSearchURL, Uri.encode(restoName));

        Uri search = Uri.parse(googleSearchURL);

        // Launch web browser
        Intent intent = new Intent(Intent.ACTION_VIEW, search);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
