package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import npe.com.restonpe.AboutActivity;
import npe.com.restonpe.AddRestoActivity;
import npe.com.restonpe.FavRestoActivity;
import npe.com.restonpe.RestoSearchActivity;
import npe.com.restonpe.NearRestosActivity;
import npe.com.restonpe.R;
import npe.com.restonpe.TipActivity;

/**
 * The content of the MainActivity of RestoNPE.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.1
 * @since 01/12/2016
 */
public class IndexFragment extends Fragment {
    private ImageView favResto, nearResto, findResto, tipCalculator, addResto, logo;
    private final String TAG = "IndexFragment";

    /**
     * Inflates a layout as the content of the MainActivity.
     * <p>
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
        Log.d(TAG,"onCreateView called");
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    /**
     * Sets up the onclick events for the 6 clickable items which will
     * all start a different activity.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG,"onActivityCreated called");
        super.onActivityCreated(savedInstanceState);
        favResto = (ImageView) getActivity().findViewById(R.id.favResto);
        nearResto = (ImageView) getActivity().findViewById(R.id.nearResto);
        findResto = (ImageView) getActivity().findViewById(R.id.findResto);
        addResto = (ImageView) getActivity().findViewById(R.id.addResto);
        tipCalculator = (ImageView) getActivity().findViewById(R.id.tipCalculator);
        logo = (ImageView) getActivity().findViewById(R.id.logo);

        View.OnClickListener listener = new View.OnClickListener() {
            /**
             * An OnClickListener that will launch an activity based on the
             * what the id of the view through a switch statement.
             *
             * @param v The view which triggered the event.
             */
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onActivityCreated - onClick called");
                Intent intent = null;

                switch (v.getId()) {
                    case R.id.favResto:
                        Log.d(TAG,"onActivityCreated - onClick:  favResto");
                        intent = new Intent(getActivity(), FavRestoActivity.class);
                        break;
                    case R.id.nearResto:
                        Log.d(TAG,"onActivityCreated - onClick: nearResto");
                        intent = new Intent(getActivity(), NearRestosActivity.class);
                        break;
                    case R.id.findResto:
                        Log.d(TAG,"onActivityCreated - onClick: findResto");
                        intent = new Intent(getActivity(), RestoSearchActivity.class);
                        break;
                    case R.id.addResto:
                        Log.d(TAG,"onActivityCreated - onClick: addResto");
                        intent = new Intent(getActivity(), AddRestoActivity.class);
                        break;
                    case R.id.tipCalculator:
                        Log.d(TAG,"onActivityCreated - onClick: tipCalculator");
                        intent = new Intent(getActivity(), TipActivity.class);
                        break;
                    case R.id.logo:
                        Log.d(TAG,"onActivityCreated - onClick: logo");
                        intent = new Intent(getActivity(), AboutActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        };

        favResto.setOnClickListener(listener);
        nearResto.setOnClickListener(listener);
        findResto.setOnClickListener(listener);
        addResto.setOnClickListener(listener);
        tipCalculator.setOnClickListener(listener);
        logo.setOnClickListener(listener);
    }
}
