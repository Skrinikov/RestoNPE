package npe.com.restonpe.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import npe.com.restonpe.R;

/**
 * Fragment of the addResto component. Inflates the UI, fills the spinners with data and creates on
 * focus out listeners for the edit texts.
 */
public class AddRestoFragment extends Fragment {

    /**
     * Inflates a layout to be the content layout of the AddRestoActivity.
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
        return inflater.inflate(R.layout.activity_add_resto, container, false);
    }

    /**
     * Is called whenever the add resto activity is created. Populates the spinners with data from
     * the string-arrays.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If add onFocusOutListeners add a method call here.
        addContentToSpinners();
    }

    /**
     * Adds the genre and price range array-strings to the two spinners of the UI.
     */
    private void addContentToSpinners() {
        // Adding all genres to the spinner.
        Spinner genres = (Spinner) getActivity().findViewById(R.id.genresSpinner);
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(getActivity(),R.array.genres,android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genres.setAdapter(aa);

        Spinner priceRanges = (Spinner) getActivity().findViewById(R.id.priceRangeSpinner);
        ArrayAdapter<CharSequence> aa2 = ArrayAdapter.createFromResource(getActivity(),R.array.price_ranges,android.R.layout.simple_spinner_item);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceRanges.setAdapter(aa2);
    }
}
