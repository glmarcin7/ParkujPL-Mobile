package pl.parkujznami.parkujpl_mobile.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.utils.ViewHolder;

/**
 * Created by Marcin on 2015-04-18.
 */
public class ParkingAdapter extends ArrayAdapter {
    public ParkingAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        if (convertView == null) {
            // inflate the layout
            convertView = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView titleTextView = ViewHolder.get(convertView, android.R.id.text1);
        titleTextView.setText(((Parking) getItem(position)).getDistance());
        return convertView;
    }
}
