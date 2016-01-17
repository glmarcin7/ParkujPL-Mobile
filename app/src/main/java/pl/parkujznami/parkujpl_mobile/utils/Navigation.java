package pl.parkujznami.parkujpl_mobile.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.views.notifications.ChangeParkingNotification;
import pl.parkujznami.parkujpl_mobile.views.notifications.ChooseNumberOfFreeSpotsNotification;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Marcin on 2015-04-18.
 */
public class Navigation {

    private static Context mContext;
    private static int mParkingId;
    private static Timer timer = null;

    public static void startNavigation(Integer parkingId, Coords coords, Context context, Boolean informationAboutLittleSpaceNotKnown) {
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
            if (informationAboutLittleSpaceNotKnown) {
                startNumberOfFreeSpotsChangeService();
            }
            startNavigationStopService(coords);
        } catch (ActivityNotFoundException e) {
            Timber.i("Brak zainstalowanych map google");
            //TODO
        }
    }

    private static void startNumberOfFreeSpotsChangeService() {
        int updateFreqInMilliseconds = Integer.parseInt(mContext.getString(R.string.update_freq)) * 1000;
        int delay = updateFreqInMilliseconds;
        int period = updateFreqInMilliseconds;
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ApiClient.getParkujPlApiClient(mContext).parking(mParkingId, new Callback<Parking>() {
                    @Override
                    public void success(Parking parking, Response response) {
                        if (parking.getAvailabilty().toString(mContext).equals(mContext.getString(R.string.s_really_little_space))) {
                            showOnChangeNotification();
                            cancel();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(mContext, mContext.getString(R.string.parking_not_found), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, delay, period);
    }

    private static void showOnChangeNotification() {
        Timber.i("Number of free spots has changed");
        ChangeParkingNotification.notify(
                mContext,
                mContext.getString(R.string.number_of_free_spots_has_changed_ticker)
        );
    }

    private static void startNavigationStopService(final Coords coords) {
        final SmartLocation smartLocation = new SmartLocation.Builder(mContext).logging(true).build();
        smartLocation
                .location()
                .config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Timber.d("LocationUpdate - onStop");
                        if (onPosition(
                                location,
                                coords,
                                Double.parseDouble(
                                        mContext.getString(
                                                R.string.distance_to_parking_when_notification_is_shown
                                        )
                                )
                        )) {
                            showStopNotification();
                            smartLocation.location().stop();
                            if (timer != null) {
                                timer.cancel();
                            }
                        }
                    }
                });
    }

    private static void showStopNotification() {
        Timber.i("OnPosition");
        ChooseNumberOfFreeSpotsNotification.notify(
                mContext,
                mContext.getString(R.string.choose_number_of_free_spots_ticker),
                mParkingId
        );
    }

    /**
     * @param myLocation       - my location
     * @param parkingsLocation - parking's location
     * @param accuracy         - in km
     * @return
     */
    private static boolean onPosition(Location myLocation, Coords parkingsLocation, Double accuracy) {
        if (distance(
                myLocation.getLatitude(),
                myLocation.getLongitude(),
                Double.parseDouble(parkingsLocation.getLatitude()),
                Double.parseDouble(parkingsLocation.getLongitude())
        ) <= accuracy) {
            return true;
        }
        return false;
    }

    /**
     * Calculate distance between two points
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return distance in km
     */
    public static Double distance(double lat1, double lon1, double lat2, double lon2) {
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
