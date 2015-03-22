package pl.parkujznami.parkujpl_mobile.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import pl.parkujznami.parkujpl_mobile.R;

/**
 * Created by Marcin on 2015-03-22.
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
