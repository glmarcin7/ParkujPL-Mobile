package pl.parkujznami.parkujpl_mobile.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import pl.parkujznami.parkujpl_mobile.R;

/**
 * Created by Marcin on 2015-03-22.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Suppress the soft-keyboard until the user actually touches the editText View.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
