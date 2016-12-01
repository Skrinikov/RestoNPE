package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        return inflater.inflate(R.layout.activity_setting, container, false);
    }
}
