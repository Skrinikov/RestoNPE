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

import npe.com.restonpe.R;

/**
 * Created by Hung on 11/23/2016.
 */
public class AboutFragment extends Fragment {
    private LinearLayout dawson_ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_about, container, false);
    }

    /**
     * Sets up the onclick events of the different elements.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dawson_ll = (LinearLayout) getActivity().findViewById(R.id.dawson_logo);
        dawson_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/"));
                startActivity(intent);
            }
        });

    }
}
