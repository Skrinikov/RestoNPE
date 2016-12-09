package npe.com.restonpe.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.FavRestoActivity;
import npe.com.restonpe.Heroku.HerokuRestos;
import npe.com.restonpe.R;
import npe.com.restonpe.Services.RestoNetworkManager;
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
    public static final String LOCAL_ID = "local_id";
    public static final String ZOMATO_ID = "zomato_id";
    public static final String HEROKU_ID = "heroku_id";

    private static final String SUBMITTER = "submitter";
    private static final String TAG = RestoAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private final Context context;
    private List<RestoItem> list;
    private double longitude, latitude;

    /**
     * Constructor that will keep a reference to the given parameter and parse the
     * String longitude and latitude to double and create a layoutInflater.
     *
     * @param context    The activity that instantiate this object.
     * @param list       The data in List form.
     * @param longitude  The current longitude location. If {@code null} or empty string, the
     *                   latitude will be set to -1.
     * @param latitude   The current latitude location. If {@code null} or empty string, the
     *                   longitude will be set to -1.
     */
    public RestoAdapter(Context context, List<RestoItem> list, String longitude, String latitude) {
        this.context = context;
        this.list = list;
        if ((latitude != null && longitude != null) && (!latitude.isEmpty() && !longitude.isEmpty())) {
            this.longitude = Double.parseDouble(longitude);
            this.latitude = Double.parseDouble(latitude);
        } else {
            this.longitude = -1;
            this.latitude = -1;
        }
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Returns the size of the data list.
     *
     * @return int The size of the list.
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

        double calculated_distance = -1;
        if (latitude != -1 && longitude != -1) {
            calculated_distance = DistanceCalculator.calculateDistance
                    (list.get(position).getLatitude(), list.get(position).getLongitude(), latitude, longitude);
        }

        RestoItem item = list.get(position);

        // Put id's into list item so that it may be retrieved later when ShowRestoActivity is created
        rowView.setTag(R.string.local_id_code, item.getId());
        rowView.setTag(R.string.zomato_id_code, item.getZomatoId());
        rowView.setTag(R.string.heroku_id_code, item.getHerokuId());

        name.setText(item.getName());
        price.setText(item.getPriceRange());
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
     * Code that creates and set the event handler for adding/removing resto to db.
     *
     * @param addResto the View to contain the handler.
     */
    private void setAddRestoListener(ImageView addResto) {
//        Log.d(TAG, "setAddRestoListener called");

        LinearLayout parent = (LinearLayout) addResto.getParent();
        final long localId = (long) parent.getTag(R.string.local_id_code);
        final long zomatoId = (long) parent.getTag(R.string.zomato_id_code);
        final long herokuId = (long) parent.getTag(R.string.heroku_id_code);

        addResto.setOnClickListener(new View.OnClickListener() {
            /**
             * Event handler that will add or remove the clicked item's row to the
             * database depending on which Activity class called this adapter.
             *
             * @param v The view which triggers the event.
             */
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setAddRestoListener - onClick called");

                // Remove from favourites if the current Activity running is the Favourite's Activity
                if (FavRestoActivity.class == context.getClass()) {
                    Log.d(TAG, "ID to remove is: " + localId);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.remove);
                    builder.setMessage(R.string.confirm_remove);

                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        /**
                         * Removes the resto from local database
                         *
                         * @param dialog The dialog that is currently shown / the on pressed on.
                         * @param which The button pressed.
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RestoDAO.getDatabase(context).deleteRestaurant(localId);
                            Toast.makeText(context, R.string.removed, Toast.LENGTH_LONG).show();
                            ((FavRestoActivity) context).updateDbList();
                        }
                    });

                    builder.setNegativeButton(R.string.no, null);

                    Dialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Add to favourites list
                    // Find Resto information
                    if (zomatoId > 0) {
                        // Get from Zomato
                        Log.d(TAG, "Adding resto with id " + zomatoId + " from Zomato");
                        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(context) {
                            @Override
                            public void onPostExecute(List<Resto> list) {
                                if (list.size() == 1) {
                                    RestoDAO dao = RestoDAO.getDatabase(context);
                                    Resto resto = list.get(0);
                                    resto.setSubmitterName("Zomato");
                                    resto.setSubmitterEmail("ZomatoEmail");
                                    dao.addRestaurant(resto);
                                    Toast.makeText(context, R.string.added, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            protected List<Resto> readJson(JsonReader reader) {
                                Log.i(TAG, "Reading Json response...");

                                try {
                                    ZomatoRestos zomatoRestos = new ZomatoRestos(context);
                                    return zomatoRestos.readRestoInformation(reader);
                                } catch (IOException e) {
                                    Log.e(TAG, "An IO exception occurred: " + e.getMessage());
                                }
                                return null;
                            }
                        };

                        restoNetworkManager.findRestoInformation(zomatoId);
                    } else if (herokuId > 0) {
                        // Get from Heroku
                        Log.d(TAG, "Adding resto with id " + herokuId + " from Heroku");
                        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(context) {
                            @Override
                            public void onPostExecute(List<Resto> list) {
                                if (list.size() == 1) {
                                    RestoDAO dao = RestoDAO.getDatabase(context);
                                    Resto resto = list.get(0);
                                    resto.setSubmitterName("Zomato");
                                    resto.setSubmitterEmail("ZomatoEmail");
                                    dao.addRestaurant(resto);
                                    Toast.makeText(context, R.string.added, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            protected List<Resto> readJson(JsonReader reader) {
                                Log.i(TAG, "Reading Json response...");

                                try {
                                    HerokuRestos herokuRestos = new HerokuRestos(context);
                                    return herokuRestos.readRestoInformation(reader);
                                } catch (IOException e) {
                                    Log.e(TAG, "An IO exception occurred: " + e.getMessage());
                                }
                                return null;
                            }
                        };

                        restoNetworkManager.findRestoInformationFromHeroku(herokuId);
                    } else if (localId > 0) {
                        // Get form local db
                        Log.d(TAG, "Adding resto with id " + localId + " from local database");
                        RestoDAO dao = RestoDAO.getDatabase(context);
                        Resto resto = dao.getSingleRestaurant(localId);
                        resto.setSubmitterName("Zomato");
                        resto.setSubmitterEmail("ZomatoEmail");
                        dao.addRestaurant(resto);
                        Toast.makeText(context, R.string.added, Toast.LENGTH_LONG).show();
                    }
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
//        Log.d(TAG, "setRowViewListener called");

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

                String key = "error";
                long id = -1;
                long localId = (long) v.getTag(R.string.local_id_code);
                long zomatoId = (long) v.getTag(R.string.zomato_id_code);
                long herokuId = (long) v.getTag(R.string.heroku_id_code);

                if (localId > 0) {
                    Log.i(TAG, "Putting local id " + localId + " in extras");
                    key = LOCAL_ID;
                    id = localId;
                }
                if (zomatoId > 0) {
                    Log.i(TAG, "Putting Zomato id " + zomatoId + " in extras");
                    key = ZOMATO_ID;
                    id = zomatoId;
                }
                if (herokuId > 0) {
                    Log.i(TAG, "Putting Heroku id " + herokuId + " in extras");
                    key = HEROKU_ID;
                    id = herokuId;
                }

                intent.putExtra(key, id);

                Resto resto = RestoDAO.getDatabase(context).getSingleRestaurant(id);
                if (resto.getSubmitterName() != null) {
                    intent.putExtra(SUBMITTER, resto.getSubmitterName());
                } else {
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
