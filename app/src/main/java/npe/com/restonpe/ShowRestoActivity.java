package npe.com.restonpe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import npe.com.restonpe.Fragments.ShowRestoFragment;
import npe.com.restonpe.database.RestoDAO;

/**
 * Creates an instance of the ShowResto Activity. This {@code Activity} will allow the user to
 * view details of a restaurant.
 *
 * @author Jeegna Patel, Uen Yi Cindy Hung
 * @version 1.0
 * @since 07/12/2016
 */
public class ShowRestoActivity extends BaseActivity {

    private static final String TAG = ShowRestoActivity.class.getSimpleName();
    public static final String LOCAL_ID = "local_id";
    public static final String SUBMITTER = "submitter";

    private Context mContext;
    private ShowRestoFragment fragment;
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

        fragment = new ShowRestoFragment();
        createFragments(fragment);
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

        String submitter = extras.getString(SUBMITTER);

        if (submitter != null && submitter.toString().length() > 0) {
            menu.getItem(1).setIcon(R.drawable.ic_remove);
            menu.getItem(0).setIcon(R.drawable.ic_edit);
        } else {
            menu.getItem(1).setIcon(R.drawable.ic_add);
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

        if (item.getItemId() == R.id.add_resto) {
            addRemoveResto(item);
        } else {
            Intent intent = new Intent(this, EditRestoActivity.class);
            intent.putExtra("id", fragment.getRestoID());
            startActivity(intent);
        }

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

    /**
     * Determine if it is a add or remove resto from the local database
     * with confirmation dialog if removing.
     *
     * @param item the menu item clicked.
     */
    private void addRemoveResto(final MenuItem item) {
        String submitter = extras.getString(SUBMITTER);

        // Delete if already added
        if (submitter != null && submitter.toString().length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.remove));
            builder.setMessage(getString(R.string.confirm_remove));

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                /**
                 * Removes the resto from local database
                 *
                 * @param dialog The dialog that is currently shown / the on pressed on.
                 * @param which  The button pressed.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RestoDAO.getDatabase(mContext).deleteRestaurant(extras.getLong(LOCAL_ID));
                    Toast.makeText(mContext, R.string.removed, Toast.LENGTH_LONG).show();
                    item.setVisible(false);
                }
            });

            builder.setNegativeButton(R.string.no, null);

            Dialog dialog = builder.create();
            dialog.show();
        } else {
            fragment.addRestoToFavourites();
            item.setVisible(false);
        }
    }

    /**
     * Removes the Add Review button
     */
    public void removeReviewButton() {
        Button addReview = (Button) findViewById(R.id.buttonAddReview);
        addReview.setVisibility(View.INVISIBLE);
    }

    /**
     * Starts the AddReview activity.
     *
     * @param view The view upon which the on click was called.
     */
    public void showAddReviewActivity(View view) {
        fragment.addReview();
    }
}
