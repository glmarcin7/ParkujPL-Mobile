package pl.parkujznami.parkujpl_mobile.activities;

import android.os.Bundle;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.fragments.ChooseNumberOfFreeSpotsFragment;

/**
 * Created by Marcin on 2015-04-19.
 */
public class ChooseNumberOfFreeSpotsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ChooseNumberOfFreeSpotsFragment())
                    .commit();
        }
    }
}
