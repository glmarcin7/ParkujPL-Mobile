package pl.parkujznami.parkujpl_mobile.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.views.notifications.ChooseNumberOfFreeSpotsNotification;
import timber.log.Timber;

/**
 * Created by Marcin on 2015-04-18.
 */
public class Navigation {

    private static Context mContext;
    private static int mParkingId;

    public static void startNavigation(Integer parkingId, Coords coords, Context context) {
        mContext = context;
        mParkingId = parkingId;
        Uri gmmIntentUri = Uri.parse(
                "google.navigation:q="
                        + coords.getLatitude()
                        + ","
                        + coords.getLongitude()
        );
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            mContext.startActivity(mapIntent);
            startNavigationStopService(coords);
        } catch (ActivityNotFoundException e) {
            Timber.i("Brak zainstalowanych map google");
            //TODO
        }
    }

    private static void startNavigationStopService(final Coords coords) {
        final SmartLocation smartLocation = new SmartLocation.Builder(mContext).logging(true).build();
        smartLocation
                .location()
                .config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Timber.d("LocationUpdate");
                        if(onPosition(location, coords, 0.1)){
                            showNotification();
                            smartLocation.location().stop();
                        }
                    }
                });
    }

    private static void showNotification() {
        Timber.i("OnPosition");
        ChooseNumberOfFreeSpotsNotification.notify(
                mContext,
                mContext.getString(R.string.notification_ticker),
                mParkingId
        );
    }

    /**
     *
     * @param myLocation - my location
     * @param parkingsLocation - parking's location
     * @param accuracy - in km
     * @return
     */
    private static boolean onPosition(Location myLocation, Coords parkingsLocation, Double accuracy) {
        if(distance(
                myLocation.getLatitude(),
                myLocation.getLongitude(),
                Double.parseDouble(parkingsLocation.getLatitude()),
                Double.parseDouble(parkingsLocation.getLongitude())
        ) <= accuracy){
            return true;
        }
        return false;
    }

    /**
     * Calculate distance between two points
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return distance in km
     */
    private static Double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.853159616;
        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
