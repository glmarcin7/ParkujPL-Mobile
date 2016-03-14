package pl.parkujznami.parkujpl_mobile.activities;

import android.os.Bundle;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.fragments.ChooseNumberOfFreeSpotsFragment;

/**
 * Activity that manages ChooseNumberOfFreeSpotsFragment
 *
 * @author Marcin Głowacki
 */
public class ChooseNumberOfFreeSpotsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.rl_container, new ChooseNumberOfFreeSpotsFragment())
                    .commit();
        }
    }
}
