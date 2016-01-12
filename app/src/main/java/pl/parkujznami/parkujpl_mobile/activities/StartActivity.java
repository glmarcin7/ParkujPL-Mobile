package pl.parkujznami.parkujpl_mobile.activities;

import android.os.Bundle;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.fragments.StartFragment;


public class StartActivity extends BaseActivity {

    private static StartActivity startActivityRunningInstance;

    public static StartActivity  getInstace(){
        return startActivityRunningInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivityRunningInstance = this;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new StartFragment())
                    .commit();
        }
    }
}
