package npe.com.restonpe.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import npe.com.restonpe.BaseActivity;
import npe.com.restonpe.Beans.Cuisine;
import npe.com.restonpe.R;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.util.CuisineAdapter;

/**
 * Fragment class that will load the content of the RestoSearchActivity.
 *
 * @author Uen Yi Cindy Hung, Jeegna Patel
 * @version 1.0
 * @since 11/29/2016
 */
public class RestoSearchFragment extends Fragment {

    private static final String TAG = NearRestoFragment.class.getSimpleName();

    private Activity activity;

    /**
     * Inflates a layout to be the content layout of the RestoSearchActivity.
     *
     * Used as reference
     * source: https://developer.android.com/guide/components/fragments.html
     *
     * @param inflater Layout inflater needed to inflate the xml file.
     * @param container View where the xml file will be loaded into.
     * @param savedInstanceState bundle where values are stored.
     * @return The View inflated.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.activity_find_restos, container, false);
    }
    /**
     * Sets up the onclick events for the 6 clickable items which will
     * all start a different activity.
     *
     * @param savedInstanceState bundle where values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");

        this.activity = getActivity();
        findCuisines();
    }

    public void buttonSearchClick(View v) {

        String name;
        String city;
        String[] cuisines;


//        ZomatoRestos zomatoRestos = new ZomatoRestos(this);
//        zomatoRestos.findRestos(name, city, cuisines);
    }

    private void findCuisines() {
        SharedPreferences preferences = activity.getSharedPreferences(BaseActivity.SHARED_PREFS, Activity.MODE_PRIVATE);
        String latitude = preferences.getString(BaseActivity.LATITUDE, null);
        String longitude = preferences.getString(BaseActivity.LONGITUDE, null);

        ZomatoRestos zomatoRestos = new ZomatoRestos(activity) {
            @Override
            public void handleResults(List<?> list) {
                List<Cuisine> cuisines = (List<Cuisine>) list;
                ListView listView = (ListView) activity.findViewById(R.id.selectList);

                CuisineAdapter adapter = new CuisineAdapter(activity, cuisines);
                listView.setAdapter(adapter);
            }
        };
        zomatoRestos.findCuisines(latitude, longitude);
    }
}
