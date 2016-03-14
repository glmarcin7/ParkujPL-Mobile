package pl.parkujznami.parkujpl_mobile;

import android.app.Application;

import timber.log.Timber;

/**
 * Class needed for initializing some libraries
 *
 * @author Marcin Głowacki
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
