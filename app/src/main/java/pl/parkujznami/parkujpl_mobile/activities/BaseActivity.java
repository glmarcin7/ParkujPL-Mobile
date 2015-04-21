package pl.parkujznami.parkujpl_mobile.activities;

import android.app.Activity;
import android.os.Bundle;

import pl.parkujznami.parkujpl_mobile.R;

/**
 * Created by Marcin on 2015-03-22.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
