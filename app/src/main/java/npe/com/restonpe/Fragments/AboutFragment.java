package npe.com.restonpe.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import npe.com.restonpe.R;

/**
 * Fragment class that takes cares of loading the layout
 * and setting the events for the different elements for the
 * about fragment.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.1
 * @since 30/11/2016
 */
public class AboutFragment extends Fragment {
    private ImageView dawson_logo;
    private TextView dawson_name, course_id;
    private LinearLayout cindy, jeegna, thaivu, haugilles, danieil;
    private final String TAG = "AboutFragment";

    /**
     * Inflates the xml file that contains the layout for the about activity.
     * <p>
     * Used as reference to inflate the fragment layout.
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
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.activity_about, container, false);
    }

    /**
     * Calls methods that will get a handle on the different section of elements to add
     * on click event onto them to either open an url or a dialog.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        openUrlClickEvents();
        authorClickEvents();
    }

    /**
     * Method that will get the handle on the text view and image view that will
     * open an url when clicked on.
     */
    private void openUrlClickEvents() {
        Log.d(TAG, "openUrlClickEvents called");
        dawson_logo = (ImageView) getActivity().findViewById(R.id.dawson_logo);
        dawson_name = (TextView) getActivity().findViewById(R.id.dawson_name);
        course_id = (TextView) getActivity().findViewById(R.id.course_id);

        View.OnClickListener dawsonUrl = new View.OnClickListener() {
            /**
             * Method that will use a switch statement to determine which url
             * to launch by using the view's id.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = null;

                switch (v.getId()) {
                    case R.id.dawson_logo:
                    case R.id.dawson_name:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/"));
                        break;
                    case R.id.course_id:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
                        break;
                }

                if(intent != null) {
                    startActivity(intent);
                }
            }
        };

        dawson_logo.setOnClickListener(dawsonUrl);
        dawson_name.setOnClickListener(dawsonUrl);
        course_id.setOnClickListener(dawsonUrl);
    }

    /**
     * Method that will get the handle on the different linear layout enveloping
     * the pictures and name of the 5 authors. Set a click event on all of them which will
     * launch a dialog.
     * <p>
     * Used as reference
     * source: https://developer.android.com/guide/topics/ui/dialogs.html
     */
    private void authorClickEvents() {
        Log.d(TAG, "authorClickEvents called");
        cindy = (LinearLayout) getActivity().findViewById(R.id.cindy);
        jeegna = (LinearLayout) getActivity().findViewById(R.id.jeegna);
        thaivu = (LinearLayout) getActivity().findViewById(R.id.thaivu);
        haugilles = (LinearLayout) getActivity().findViewById(R.id.haugilles);
        danieil = (LinearLayout) getActivity().findViewById(R.id.danieil);

        View.OnClickListener authorDialog = new View.OnClickListener() {
            /**
             * Method that will use a switch statement to determine what information
             * to display in the dialog by using the view's id.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                switch (getResources().getResourceEntryName(v.getId())) {
                    case "cindy":
                        Log.d(TAG, "authorClickEvents - cindy");
                        builder.setTitle(getString(R.string.cindy));
                        builder.setMessage(getString(R.string.cindy_info));
                        break;
                    case "jeegna":
                        Log.d(TAG, "authorClickEvents - jeegna");
                        builder.setTitle(getString(R.string.jeegna));
                        builder.setMessage(getString(R.string.cindy_info));
                        break;
                    case "thaivu":
                        Log.d(TAG, "authorClickEvents - thaivu");
                        builder.setTitle(getString(R.string.thaivu));
                        builder.setMessage(getString(R.string.cindy_info));
                        break;
                    case "danieil":
                        Log.d(TAG, "authorClickEvents - danieil");
                        builder.setTitle(getString(R.string.danieil));
                        builder.setMessage(getString(R.string.cindy_info));
                        break;
                    case "haugilles":
                        Log.d(TAG, "authorClickEvents - haugilles");
                        builder.setTitle(getString(R.string.haugilles));
                        builder.setMessage(getString(R.string.cindy_info));
                        break;
                }

                builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "authorClickEvents - dismiss dialog.");
                        dialog.dismiss();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
            }
        };

        cindy.setOnClickListener(authorDialog);
        jeegna.setOnClickListener(authorDialog);
        thaivu.setOnClickListener(authorDialog);
        haugilles.setOnClickListener(authorDialog);
        danieil.setOnClickListener(authorDialog);
    }
}
