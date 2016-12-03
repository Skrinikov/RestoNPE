package npe.com.restonpe.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Fragments.ShowRestoFragment;
import npe.com.restonpe.R;

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
 * @since 01/12/2016
 */
public class RestoAdapter extends BaseAdapter {
    private final Context context;
    private List<RestoItem> list;
    private double longitude, latitude;
    private static LayoutInflater inflater = null;

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
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Returns the size of the data list.
     *
     * @return int
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Returns the resto at the given position in the list.
     *
     * @param position The index of the object to retrieve.
     * @return RestoItem
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Returns the id of the restoItem at the position in the list.
     *
     * @param position The index of the object's id to retrieve.
     * @return long
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
     * @param position
     * @param convertView
     * @param parent
     * @return View The View of one single item/row.
     */
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.resto_list, null);
        //ImageView icon = (ImageView) rowView.findViewById(R.id.cuisine_icon);
        TextView name = (TextView) rowView.findViewById(R.id.resto_name);
        TextView price = (TextView) rowView.findViewById(R.id.resto_price);
        TextView distance = (TextView) rowView.findViewById(R.id.resto_distance);

        double calculated_distance = DistanceCalculator.calculateDistance
                (list.get(position).getLatitude(), list.get(position).getLongitude(), latitude, longitude);

        name.setText(list.get(position).getName());
        price.setText(list.get(position).getPriceRange());
        distance.setText(String.format("%.1f km", calculated_distance));

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
                Intent intent = new Intent(context, ShowRestoFragment.class);
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
                String phone = (list.get(position).getPhone() > 0) ? list.get(position).getPhone() + "" : "";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, R.string.no_dial, Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });

        return rowView;
    }
}
