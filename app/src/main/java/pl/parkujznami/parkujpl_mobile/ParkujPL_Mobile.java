package pl.parkujznami.parkujpl_mobile;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Marcin on 2015-04-14.
 */
public class ParkujPL_Mobile extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
