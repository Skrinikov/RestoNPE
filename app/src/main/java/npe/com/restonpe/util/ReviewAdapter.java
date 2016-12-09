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
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String RATING = "rating";
    public static final String SUBMITTER = "submitter";
    public static final String SUBMITTER_EMAIL = "submitter_email";
    public static final String LIKES = "likes";
    public static final String RESTO_ID = "resto_id";
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private final Context context;
    private List<Review> list;

    /**
     * Constructor that will keep a reference to the given parameter and create a layoutInflater.
     *
     * @param context The activity that instantiate this object.
     * @param list    The data in List form.
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

        // Put review into list item
        rowView.setTag(R.string.review_id_code, item.getId());
        rowView.setTag(R.string.review_title_code, item.getTitle());
        rowView.setTag(R.string.review_content_code, item.getContent());
        rowView.setTag(R.string.review_rating_code, item.getRating());
        rowView.setTag(R.string.review_submitter_code, item.getSubmitter());
        rowView.setTag(R.string.review_submitter_email_code, item.getSubmitterEmail());
        rowView.setTag(R.string.review_likes_code, item.getLikes());
        rowView.setTag(R.string.review_resto_id_code, item.getRestoId());

        setRowClickListener(rowView);

        return rowView;
    }

    /**
     * Add a listener for when the a row is clicked.
     *
     * @param rowView  the row to which the handler should be applied.
     */
    private void setRowClickListener(View rowView) {
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

                long id = (long) v.getTag(R.string.review_id_code);
                String title = (String) v.getTag(R.string.review_title_code);
                String content = (String) v.getTag(R.string.review_content_code);
                double rating = (double) v.getTag(R.string.review_rating_code);
                String submitter = (String) v.getTag(R.string.review_submitter_code);
                String submitterEmail = (String) v.getTag(R.string.review_submitter_email_code);
                int likes = (int) v.getTag(R.string.review_likes_code);
                long restoId = (long) v.getTag(R.string.review_resto_id_code);

                Log.i(TAG, "Putting " + id + " in extras");
                Log.i(TAG, "Putting " + title + " in extras");
                Log.i(TAG, "Putting " + content + " in extras");
                Log.i(TAG, "Putting " + rating + " in extras");
                Log.i(TAG, "Putting " + submitter + " in extras");
                Log.i(TAG, "Putting " + submitterEmail + " in extras");
                Log.i(TAG, "Putting " + likes + " in extras");
                Log.i(TAG, "Putting " + restoId + " in extras");

                intent.putExtra(ID, id);
                intent.putExtra(TITLE, title);
                intent.putExtra(CONTENT, content);
                intent.putExtra(RATING, rating);
                intent.putExtra(SUBMITTER, submitter);
                intent.putExtra(SUBMITTER_EMAIL, submitterEmail);
                intent.putExtra(LIKES, likes);
                intent.putExtra(RESTO_ID, restoId);

                context.startActivity(intent);
            }
        });
    }
}
