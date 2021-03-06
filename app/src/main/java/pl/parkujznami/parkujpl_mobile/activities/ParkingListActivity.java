package pl.parkujznami.parkujpl_mobile.activities;

import android.content.Intent;
import android.os.Bundle;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.fragments.ParkingListFragment;

/**
 * Activity that manages ParkingListFragment
 *
 * @author Marcin Głowacki
 */
public class ParkingListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.rl_container, new ParkingListFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}
