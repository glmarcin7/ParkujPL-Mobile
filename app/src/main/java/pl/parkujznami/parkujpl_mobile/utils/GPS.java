package pl.parkujznami.parkujpl_mobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import pl.parkujznami.parkujpl_mobile.R;

/**
 * Created by Marcin on 2015-04-22.
 */
public class GPS {

    private static Activity mActivity;

    /**
     * Check gps status and show AlertBox if necessary
     *
     * @param activity - application context
     * @return true - gps is enabled, false - gps was disabled
     */
    public static boolean enableGPS(Activity activity) {
        mActivity = activity;

        final LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private static void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(mActivity.getString(R.string.enable_gps_dialog_message_text))
                .setCancelable(false)
                .setPositiveButton(mActivity.getString(R.string.enable_gps_dialog_btn_positive), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        mActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(mActivity.getString(R.string.enable_gps_dialog_btn_negative), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
