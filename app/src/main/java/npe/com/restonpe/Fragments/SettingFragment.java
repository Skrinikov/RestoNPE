package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import npe.com.restonpe.R;

/**
 * Fragment class that will load the content of the SettingActivity of RestoNPE.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.1
 * @since 30/11/2016
 */

public class SettingFragment extends Fragment {
    private TextView username, emailAdr,postalCode;
    private final String TAG = "SettingFragment";


    /**
     * Inflates a layout to be the content layout of the SettingActivity.
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
        Log.d(TAG,"onCreateView called");
        return inflater.inflate(R.layout.activity_setting, container, false);
    }

    /**
     * Retrieves the data saved in the shared preferences, if there is one
     * and display them into the text view of the inflated layout.
     *
     * @param savedInstanceState Bundle where the run values are stored.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(TAG,"onActivityCreated called");
        super.onActivityCreated(savedInstanceState);
    }
}
