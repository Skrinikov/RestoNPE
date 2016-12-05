package npe.com.restonpe.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.FavRestoActivity;
import npe.com.restonpe.R;
import npe.com.restonpe.ShowRestoActivity;
import npe.com.restonpe.Zomato.ZomatoRestos;
import npe.com.restonpe.database.RestoDAO;

/**
 * Custom adapter made to display information about restaurants
 * using a custom listing layout which contains a name, price range,
 * distance, cuisine type and rating.
 * <p>
 * Used as reference
 * source: My android dino lab.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.0
 * @since 05/12/2016
 */
public class RestoAdapter extends BaseAdapter {
    private final Context context;
    private List<RestoItem> list;
    private double longitude, latitude;
    private static LayoutInflater inflater = null;

    public static final String ID = "id";
    private static final String SUBMITTER = "submitter";
    private static final String TAG = RestoAdapter.class.getSimpleName();

    /**
     * Constructor that will keep a reference to the given parameter and parse the
     * String longitude and latitude to double and create a layoutInflater.
     *
     * @param context   The activity that instantiate this object.
     * @param list      The data in List form.
     * @param longitude The current longitude location.
     * @param latitude  The current latitude location.
     */
    public RestoAdapter(Context context, List<RestoItem> list, String longitude, String latitude) {
        this.context = context;
        this.list = list;
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
     * @param position    The position in the data list
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view of this list item
     * @return View The View of one single item/row.
     */
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.resto_item, null);
        //ImageView icon = (ImageView) rowView.findViewById(R.id.cuisine_icon);
        TextView name = (TextView) rowView.findViewById(R.id.resto_name);
        TextView price = (TextView) rowView.findViewById(R.id.resto_price);
        TextView distance = (TextView) rowView.findViewById(R.id.resto_distance);
        final ImageView addResto = (ImageView) rowView.findViewById(R.id.resto_add);

        double calculated_distance = DistanceCalculator.calculateDistance
                (list.get(position).getLatitude(), list.get(position).getLongitude(), latitude, longitude);

        // Put id of RestoItem into list item so that it may be retrieved later when ShowRestoActivity is created
        rowView.setTag(list.get(position).getId());
        name.setText(list.get(position).getName());
        price.setText(list.get(position).getPriceRange());
        distance.setText(String.format("%.1f km", calculated_distance));

        if (FavRestoActivity.class == context.getClass()) {
            addResto.setImageResource(R.drawable.ic_remove);
        } else {
            addResto.setImageResource(R.drawable.ic_add);
        }

        setAddRestoListener(addResto);
        setRowViewListener(rowView, position);

        return rowView;
    }

    /**
     * Code that creates and set the event handler for adding resto to db.
     *
     * @param addResto the View to contain the handler.
     */
    private void setAddRestoListener(ImageView addResto) {
        Log.d(TAG, "setAddRestoListener called");
        addResto.setOnClickListener(new View.OnClickListener() {
            /**
             * Event handler that will add the clicked item's row to the
             * database.
             *
             * @param v The view which triggers the event.
             */
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setAddRestoListener - onClick called");

                if (FavRestoActivity.class == context.getClass()) {
                    //delete?
                } else {
                    Log.d(TAG, "setAddRestoListener - onClick: before Zomato");
                    ZomatoRestos zomato = new ZomatoRestos(context) {
                        @Override
                        public void handleResults(List<?> list) {
                            if (list.size() == 1) {
                                RestoDAO dao = RestoDAO.getDatabase(context);
                                Resto resto = (Resto) list.get(0);
                                resto.setSubmitterName("Zomato");
                                resto.setSubmitterEmail("ZomatoEmail");
                                dao.addRestaurant(resto);
                                Toast.makeText(context, R.string.added, Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    Log.d(TAG, "view's id is " + ((View) v.getParent()).getTag());
                    Log.d(TAG, "setAddRestoListener - onClick: before Zomato find");
                    zomato.findRestoInformation((int) ((View) v.getParent()).getTag());
                    Log.d(TAG, "setAddRestoListener - onClick: after Zomato find");
                }
            }
        });
    }

    /**
     * Code that creates and set the event handler for the rowView resto to db.
     *
     * @param rowView  the View to contain the handler.
     * @param position The item index
     */
    private void setRowViewListener(View rowView, final int position) {
        Log.d(TAG, "setRowViewListener called");

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
                Log.d(TAG, "setRowViewListener - setOnClickListener called");
                Intent intent = new Intent(context, ShowRestoActivity.class);

                int id = (int) v.getTag();
                Log.i(TAG, "Putting id of " + id + " in extras");
                intent.putExtra(ID, id);

                Resto resto = RestoDAO.getDatabase(context).getSingleRestaurant(id);
                if(resto.getSubmitterName() != null) {
                    intent.putExtra(SUBMITTER, resto.getSubmitterName());
                }else{
                    intent.putExtra(SUBMITTER, "");
                }

                context.startActivity(intent);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            /**
             * Method that will launch an implicit intent to dial the phone number in the
             * restoItem. Will make Toast if no application can handle the intent.
             *
             * Used as reference
             * source: http://stackoverflow.com/questions/4275678/how-to-make-phone-call-using-intent-in-android
             * source: https://developer.android.com/guide/components/intents-filters.html#Building
             *
             * @param v The view which is triggering the event.
             * @return boolean
             */
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "setRowViewListener - setOnLongClickListener called");
                Log.d(TAG, "onLongClick called");

                String phone = (list.get(position).getPhone() > 0) ? list.get(position).getPhone() + "" : "";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    Log.d(TAG, "onLongClick: not null");
                    context.startActivity(intent);
                } else {
                    Log.d(TAG, "onLongClick: null");
                    Toast.makeText(context, R.string.no_dial, Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });
    }
}
