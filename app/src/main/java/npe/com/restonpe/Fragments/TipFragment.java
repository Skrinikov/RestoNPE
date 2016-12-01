package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import npe.com.restonpe.R;

/**
 * Fragment class that will take care of the display and calculations
 * needed to get the tip amount, grand total, amount per person for
 * an amount on bill.
 *
 * @author Uen Yi Cindy Hung
 * @since 30/11/2016
 * @version 1.0
 */

public class TipFragment extends Fragment {

    private EditText subtotal, tip_percent, tip_amount, num_of_people, person_person, grand_total;
    private final String TAG = "TipFragment";

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
     * Sets up the text changing events using a TextWatcher which will extract the needed data to
     * then call upon the method that will do the calculations.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get a handle on all the text fields to extract and display data.
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

            /**
             * Method that will be called at every text change. Check is there is any string
             * to extract, if yes, convert them to double/int to send to the calculate method.
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged called");
                double input_subtotal;
                int input_tip, input_num_people;

                if (s.length() > 0) {
                    Log.d(TAG, "char sequence is: " + s + " and its length is: " + s.length());

                    if (subtotal.getText().toString().length() > 0) {
                        Log.d(TAG, "subTotal is not empty");
                        input_subtotal = Double.parseDouble("0" + subtotal.getText().toString());

                        if (tip_percent.getText().toString().length() < 1) {
                            Log.d(TAG, "tip percentage is empty");
                            tip_percent.setText(getString(R.string.tip_default));
                        }

                        input_tip = Integer.parseInt(tip_percent.getText().toString());

                        if (num_of_people.getText().length() < 1 || num_of_people.getText().toString().equals("0")) {
                            Log.d(TAG, "number of people is empty");
                            num_of_people.setText(getString(R.string.person_default));
                        }

                        input_num_people = Integer.parseInt(num_of_people.getText().toString());

                        Log.d(TAG, "variables are: " + input_subtotal + "\t" + input_tip + "\t" + input_num_people);
                        calculate(input_subtotal, input_tip, input_num_people);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Add the TextWatcher as the listener.
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
        Log.d(TAG, "calculate called");

        double tip = Math.round(money * percent) / 100.0;
        double total = money + tip;
        double perPerson = Math.round(total / population * 100) / 100.0;

        tip_amount.setText(tip + "");
        person_person.setText(perPerson + "");
        grand_total.setText(total + "");
    }
}
