package pl.parkujznami.parkujpl_mobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import pl.parkujznami.parkujpl_mobile.R;

/**
 * Base class for all activities in project. Here is set basic content view for whole app
 * and other things that should be apply for whole program
 *
 * @author Marcin Głowacki
 */
public class BaseActivity extends AppCompatActivity {
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
