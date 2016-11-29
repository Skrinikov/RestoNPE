package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import npe.com.restonpe.FindRestosActivity;
import npe.com.restonpe.MainActivity;
import npe.com.restonpe.NearRestosActivity;
import npe.com.restonpe.R;

/**
 * Created by Hung on 11/23/2016.
 */
public class IndexFragment extends Fragment {
    private ImageView favResto, nearResto, findResto,tipCalculator,addResto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        favResto = (ImageView)getActivity().findViewById(R.id.favResto);
        nearResto = (ImageView)getActivity().findViewById(R.id.nearResto);
        findResto = (ImageView)getActivity().findViewById(R.id.findResto);
        addResto = (ImageView)getActivity().findViewById(R.id.addResto);
        tipCalculator = (ImageView)getActivity().findViewById(R.id.tipCalculator);

        favResto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"favResto",Toast.LENGTH_LONG).show();
            }
        });

        nearResto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearRestosActivity.class);
                startActivity(intent);
            }
        });

        findResto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindRestosActivity.class);
                startActivity(intent);
            }
        });

        addResto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"favResto",Toast.LENGTH_LONG).show();
            }
        });

        tipCalculator.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"tipCalculator",Toast.LENGTH_LONG).show();
            }
        });
    }
}
