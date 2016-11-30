package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import npe.com.restonpe.R;

/**
 * Created by Hung on 11/29/2016.
 */

public class TipFragment extends Fragment {

    private EditText subtotal, tip_percent, tip_amount, num_of_people, person_person, grand_total;

    /**
     * Inflates the xml file that contains the layout for the tip calculator.
     * <p>
     * Inspired by internet images.
     * source: http://www.imore.com/best-check-splitting-and-tip-apps-iphone-plates-gratuity-tab-and-more
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
        return inflater.inflate(R.layout.activity_tip, container, false);
    }

    /**
     * Sets up the onKeyUp events and the logic of a tip calculator.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subtotal = (EditText) getActivity().findViewById(R.id.bill_subtotal);
        tip_percent = (EditText) getActivity().findViewById(R.id.tip_percent);
        tip_amount = (EditText) getActivity().findViewById(R.id.tip_amount);
        num_of_people = (EditText) getActivity().findViewById(R.id.num_of_people);
        person_person = (EditText) getActivity().findViewById(R.id.amount_person);
        grand_total = (EditText) getActivity().findViewById(R.id.grand_total);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double input_subtotal;
                int input_tip,input_num_people;

                if (s.length() + count > 0) {
                    if (subtotal.getText().toString().length() > 0) {
                        input_subtotal = Double.parseDouble(subtotal.getText().toString());

                        if (tip_percent.getText().toString().length() < 1) {
                            tip_percent.setText(getString(R.string.tip_default));
                        }

                        input_tip = Integer.parseInt(tip_percent.getText().toString());

                        if (num_of_people.getText().length() < 1) {
                            num_of_people.setText(getString(R.string.person_default));
                        }

                        input_num_people = Integer.parseInt(num_of_people.getText().toString());

                        calculate(input_subtotal, input_tip, input_num_people);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        subtotal.addTextChangedListener(watcher);
        tip_percent.addTextChangedListener(watcher);
        num_of_people.addTextChangedListener(watcher);


    }

    /**
     * Calculates the tip amount, the total with the tip, the amount to pay per person
     * and set the text to reflect these calculated number.
     *
     * @param money
     * @param percent
     * @param population
     */
    private void calculate(double money, int percent, int population) {
        double tip = money * (percent / 100.0);
        double total = money + tip;
        double perPerson = total / Math.abs(population);

        tip_amount.setText(tip + "");
        person_person.setText(perPerson + "");
        grand_total.setText(total + "");
        num_of_people.setText(Math.abs(population));


    }
}
