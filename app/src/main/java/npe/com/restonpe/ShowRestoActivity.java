package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Fragments.ShowRestoFragment;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.database.RestoDAO;

/**
 * Creates an instance of the ShowResto Activity. This {@code Activity} will allow the user to
 * view details of a restaurant.
 *
 * @author Jeegna Patel
 * @since 04/12/2016
 * @version 1.0
 */
public class ShowRestoActivity extends BaseActivity {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.title_activity_show_restos);
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

        if(getIntent().getExtras().get("submitter").toString().length() > 0){
            menu.getItem(0).setIcon(R.drawable.ic_remove);
        }else{
            menu.getItem(0).setIcon(R.drawable.ic_add);
        }

        return true;
    }

    /**
     * Starts the add resto activity.
     *
     * @param item The selected item.
     * @return boolean depicting that an item has been clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected called");
        int id = item.getItemId();

        if(getIntent().getExtras().get("submitter").toString().length() > 0){
            Toast.makeText(this,R.string.removed,Toast.LENGTH_LONG).show();
        }else{
            ZomatoRestos zomato = new ZomatoRestos(this) {
                @Override
                public void handleResults(List<?> list) {
                    if (list.size() == 1) {
                        RestoDAO dao = RestoDAO.getDatabase(ShowRestoActivity.this);
                        Resto resto = (Resto) list.get(0);
                        resto.setSubmitterName("Zomato");
                        resto.setSubmitterEmail("ZomatoEmail");
                        dao.addRestaurant(resto);
                        Toast.makeText(ShowRestoActivity.this, R.string.added, Toast.LENGTH_LONG).show();
                    }
                }
            };

            zomato.findRestoInformation(getIntent().getExtras().getInt("id"));
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO Add this: android:onClick="searchGoogle" to the TextView that holds the name of the restaurant
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
