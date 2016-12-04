package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import npe.com.restonpe.R;

/**
 * A fragment representing a list of Restos.
 */
public class RestoListFragment extends Fragment {

    private static final String TAG = RestoListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.resto_list, container, false);
    }

    public void showRestoInformation(View v) {

    }
}
