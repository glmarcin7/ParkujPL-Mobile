package pl.parkujznami.parkujpl_mobile.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
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
                    .inflate(R.layout.list_view_with_parkings_item, parent, false);
        }
        Parking parking = (Parking) getItem(position);

        TextView nameTextView = ViewHolder.get(convertView, R.id.tv_parkings_name);
        nameTextView.setText(parking.getName());

        TextView distanceTextView = ViewHolder.get(convertView, R.id.tv_distance);
        distanceTextView.setText(parking.getDistance());

        TextView priceTextView = ViewHolder.get(convertView, R.id.tv_price);
        priceTextView.setText(parking.getPrice() + "zł");

        TextView availabilityTextView = ViewHolder.get(convertView, R.id.tv_availability);
        availabilityTextView.setText("Duża liczba miejsc");

        return convertView;
    }
}
