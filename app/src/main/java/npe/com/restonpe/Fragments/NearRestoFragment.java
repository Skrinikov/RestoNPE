package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import npe.com.restonpe.R;

/**
 * Created by Hung on 11/29/2016.
 */

public class NearRestoFragment extends Fragment {

    /**
     * source: https://developer.android.com/guide/components/fragments.html
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_near_restos, container, false);
    }
}
