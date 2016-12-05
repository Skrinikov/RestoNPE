package npe.com.restonpe.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import npe.com.restonpe.Beans.Cuisine;
import npe.com.restonpe.R;

/**
 * Custom adapter made to display information about restaurants
 * using a custom listing layout which contains a name, price range,
 * distance, cuisine type and rating.
 * <p>
 * Used as reference
 * source: Cindy's code.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 04/12/2016
 */
public class CuisineAdapter extends BaseAdapter {
    private List<Cuisine> list;
    private static LayoutInflater inflater = null;

    private static final String TAG = CuisineAdapter.class.getSimpleName();

    /**
     * Constructor that will keep a reference to the given parameter.
     *
     * @param context   The activity that instantiate this object.
     * @param list      The data in List form.
     */
    public CuisineAdapter(Context context, List<Cuisine> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Returns the size of the data list.
     *
     * @return int The size of the list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Returns the resto at the given position in the list.
     *
     * @param position The index of the object to retrieve.
     * @return RestoItem The {@code RestoItem} at the given position
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Returns the id of the restoItem at the position in the list.
     *
     * @param position The index of the object's id to retrieve.
     * @return long The id of the item at the given position
     */
    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    /**
     * Inflates the list xml layout and insert the data into the
     * different views in the layout.
     * <p>
     * Data being: the resto's name, price range, calculated distance from current
     * location using the DistanceCalculator.
     *
     * @param position The position in the data list
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent view of this list item
     *
     * @return View The View of one single item/row.
     */
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.cuisine_item, null);
        TextView name = (TextView) rowView.findViewById(R.id.cuisine_name);

        // Put id of RestoItem into list item so that it may be retrieved later when ShowRestoActivity is created
        rowView.setTag(list.get(position).getId());
        name.setText(list.get(position).getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that will communicate back with the activity and
             * display showRestoFragment.
             *
             * Used as reference
             * source: https://developer.android.com/training/basics/fragments/communicating.html
             *
             * @param v The view which is triggering the event.
             */
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        return rowView;
    }
}
