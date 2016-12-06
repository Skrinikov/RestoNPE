package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.R;
import npe.com.restonpe.database.RestoDAO;
import npe.com.restonpe.util.RestoAdapter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment class that takes cares of loading the layout and setting the
 * events for the different elements for the FavResto fragment.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.1
 * @since 01/12/2016
 */

public class FavRestoFragment extends Fragment {
    private RestoDAO restoDAO;
    private final String TAG = "FavRestoFragment";
    private SharedPreferences prefs;

    /**
     * Inflates a layout to be the content layout of the FavRestoActivity.
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
        return inflater.inflate(R.layout.activity_fav_resto, container, false);
    }

    /**
     * Attempts to list all the resto store in database into the ListView using AsyncTask.
     * <p>
     * Uses the custom RestoAdapter to list the objects. Needs to have sharedPreferences
     * set in order to be used, because it needs the longitude and latitude of the user.
     * <p>
     * If there is no result, will display a no result TextView.
     *
     * @param savedInstanceState Bundle that contains current run values.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated called");
        super.onActivityCreated(savedInstanceState);

        prefs = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        /*
        ListView resto_list = (ListView) getActivity().findViewById(R.id.resto_list);
        TextView no_result = (TextView) getActivity().findViewById(R.id.no_result);*/

        if (prefs != null) {
            Log.d(TAG, "onActivityCreated: prefs is not null.");
            DataLoader loader = new DataLoader();
            loader.execute();

            /*
            restoDAO = RestoDAO.getDatabase(getActivity());
            List<RestoItem> restos = restoDAO.getAllRestaurantsSmall();

            if (restos.size() > 0) {
                Log.d(TAG, "onActivityCreated: there are restos.");
                no_result.setVisibility(View.GONE);

                RestoAdapter restoAdapter = new RestoAdapter(getActivity(), restos, prefs.getString("longitude", "0"),
                        prefs.getString("latitude", "0"));
                resto_list.setAdapter(restoAdapter);


            } else {
                no_result.setVisibility(View.VISIBLE);
            }*/
        }
    }

    /**
     * AsyncTask that will call in the background database actions.
     */
    public class DataLoader extends AsyncTask<Void,Integer,List<RestoItem>>{
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        /**
         * Gets the restaurant data from SQLite database. All done in the bkg.
         *
         * @param params
         * @return
         */
        @Override
        protected List<RestoItem> doInBackground(Void... params) {
            restoDAO = RestoDAO.getDatabase(getActivity());
            return restoDAO.getAllRestaurantsSmall();
        }

        /**
         * Verify if there is data in the list. If there is no result, will display a no result TextView.
         * Else will populate the list using a custom adapter, RestoAdapter.
         *
         * @param restos The list data return by the query.
         */
        protected void onPostExecute(List<RestoItem> restos) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            ListView resto_list = (ListView) getActivity().findViewById(R.id.resto_list);
            TextView no_result = (TextView) getActivity().findViewById(R.id.no_result);

            if (restos.size() > 0) {
                Log.d(TAG, "onActivityCreated: there are restos.");
                no_result.setVisibility(View.GONE);

                RestoAdapter restoAdapter = new RestoAdapter(getActivity(), restos, prefs.getString("longitude", "0"),
                        prefs.getString("latitude", "0"), false);
                resto_list.setAdapter(restoAdapter);
            } else {
                no_result.setVisibility(View.VISIBLE);
            }
        }
    }
}
