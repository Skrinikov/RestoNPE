package npe.com.restonpe.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.R;

/**
 * Custom adapter made to display information about restaurants
 * using a custom listing layout which contains a name, price range,
 * distance, cuisine type and rating.
 *
 * Used as reference
 * source: My android dino lab.
 *
 * @author Uen Yi Cindy Hung
 * @version 1.0
 * @since 01/12/2016
 */
public class RestoAdapter extends BaseAdapter {
    private Context context;
    private List<RestoItem> list;
    private static LayoutInflater inflater = null;

    public RestoAdapter(Context context, List<RestoItem> list){
        this.context = context;
        this.list = list;
        this.inflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.resto_list,null);
        //ImageView icon = (ImageView) rowView.findViewById(R.id.cuisine_icon);
        TextView name = (TextView)rowView.findViewById(R.id.resto_name);
        TextView price = (TextView)rowView.findViewById(R.id.resto_price);
        TextView distance = (TextView)rowView.findViewById(R.id.resto_distance);

        //double calculated_distance = DistanceCalculator.calculateDistance(list.get(position).getLatitude(),list.get(position).getLongitude(),0.0,0.0);

        name.setText(list.get(position).getName());
        price.setText(list.get(position).getPriceRange());
        //distance.setText(String.format("%.1f m",calculated_distance));
        distance.setText("10 m");

        return rowView;
    }
}
