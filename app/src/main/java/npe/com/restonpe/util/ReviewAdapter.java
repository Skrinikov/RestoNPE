package npe.com.restonpe.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.R;
import npe.com.restonpe.ShowReviewActivity;

/**
 * Custom adapter made to display information about reviews
 * using a custom listing layout which contains a title and submitter name
 * <p>
 * Used as reference
 * source: Cindy's RestoAdapter
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 07/12/2016
 */
public class ReviewAdapter extends BaseAdapter {
    private final Context context;
    private List<Review> list;
    private static LayoutInflater inflater = null;

    public static final String ID = "id";
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    /**
     * Constructor that will keep a reference to the given parameter and create a layoutInflater.
     *
     * @param context    The activity that instantiate this object.
     * @param list       The data in List form.
     */
    public ReviewAdapter(Context context, List<Review> list) {
        this.context = context;
        this.list = list;

        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Returns the size of the data list.
     *
     * @return The size of the list.
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Returns the Review at the given position in the list.
     *
     * @param position The index of the object to retrieve.
     * @return The {@code Review} at the given position
     */
    @Override
    public Review getItem(int position) {
        return list.get(position);
    }

    /**
     * Returns the id of the Review at the position in the list.
     *
     * @param position The index of the object's id to retrieve.
     * @return The id of the item at the given position
     */
    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    /**
     * Inflates the list xml layout and insert the data into the
     * different views in the layout.
     * <p>
     * Data being: the review's title and submitter name
     *
     * @param position    The position in the data list
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view of this list item
     * @return The View of one single item/row.
     */
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.review_item, null);
        TextView title = (TextView) rowView.findViewById(R.id.review_title);
        TextView submitter = (TextView) rowView.findViewById(R.id.review_submitter);

        Review item = list.get(position);

        title.setText(item.getTitle());
        submitter.setText(item.getSubmitter());

        // Put id of Review into list item
        rowView.setTag(list.get(position).getId());

        setRowClickListener(rowView, position);

        return rowView;
    }

    /**
     * Add a listener for when the a row is clicked.
     *
     * @param rowView  the row to which the handler should be applied.
     * @param position The item index.
     */
    private void setRowClickListener(View rowView, final int position) {
        Log.d(TAG, "setRowClickListener called");

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
                Log.d(TAG, "setRowClickListener - setOnClickListener called");
                Intent intent = new Intent(context, ShowReviewActivity.class);

                long id = (long) v.getTag();
                Log.i(TAG, "Putting id of " + id + " in extras");
                intent.putExtra(ID, id);

                context.startActivity(intent);
            }
        });
    }
}
