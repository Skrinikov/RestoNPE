package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import npe.com.restonpe.RestoSearchActivity;
import npe.com.restonpe.NearRestosActivity;
import npe.com.restonpe.R;
import npe.com.restonpe.TipActivity;

/**
 * The content of the MainActivity of RestoNPE.
 *
 * @author Uen Yi Cindy Hung
 * @since 29/11/2016
 * @version 1.1
 */
public class IndexFragment extends Fragment {
    private ImageView favResto, nearResto, findResto,tipCalculator,addResto;

    /**
     * Inflates a layout as the content of the MainActivity.
     *
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
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    /**
     * Sets up the onclick events for the 5 clickable items which will
     * all start a different activity.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        favResto = (ImageView)getActivity().findViewById(R.id.favResto);
        nearResto = (ImageView)getActivity().findViewById(R.id.nearResto);
        findResto = (ImageView)getActivity().findViewById(R.id.findResto);
        addResto = (ImageView)getActivity().findViewById(R.id.addResto);
        tipCalculator = (ImageView)getActivity().findViewById(R.id.tipCalculator);

        favResto.setOnClickListener(new View.OnClickListener(){
            /**
             * Start the FavResto Activity.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"favResto",Toast.LENGTH_LONG).show();
                /*
                Intent intent = new Intent(getActivity(), FavRestoActivity.class);
                startActivity(intent);*/
            }
        });

        nearResto.setOnClickListener(new View.OnClickListener(){
            /**
             * Start the NearResto Activity.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearRestosActivity.class);
                startActivity(intent);
            }
        });

        findResto.setOnClickListener(new View.OnClickListener(){
            /**
             * Start the FinfResto Activity.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RestoSearchActivity.class);
                startActivity(intent);
            }
        });

        addResto.setOnClickListener(new View.OnClickListener(){
            /**
             * Start the addResto Activity.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"addResto",Toast.LENGTH_LONG).show();
                /*
                Intent intent = new Intent(getActivity(), AddRestoActivity.class);
                startActivity(intent);*/
            }
        });

        tipCalculator.setOnClickListener(new View.OnClickListener(){
            /**
             * Start the Tip Activity.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TipActivity.class);
                startActivity(intent);
            }
        });
    }
}
