package pl.parkujznami.parkujpl_mobile.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import pl.parkujznami.parkujpl_mobile.activities.StartActivity;

/**
 * Created by Marcin on 2016-01-12.
 */
public class GPSLocationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
            LocationManager locationManager = (LocationManager) StartActivity.getInstace().getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent startStartActivityAgain = new Intent(context, StartActivity.class);
                startStartActivityAgain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startStartActivityAgain);
            }
        }
    }
}
